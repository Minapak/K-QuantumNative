/**
 * K-QuantumNative - Home Screen
 * Port of QuantumNative HomeView
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kquantum.nativeapp.presentation.theme.*
import com.kquantum.nativeapp.presentation.viewmodels.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToLearn: () -> Unit,
    onNavigateToPractice: () -> Unit,
    onNavigateToExplore: () -> Unit,
    onNavigateToBridge: () -> Unit,
    onNavigateToAchievements: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSubscription: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val userProgress by viewModel.userProgress.collectAsState()
    val dailyChallenge by viewModel.dailyChallenge.collectAsState()
    val featuredContent by viewModel.featuredContent.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Header
            item {
                HomeHeader(
                    level = userProgress.level,
                    xp = userProgress.totalXp,
                    streak = userProgress.currentStreak,
                    onProfileClick = onNavigateToProfile
                )
            }

            // Quick Actions
            item {
                QuickActionsSection(
                    onLearnClick = onNavigateToLearn,
                    onPracticeClick = onNavigateToPractice,
                    onExploreClick = onNavigateToExplore,
                    onBridgeClick = onNavigateToBridge
                )
            }

            // Daily Challenge
            item {
                DailyChallengeCard(
                    challenge = dailyChallenge,
                    onStartChallenge = { viewModel.startDailyChallenge() }
                )
            }

            // Progress Section
            item {
                ProgressSection(
                    level = userProgress.level,
                    progress = userProgress.levelProgress,
                    xpToNext = userProgress.xpToNextLevel
                )
            }

            // Featured Content
            item {
                FeaturedContentSection(
                    content = featuredContent,
                    onContentClick = { onNavigateToExplore() }
                )
            }

            // Achievements Preview
            item {
                AchievementsPreview(
                    achievementCount = viewModel.recentAchievementsCount,
                    onViewAll = onNavigateToAchievements
                )
            }
        }
    }
}

@Composable
private fun HomeHeader(
    level: Int,
    xp: Int,
    streak: Int,
    onProfileClick: () -> Unit
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
                text = "K-Quantum",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🔥", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$streak day streak",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // XP Badge
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = DarkCard
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "⭐", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$xp XP",
                        style = MaterialTheme.typography.labelLarge,
                        color = QuantumYellow,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Profile Button
            IconButton(
                onClick = onProfileClick,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(DarkCard)
            ) {
                Text(
                    text = "Lv$level",
                    style = MaterialTheme.typography.labelMedium,
                    color = QuantumBlue,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun QuickActionsSection(
    onLearnClick: () -> Unit,
    onPracticeClick: () -> Unit,
    onExploreClick: () -> Unit,
    onBridgeClick: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                emoji = "📚",
                title = "Learn",
                color = QuantumBlue,
                modifier = Modifier.weight(1f),
                onClick = onLearnClick
            )
            QuickActionCard(
                emoji = "🎯",
                title = "Practice",
                color = QuantumGreen,
                modifier = Modifier.weight(1f),
                onClick = onPracticeClick
            )
            QuickActionCard(
                emoji = "🔭",
                title = "Explore",
                color = QuantumPurple,
                modifier = Modifier.weight(1f),
                onClick = onExploreClick
            )
            QuickActionCard(
                emoji = "🖥️",
                title = "Bridge",
                color = QuantumCyan,
                modifier = Modifier.weight(1f),
                onClick = onBridgeClick
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    emoji: String,
    title: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = color,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun DailyChallengeCard(
    challenge: com.kquantum.nativeapp.presentation.viewmodels.DailyChallenge?,
    onStartChallenge: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            QuantumPurple.copy(alpha = 0.8f),
                            QuantumBlue.copy(alpha = 0.8f)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🎯", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Daily Challenge",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = challenge?.description ?: "Complete a quantum circuit",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "+${challenge?.xpReward ?: 50} XP",
                        style = MaterialTheme.typography.labelMedium,
                        color = QuantumYellow,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = onStartChallenge,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Start", color = TextPrimary)
                }
            }
        }
    }
}

@Composable
private fun ProgressSection(
    level: Int,
    progress: Float,
    xpToNext: Int
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        color = DarkCard
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Level $level",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "$xpToNext XP to Level ${level + 1}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { progress },
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
private fun FeaturedContentSection(
    content: List<com.kquantum.nativeapp.presentation.viewmodels.FeaturedContent>,
    onContentClick: (String) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Featured",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(content) { item ->
                val (emoji, color) = when (item.type) {
                    com.kquantum.nativeapp.presentation.viewmodels.ContentType.LESSON -> "📚" to QuantumBlue
                    com.kquantum.nativeapp.presentation.viewmodels.ContentType.ALGORITHM -> "🔍" to QuantumPurple
                    com.kquantum.nativeapp.presentation.viewmodels.ContentType.PRACTICE -> "🎯" to QuantumGreen
                    com.kquantum.nativeapp.presentation.viewmodels.ContentType.ARTICLE -> "📰" to QuantumPink
                }
                FeaturedCard(
                    title = item.title,
                    emoji = emoji,
                    color = color,
                    onClick = { onContentClick(item.id) }
                )
            }
        }
    }
}

@Composable
private fun FeaturedCard(
    title: String,
    emoji: String,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(160.dp)
            .height(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = emoji, fontSize = 32.sp)
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = color,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun AchievementsPreview(
    achievementCount: Int,
    onViewAll: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onViewAll),
        shape = RoundedCornerShape(16.dp),
        color = DarkCard
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🏆", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Achievements",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "$achievementCount unlocked",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextTertiary
            )
        }
    }
}
