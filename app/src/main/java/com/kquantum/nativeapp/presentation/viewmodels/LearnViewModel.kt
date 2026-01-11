/**
 * K-QuantumNative - Learn ViewModel
 * Port of QuantumNative/ViewModels/LearnViewModel.swift
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kquantum.nativeapp.data.models.*
import com.kquantum.nativeapp.services.learning.LearningService
import com.kquantum.nativeapp.services.progress.ProgressService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LearnViewModel @Inject constructor(
    private val learningService: LearningService,
    private val progressService: ProgressService
) : ViewModel() {

    val tracks: StateFlow<List<LearningTrack>> = learningService.tracks
    val isLoading: StateFlow<Boolean> = learningService.isLoading
    val error: StateFlow<String?> = learningService.error

    private val _currentTrack = MutableStateFlow(Track.BEGINNER)
    val currentTrack: StateFlow<Track> = _currentTrack.asStateFlow()

    private val _currentLevel = MutableStateFlow<LearningLevel?>(null)
    val currentLevel: StateFlow<LearningLevel?> = _currentLevel.asStateFlow()

    private val _availableLevels = MutableStateFlow<List<LearningLevel>>(emptyList())
    val availableLevels: StateFlow<List<LearningLevel>> = _availableLevels.asStateFlow()

    // Learning strategies
    private val _selectedStrategy = MutableStateFlow(LearningStrategy.MEMORY)
    val selectedStrategy: StateFlow<LearningStrategy> = _selectedStrategy.asStateFlow()

    private val _memoryTriggers = MutableStateFlow<List<MemoryTrigger>>(emptyList())
    val memoryTriggers: StateFlow<List<MemoryTrigger>> = _memoryTriggers.asStateFlow()

    private val _feynmanExplanation = MutableStateFlow<FeynmanExplanation?>(null)
    val feynmanExplanation: StateFlow<FeynmanExplanation?> = _feynmanExplanation.asStateFlow()

    private val _learningPace = MutableStateFlow(LearningPace.BALANCED)
    val learningPace: StateFlow<LearningPace> = _learningPace.asStateFlow()

    private val _showHints = MutableStateFlow(true)
    val showHints: StateFlow<Boolean> = _showHints.asStateFlow()

    val completedLevels: Set<String>
        get() = progressService.userProgress.value.completedLevels

    init {
        loadTracks()
    }

    fun loadTracks() {
        viewModelScope.launch {
            learningService.loadTracks()
            loadLevelsForCurrentTrack()
        }
    }

    fun selectTrack(track: Track) {
        _currentTrack.value = track
        viewModelScope.launch {
            loadLevelsForCurrentTrack()
        }
    }

    private suspend fun loadLevelsForCurrentTrack() {
        _availableLevels.value = learningService.loadLevelsForTrack(_currentTrack.value)
    }

    fun selectLevel(level: LearningLevel) {
        _currentLevel.value = level
        loadLearningStrategies(level)
    }

    fun clearCurrentLevel() {
        _currentLevel.value = null
    }

    private fun loadLearningStrategies(level: LearningLevel) {
        _memoryTriggers.value = learningService.generateMemoryTriggers(level)
        _feynmanExplanation.value = learningService.generateFeynmanExplanation(level)
    }

    fun setStrategy(strategy: LearningStrategy) {
        _selectedStrategy.value = strategy
    }

    fun setLearningPace(pace: LearningPace) {
        _learningPace.value = pace
    }

    fun toggleHints() {
        _showHints.value = !_showHints.value
    }

    suspend fun completeLevel(level: LearningLevel) {
        progressService.completeLevel(level.id, level.xpReward)
        _currentLevel.value = null
        loadLevelsForCurrentTrack()
    }

    fun isLevelUnlocked(level: LearningLevel): Boolean {
        if (level.prerequisites.isEmpty()) return true
        return level.prerequisites.all { it in completedLevels }
    }

    fun isLevelCompleted(levelId: String): Boolean {
        return progressService.isLevelCompleted(levelId)
    }

    fun getLevelProgress(track: Track): Double {
        val levels = _availableLevels.value.filter { it.trackEnum == track }
        if (levels.isEmpty()) return 0.0
        val completed = levels.count { isLevelCompleted(it.id) }
        return completed.toDouble() / levels.size
    }

    fun clearError() {
        learningService.clearError()
    }
}
