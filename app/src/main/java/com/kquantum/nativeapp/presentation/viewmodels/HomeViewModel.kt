/**
 * K-QuantumNative - Home ViewModel
 * Port of QuantumNative/ViewModels/HomeViewModel.swift
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kquantum.nativeapp.data.models.*
import com.kquantum.nativeapp.services.auth.AuthService
import com.kquantum.nativeapp.services.learning.LearningService
import com.kquantum.nativeapp.services.progress.ProgressService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DailyChallenge(
    val id: String,
    val title: String,
    val description: String,
    val xpReward: Int,
    val difficulty: Difficulty,
    val isCompleted: Boolean = false
)

data class FeaturedContent(
    val id: String,
    val title: String,
    val subtitle: String,
    val iconName: String,
    val type: ContentType
)

enum class ContentType {
    LESSON, ALGORITHM, PRACTICE, ARTICLE
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authService: AuthService,
    private val progressService: ProgressService,
    private val learningService: LearningService
) : ViewModel() {

    val userProgress: StateFlow<UserProgress> = progressService.userProgress
    val isLoggedIn: StateFlow<Boolean> = authService.isLoggedIn

    private val _dailyChallenge = MutableStateFlow<DailyChallenge?>(null)
    val dailyChallenge: StateFlow<DailyChallenge?> = _dailyChallenge.asStateFlow()

    private val _featuredContent = MutableStateFlow<List<FeaturedContent>>(emptyList())
    val featuredContent: StateFlow<List<FeaturedContent>> = _featuredContent.asStateFlow()

    private val _continueLearning = MutableStateFlow<LearningLevel?>(null)
    val continueLearning: StateFlow<LearningLevel?> = _continueLearning.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _isLoading.value = true

            // Load progress
            progressService.loadProgress()
            progressService.updateStreak()

            // Load daily challenge
            loadDailyChallenge()

            // Load featured content
            loadFeaturedContent()

            // Load continue learning
            loadContinueLearning()

            _isLoading.value = false
        }
    }

    private fun loadDailyChallenge() {
        // Generate daily challenge based on date
        val today = java.time.LocalDate.now()
        val challengeIndex = today.dayOfYear % dailyChallenges.size

        _dailyChallenge.value = dailyChallenges[challengeIndex].copy(
            isCompleted = userProgress.value.dailyChallengeCompletedToday
        )
    }

    private fun loadFeaturedContent() {
        _featuredContent.value = listOf(
            FeaturedContent(
                id = "bell-state",
                title = "Bell State Tutorial",
                subtitle = "Create quantum entanglement",
                iconName = "link",
                type = ContentType.LESSON
            ),
            FeaturedContent(
                id = "grovers",
                title = "Grover's Algorithm",
                subtitle = "Quantum search speedup",
                iconName = "search",
                type = ContentType.ALGORITHM
            ),
            FeaturedContent(
                id = "bloch-sphere",
                title = "Bloch Sphere Visualizer",
                subtitle = "Interactive 3D visualization",
                iconName = "3d_rotation",
                type = ContentType.PRACTICE
            ),
            FeaturedContent(
                id = "harvard-mit",
                title = "Harvard-MIT 2025 Research",
                subtitle = "Latest quantum breakthroughs",
                iconName = "article",
                type = ContentType.ARTICLE
            )
        )
    }

    private suspend fun loadContinueLearning() {
        val tracks = learningService.tracks.value
        if (tracks.isEmpty()) {
            learningService.loadTracks()
        }

        // Find next incomplete level
        val progress = userProgress.value
        for (track in learningService.tracks.value) {
            for (level in track.levels) {
                if (level.id !in progress.completedLevels) {
                    _continueLearning.value = level
                    return
                }
            }
        }
    }

    suspend fun completeDailyChallenge() {
        _dailyChallenge.value?.let { challenge ->
            if (!challenge.isCompleted) {
                progressService.addXp(challenge.xpReward, "Daily Challenge: ${challenge.title}")
                _dailyChallenge.value = challenge.copy(isCompleted = true)
            }
        }
    }

    val userName: String
        get() = authService.currentUser.value?.username ?: userProgress.value.userName

    val totalXp: Int
        get() = userProgress.value.totalXp

    val currentLevel: Int
        get() = userProgress.value.userLevel

    val currentStreak: Int
        get() = userProgress.value.currentStreak

    val levelProgress: Float
        get() = userProgress.value.levelProgress.toFloat()

    val recentAchievementsCount: Int
        get() = userProgress.value.achievements.size

    fun startDailyChallenge() {
        viewModelScope.launch {
            completeDailyChallenge()
        }
    }

    companion object {
        private val dailyChallenges = listOf(
            DailyChallenge(
                id = "dc-superposition",
                title = "Superposition Challenge",
                description = "Create a qubit in equal superposition",
                xpReward = 50,
                difficulty = Difficulty.BEGINNER
            ),
            DailyChallenge(
                id = "dc-bell",
                title = "Bell State Challenge",
                description = "Create a Bell state with two qubits",
                xpReward = 75,
                difficulty = Difficulty.INTERMEDIATE
            ),
            DailyChallenge(
                id = "dc-grover",
                title = "Grover's Search",
                description = "Implement Grover's algorithm for 2 qubits",
                xpReward = 100,
                difficulty = Difficulty.ADVANCED
            ),
            DailyChallenge(
                id = "dc-teleport",
                title = "Quantum Teleportation",
                description = "Implement quantum teleportation",
                xpReward = 100,
                difficulty = Difficulty.ADVANCED
            ),
            DailyChallenge(
                id = "dc-circuit",
                title = "Circuit Builder",
                description = "Build a 3-qubit circuit with 5 gates",
                xpReward = 60,
                difficulty = Difficulty.BEGINNER
            ),
            DailyChallenge(
                id = "dc-measure",
                title = "Measurement Mystery",
                description = "Predict measurement outcomes",
                xpReward = 50,
                difficulty = Difficulty.BEGINNER
            ),
            DailyChallenge(
                id = "dc-phase",
                title = "Phase Kickback",
                description = "Demonstrate phase kickback",
                xpReward = 75,
                difficulty = Difficulty.INTERMEDIATE
            )
        )
    }
}
