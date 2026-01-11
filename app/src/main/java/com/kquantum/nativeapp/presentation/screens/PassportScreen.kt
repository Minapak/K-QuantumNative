/**
 * K-QuantumNative - Passport Screen
 * Port of QuantumNative PassportView
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kquantum.nativeapp.data.models.Achievement
import com.kquantum.nativeapp.data.models.AchievementRarity
import com.kquantum.nativeapp.presentation.theme.*
import com.kquantum.nativeapp.presentation.viewmodels.AchievementViewModel
import com.kquantum.nativeapp.presentation.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassportScreen(
    onNavigateBack: () -> Unit,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    achievementViewModel: AchievementViewModel = hiltViewModel()
) {
    val profileStats by profileViewModel.profileStats.collectAsState()
    val achievements by achievementViewModel.achievements.collectAsState()
    val recentUnlocks = achievementViewModel.recentUnlocks

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quantum Passport", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Share */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = TextPrimary,
                    navigationIconContentColor = TextPrimary,
                    actionIconContentColor = TextPrimary
                )
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Passport Header
            item {
                PassportHeader(
                    level = profileStats.level,
                    totalXp = profileStats.totalXp,
                    streak = profileStats.currentStreak,
                    achievementsCount = profileStats.achievementsUnlocked
                )
            }

            // Stats Grid
            item {
                StatsGrid(
                    lessonsCompleted = profileStats.lessonsCompleted,
                    practiceSessionsCompleted = profileStats.practiceSessionsCompleted,
                    longestStreak = profileStats.longestStreak,
                    totalStudyMinutes = profileStats.totalStudyTimeMinutes
                )
            }

            // Recent Achievements
            if (recentUnlocks.isNotEmpty()) {
                item {
                    Text(
                        text = "Recent Achievements",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }

                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(recentUnlocks) { achievement ->
                            RecentAchievementCard(achievement = achievement)
                        }
                    }
                }
            }

            // Achievement Stamps
            item {
                Text(
                    text = "Achievement Stamps",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            }

            item {
                AchievementStampsGrid(
                    achievements = achievements.filter { it.isUnlocked }
                )
            }

            // Milestones
            item {
                MilestonesSection(
                    level = profileStats.level,
                    xp = profileStats.totalXp
                )
            }
        }
    }
}

@Composable
private fun PassportHeader(
    level: Int,
    totalXp: Int,
    streak: Int,
    achievementsCount: Int
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            QuantumPurple.copy(alpha = 0.8f),
                            QuantumBlue.copy(alpha = 0.8f)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Passport badge
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚛️",
                        fontSize = 48.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "QUANTUM PASSPORT",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.8f),
                    letterSpacing = 4.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Level $level Explorer",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Quick stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PassportStat(value = "$totalXp", label = "XP")
                    PassportStat(value = "$streak", label = "Streak")
                    PassportStat(value = "$achievementsCount", label = "Badges")
                }
            }
        }
    }
}

@Composable
private fun PassportStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun StatsGrid(
    lessonsCompleted: Int,
    practiceSessionsCompleted: Int,
    longestStreak: Int,
    totalStudyMinutes: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatGridItem(
                emoji = "📚",
                value = "$lessonsCompleted",
                label = "Lessons",
                color = QuantumBlue
            )
            StatGridItem(
                emoji = "🔥",
                value = "$longestStreak",
                label = "Best Streak",
                color = QuantumOrange
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatGridItem(
                emoji = "🎯",
                value = "$practiceSessionsCompleted",
                label = "Practice",
                color = QuantumGreen
            )
            StatGridItem(
                emoji = "⏱️",
                value = "${totalStudyMinutes / 60}h",
                label = "Study Time",
                color = QuantumPurple
            )
        }
    }
}

@Composable
private fun StatGridItem(
    emoji: String,
    value: String,
    label: String,
    color: Color
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun RecentAchievementCard(achievement: Achievement) {
    val rarityColor = when (achievement.rarity) {
        AchievementRarity.COMMON -> RarityCommon
        AchievementRarity.UNCOMMON -> RarityUncommon
        AchievementRarity.RARE -> RarityRare
        AchievementRarity.EPIC -> RarityEpic
        AchievementRarity.LEGENDARY -> RarityLegendary
    }

    Surface(
        modifier = Modifier.width(120.dp),
        shape = RoundedCornerShape(16.dp),
        color = rarityColor.copy(alpha = 0.15f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = achievement.emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = achievement.title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

@Composable
private fun AchievementStampsGrid(achievements: List<Achievement>) {
    if (achievements.isEmpty()) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DarkCard
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🏆", fontSize = 32.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No achievements yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }
    } else {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DarkCard
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Group achievements in rows of 4
                achievements.chunked(4).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        row.forEach { achievement ->
                            val rarityColor = when (achievement.rarity) {
                                AchievementRarity.COMMON -> RarityCommon
                                AchievementRarity.UNCOMMON -> RarityUncommon
                                AchievementRarity.RARE -> RarityRare
                                AchievementRarity.EPIC -> RarityEpic
                                AchievementRarity.LEGENDARY -> RarityLegendary
                            }

                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(rarityColor.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = achievement.emoji,
                                    fontSize = 24.sp
                                )
                            }
                        }
                        // Fill empty spaces
                        repeat(4 - row.size) {
                            Spacer(modifier = Modifier.size(56.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun MilestonesSection(level: Int, xp: Int) {
    Column {
        Text(
            text = "Milestones",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        val milestones = listOf(
            Triple("First Steps", 100, "⭐"),
            Triple("Explorer", 500, "🌟"),
            Triple("Quantum Learner", 1000, "💫"),
            Triple("Master", 5000, "🏆"),
            Triple("Legend", 10000, "👑")
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            milestones.forEach { (name, requiredXp, emoji) ->
                val isCompleted = xp >= requiredXp
                val progress = (xp.toFloat() / requiredXp).coerceIn(0f, 1f)

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isCompleted) StatusSuccess.copy(alpha = 0.15f) else DarkCard
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = emoji, fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isCompleted) StatusSuccess else TextPrimary
                            )
                            Text(
                                text = "$requiredXp XP",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                            if (!isCompleted) {
                                Spacer(modifier = Modifier.height(8.dp))
                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp)
                                        .clip(RoundedCornerShape(2.dp)),
                                    color = QuantumBlue,
                                    trackColor = DarkCardElevated,
                                )
                            }
                        }
                        if (isCompleted) {
                            Text(
                                text = "✓",
                                style = MaterialTheme.typography.titleLarge,
                                color = StatusSuccess
                            )
                        }
                    }
                }
            }
        }
    }
}
