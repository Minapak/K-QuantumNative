/**
 * K-QuantumNative - Profile ViewModel
 * Port of QuantumNative ProfileView integration
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kquantum.nativeapp.data.models.*
import com.kquantum.nativeapp.services.auth.AuthService
import com.kquantum.nativeapp.services.progress.ProgressService
import com.kquantum.nativeapp.services.achievement.AchievementService
import com.kquantum.nativeapp.services.billing.BillingService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileStats(
    val totalXp: Int = 0,
    val level: Int = 1,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val achievementsUnlocked: Int = 0,
    val totalAchievements: Int = 0,
    val lessonsCompleted: Int = 0,
    val practiceSessionsCompleted: Int = 0,
    val totalStudyTimeMinutes: Int = 0
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authService: AuthService,
    private val progressService: ProgressService,
    private val achievementService: AchievementService,
    private val billingService: BillingService
) : ViewModel() {

    val currentUser: StateFlow<UserResponse?> = authService.currentUser
    val userProgress: StateFlow<UserProgress> = progressService.userProgress
    val isLoading: StateFlow<Boolean> = authService.isLoading

    private val _profileStats = MutableStateFlow(ProfileStats())
    val profileStats: StateFlow<ProfileStats> = _profileStats.asStateFlow()

    private val _showLogoutConfirmation = MutableStateFlow(false)
    val showLogoutConfirmation: StateFlow<Boolean> = _showLogoutConfirmation.asStateFlow()

    private val _showDeleteAccountConfirmation = MutableStateFlow(false)
    val showDeleteAccountConfirmation: StateFlow<Boolean> = _showDeleteAccountConfirmation.asStateFlow()

    private val _editingProfile = MutableStateFlow(false)
    val editingProfile: StateFlow<Boolean> = _editingProfile.asStateFlow()

    private val _selectedTab = MutableStateFlow(ProfileTab.STATS)
    val selectedTab: StateFlow<ProfileTab> = _selectedTab.asStateFlow()

    enum class ProfileTab {
        STATS, ACHIEVEMENTS, SETTINGS
    }

    init {
        loadProfileData()
        observeProgress()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            progressService.loadProgress()
            achievementService.loadAchievements()
            updateStats()
        }
    }

    private fun observeProgress() {
        viewModelScope.launch {
            combine(
                progressService.userProgress,
                achievementService.achievements
            ) { progress, achievements ->
                ProfileStats(
                    totalXp = progress.totalXp,
                    level = progress.level,
                    currentStreak = progress.currentStreak,
                    longestStreak = progress.longestStreak,
                    achievementsUnlocked = achievements.count { it.isUnlocked },
                    totalAchievements = achievements.size,
                    lessonsCompleted = progress.lessonsCompleted,
                    practiceSessionsCompleted = progress.practiceSessionsCompleted,
                    totalStudyTimeMinutes = progress.totalStudyTimeMinutes
                )
            }.collect { stats ->
                _profileStats.value = stats
            }
        }
    }

    private fun updateStats() {
        val progress = progressService.userProgress.value
        val achievements = achievementService.achievements.value

        _profileStats.value = ProfileStats(
            totalXp = progress.totalXp,
            level = progress.level,
            currentStreak = progress.currentStreak,
            longestStreak = progress.longestStreak,
            achievementsUnlocked = achievements.count { it.isUnlocked },
            totalAchievements = achievements.size,
            lessonsCompleted = progress.lessonsCompleted,
            practiceSessionsCompleted = progress.practiceSessionsCompleted,
            totalStudyTimeMinutes = progress.totalStudyTimeMinutes
        )
    }

    fun selectTab(tab: ProfileTab) {
        _selectedTab.value = tab
    }

    fun startEditingProfile() {
        _editingProfile.value = true
    }

    fun cancelEditingProfile() {
        _editingProfile.value = false
    }

    fun updateProfile(displayName: String?, avatarUrl: String?) {
        viewModelScope.launch {
            // Update profile via API
            _editingProfile.value = false
        }
    }

    fun showLogoutConfirmation() {
        _showLogoutConfirmation.value = true
    }

    fun dismissLogoutConfirmation() {
        _showLogoutConfirmation.value = false
    }

    fun logout() {
        viewModelScope.launch {
            authService.logout()
            _showLogoutConfirmation.value = false
        }
    }

    fun showDeleteAccountConfirmation() {
        _showDeleteAccountConfirmation.value = true
    }

    fun dismissDeleteAccountConfirmation() {
        _showDeleteAccountConfirmation.value = false
    }

    fun deleteAccount() {
        viewModelScope.launch {
            // Delete account via API
            authService.logout()
            _showDeleteAccountConfirmation.value = false
        }
    }

    val isPremium: Boolean
        get() = billingService.isPremium

    val subscriptionTier: SubscriptionTier
        get() = billingService.currentTier

    val formattedStudyTime: String
        get() {
            val minutes = _profileStats.value.totalStudyTimeMinutes
            val hours = minutes / 60
            val remainingMinutes = minutes % 60
            return when {
                hours > 0 -> "${hours}h ${remainingMinutes}m"
                else -> "${minutes}m"
            }
        }

    val levelProgress: Float
        get() = progressService.userProgress.value.levelProgress

    val xpToNextLevel: Int
        get() = progressService.userProgress.value.xpToNextLevel

    val recentAchievements: List<Achievement>
        get() = achievementService.recentUnlocks
}
