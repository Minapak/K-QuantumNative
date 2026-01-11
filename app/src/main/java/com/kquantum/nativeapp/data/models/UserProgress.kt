/**
 * K-QuantumNative - User Progress Models
 * Port of QuantumNative/Models/UserProgress.swift
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.min

@Serializable
data class UserProgress(
    @SerialName("total_xp")
    val totalXp: Int = 0,
    @SerialName("current_level")
    val currentLevel: Int = 1,
    @SerialName("completed_levels")
    val completedLevels: Set<String> = emptySet(),
    val achievements: List<String> = emptyList(),
    @SerialName("current_streak")
    val currentStreak: Int = 0,
    @SerialName("last_active_date")
    val lastActiveDate: String? = null,
    @SerialName("study_time_minutes")
    val studyTimeMinutes: Int = 0,
    @SerialName("practice_sessions_completed")
    val practiceSessionsCompleted: Int = 0,
    @SerialName("user_name")
    val userName: String = "Quantum Explorer",
    @SerialName("longest_streak")
    val longestStreak: Int = 0,
    @SerialName("current_level_id")
    val currentLevelId: String? = null,
    @SerialName("current_level_progress")
    val currentLevelProgress: Double = 0.0,
    @SerialName("daily_challenge_completed_today")
    val dailyChallengeCompletedToday: Boolean = false,
    @SerialName("last_daily_challenge_date")
    val lastDailyChallengeDate: String? = null,
    @SerialName("lessons_completed")
    val lessonsCompleted: Int = 0,
    @SerialName("total_study_time_minutes")
    val totalStudyTimeMinutes: Int = 0
) {
    companion object {
        private val XP_PER_LEVEL = listOf(
            0, 100, 250, 500, 800, 1200, 1700, 2300, 3000, 3800,
            4700, 5700, 6800, 8000, 9300, 10700, 12200, 13800, 15500, 17300,
            19200, 21200, 23300, 25500, 27800, 30200, 32700, 35300, 38000, 40800
        )

        fun xpForLevel(level: Int): Int {
            return if (level <= 0) 0
            else if (level <= XP_PER_LEVEL.size) XP_PER_LEVEL[level - 1]
            else XP_PER_LEVEL.last() + (level - XP_PER_LEVEL.size) * 3000
        }
    }

    val userLevel: Int
        get() {
            var level = 1
            for (i in XP_PER_LEVEL.indices) {
                if (totalXp >= XP_PER_LEVEL[i]) level = i + 1
                else break
            }
            return level
        }

    // Alias for userLevel (used by ProfileViewModel)
    val level: Int
        get() = userLevel

    val xpForNextLevel: Int
        get() = xpForLevel(userLevel + 1)

    val xpUntilNextLevel: Int
        get() = xpForNextLevel - totalXp

    // Alias for xpUntilNextLevel (used by ProfileViewModel)
    val xpToNextLevel: Int
        get() = xpUntilNextLevel

    val levelProgress: Float
        get() {
            val currentLevelXp = xpForLevel(userLevel)
            val nextLevelXp = xpForLevel(userLevel + 1)
            val range = nextLevelXp - currentLevelXp
            if (range <= 0) return 1.0f
            return min(1.0f, (totalXp - currentLevelXp).toFloat() / range)
        }

    val studyTimeText: String
        get() {
            val hours = studyTimeMinutes / 60
            val minutes = studyTimeMinutes % 60
            return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
        }

    val levelTitle: String
        get() = when (userLevel) {
            in 1..5 -> "Quantum Novice"
            in 6..10 -> "Quantum Explorer"
            in 11..15 -> "Quantum Researcher"
            in 16..20 -> "Quantum Scientist"
            in 21..25 -> "Quantum Master"
            else -> "Quantum Legend"
        }
}

@Serializable
data class ProgressUpdateRequest(
    @SerialName("xp_gained")
    val xpGained: Int,
    val reason: String? = null
)

@Serializable
data class LevelCompleteRequest(
    @SerialName("level_id")
    val levelId: String,
    @SerialName("xp_earned")
    val xpEarned: Int
)
