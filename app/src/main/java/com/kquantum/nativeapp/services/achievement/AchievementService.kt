/**
 * K-QuantumNative - Achievement Service
 * Port of QuantumNative/Services/AchievementService.swift
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.services.achievement

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kquantum.nativeapp.data.models.*
import com.kquantum.nativeapp.data.remote.ApiClient
import com.kquantum.nativeapp.data.remote.ApiResult
import com.kquantum.nativeapp.data.remote.safeApiCall
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.achievementDataStore by preferencesDataStore(name = "achievement_prefs")

@Singleton
class AchievementService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiClient: ApiClient
) {
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    companion object {
        private val KEY_ACHIEVEMENTS = stringPreferencesKey("achievements")
    }

    val unlockedCount: Int
        get() = _achievements.value.count { it.isUnlocked }

    val totalCount: Int
        get() = _achievements.value.size

    val completionPercentage: Double
        get() = if (totalCount > 0) unlockedCount.toDouble() / totalCount else 0.0

    val totalXpEarned: Int
        get() = _achievements.value.filter { it.isUnlocked }.sumOf { it.xpReward }

    val recentUnlocks: List<Achievement>
        get() = _achievements.value
            .filter { it.isUnlocked }
            .sortedByDescending { it.unlockedDate }
            .take(5)

    suspend fun loadAchievements() {
        _isLoading.value = true
        _error.value = null

        val result = safeApiCall { apiClient.api.getAchievements() }

        when (result) {
            is ApiResult.Success -> {
                _achievements.value = result.data.map { it.toAchievement() }
                saveAchievementsLocally(_achievements.value)
            }
            is ApiResult.Error -> {
                // Load from local or use defaults
                loadAchievementsLocally()
            }
            else -> {}
        }

        _isLoading.value = false
    }

    private suspend fun loadAchievementsLocally() {
        try {
            val prefs = context.achievementDataStore.data.first()
            val achievementsJson = prefs[KEY_ACHIEVEMENTS]
            if (achievementsJson != null) {
                _achievements.value = json.decodeFromString(achievementsJson)
            } else {
                _achievements.value = getDefaultAchievements()
            }
        } catch (e: Exception) {
            _achievements.value = getDefaultAchievements()
        }
    }

    private suspend fun saveAchievementsLocally(achievements: List<Achievement>) {
        try {
            context.achievementDataStore.edit { prefs ->
                prefs[KEY_ACHIEVEMENTS] = json.encodeToString(achievements)
            }
        } catch (e: Exception) {
            // Ignore
        }
    }

    suspend fun unlockAchievement(achievementId: String): Achievement? {
        val achievement = _achievements.value.find { it.id == achievementId }
        if (achievement == null || achievement.isUnlocked) return null

        val unlockedAchievement = achievement.copy(
            unlockedDate = System.currentTimeMillis()
        )

        _achievements.value = _achievements.value.map {
            if (it.id == achievementId) unlockedAchievement else it
        }

        saveAchievementsLocally(_achievements.value)

        // Sync with server
        safeApiCall { apiClient.api.unlockAchievement(achievementId) }

        return unlockedAchievement
    }

    fun getAchievementsByCategory(category: AchievementCategory): List<Achievement> {
        return _achievements.value.filter { it.category == category }
    }

    fun getAchievementsByRarity(rarity: AchievementRarity): List<Achievement> {
        return _achievements.value.filter { it.rarity == rarity }
    }

    private fun getDefaultAchievements(): List<Achievement> {
        return listOf(
            // Progress achievements
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
                id = "first_circuit",
                title = "Circuit Builder",
                description = "Build your first quantum circuit",
                emoji = "🔧",
                iconName = "build",
                xpReward = 50,
                category = AchievementCategory.PROGRESS,
                rarity = AchievementRarity.COMMON
            ),
            Achievement(
                id = "first_algorithm",
                title = "Algorithm Explorer",
                description = "Run your first quantum algorithm",
                emoji = "🧪",
                iconName = "science",
                xpReward = 75,
                category = AchievementCategory.PROGRESS,
                rarity = AchievementRarity.UNCOMMON
            ),

            // Mastery achievements
            Achievement(
                id = "hadamard_100",
                title = "Superposition Master",
                description = "Apply 100 Hadamard gates",
                emoji = "🌀",
                iconName = "blur_on",
                xpReward = 100,
                category = AchievementCategory.MASTERY,
                rarity = AchievementRarity.RARE
            ),
            Achievement(
                id = "entanglement_50",
                title = "Entanglement Expert",
                description = "Create 50 entangled states",
                emoji = "🔗",
                iconName = "link",
                xpReward = 150,
                category = AchievementCategory.MASTERY,
                rarity = AchievementRarity.EPIC
            ),
            Achievement(
                id = "all_algorithms",
                title = "Quantum Sage",
                description = "Master all quantum algorithms",
                emoji = "🧙",
                iconName = "auto_awesome",
                xpReward = 300,
                category = AchievementCategory.MASTERY,
                rarity = AchievementRarity.LEGENDARY
            ),

            // Streak achievements
            Achievement(
                id = "streak_3",
                title = "Getting Started",
                description = "3 day learning streak",
                emoji = "🔥",
                iconName = "local_fire_department",
                xpReward = 30,
                category = AchievementCategory.STREAK,
                rarity = AchievementRarity.COMMON
            ),
            Achievement(
                id = "streak_7",
                title = "Week Warrior",
                description = "7 day learning streak",
                emoji = "🔥",
                iconName = "local_fire_department",
                xpReward = 75,
                category = AchievementCategory.STREAK,
                rarity = AchievementRarity.UNCOMMON
            ),
            Achievement(
                id = "streak_30",
                title = "Monthly Master",
                description = "30 day learning streak",
                emoji = "🏆",
                iconName = "emoji_events",
                xpReward = 250,
                category = AchievementCategory.STREAK,
                rarity = AchievementRarity.EPIC
            ),
            Achievement(
                id = "streak_100",
                title = "Centurion",
                description = "100 day learning streak",
                emoji = "👑",
                iconName = "military_tech",
                xpReward = 500,
                category = AchievementCategory.STREAK,
                rarity = AchievementRarity.LEGENDARY
            ),

            // XP achievements
            Achievement(
                id = "xp_100",
                title = "Quantum Novice",
                description = "Earn 100 XP",
                emoji = "⭐",
                iconName = "star",
                xpReward = 10,
                category = AchievementCategory.XP,
                rarity = AchievementRarity.COMMON
            ),
            Achievement(
                id = "xp_1000",
                title = "Quantum Explorer",
                description = "Earn 1,000 XP",
                emoji = "🌟",
                iconName = "stars",
                xpReward = 50,
                category = AchievementCategory.XP,
                rarity = AchievementRarity.UNCOMMON
            ),
            Achievement(
                id = "xp_10000",
                title = "Quantum Master",
                description = "Earn 10,000 XP",
                emoji = "💫",
                iconName = "auto_awesome",
                xpReward = 200,
                category = AchievementCategory.XP,
                rarity = AchievementRarity.EPIC
            ),

            // Time achievements
            Achievement(
                id = "time_1h",
                title = "Dedicated Learner",
                description = "Study for 1 hour",
                emoji = "⏰",
                iconName = "schedule",
                xpReward = 25,
                category = AchievementCategory.TIME,
                rarity = AchievementRarity.COMMON
            ),
            Achievement(
                id = "time_10h",
                title = "Knowledge Seeker",
                description = "Study for 10 hours",
                emoji = "📚",
                iconName = "menu_book",
                xpReward = 100,
                category = AchievementCategory.TIME,
                rarity = AchievementRarity.RARE
            ),

            // Special achievements
            Achievement(
                id = "bridge_first",
                title = "Real Quantum",
                description = "Run on real quantum hardware",
                emoji = "🖥️",
                iconName = "computer",
                xpReward = 200,
                category = AchievementCategory.SPECIAL,
                rarity = AchievementRarity.EPIC
            ),
            Achievement(
                id = "complete_all",
                title = "Quantum Legend",
                description = "Complete all learning tracks",
                emoji = "👑",
                iconName = "military_tech",
                xpReward = 500,
                category = AchievementCategory.SPECIAL,
                rarity = AchievementRarity.LEGENDARY
            )
        )
    }

    fun clearError() {
        _error.value = null
    }
}
