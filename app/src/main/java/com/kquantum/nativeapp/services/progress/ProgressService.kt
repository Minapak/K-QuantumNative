/**
 * K-QuantumNative - Progress Service
 * Port of QuantumNative/Services/ProgressService.swift
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.services.progress

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
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

private val Context.progressDataStore: DataStore<Preferences> by preferencesDataStore(name = "progress_prefs")

@Singleton
class ProgressService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiClient: ApiClient
) {
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    private val _userProgress = MutableStateFlow(UserProgress())
    val userProgress: StateFlow<UserProgress> = _userProgress.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    companion object {
        private val KEY_PROGRESS = stringPreferencesKey("user_progress")
        private val KEY_LAST_SYNC = longPreferencesKey("last_sync")
    }

    suspend fun loadProgress() {
        _isLoading.value = true

        // Try to load from server first
        val serverResult = safeApiCall { apiClient.api.getUserStats() }

        when (serverResult) {
            is ApiResult.Success -> {
                val stats = serverResult.data
                val progress = UserProgress(
                    totalXp = stats.totalXp,
                    currentLevel = stats.currentLevel,
                    currentStreak = stats.currentStreak,
                    longestStreak = stats.longestStreak,
                    studyTimeMinutes = stats.totalStudyTimeMinutes
                )
                _userProgress.value = progress
                saveProgressLocally(progress)
            }
            is ApiResult.Error -> {
                // Load from local storage
                loadProgressLocally()
            }
            else -> {}
        }

        _isLoading.value = false
    }

    private suspend fun loadProgressLocally() {
        try {
            val prefs = context.progressDataStore.data.first()
            val progressJson = prefs[KEY_PROGRESS]
            if (progressJson != null) {
                _userProgress.value = json.decodeFromString(progressJson)
            }
        } catch (e: Exception) {
            // Use default progress
            _userProgress.value = UserProgress()
        }
    }

    private suspend fun saveProgressLocally(progress: UserProgress) {
        try {
            context.progressDataStore.edit { prefs ->
                prefs[KEY_PROGRESS] = json.encodeToString(progress)
                prefs[KEY_LAST_SYNC] = System.currentTimeMillis()
            }
        } catch (e: Exception) {
            // Ignore save errors
        }
    }

    suspend fun addXp(amount: Int, reason: String? = null): Boolean {
        val currentProgress = _userProgress.value
        val newProgress = currentProgress.copy(
            totalXp = currentProgress.totalXp + amount
        )

        _userProgress.value = newProgress
        saveProgressLocally(newProgress)

        // Sync with server
        safeApiCall {
            apiClient.api.addXp(ProgressUpdateRequest(amount, reason))
        }

        return true
    }

    suspend fun completeLevel(levelId: String, xpReward: Int) {
        val currentProgress = _userProgress.value
        val newCompletedLevels = currentProgress.completedLevels + levelId
        val newProgress = currentProgress.copy(
            totalXp = currentProgress.totalXp + xpReward,
            completedLevels = newCompletedLevels
        )

        _userProgress.value = newProgress
        saveProgressLocally(newProgress)

        // Sync with server
        safeApiCall {
            apiClient.api.completeLevel(levelId, LevelCompleteRequest(levelId, xpReward))
        }
    }

    suspend fun updateStreak() {
        val currentProgress = _userProgress.value
        val today = java.time.LocalDate.now().toString()

        val newStreak = if (currentProgress.lastActiveDate == today) {
            currentProgress.currentStreak
        } else {
            val yesterday = java.time.LocalDate.now().minusDays(1).toString()
            if (currentProgress.lastActiveDate == yesterday) {
                currentProgress.currentStreak + 1
            } else {
                1
            }
        }

        val newLongestStreak = maxOf(newStreak, currentProgress.longestStreak)

        val newProgress = currentProgress.copy(
            currentStreak = newStreak,
            longestStreak = newLongestStreak,
            lastActiveDate = today
        )

        _userProgress.value = newProgress
        saveProgressLocally(newProgress)
    }

    suspend fun addStudyTime(minutes: Int) {
        val currentProgress = _userProgress.value
        val newProgress = currentProgress.copy(
            studyTimeMinutes = currentProgress.studyTimeMinutes + minutes
        )

        _userProgress.value = newProgress
        saveProgressLocally(newProgress)
    }

    suspend fun resetProgress() {
        _userProgress.value = UserProgress()
        context.progressDataStore.edit { it.clear() }
    }

    fun isLevelCompleted(levelId: String): Boolean {
        return levelId in _userProgress.value.completedLevels
    }
}
