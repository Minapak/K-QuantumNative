/**
 * K-QuantumNative - Bridge ViewModel
 * Port of QuantumNative QuantumBridgeView integration
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kquantum.nativeapp.data.models.*
import com.kquantum.nativeapp.services.bridge.QuantumBridgeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class BridgeUiState {
    object Idle : BridgeUiState()
    object Connecting : BridgeUiState()
    object Connected : BridgeUiState()
    data class Running(val job: BridgeJob) : BridgeUiState()
    data class Completed(val results: BridgeJobResults) : BridgeUiState()
    data class Error(val message: String) : BridgeUiState()
}

@HiltViewModel
class BridgeViewModel @Inject constructor(
    private val bridgeService: QuantumBridgeService
) : ViewModel() {

    val isConnected: StateFlow<Boolean> = bridgeService.isConnected
    val availableBackends: StateFlow<List<QuantumBackend>> = bridgeService.availableBackends
    val currentJob: StateFlow<BridgeJob?> = bridgeService.currentJob
    val noiseData: StateFlow<RealTimeNoiseData?> = bridgeService.noiseData
    val isLoading: StateFlow<Boolean> = bridgeService.isLoading
    val error: StateFlow<String?> = bridgeService.error

    private val _uiState = MutableStateFlow<BridgeUiState>(BridgeUiState.Idle)
    val uiState: StateFlow<BridgeUiState> = _uiState.asStateFlow()

    private val _selectedBackend = MutableStateFlow<QuantumBackend?>(null)
    val selectedBackend: StateFlow<QuantumBackend?> = _selectedBackend.asStateFlow()

    private val _circuitQasm = MutableStateFlow("")
    val circuitQasm: StateFlow<String> = _circuitQasm.asStateFlow()

    private val _shots = MutableStateFlow(1024)
    val shots: StateFlow<Int> = _shots.asStateFlow()

    private val _showBackendPicker = MutableStateFlow(false)
    val showBackendPicker: StateFlow<Boolean> = _showBackendPicker.asStateFlow()

    private val _jobHistory = MutableStateFlow<List<BridgeJob>>(emptyList())
    val jobHistory: StateFlow<List<BridgeJob>> = _jobHistory.asStateFlow()

    private var noiseMonitorJob: Job? = null
    private var jobPollingJob: Job? = null

    init {
        observeConnection()
    }

    private fun observeConnection() {
        viewModelScope.launch {
            isConnected.collect { connected ->
                _uiState.value = if (connected) {
                    BridgeUiState.Connected
                } else {
                    BridgeUiState.Idle
                }
            }
        }
    }

    fun connect() {
        viewModelScope.launch {
            _uiState.value = BridgeUiState.Connecting
            val success = bridgeService.connect()
            if (success) {
                _uiState.value = BridgeUiState.Connected
                loadBackends()
            } else {
                _uiState.value = BridgeUiState.Error("Failed to connect to quantum bridge")
            }
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            bridgeService.disconnect()
            stopNoiseMonitoring()
            _uiState.value = BridgeUiState.Idle
            _selectedBackend.value = null
        }
    }

    private fun loadBackends() {
        viewModelScope.launch {
            bridgeService.loadAvailableBackends()
        }
    }

    fun selectBackend(backend: QuantumBackend) {
        _selectedBackend.value = backend
        _showBackendPicker.value = false
        startNoiseMonitoring(backend.name)
    }

    fun showBackendPicker() {
        _showBackendPicker.value = true
    }

    fun dismissBackendPicker() {
        _showBackendPicker.value = false
    }

    fun updateCircuitQasm(qasm: String) {
        _circuitQasm.value = qasm
    }

    fun updateShots(shots: Int) {
        _shots.value = shots.coerceIn(1, 8192)
    }

    fun submitJob() {
        val backend = _selectedBackend.value ?: return
        val qasm = _circuitQasm.value
        if (qasm.isBlank()) return

        viewModelScope.launch {
            val job = bridgeService.submitJob(
                circuit = qasm,
                backend = backend.name,
                shots = _shots.value
            )

            if (job != null) {
                _uiState.value = BridgeUiState.Running(job)
                _jobHistory.value = listOf(job) + _jobHistory.value
                startJobPolling(job.id)
            } else {
                _uiState.value = BridgeUiState.Error("Failed to submit job")
            }
        }
    }

    private fun startJobPolling(jobId: String) {
        jobPollingJob?.cancel()
        jobPollingJob = viewModelScope.launch {
            while (isActive) {
                val job = bridgeService.getJobStatus(jobId)
                if (job != null) {
                    when (job.status) {
                        BridgeJobStatus.COMPLETED -> {
                            val results = bridgeService.getJobResults(jobId)
                            if (results != null) {
                                _uiState.value = BridgeUiState.Completed(results)
                            }
                            break
                        }
                        BridgeJobStatus.FAILED, BridgeJobStatus.CANCELLED -> {
                            _uiState.value = BridgeUiState.Error("Job ${job.status.name.lowercase()}")
                            break
                        }
                        else -> {
                            _uiState.value = BridgeUiState.Running(job)
                        }
                    }
                }
                delay(2000) // Poll every 2 seconds
            }
        }
    }

    private fun startNoiseMonitoring(backendName: String) {
        noiseMonitorJob?.cancel()
        noiseMonitorJob = viewModelScope.launch {
            while (isActive) {
                bridgeService.getNoiseData(backendName)
                delay(30000) // Update noise data every 30 seconds
            }
        }
    }

    private fun stopNoiseMonitoring() {
        noiseMonitorJob?.cancel()
        noiseMonitorJob = null
    }

    fun cancelJob() {
        val job = currentJob.value ?: return
        viewModelScope.launch {
            bridgeService.cancelJob(job.id)
            jobPollingJob?.cancel()
            _uiState.value = BridgeUiState.Connected
        }
    }

    fun resetToIdle() {
        _uiState.value = if (isConnected.value) {
            BridgeUiState.Connected
        } else {
            BridgeUiState.Idle
        }
    }

    fun clearError() {
        bridgeService.clearError()
        resetToIdle()
    }

    // Sample circuits for quick testing
    val sampleCircuits: List<Pair<String, String>>
        get() = listOf(
            "Bell State" to """
                OPENQASM 2.0;
                include "qelib1.inc";
                qreg q[2];
                creg c[2];
                h q[0];
                cx q[0], q[1];
                measure q -> c;
            """.trimIndent(),
            "GHZ State" to """
                OPENQASM 2.0;
                include "qelib1.inc";
                qreg q[3];
                creg c[3];
                h q[0];
                cx q[0], q[1];
                cx q[1], q[2];
                measure q -> c;
            """.trimIndent(),
            "Hadamard Test" to """
                OPENQASM 2.0;
                include "qelib1.inc";
                qreg q[1];
                creg c[1];
                h q[0];
                measure q -> c;
            """.trimIndent()
        )

    fun loadSampleCircuit(name: String) {
        sampleCircuits.find { it.first == name }?.let { (_, qasm) ->
            _circuitQasm.value = qasm
        }
    }

    override fun onCleared() {
        super.onCleared()
        noiseMonitorJob?.cancel()
        jobPollingJob?.cancel()
    }
}
