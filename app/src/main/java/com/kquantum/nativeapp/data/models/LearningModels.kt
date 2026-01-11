/**
 * K-QuantumNative - Learning Models
 * Port of QuantumNative/Models/LearningLevel.swift & LearningTrack.swift
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.data.models

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class Track(
    val displayName: String,
    val description: String,
    val iconName: String,
    val color: Long
) {
    BEGINNER(
        displayName = "Beginner",
        description = "Start your quantum journey",
        iconName = "school",
        color = 0xFF4CAF50
    ),
    INTERMEDIATE(
        displayName = "Intermediate",
        description = "Deepen your understanding",
        iconName = "science",
        color = 0xFF2196F3
    ),
    ADVANCED(
        displayName = "Advanced",
        description = "Master quantum computing",
        iconName = "psychology",
        color = 0xFF9C27B0
    );

    val composeColor: Color
        get() = Color(color)
}

@Serializable
data class Lesson(
    val id: String,
    val title: String,
    val type: LessonType = LessonType.THEORY,
    val content: String = "",
    val duration: Int = 10, // minutes
    @SerialName("xp_reward")
    val xpReward: Int = 25,
    @SerialName("is_completed")
    val isCompleted: Boolean = false
)

@Serializable
enum class LessonType {
    @SerialName("theory")
    THEORY,
    @SerialName("practice")
    PRACTICE,
    @SerialName("quiz")
    QUIZ
}

@Serializable
data class LearningLevel(
    val id: String,
    val number: Int,
    val title: String,
    val name: String = "",
    val description: String,
    val track: String = "beginner",
    @SerialName("xp_reward")
    val xpReward: Int = 100,
    @SerialName("estimated_time")
    val estimatedTime: Int = 30, // minutes
    val prerequisites: List<String> = emptyList(),
    val lessons: List<Lesson> = emptyList(),
    @SerialName("is_locked")
    val isLocked: Boolean = false
) {
    // Alias for number (used by LearnScreen)
    val level: Int
        get() = number

    val trackEnum: Track
        get() = when (track.lowercase()) {
            "intermediate" -> Track.INTERMEDIATE
            "advanced" -> Track.ADVANCED
            else -> Track.BEGINNER
        }

    val estimatedTimeText: String
        get() = if (estimatedTime >= 60) {
            "${estimatedTime / 60}h ${estimatedTime % 60}m"
        } else {
            "${estimatedTime}m"
        }
}

@Serializable
data class LearningTrack(
    val id: String,
    val name: String,
    val description: String,
    val iconName: String,
    val levels: List<LearningLevel> = emptyList(),
    @SerialName("is_premium")
    val isPremium: Boolean = false,
    val emoji: String = "📚",
    val difficulty: String = "Beginner"
) {
    // Alias for name (used by LearnScreen)
    val title: String
        get() = name

    val totalXp: Int
        get() = levels.sumOf { it.xpReward }

    val totalLessons: Int
        get() = levels.sumOf { it.lessons.size }

    val totalTime: Int
        get() = levels.sumOf { it.estimatedTime }

    val progressPercentage: Double
        get() {
            val completedLessons = levels.sumOf { level -> level.lessons.count { it.isCompleted } }
            val totalLessonsCount = totalLessons
            return if (totalLessonsCount > 0) completedLessons.toDouble() / totalLessonsCount else 0.0
        }
}

@Serializable
data class PracticeItem(
    val id: String,
    val title: String,
    val subtitle: String = "",
    val iconName: String = "code",
    val difficulty: Difficulty = Difficulty.BEGINNER,
    val isUnlocked: Boolean = true,
    val completedCount: Int = 0,
    val totalCount: Int = 1,
    // Quiz properties
    val question: String = "",
    val options: List<String> = emptyList(),
    @SerialName("correct_answer")
    val correctAnswer: Int = 0,
    val explanation: String? = null,
    @SerialName("track_id")
    val trackId: String? = null
) {
    val progressPercentage: Double
        get() = if (totalCount > 0) completedCount.toDouble() / totalCount else 0.0
}

@Serializable
enum class Difficulty {
    @SerialName("beginner")
    BEGINNER,
    @SerialName("intermediate")
    INTERMEDIATE,
    @SerialName("advanced")
    ADVANCED;

    val displayName: String
        get() = name.lowercase().replaceFirstChar { it.uppercase() }

    val color: Color
        get() = when (this) {
            BEGINNER -> Color(0xFF4CAF50)
            INTERMEDIATE -> Color(0xFF2196F3)
            ADVANCED -> Color(0xFF9C27B0)
        }
}

// Learning Strategies
enum class LearningStrategy(
    val displayName: String,
    val description: String,
    val iconName: String
) {
    MEMORY(
        displayName = "Memory Triggers",
        description = "Use mnemonics and associations",
        iconName = "psychology"
    ),
    CONCEPT_MAP(
        displayName = "Concept Maps",
        description = "Visualize connections between ideas",
        iconName = "account_tree"
    ),
    FEYNMAN(
        displayName = "Feynman Technique",
        description = "Explain concepts simply",
        iconName = "record_voice_over"
    )
}

enum class LearningPace(val displayName: String) {
    SLOW("Slow & Thorough"),
    BALANCED("Balanced"),
    FAST("Fast & Focused")
}

data class MemoryTrigger(
    val concept: String,
    val trigger: String,
    val explanation: String
)

data class ConceptMapNode(
    val id: String,
    val concept: String,
    val connections: List<String> = emptyList()
)

data class FeynmanExplanation(
    val concept: String,
    val simpleExplanation: String,
    val analogy: String,
    val keyPoints: List<String> = emptyList()
)
