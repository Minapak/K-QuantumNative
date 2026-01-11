/**
 * K-QuantumNative - Explore ViewModel
 * Port of QuantumNative ExploreView integration
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kquantum.nativeapp.data.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExploreCategory(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val items: List<ExploreItem>
)

data class ExploreItem(
    val id: String,
    val title: String,
    val description: String,
    val type: ExploreItemType,
    val difficulty: String,
    val estimatedTime: Int, // minutes
    val isPremium: Boolean = false,
    val tags: List<String> = emptyList()
)

enum class ExploreItemType {
    ALGORITHM,
    CONCEPT,
    TUTORIAL,
    CHALLENGE,
    ARTICLE
}

@HiltViewModel
class ExploreViewModel @Inject constructor() : ViewModel() {

    private val _categories = MutableStateFlow<List<ExploreCategory>>(emptyList())
    val categories: StateFlow<List<ExploreCategory>> = _categories.asStateFlow()

    private val _selectedCategory = MutableStateFlow<ExploreCategory?>(null)
    val selectedCategory: StateFlow<ExploreCategory?> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<ExploreItem>>(emptyList())
    val searchResults: StateFlow<List<ExploreItem>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _featuredItems = MutableStateFlow<List<ExploreItem>>(emptyList())
    val featuredItems: StateFlow<List<ExploreItem>> = _featuredItems.asStateFlow()

    init {
        loadCategories()
        loadFeaturedItems()
    }

    private fun loadCategories() {
        _categories.value = listOf(
            ExploreCategory(
                id = "algorithms",
                title = "Quantum Algorithms",
                description = "Explore famous quantum algorithms",
                iconName = "functions",
                items = listOf(
                    ExploreItem(
                        id = "grover",
                        title = "Grover's Search",
                        description = "Quantum search algorithm with quadratic speedup",
                        type = ExploreItemType.ALGORITHM,
                        difficulty = "Intermediate",
                        estimatedTime = 30,
                        tags = listOf("search", "speedup", "oracle")
                    ),
                    ExploreItem(
                        id = "shor",
                        title = "Shor's Algorithm",
                        description = "Integer factorization using quantum computing",
                        type = ExploreItemType.ALGORITHM,
                        difficulty = "Advanced",
                        estimatedTime = 45,
                        isPremium = true,
                        tags = listOf("cryptography", "factorization", "QFT")
                    ),
                    ExploreItem(
                        id = "deutsch-jozsa",
                        title = "Deutsch-Jozsa",
                        description = "Determine if a function is constant or balanced",
                        type = ExploreItemType.ALGORITHM,
                        difficulty = "Beginner",
                        estimatedTime = 20,
                        tags = listOf("oracle", "superposition")
                    ),
                    ExploreItem(
                        id = "qft",
                        title = "Quantum Fourier Transform",
                        description = "Quantum analog of discrete Fourier transform",
                        type = ExploreItemType.ALGORITHM,
                        difficulty = "Advanced",
                        estimatedTime = 40,
                        isPremium = true,
                        tags = listOf("transform", "phase", "estimation")
                    ),
                    ExploreItem(
                        id = "vqe",
                        title = "VQE Algorithm",
                        description = "Variational Quantum Eigensolver for chemistry",
                        type = ExploreItemType.ALGORITHM,
                        difficulty = "Advanced",
                        estimatedTime = 50,
                        isPremium = true,
                        tags = listOf("variational", "optimization", "chemistry")
                    )
                )
            ),
            ExploreCategory(
                id = "concepts",
                title = "Core Concepts",
                description = "Fundamental quantum computing concepts",
                iconName = "lightbulb",
                items = listOf(
                    ExploreItem(
                        id = "superposition",
                        title = "Superposition",
                        description = "Understanding quantum superposition states",
                        type = ExploreItemType.CONCEPT,
                        difficulty = "Beginner",
                        estimatedTime = 15,
                        tags = listOf("basics", "qubit", "state")
                    ),
                    ExploreItem(
                        id = "entanglement",
                        title = "Entanglement",
                        description = "Quantum correlations and Bell states",
                        type = ExploreItemType.CONCEPT,
                        difficulty = "Intermediate",
                        estimatedTime = 25,
                        tags = listOf("correlation", "bell", "EPR")
                    ),
                    ExploreItem(
                        id = "measurement",
                        title = "Quantum Measurement",
                        description = "Measuring quantum states and wave function collapse",
                        type = ExploreItemType.CONCEPT,
                        difficulty = "Beginner",
                        estimatedTime = 20,
                        tags = listOf("observation", "collapse", "probability")
                    ),
                    ExploreItem(
                        id = "decoherence",
                        title = "Decoherence",
                        description = "How quantum systems lose their quantum properties",
                        type = ExploreItemType.CONCEPT,
                        difficulty = "Intermediate",
                        estimatedTime = 25,
                        tags = listOf("noise", "environment", "error")
                    ),
                    ExploreItem(
                        id = "error-correction",
                        title = "Error Correction",
                        description = "Protecting quantum information from errors",
                        type = ExploreItemType.CONCEPT,
                        difficulty = "Advanced",
                        estimatedTime = 35,
                        isPremium = true,
                        tags = listOf("fault-tolerant", "codes", "syndrome")
                    )
                )
            ),
            ExploreCategory(
                id = "gates",
                title = "Quantum Gates",
                description = "Learn about quantum gate operations",
                iconName = "memory",
                items = listOf(
                    ExploreItem(
                        id = "single-qubit-gates",
                        title = "Single-Qubit Gates",
                        description = "Hadamard, Pauli, and rotation gates",
                        type = ExploreItemType.TUTORIAL,
                        difficulty = "Beginner",
                        estimatedTime = 20,
                        tags = listOf("H", "X", "Y", "Z", "rotation")
                    ),
                    ExploreItem(
                        id = "multi-qubit-gates",
                        title = "Multi-Qubit Gates",
                        description = "CNOT, CZ, SWAP, and Toffoli gates",
                        type = ExploreItemType.TUTORIAL,
                        difficulty = "Intermediate",
                        estimatedTime = 30,
                        tags = listOf("CNOT", "controlled", "entangling")
                    ),
                    ExploreItem(
                        id = "universal-gates",
                        title = "Universal Gate Sets",
                        description = "Why some gate sets can approximate any unitary",
                        type = ExploreItemType.CONCEPT,
                        difficulty = "Advanced",
                        estimatedTime = 25,
                        tags = listOf("universal", "approximation", "Solovay-Kitaev")
                    )
                )
            ),
            ExploreCategory(
                id = "applications",
                title = "Applications",
                description = "Real-world quantum computing applications",
                iconName = "rocket_launch",
                items = listOf(
                    ExploreItem(
                        id = "quantum-chemistry",
                        title = "Quantum Chemistry",
                        description = "Simulating molecules with quantum computers",
                        type = ExploreItemType.ARTICLE,
                        difficulty = "Advanced",
                        estimatedTime = 30,
                        isPremium = true,
                        tags = listOf("simulation", "molecules", "VQE")
                    ),
                    ExploreItem(
                        id = "quantum-ml",
                        title = "Quantum Machine Learning",
                        description = "Combining quantum computing with ML",
                        type = ExploreItemType.ARTICLE,
                        difficulty = "Advanced",
                        estimatedTime = 35,
                        isPremium = true,
                        tags = listOf("ML", "kernel", "classification")
                    ),
                    ExploreItem(
                        id = "cryptography",
                        title = "Quantum Cryptography",
                        description = "Quantum key distribution and post-quantum crypto",
                        type = ExploreItemType.ARTICLE,
                        difficulty = "Intermediate",
                        estimatedTime = 25,
                        tags = listOf("QKD", "BB84", "security")
                    )
                )
            ),
            ExploreCategory(
                id = "challenges",
                title = "Challenges",
                description = "Test your quantum computing skills",
                iconName = "emoji_events",
                items = listOf(
                    ExploreItem(
                        id = "bell-challenge",
                        title = "Bell State Challenge",
                        description = "Create all four Bell states",
                        type = ExploreItemType.CHALLENGE,
                        difficulty = "Beginner",
                        estimatedTime = 10,
                        tags = listOf("entanglement", "practice")
                    ),
                    ExploreItem(
                        id = "teleportation-challenge",
                        title = "Teleportation Circuit",
                        description = "Implement quantum teleportation",
                        type = ExploreItemType.CHALLENGE,
                        difficulty = "Intermediate",
                        estimatedTime = 20,
                        tags = listOf("teleportation", "protocol")
                    ),
                    ExploreItem(
                        id = "optimization-challenge",
                        title = "QAOA Challenge",
                        description = "Solve a combinatorial problem with QAOA",
                        type = ExploreItemType.CHALLENGE,
                        difficulty = "Advanced",
                        estimatedTime = 45,
                        isPremium = true,
                        tags = listOf("optimization", "variational")
                    )
                )
            )
        )
    }

    private fun loadFeaturedItems() {
        _featuredItems.value = listOf(
            ExploreItem(
                id = "grover",
                title = "Grover's Search Algorithm",
                description = "Learn the famous quantum search algorithm",
                type = ExploreItemType.ALGORITHM,
                difficulty = "Intermediate",
                estimatedTime = 30,
                tags = listOf("featured", "popular")
            ),
            ExploreItem(
                id = "entanglement",
                title = "Understanding Entanglement",
                description = "Dive deep into quantum entanglement",
                type = ExploreItemType.CONCEPT,
                difficulty = "Intermediate",
                estimatedTime = 25,
                tags = listOf("featured", "essential")
            ),
            ExploreItem(
                id = "teleportation-challenge",
                title = "Teleportation Challenge",
                description = "Build a quantum teleportation circuit",
                type = ExploreItemType.CHALLENGE,
                difficulty = "Intermediate",
                estimatedTime = 20,
                tags = listOf("featured", "hands-on")
            )
        )
    }

    fun selectCategory(category: ExploreCategory?) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        performSearch(query)
    }

    private fun performSearch(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        val allItems = _categories.value.flatMap { it.items }
        val lowercaseQuery = query.lowercase()

        _searchResults.value = allItems.filter { item ->
            item.title.lowercase().contains(lowercaseQuery) ||
            item.description.lowercase().contains(lowercaseQuery) ||
            item.tags.any { it.lowercase().contains(lowercaseQuery) }
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = emptyList()
    }

    fun getItemsByType(type: ExploreItemType): List<ExploreItem> {
        return _categories.value.flatMap { it.items }.filter { it.type == type }
    }

    fun getItemsByDifficulty(difficulty: String): List<ExploreItem> {
        return _categories.value.flatMap { it.items }.filter { it.difficulty == difficulty }
    }
}
