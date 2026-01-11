/**
 * K-QuantumNative - Practice ViewModel
 * Port of QuantumNative PracticeView integration
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

sealed class PracticeState {
    object Loading : PracticeState()
    data class Question(
        val item: PracticeItem,
        val questionNumber: Int,
        val totalQuestions: Int
    ) : PracticeState()
    data class Result(
        val score: Int,
        val totalQuestions: Int,
        val xpEarned: Int,
        val correctAnswers: Int
    ) : PracticeState()
    data class Error(val message: String) : PracticeState()
}

@HiltViewModel
class PracticeViewModel @Inject constructor(
    private val learningService: LearningService,
    private val progressService: ProgressService
) : ViewModel() {

    private val _practiceState = MutableStateFlow<PracticeState>(PracticeState.Loading)
    val practiceState: StateFlow<PracticeState> = _practiceState.asStateFlow()

    private val _currentItems = MutableStateFlow<List<PracticeItem>>(emptyList())
    val currentItems: StateFlow<List<PracticeItem>> = _currentItems.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _selectedAnswer = MutableStateFlow<Int?>(null)
    val selectedAnswer: StateFlow<Int?> = _selectedAnswer.asStateFlow()

    private val _showExplanation = MutableStateFlow(false)
    val showExplanation: StateFlow<Boolean> = _showExplanation.asStateFlow()

    private val _answers = MutableStateFlow<MutableList<Int>>(mutableListOf())

    private val _practiceMode = MutableStateFlow<PracticeMode>(PracticeMode.QUICK)
    val practiceMode: StateFlow<PracticeMode> = _practiceMode.asStateFlow()

    private val _difficulty = MutableStateFlow<PracticeDifficulty>(PracticeDifficulty.MEDIUM)
    val difficulty: StateFlow<PracticeDifficulty> = _difficulty.asStateFlow()

    enum class PracticeMode {
        QUICK,      // 5 questions
        STANDARD,   // 10 questions
        CHALLENGE   // 20 questions
    }

    enum class PracticeDifficulty {
        EASY, MEDIUM, HARD, MIXED
    }

    fun setMode(mode: PracticeMode) {
        _practiceMode.value = mode
    }

    fun setDifficulty(difficulty: PracticeDifficulty) {
        _difficulty.value = difficulty
    }

    fun startPractice(trackId: String? = null) {
        viewModelScope.launch {
            _practiceState.value = PracticeState.Loading
            _currentIndex.value = 0
            _selectedAnswer.value = null
            _showExplanation.value = false
            _answers.value = mutableListOf()

            val questionCount = when (_practiceMode.value) {
                PracticeMode.QUICK -> 5
                PracticeMode.STANDARD -> 10
                PracticeMode.CHALLENGE -> 20
            }

            val items = learningService.getPracticeItems(trackId, questionCount)

            if (items.isNotEmpty()) {
                _currentItems.value = items
                showCurrentQuestion()
            } else {
                _practiceState.value = PracticeState.Error("No practice items available")
            }
        }
    }

    private fun showCurrentQuestion() {
        val items = _currentItems.value
        val index = _currentIndex.value

        if (index < items.size) {
            _practiceState.value = PracticeState.Question(
                item = items[index],
                questionNumber = index + 1,
                totalQuestions = items.size
            )
            _selectedAnswer.value = null
            _showExplanation.value = false
        } else {
            calculateResults()
        }
    }

    fun selectAnswer(answerIndex: Int) {
        if (_selectedAnswer.value != null) return // Already answered

        _selectedAnswer.value = answerIndex
        _answers.value.add(answerIndex)
        _showExplanation.value = true
    }

    fun nextQuestion() {
        _currentIndex.value++
        showCurrentQuestion()
    }

    private fun calculateResults() {
        val items = _currentItems.value
        val answers = _answers.value

        var correctCount = 0
        items.forEachIndexed { index, item ->
            if (index < answers.size && answers[index] == item.correctAnswer) {
                correctCount++
            }
        }

        val score = ((correctCount.toDouble() / items.size) * 100).toInt()
        val xpEarned = calculateXpEarned(correctCount, items.size)

        // Update progress
        viewModelScope.launch {
            progressService.addXp(xpEarned)
        }

        _practiceState.value = PracticeState.Result(
            score = score,
            totalQuestions = items.size,
            xpEarned = xpEarned,
            correctAnswers = correctCount
        )
    }

    private fun calculateXpEarned(correct: Int, total: Int): Int {
        val baseXp = correct * 10
        val bonusMultiplier = when (_practiceMode.value) {
            PracticeMode.QUICK -> 1.0
            PracticeMode.STANDARD -> 1.5
            PracticeMode.CHALLENGE -> 2.0
        }
        val difficultyMultiplier = when (_difficulty.value) {
            PracticeDifficulty.EASY -> 0.8
            PracticeDifficulty.MEDIUM -> 1.0
            PracticeDifficulty.HARD -> 1.5
            PracticeDifficulty.MIXED -> 1.2
        }

        // Perfect score bonus
        val perfectBonus = if (correct == total) 25 else 0

        return ((baseXp * bonusMultiplier * difficultyMultiplier) + perfectBonus).toInt()
    }

    fun restartPractice() {
        val currentTrack = _currentItems.value.firstOrNull()?.trackId
        startPractice(currentTrack)
    }

    val currentItem: PracticeItem?
        get() = _currentItems.value.getOrNull(_currentIndex.value)

    val isLastQuestion: Boolean
        get() = _currentIndex.value >= _currentItems.value.size - 1

    val progressPercentage: Float
        get() {
            val total = _currentItems.value.size
            return if (total > 0) (_currentIndex.value + 1).toFloat() / total else 0f
        }
}
