/**
 * K-QuantumNative - Achievement Models
 * Port of QuantumNative/Models/Achievement.swift
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.data.models

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Serializable
enum class AchievementCategory {
    @SerialName("progress")
    PROGRESS,
    @SerialName("streak")
    STREAK,
    @SerialName("xp")
    XP,
    @SerialName("time")
    TIME,
    @SerialName("mastery")
    MASTERY,
    @SerialName("special")
    SPECIAL;

    val displayName: String
        get() = name.lowercase().replaceFirstChar { it.uppercase() }

    val iconName: String
        get() = when (this) {
            PROGRESS -> "trending_up"
            STREAK -> "local_fire_department"
            XP -> "stars"
            TIME -> "schedule"
            MASTERY -> "military_tech"
            SPECIAL -> "auto_awesome"
        }
}

@Serializable
enum class AchievementRarity {
    @SerialName("common")
    COMMON,
    @SerialName("uncommon")
    UNCOMMON,
    @SerialName("rare")
    RARE,
    @SerialName("epic")
    EPIC,
    @SerialName("legendary")
    LEGENDARY;

    val displayName: String
        get() = name.lowercase().replaceFirstChar { it.uppercase() }

    val color: Color
        get() = when (this) {
            COMMON -> Color(0xFF9E9E9E)
            UNCOMMON -> Color(0xFF4CAF50)
            RARE -> Color(0xFF2196F3)
            EPIC -> Color(0xFF9C27B0)
            LEGENDARY -> Color(0xFFFF9800)
        }

    val gradientColors: List<Color>
        get() = when (this) {
            COMMON -> listOf(Color(0xFF757575), Color(0xFF9E9E9E))
            UNCOMMON -> listOf(Color(0xFF388E3C), Color(0xFF4CAF50))
            RARE -> listOf(Color(0xFF1976D2), Color(0xFF2196F3))
            EPIC -> listOf(Color(0xFF7B1FA2), Color(0xFF9C27B0))
            LEGENDARY -> listOf(Color(0xFFF57C00), Color(0xFFFFB74D))
        }

    val gradient: Brush
        get() = Brush.linearGradient(gradientColors)
}

@Serializable
data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String = "🏆",
    @SerialName("icon_name")
    val iconName: String = "emoji_events",
    @SerialName("xp_reward")
    val xpReward: Int = 50,
    val category: AchievementCategory = AchievementCategory.PROGRESS,
    val rarity: AchievementRarity = AchievementRarity.COMMON,
    @SerialName("unlocked_date")
    val unlockedDate: Long? = null
) {
    val isUnlocked: Boolean
        get() = unlockedDate != null

    val unlockDateText: String?
        get() = unlockedDate?.let {
            val dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(it),
                ZoneId.systemDefault()
            )
            dateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
        }

    companion object {
        val sampleAchievements = listOf(
            Achievement(
                id = "first_qubit",
                title = "First Qubit",
                description = "Create your first qubit",
                emoji = "⚛️",
                iconName = "science",
                xpReward = 25,
                category = AchievementCategory.PROGRESS,
                rarity = AchievementRarity.COMMON
            ),
            Achievement(
                id = "superposition_master",
                title = "Superposition Master",
                description = "Apply 100 Hadamard gates",
                emoji = "🌀",
                iconName = "blur_on",
                xpReward = 100,
                category = AchievementCategory.MASTERY,
                rarity = AchievementRarity.RARE
            ),
            Achievement(
                id = "entanglement_expert",
                title = "Entanglement Expert",
                description = "Create 50 Bell states",
                emoji = "🔗",
                iconName = "link",
                xpReward = 150,
                category = AchievementCategory.MASTERY,
                rarity = AchievementRarity.EPIC
            ),
            Achievement(
                id = "quantum_legend",
                title = "Quantum Legend",
                description = "Complete all learning tracks",
                emoji = "👑",
                iconName = "military_tech",
                xpReward = 500,
                category = AchievementCategory.SPECIAL,
                rarity = AchievementRarity.LEGENDARY
            ),
            Achievement(
                id = "week_streak",
                title = "Week Warrior",
                description = "7 day learning streak",
                emoji = "🔥",
                iconName = "local_fire_department",
                xpReward = 75,
                category = AchievementCategory.STREAK,
                rarity = AchievementRarity.UNCOMMON
            ),
            Achievement(
                id = "month_streak",
                title = "Monthly Master",
                description = "30 day learning streak",
                emoji = "🏆",
                iconName = "emoji_events",
                xpReward = 250,
                category = AchievementCategory.STREAK,
                rarity = AchievementRarity.EPIC
            )
        )
    }
}

@Serializable
data class AchievementResponse(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String? = null,
    @SerialName("icon_name")
    val iconName: String? = null,
    @SerialName("xp_reward")
    val xpReward: Int? = null,
    val category: String? = null,
    val rarity: String? = null,
    @SerialName("unlocked_at")
    val unlockedAt: String? = null
) {
    fun toAchievement(): Achievement {
        val categoryEnum = try {
            AchievementCategory.valueOf(category?.uppercase() ?: "PROGRESS")
        } catch (_: Exception) {
            AchievementCategory.PROGRESS
        }

        val rarityEnum = try {
            AchievementRarity.valueOf(rarity?.uppercase() ?: "COMMON")
        } catch (_: Exception) {
            AchievementRarity.COMMON
        }

        return Achievement(
            id = id,
            title = title,
            description = description,
            emoji = emoji ?: "🏆",
            iconName = iconName ?: "emoji_events",
            xpReward = xpReward ?: 50,
            category = categoryEnum,
            rarity = rarityEnum,
            unlockedDate = unlockedAt?.let {
                try {
                    Instant.parse(it).toEpochMilli()
                } catch (_: Exception) {
                    null
                }
            }
        )
    }
}
