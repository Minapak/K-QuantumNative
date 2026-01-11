/**
 * K-QuantumNative - Bridge Screen
 * Port of QuantumNative QuantumBridgeView
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kquantum.nativeapp.data.models.BridgeJobStatus
import com.kquantum.nativeapp.data.models.QuantumBackend
import com.kquantum.nativeapp.presentation.theme.*
import com.kquantum.nativeapp.presentation.viewmodels.BridgeUiState
import com.kquantum.nativeapp.presentation.viewmodels.BridgeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BridgeScreen(
    onNavigateBack: () -> Unit,
    viewModel: BridgeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()
    val availableBackends by viewModel.availableBackends.collectAsState()
    val selectedBackend by viewModel.selectedBackend.collectAsState()
    val circuitQasm by viewModel.circuitQasm.collectAsState()
    val shots by viewModel.shots.collectAsState()
    val noiseData by viewModel.noiseData.collectAsState()
    val showBackendPicker by viewModel.showBackendPicker.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quantum Bridge", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (isConnected) {
                        IconButton(onClick = { viewModel.disconnect() }) {
                            Icon(
                                Icons.Default.LinkOff,
                                contentDescription = "Disconnect",
                                tint = StatusError
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = TextPrimary,
                    navigationIconContentColor = TextPrimary
                )
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is BridgeUiState.Idle -> {
                    IdleContent(
                        onConnect = { viewModel.connect() }
                    )
                }

                is BridgeUiState.Connecting -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = QuantumCyan)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Connecting to Quantum Bridge...",
                                color = TextSecondary
                            )
                        }
                    }
                }

                is BridgeUiState.Connected -> {
                    ConnectedContent(
                        backends = availableBackends,
                        selectedBackend = selectedBackend,
                        circuitQasm = circuitQasm,
                        shots = shots,
                        noiseData = noiseData,
                        sampleCircuits = viewModel.sampleCircuits,
                        onBackendSelect = { viewModel.showBackendPicker() },
                        onCircuitChange = { viewModel.updateCircuitQasm(it) },
                        onShotsChange = { viewModel.updateShots(it) },
                        onLoadSample = { viewModel.loadSampleCircuit(it) },
                        onSubmit = { viewModel.submitJob() }
                    )
                }

                is BridgeUiState.Running -> {
                    val job = (uiState as BridgeUiState.Running).job
                    RunningContent(
                        jobId = job.id,
                        status = job.status,
                        onCancel = { viewModel.cancelJob() }
                    )
                }

                is BridgeUiState.Completed -> {
                    val results = (uiState as BridgeUiState.Completed).results
                    CompletedContent(
                        counts = results.measurements,
                        onNewJob = { viewModel.resetToIdle() },
                        onBack = onNavigateBack
                    )
                }

                is BridgeUiState.Error -> {
                    val message = (uiState as BridgeUiState.Error).message
                    ErrorContent(
                        message = message,
                        onRetry = { viewModel.clearError() }
                    )
                }
            }

            // Backend picker dialog
            if (showBackendPicker) {
                BackendPickerDialog(
                    backends = availableBackends,
                    selectedBackend = selectedBackend,
                    onSelect = { viewModel.selectBackend(it) },
                    onDismiss = { viewModel.dismissBackendPicker() }
                )
            }
        }
    }
}

@Composable
private fun IdleContent(onConnect: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "🖥️", fontSize = 80.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Quantum Bridge",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Connect to real IBM Quantum hardware and run your circuits on actual quantum processors.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onConnect,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = QuantumCyan),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Link, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Connect to Bridge",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ConnectedContent(
    backends: List<QuantumBackend>,
    selectedBackend: QuantumBackend?,
    circuitQasm: String,
    shots: Int,
    noiseData: com.kquantum.nativeapp.data.models.RealTimeNoiseData?,
    sampleCircuits: List<Pair<String, String>>,
    onBackendSelect: () -> Unit,
    onCircuitChange: (String) -> Unit,
    onShotsChange: (Int) -> Unit,
    onLoadSample: (String) -> Unit,
    onSubmit: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Connection status
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = StatusSuccess.copy(alpha = 0.15f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(StatusSuccess)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Connected to Quantum Bridge",
                        style = MaterialTheme.typography.labelLarge,
                        color = StatusSuccess
                    )
                }
            }
        }

        // Backend selector
        item {
            Text(
                text = "Select Backend",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onBackendSelect),
                shape = RoundedCornerShape(12.dp),
                color = DarkCard
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = selectedBackend?.name ?: "Choose a backend",
                            style = MaterialTheme.typography.titleSmall,
                            color = if (selectedBackend != null) TextPrimary else TextSecondary
                        )
                        if (selectedBackend != null) {
                            Text(
                                text = "${selectedBackend.numQubits} qubits",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                    Icon(
                        Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = TextTertiary
                    )
                }
            }
        }

        // Sample circuits
        item {
            Text(
                text = "Sample Circuits",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                sampleCircuits.forEach { (name, _) ->
                    Surface(
                        modifier = Modifier.clickable { onLoadSample(name) },
                        shape = RoundedCornerShape(8.dp),
                        color = QuantumPurple.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = name,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = QuantumPurple
                        )
                    }
                }
            }
        }

        // QASM input
        item {
            Text(
                text = "Circuit (QASM)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = circuitQasm,
                onValueChange = onCircuitChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                placeholder = { Text("Enter OpenQASM 2.0 code...") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = QuantumBlue,
                    unfocusedBorderColor = TextTertiary,
                    focusedContainerColor = DarkCard,
                    unfocusedContainerColor = DarkCard
                )
            )
        }

        // Shots selector
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Shots",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Text(
                    text = "$shots",
                    style = MaterialTheme.typography.titleMedium,
                    color = QuantumBlue
                )
            }
            Slider(
                value = shots.toFloat(),
                onValueChange = { onShotsChange(it.toInt()) },
                valueRange = 1f..8192f,
                steps = 7,
                colors = SliderDefaults.colors(
                    thumbColor = QuantumBlue,
                    activeTrackColor = QuantumBlue
                )
            )
        }

        // Submit button
        item {
            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = selectedBackend != null && circuitQasm.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = QuantumCyan),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Submit Job",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun RunningContent(
    jobId: String,
    status: BridgeJobStatus,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(80.dp),
            color = QuantumCyan,
            strokeWidth = 6.dp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Job Running",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Status: ${status.name}",
            style = MaterialTheme.typography.bodyLarge,
            color = QuantumCyan
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Job ID: ${jobId.take(8)}...",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(
            onClick = onCancel,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusError)
        ) {
            Text("Cancel Job")
        }
    }
}

@Composable
private fun CompletedContent(
    counts: Map<String, Int>,
    onNewJob: () -> Unit,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "✅", fontSize = 60.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Job Completed",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = StatusSuccess
                )
            }
        }

        item {
            Text(
                text = "Measurement Results",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }

        val totalShots = counts.values.sum()
        items(counts.entries.toList().sortedByDescending { it.value }) { (state, count) ->
            val probability = count.toFloat() / totalShots
            ResultBar(
                state = state,
                count = count,
                probability = probability
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Done")
                }
                Button(
                    onClick = onNewJob,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = QuantumCyan)
                ) {
                    Text("New Job")
                }
            }
        }
    }
}

@Composable
private fun ResultBar(
    state: String,
    count: Int,
    probability: Float
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DarkCard
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "|$state⟩",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                Text(
                    text = "$count (${(probability * 100).toInt()}%)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { probability },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = QuantumBlue,
                trackColor = DarkCardElevated,
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "❌", fontSize = 60.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Error",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = StatusError
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = QuantumBlue)
        ) {
            Text("Try Again")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BackendPickerDialog(
    backends: List<QuantumBackend>,
    selectedBackend: QuantumBackend?,
    onSelect: (QuantumBackend) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Backend", fontWeight = FontWeight.Bold) },
        text = {
            LazyColumn {
                items(backends) { backend ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(backend) }
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = if (backend == selectedBackend)
                            QuantumBlue.copy(alpha = 0.15f) else DarkCard
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = backend.name,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${backend.numQubits} qubits • ${backend.status}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        if (backend.status == "online") StatusSuccess
                                        else StatusWarning
                                    )
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        containerColor = DarkSurface
    )
}
