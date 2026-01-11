/**
 * K-QuantumNative - Learn Screen
 * Port of QuantumNative LearnView
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kquantum.nativeapp.data.models.LearningTrack
import com.kquantum.nativeapp.presentation.theme.*
import com.kquantum.nativeapp.presentation.viewmodels.LearnViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTrack: (String) -> Unit,
    viewModel: LearnViewModel = hiltViewModel()
) {
    val tracks by viewModel.tracks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Learn",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = QuantumBlue)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(tracks) { track ->
                    LearningTrackCard(
                        track = track,
                        onClick = { onNavigateToTrack(track.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun LearningTrackCard(
    track: LearningTrack,
    onClick: () -> Unit
) {
    val trackColor = when (track.id) {
        "fundamentals" -> QuantumBlue
        "gates" -> QuantumGreen
        "algorithms" -> QuantumPurple
        "advanced" -> QuantumPink
        else -> QuantumCyan
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = DarkCard
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = track.emoji,
                            fontSize = 28.sp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = track.title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "${track.levels.size} levels",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                }

                if (track.isPremium) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = TierPremium.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = TierPremium
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "PRO",
                                style = MaterialTheme.typography.labelSmall,
                                color = TierPremium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = track.description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Progress bar
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Progress",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextTertiary
                    )
                    Text(
                        text = "${(track.progressPercentage * 100).toInt()}%",
                        style = MaterialTheme.typography.labelMedium,
                        color = trackColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { track.progressPercentage.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = trackColor,
                    trackColor = DarkCardElevated,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // XP reward
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "⭐", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${track.totalXp} XP total",
                        style = MaterialTheme.typography.labelMedium,
                        color = QuantumYellow
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = trackColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = track.difficulty,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = trackColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnDetailScreen(
    trackId: String,
    onNavigateBack: () -> Unit,
    onNavigateToLesson: (String) -> Unit,
    viewModel: LearnViewModel = hiltViewModel()
) {
    val tracks by viewModel.tracks.collectAsState()
    val track = tracks.find { it.id == trackId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        track?.title ?: "Track",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
        if (track == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Track not found", color = TextSecondary)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = track.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                items(track.levels) { level ->
                    LevelCard(
                        levelNumber = level.level,
                        title = level.title,
                        lessonsCount = level.lessons.size,
                        completedCount = level.lessons.count { it.isCompleted },
                        isLocked = level.isLocked,
                        onClick = {
                            if (!level.isLocked) {
                                level.lessons.firstOrNull { !it.isCompleted }?.let {
                                    onNavigateToLesson(it.id)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LevelCard(
    levelNumber: Int,
    title: String,
    lessonsCount: Int,
    completedCount: Int,
    isLocked: Boolean,
    onClick: () -> Unit
) {
    val progress = if (lessonsCount > 0) completedCount.toFloat() / lessonsCount else 0f

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isLocked, onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = if (isLocked) DarkCard.copy(alpha = 0.5f) else DarkCard
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Level number
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isLocked) TextTertiary.copy(alpha = 0.3f)
                        else QuantumBlue.copy(alpha = 0.2f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isLocked) {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = TextTertiary
                    )
                } else {
                    Text(
                        text = "$levelNumber",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = QuantumBlue
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isLocked) TextTertiary else TextPrimary
                )
                Text(
                    text = "$completedCount/$lessonsCount lessons",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = if (isLocked) TextTertiary else QuantumGreen,
                    trackColor = DarkCardElevated,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScreen(
    lessonId: String,
    onNavigateBack: () -> Unit,
    onLessonComplete: () -> Unit
) {
    // Placeholder lesson screen
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lesson", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "📚",
                    fontSize = 48.sp
                )
                Text(
                    text = "Lesson Content",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary
                )
                Text(
                    text = "Lesson ID: $lessonId",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Button(
                    onClick = onLessonComplete,
                    colors = ButtonDefaults.buttonColors(containerColor = QuantumGreen)
                ) {
                    Text("Complete Lesson")
                }
            }
        }
    }
}
