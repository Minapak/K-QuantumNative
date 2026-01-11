/**
 * K-QuantumNative - Achievements Screen
 * Port of QuantumNative AchievementsView
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kquantum.nativeapp.data.models.Achievement
import com.kquantum.nativeapp.data.models.AchievementCategory
import com.kquantum.nativeapp.data.models.AchievementRarity
import com.kquantum.nativeapp.presentation.theme.*
import com.kquantum.nativeapp.presentation.viewmodels.AchievementViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(
    onNavigateBack: () -> Unit,
    viewModel: AchievementViewModel = hiltViewModel()
) {
    val achievements by viewModel.achievements.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Achievements", fontWeight = FontWeight.Bold) },
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
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                // Stats header
                item {
                    AchievementStatsHeader(
                        unlockedCount = viewModel.unlockedCount,
                        totalCount = viewModel.totalCount,
                        completionPercentage = viewModel.completionPercentage,
                        totalXpEarned = viewModel.totalXpEarned
                    )
                }

                // Category filter
                item {
                    CategoryFilterRow(
                        selectedCategory = selectedCategory,
                        onCategorySelect = { viewModel.selectCategory(it) }
                    )
                }

                // Achievements list
                val filteredAchievements = viewModel.filteredAchievements
                val groupedAchievements = filteredAchievements.groupBy { it.category }

                groupedAchievements.forEach { (category, categoryAchievements) ->
                    item {
                        Text(
                            text = category.name.replace("_", " "),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    items(categoryAchievements) { achievement ->
                        AchievementCard(achievement = achievement)
                    }
                }
            }
        }
    }
}

@Composable
private fun AchievementStatsHeader(
    unlockedCount: Int,
    totalCount: Int,
    completionPercentage: Double,
    totalXpEarned: Int
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        color = DarkCard
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "🏆", fontSize = 48.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "$unlockedCount / $totalCount",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = "Achievements Unlocked",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = { completionPercentage.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = QuantumPurple,
                trackColor = DarkCardElevated,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${(completionPercentage * 100).toInt()}%",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = QuantumPurple
                    )
                    Text(
                        text = "Complete",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$totalXpEarned",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = QuantumYellow
                    )
                    Text(
                        text = "XP Earned",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryFilterRow(
    selectedCategory: AchievementCategory?,
    onCategorySelect: (AchievementCategory?) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategorySelect(null) },
                label = { Text("All") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = QuantumBlue.copy(alpha = 0.2f),
                    selectedLabelColor = QuantumBlue
                )
            )
        }
        items(AchievementCategory.entries) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategorySelect(category) },
                label = { Text(category.name.replace("_", " ")) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = QuantumBlue.copy(alpha = 0.2f),
                    selectedLabelColor = QuantumBlue
                )
            )
        }
    }
}

@Composable
private fun AchievementCard(achievement: Achievement) {
    val rarityColor = when (achievement.rarity) {
        AchievementRarity.COMMON -> RarityCommon
        AchievementRarity.UNCOMMON -> RarityUncommon
        AchievementRarity.RARE -> RarityRare
        AchievementRarity.EPIC -> RarityEpic
        AchievementRarity.LEGENDARY -> RarityLegendary
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .alpha(if (achievement.isUnlocked) 1f else 0.6f),
        shape = RoundedCornerShape(16.dp),
        color = DarkCard
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Achievement icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(rarityColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = achievement.emoji,
                    fontSize = 28.sp
                )
                if (achievement.isUnlocked) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 4.dp, y = 4.dp)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(StatusSuccess),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = achievement.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = rarityColor.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = achievement.rarity.name,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = rarityColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = achievement.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "⭐", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "+${achievement.xpReward} XP",
                        style = MaterialTheme.typography.labelSmall,
                        color = QuantumYellow,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
