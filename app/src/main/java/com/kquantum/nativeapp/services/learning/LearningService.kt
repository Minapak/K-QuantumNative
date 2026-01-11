/**
 * K-QuantumNative - Learning Service
 * Port of QuantumNative/Services/LearningService.swift
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.services.learning

import com.kquantum.nativeapp.data.models.*
import com.kquantum.nativeapp.data.remote.ApiClient
import com.kquantum.nativeapp.data.remote.ApiResult
import com.kquantum.nativeapp.data.remote.safeApiCall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LearningService @Inject constructor(
    private val apiClient: ApiClient
) {
    private val _tracks = MutableStateFlow<List<LearningTrack>>(emptyList())
    val tracks: StateFlow<List<LearningTrack>> = _tracks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    suspend fun loadTracks() {
        _isLoading.value = true
        _error.value = null

        val result = safeApiCall { apiClient.api.getLearningTracks() }

        when (result) {
            is ApiResult.Success -> {
                _tracks.value = result.data
            }
            is ApiResult.Error -> {
                // Use default tracks
                _tracks.value = getDefaultTracks()
            }
            else -> {}
        }

        _isLoading.value = false
    }

    suspend fun loadLevelsForTrack(track: Track): List<LearningLevel> {
        val result = safeApiCall {
            apiClient.api.getLevelsForTrack(track.name.lowercase())
        }

        return when (result) {
            is ApiResult.Success -> result.data
            else -> getDefaultLevels(track)
        }
    }

    private fun getDefaultTracks(): List<LearningTrack> {
        return listOf(
            LearningTrack(
                id = "beginner",
                name = "Quantum Basics",
                description = "Start your quantum journey",
                iconName = "school",
                levels = getDefaultLevels(Track.BEGINNER)
            ),
            LearningTrack(
                id = "intermediate",
                name = "Quantum Mechanics",
                description = "Deepen your understanding",
                iconName = "science",
                levels = getDefaultLevels(Track.INTERMEDIATE)
            ),
            LearningTrack(
                id = "advanced",
                name = "Quantum Algorithms",
                description = "Master quantum computing",
                iconName = "psychology",
                levels = getDefaultLevels(Track.ADVANCED)
            )
        )
    }

    private fun getDefaultLevels(track: Track): List<LearningLevel> {
        return when (track) {
            Track.BEGINNER -> listOf(
                LearningLevel(
                    id = "intro-qubits",
                    number = 1,
                    title = "Introduction to Qubits",
                    name = "What is a Qubit?",
                    description = "Learn the fundamental unit of quantum information",
                    track = "beginner",
                    xpReward = 100,
                    estimatedTime = 15,
                    lessons = listOf(
                        Lesson("l1", "What is a Qubit?", LessonType.THEORY, "Introduction to quantum bits"),
                        Lesson("l2", "Qubit States", LessonType.THEORY, "Understanding |0⟩ and |1⟩"),
                        Lesson("l3", "Practice: Create a Qubit", LessonType.PRACTICE),
                        Lesson("l4", "Quiz", LessonType.QUIZ)
                    )
                ),
                LearningLevel(
                    id = "superposition",
                    number = 2,
                    title = "Superposition",
                    name = "Quantum Superposition",
                    description = "Understand how qubits can be in multiple states",
                    track = "beginner",
                    xpReward = 150,
                    estimatedTime = 20,
                    prerequisites = listOf("intro-qubits"),
                    lessons = listOf(
                        Lesson("l5", "What is Superposition?", LessonType.THEORY),
                        Lesson("l6", "The Hadamard Gate", LessonType.THEORY),
                        Lesson("l7", "Practice: Create Superposition", LessonType.PRACTICE),
                        Lesson("l8", "Quiz", LessonType.QUIZ)
                    )
                ),
                LearningLevel(
                    id = "measurement",
                    number = 3,
                    title = "Quantum Measurement",
                    name = "Measuring Qubits",
                    description = "Learn how measurement affects quantum states",
                    track = "beginner",
                    xpReward = 150,
                    estimatedTime = 20,
                    prerequisites = listOf("superposition"),
                    lessons = listOf(
                        Lesson("l9", "Measurement Basics", LessonType.THEORY),
                        Lesson("l10", "Probability and Measurement", LessonType.THEORY),
                        Lesson("l11", "Practice: Measure States", LessonType.PRACTICE),
                        Lesson("l12", "Quiz", LessonType.QUIZ)
                    )
                ),
                LearningLevel(
                    id = "entanglement-intro",
                    number = 4,
                    title = "Entanglement Basics",
                    name = "Introduction to Entanglement",
                    description = "Discover the spooky action at a distance",
                    track = "beginner",
                    xpReward = 200,
                    estimatedTime = 25,
                    prerequisites = listOf("measurement"),
                    lessons = listOf(
                        Lesson("l13", "What is Entanglement?", LessonType.THEORY),
                        Lesson("l14", "Bell States", LessonType.THEORY),
                        Lesson("l15", "Practice: Create Bell State", LessonType.PRACTICE),
                        Lesson("l16", "Quiz", LessonType.QUIZ)
                    )
                )
            )
            Track.INTERMEDIATE -> listOf(
                LearningLevel(
                    id = "quantum-gates",
                    number = 1,
                    title = "Quantum Gates",
                    name = "Universal Gate Set",
                    description = "Learn the building blocks of quantum circuits",
                    track = "intermediate",
                    xpReward = 200,
                    estimatedTime = 30,
                    lessons = listOf(
                        Lesson("l17", "Single-Qubit Gates", LessonType.THEORY),
                        Lesson("l18", "Multi-Qubit Gates", LessonType.THEORY),
                        Lesson("l19", "Practice: Build Circuits", LessonType.PRACTICE),
                        Lesson("l20", "Quiz", LessonType.QUIZ)
                    )
                ),
                LearningLevel(
                    id = "quantum-circuits",
                    number = 2,
                    title = "Quantum Circuits",
                    name = "Building Quantum Circuits",
                    description = "Design and analyze quantum circuits",
                    track = "intermediate",
                    xpReward = 250,
                    estimatedTime = 35,
                    prerequisites = listOf("quantum-gates"),
                    lessons = listOf(
                        Lesson("l21", "Circuit Composition", LessonType.THEORY),
                        Lesson("l22", "Circuit Optimization", LessonType.THEORY),
                        Lesson("l23", "Practice: Complex Circuits", LessonType.PRACTICE),
                        Lesson("l24", "Quiz", LessonType.QUIZ)
                    )
                )
            )
            Track.ADVANCED -> listOf(
                LearningLevel(
                    id = "grovers",
                    number = 1,
                    title = "Grover's Algorithm",
                    name = "Quantum Search",
                    description = "Learn the quadratic speedup for unstructured search",
                    track = "advanced",
                    xpReward = 300,
                    estimatedTime = 45,
                    lessons = listOf(
                        Lesson("l25", "Oracle Construction", LessonType.THEORY),
                        Lesson("l26", "Amplitude Amplification", LessonType.THEORY),
                        Lesson("l27", "Practice: Implement Grover's", LessonType.PRACTICE),
                        Lesson("l28", "Quiz", LessonType.QUIZ)
                    )
                ),
                LearningLevel(
                    id = "shors",
                    number = 2,
                    title = "Shor's Algorithm",
                    name = "Quantum Factoring",
                    description = "Break RSA encryption with quantum computing",
                    track = "advanced",
                    xpReward = 400,
                    estimatedTime = 60,
                    prerequisites = listOf("grovers"),
                    lessons = listOf(
                        Lesson("l29", "Period Finding", LessonType.THEORY),
                        Lesson("l30", "Quantum Fourier Transform", LessonType.THEORY),
                        Lesson("l31", "Practice: Factoring", LessonType.PRACTICE),
                        Lesson("l32", "Quiz", LessonType.QUIZ)
                    )
                )
            )
        }
    }

    // Learning strategies
    fun generateMemoryTriggers(level: LearningLevel): List<MemoryTrigger> {
        return when {
            level.id.contains("qubit") -> listOf(
                MemoryTrigger(
                    "Qubit",
                    "Think of Schrödinger's cat",
                    "Like the cat that's both alive and dead, a qubit is both 0 and 1"
                ),
                MemoryTrigger(
                    "State Vector",
                    "Arrow on a sphere",
                    "The Bloch sphere arrow points to the qubit's state"
                )
            )
            level.id.contains("superposition") -> listOf(
                MemoryTrigger(
                    "Superposition",
                    "Spinning coin",
                    "Like a spinning coin, a qubit in superposition is both heads and tails"
                ),
                MemoryTrigger(
                    "Hadamard",
                    "H for Half-and-half",
                    "The H gate creates a 50-50 superposition"
                )
            )
            else -> listOf(
                MemoryTrigger(
                    "Quantum",
                    "Discrete energy packets",
                    "Quantum means discrete, countable units"
                )
            )
        }
    }

    fun generateFeynmanExplanation(level: LearningLevel): FeynmanExplanation {
        return when {
            level.id.contains("qubit") -> FeynmanExplanation(
                concept = "Qubit",
                simpleExplanation = "A qubit is like a light switch that can be on, off, or magically both at once.",
                analogy = "Imagine a coin spinning in the air - it's neither heads nor tails until it lands.",
                keyPoints = listOf(
                    "Classical bits are 0 or 1",
                    "Qubits can be both 0 and 1",
                    "Measurement 'collapses' the state"
                )
            )
            level.id.contains("entanglement") -> FeynmanExplanation(
                concept = "Entanglement",
                simpleExplanation = "Two qubits become so connected that measuring one instantly affects the other.",
                analogy = "Like a pair of magic dice that always show matching numbers, no matter how far apart.",
                keyPoints = listOf(
                    "Correlated quantum states",
                    "Spooky action at a distance",
                    "Key resource for quantum computing"
                )
            )
            else -> FeynmanExplanation(
                concept = level.title,
                simpleExplanation = "Breaking down ${level.title} into simple terms.",
                analogy = "Think of it like...",
                keyPoints = listOf("Key point 1", "Key point 2")
            )
        }
    }

    fun clearError() {
        _error.value = null
    }

    suspend fun getPracticeItems(trackId: String?, count: Int): List<PracticeItem> {
        // Generate practice items based on track and count
        val allItems = generateDefaultPracticeItems()
        val filteredItems = if (trackId != null) {
            allItems.filter { it.trackId == trackId }
        } else {
            allItems
        }
        return filteredItems.shuffled().take(count)
    }

    private fun generateDefaultPracticeItems(): List<PracticeItem> {
        return listOf(
            PracticeItem(
                id = "q1",
                title = "Qubit Basics",
                question = "What is a qubit?",
                options = listOf(
                    "A classical bit",
                    "A quantum bit that can be in superposition",
                    "A type of computer memory",
                    "A programming language"
                ),
                correctAnswer = 1,
                explanation = "A qubit (quantum bit) is the basic unit of quantum information. Unlike classical bits, qubits can exist in a superposition of both 0 and 1 states simultaneously.",
                trackId = "beginner"
            ),
            PracticeItem(
                id = "q2",
                title = "Superposition",
                question = "What happens when you measure a qubit in superposition?",
                options = listOf(
                    "It stays in superposition",
                    "It collapses to either |0⟩ or |1⟩",
                    "It becomes entangled",
                    "Nothing happens"
                ),
                correctAnswer = 1,
                explanation = "When a qubit in superposition is measured, its quantum state collapses to one of the classical states (|0⟩ or |1⟩) based on the probability amplitudes.",
                trackId = "beginner"
            ),
            PracticeItem(
                id = "q3",
                title = "Hadamard Gate",
                question = "What does the Hadamard gate do?",
                options = listOf(
                    "Flips the qubit state",
                    "Creates an equal superposition from |0⟩ or |1⟩",
                    "Entangles two qubits",
                    "Measures the qubit"
                ),
                correctAnswer = 1,
                explanation = "The Hadamard gate creates an equal superposition of |0⟩ and |1⟩ states. It's one of the most fundamental quantum gates.",
                trackId = "beginner"
            ),
            PracticeItem(
                id = "q4",
                title = "Entanglement",
                question = "Which gate is commonly used to create entanglement?",
                options = listOf(
                    "Hadamard gate",
                    "Pauli-X gate",
                    "CNOT gate",
                    "Phase gate"
                ),
                correctAnswer = 2,
                explanation = "The CNOT (Controlled-NOT) gate, combined with a Hadamard gate, is commonly used to create entangled qubit pairs like Bell states.",
                trackId = "beginner"
            ),
            PracticeItem(
                id = "q5",
                title = "Quantum States",
                question = "How many complex numbers are needed to describe a single qubit state?",
                options = listOf(
                    "One",
                    "Two",
                    "Four",
                    "Eight"
                ),
                correctAnswer = 1,
                explanation = "A single qubit state is described by two complex numbers (amplitudes) for the |0⟩ and |1⟩ basis states, normalized so their squared magnitudes sum to 1.",
                trackId = "beginner"
            ),
            PracticeItem(
                id = "q6",
                title = "Bloch Sphere",
                question = "What does the Bloch sphere represent?",
                options = listOf(
                    "A quantum computer",
                    "The state space of a single qubit",
                    "An entangled pair of qubits",
                    "A quantum algorithm"
                ),
                correctAnswer = 1,
                explanation = "The Bloch sphere is a geometric representation of the state space of a single qubit. Any pure state can be represented as a point on the surface of the sphere.",
                trackId = "intermediate"
            ),
            PracticeItem(
                id = "q7",
                title = "Quantum Gates",
                question = "Which of these is NOT a single-qubit gate?",
                options = listOf(
                    "Pauli-X gate",
                    "CNOT gate",
                    "Hadamard gate",
                    "Phase gate"
                ),
                correctAnswer = 1,
                explanation = "The CNOT gate operates on two qubits (control and target), making it a two-qubit gate. All other options are single-qubit gates.",
                trackId = "intermediate"
            ),
            PracticeItem(
                id = "q8",
                title = "Measurement",
                question = "What is the probability of measuring |0⟩ from the state (|0⟩ + |1⟩)/sqrt(2)?",
                options = listOf(
                    "0%",
                    "25%",
                    "50%",
                    "100%"
                ),
                correctAnswer = 2,
                explanation = "The state (|0⟩ + |1⟩)/sqrt(2) is an equal superposition, so the probability of measuring |0⟩ is |1/sqrt(2)|^2 = 1/2 = 50%.",
                trackId = "intermediate"
            ),
            PracticeItem(
                id = "q9",
                title = "Grover's Algorithm",
                question = "What type of problem does Grover's algorithm solve?",
                options = listOf(
                    "Factoring large numbers",
                    "Unstructured database search",
                    "Simulating quantum systems",
                    "Error correction"
                ),
                correctAnswer = 1,
                explanation = "Grover's algorithm provides a quadratic speedup for unstructured search problems, finding a marked item in an unsorted database.",
                trackId = "advanced"
            ),
            PracticeItem(
                id = "q10",
                title = "Shor's Algorithm",
                question = "What is Shor's algorithm famous for?",
                options = listOf(
                    "Quantum machine learning",
                    "Factoring large numbers exponentially faster",
                    "Quantum teleportation",
                    "Quantum key distribution"
                ),
                correctAnswer = 1,
                explanation = "Shor's algorithm can factor large numbers exponentially faster than the best known classical algorithms, threatening RSA encryption.",
                trackId = "advanced"
            )
        )
    }
}
