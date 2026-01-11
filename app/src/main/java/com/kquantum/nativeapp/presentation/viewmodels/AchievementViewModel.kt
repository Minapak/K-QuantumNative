/**
 * K-QuantumNative - Achievement ViewModel
 * Port of QuantumNative AchievementView integration
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kquantum.nativeapp.data.models.*
import com.kquantum.nativeapp.services.achievement.AchievementService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AchievementViewModel @Inject constructor(
    private val achievementService: AchievementService
) : ViewModel() {

    val achievements: StateFlow<List<Achievement>> = achievementService.achievements
    val isLoading: StateFlow<Boolean> = achievementService.isLoading
    val error: StateFlow<String?> = achievementService.error

    private val _selectedCategory = MutableStateFlow<AchievementCategory?>(null)
    val selectedCategory: StateFlow<AchievementCategory?> = _selectedCategory.asStateFlow()

    private val _selectedRarity = MutableStateFlow<AchievementRarity?>(null)
    val selectedRarity: StateFlow<AchievementRarity?> = _selectedRarity.asStateFlow()

    private val _newlyUnlockedAchievement = MutableStateFlow<Achievement?>(null)
    val newlyUnlockedAchievement: StateFlow<Achievement?> = _newlyUnlockedAchievement.asStateFlow()

    init {
        loadAchievements()
    }

    fun loadAchievements() {
        viewModelScope.launch {
            achievementService.loadAchievements()
        }
    }

    fun selectCategory(category: AchievementCategory?) {
        _selectedCategory.value = category
    }

    fun selectRarity(rarity: AchievementRarity?) {
        _selectedRarity.value = rarity
    }

    val filteredAchievements: List<Achievement>
        get() {
            var result = achievements.value

            _selectedCategory.value?.let { category ->
                result = result.filter { it.category == category }
            }

            _selectedRarity.value?.let { rarity ->
                result = result.filter { it.rarity == rarity }
            }

            return result
        }

    val unlockedCount: Int
        get() = achievementService.unlockedCount

    val totalCount: Int
        get() = achievementService.totalCount

    val completionPercentage: Double
        get() = achievementService.completionPercentage

    val totalXpEarned: Int
        get() = achievementService.totalXpEarned

    val recentUnlocks: List<Achievement>
        get() = achievementService.recentUnlocks

    fun getAchievementsByCategory(category: AchievementCategory): List<Achievement> {
        return achievementService.getAchievementsByCategory(category)
    }

    fun getAchievementsByRarity(rarity: AchievementRarity): List<Achievement> {
        return achievementService.getAchievementsByRarity(rarity)
    }

    fun unlockAchievement(achievementId: String) {
        viewModelScope.launch {
            val unlocked = achievementService.unlockAchievement(achievementId)
            if (unlocked != null) {
                _newlyUnlockedAchievement.value = unlocked
            }
        }
    }

    fun dismissUnlockNotification() {
        _newlyUnlockedAchievement.value = null
    }

    fun clearError() {
        achievementService.clearError()
    }

    // Stats for display
    val categoryStats: Map<AchievementCategory, Pair<Int, Int>>
        get() {
            val stats = mutableMapOf<AchievementCategory, Pair<Int, Int>>()
            AchievementCategory.entries.forEach { category ->
                val categoryAchievements = achievements.value.filter { it.category == category }
                val unlocked = categoryAchievements.count { it.isUnlocked }
                val total = categoryAchievements.size
                stats[category] = Pair(unlocked, total)
            }
            return stats
        }

    val rarityStats: Map<AchievementRarity, Pair<Int, Int>>
        get() {
            val stats = mutableMapOf<AchievementRarity, Pair<Int, Int>>()
            AchievementRarity.entries.forEach { rarity ->
                val rarityAchievements = achievements.value.filter { it.rarity == rarity }
                val unlocked = rarityAchievements.count { it.isUnlocked }
                val total = rarityAchievements.size
                stats[rarity] = Pair(unlocked, total)
            }
            return stats
        }
}
