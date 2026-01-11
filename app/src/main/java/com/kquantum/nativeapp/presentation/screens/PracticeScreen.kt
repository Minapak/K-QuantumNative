/**
 * K-QuantumNative - Practice Screen
 * Port of QuantumNative PracticeView
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kquantum.nativeapp.presentation.theme.*
import com.kquantum.nativeapp.presentation.viewmodels.PracticeState
import com.kquantum.nativeapp.presentation.viewmodels.PracticeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(
    onNavigateBack: () -> Unit,
    onStartPractice: () -> Unit,
    viewModel: PracticeViewModel = hiltViewModel()
) {
    val practiceMode by viewModel.practiceMode.collectAsState()
    val difficulty by viewModel.difficulty.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Practice", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = TextPrimary,
                    navigationIconContentColor = TextPrimary
                )
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text(
                    text = "🎯",
                    fontSize = 48.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Test Your Knowledge",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Choose your practice mode and difficulty",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text(
                    text = "Practice Mode",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PracticeModeCard(
                        title = "Quick",
                        description = "5 questions",
                        emoji = "⚡",
                        isSelected = practiceMode == PracticeViewModel.PracticeMode.QUICK,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.setMode(PracticeViewModel.PracticeMode.QUICK) }
                    )
                    PracticeModeCard(
                        title = "Standard",
                        description = "10 questions",
                        emoji = "📝",
                        isSelected = practiceMode == PracticeViewModel.PracticeMode.STANDARD,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.setMode(PracticeViewModel.PracticeMode.STANDARD) }
                    )
                    PracticeModeCard(
                        title = "Challenge",
                        description = "20 questions",
                        emoji = "🏆",
                        isSelected = practiceMode == PracticeViewModel.PracticeMode.CHALLENGE,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.setMode(PracticeViewModel.PracticeMode.CHALLENGE) }
                    )
                }
            }

            item {
                Text(
                    text = "Difficulty",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    DifficultyOption(
                        title = "Easy",
                        description = "Basic concepts",
                        color = QuantumGreen,
                        isSelected = difficulty == PracticeViewModel.PracticeDifficulty.EASY,
                        onClick = { viewModel.setDifficulty(PracticeViewModel.PracticeDifficulty.EASY) }
                    )
                    DifficultyOption(
                        title = "Medium",
                        description = "Intermediate topics",
                        color = QuantumYellow,
                        isSelected = difficulty == PracticeViewModel.PracticeDifficulty.MEDIUM,
                        onClick = { viewModel.setDifficulty(PracticeViewModel.PracticeDifficulty.MEDIUM) }
                    )
                    DifficultyOption(
                        title = "Hard",
                        description = "Advanced challenges",
                        color = QuantumPink,
                        isSelected = difficulty == PracticeViewModel.PracticeDifficulty.HARD,
                        onClick = { viewModel.setDifficulty(PracticeViewModel.PracticeDifficulty.HARD) }
                    )
                    DifficultyOption(
                        title = "Mixed",
                        description = "All difficulty levels",
                        color = QuantumPurple,
                        isSelected = difficulty == PracticeViewModel.PracticeDifficulty.MIXED,
                        onClick = { viewModel.setDifficulty(PracticeViewModel.PracticeDifficulty.MIXED) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        viewModel.startPractice()
                        onStartPractice()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = QuantumBlue),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Start Practice",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun PracticeModeCard(
    title: String,
    description: String,
    emoji: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) QuantumBlue.copy(alpha = 0.2f) else DarkCard,
        border = if (isSelected) BorderStroke(1.dp, QuantumBlue) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected) QuantumBlue else TextPrimary
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun DifficultyOption(
    title: String,
    description: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) color.copy(alpha = 0.15f) else DarkCard
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected) color else TextPrimary
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(color),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeSessionScreen(
    onNavigateBack: () -> Unit,
    onComplete: () -> Unit,
    viewModel: PracticeViewModel = hiltViewModel()
) {
    val practiceState by viewModel.practiceState.collectAsState()
    val selectedAnswer by viewModel.selectedAnswer.collectAsState()
    val showExplanation by viewModel.showExplanation.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Practice", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = TextPrimary,
                    navigationIconContentColor = TextPrimary
                )
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = practiceState) {
                is PracticeState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = QuantumBlue
                    )
                }

                is PracticeState.Question -> {
                    QuestionContent(
                        question = state.item.question,
                        options = state.item.options,
                        correctAnswer = state.item.correctAnswer,
                        selectedAnswer = selectedAnswer,
                        showExplanation = showExplanation,
                        explanation = state.item.explanation,
                        questionNumber = state.questionNumber,
                        totalQuestions = state.totalQuestions,
                        progress = viewModel.progressPercentage,
                        isLastQuestion = viewModel.isLastQuestion,
                        onSelectAnswer = { viewModel.selectAnswer(it) },
                        onNext = { viewModel.nextQuestion() }
                    )
                }

                is PracticeState.Result -> {
                    ResultContent(
                        score = state.score,
                        correctAnswers = state.correctAnswers,
                        totalQuestions = state.totalQuestions,
                        xpEarned = state.xpEarned,
                        onRetry = { viewModel.restartPractice() },
                        onFinish = onComplete
                    )
                }

                is PracticeState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Error: ${state.message}",
                            color = StatusError
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onNavigateBack) {
                            Text("Go Back")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuestionContent(
    question: String,
    options: List<String>,
    correctAnswer: Int,
    selectedAnswer: Int?,
    showExplanation: Boolean,
    explanation: String?,
    questionNumber: Int,
    totalQuestions: Int,
    progress: Float,
    isLastQuestion: Boolean,
    onSelectAnswer: (Int) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Progress
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Question $questionNumber of $totalQuestions",
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary
            )
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelLarge,
                color = QuantumBlue
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = QuantumBlue,
            trackColor = DarkCard,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Question
        Text(
            text = question,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Options
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            options.forEachIndexed { index, option ->
                val isSelected = selectedAnswer == index
                val isCorrect = index == correctAnswer
                val showResult = showExplanation

                val backgroundColor = when {
                    showResult && isCorrect -> StatusSuccess.copy(alpha = 0.15f)
                    showResult && isSelected && !isCorrect -> StatusError.copy(alpha = 0.15f)
                    isSelected -> QuantumBlue.copy(alpha = 0.15f)
                    else -> DarkCard
                }

                val borderColor = when {
                    showResult && isCorrect -> StatusSuccess
                    showResult && isSelected && !isCorrect -> StatusError
                    isSelected -> QuantumBlue
                    else -> Color.Transparent
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !showExplanation) { onSelectAnswer(index) },
                    shape = RoundedCornerShape(12.dp),
                    color = backgroundColor
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        if (showResult) {
                            if (isCorrect) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = StatusSuccess
                                )
                            } else if (isSelected) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = null,
                                    tint = StatusError
                                )
                            }
                        }
                    }
                }
            }
        }

        // Explanation
        if (showExplanation && explanation != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = QuantumBlue.copy(alpha = 0.1f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Explanation",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = QuantumBlue
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = explanation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Next button
        if (showExplanation) {
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = QuantumBlue),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = if (isLastQuestion) "See Results" else "Next Question",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ResultContent(
    score: Int,
    correctAnswers: Int,
    totalQuestions: Int,
    xpEarned: Int,
    onRetry: () -> Unit,
    onFinish: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val emoji = when {
            score >= 90 -> "🏆"
            score >= 70 -> "🎉"
            score >= 50 -> "👍"
            else -> "📚"
        }

        Text(text = emoji, fontSize = 80.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "$score%",
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            color = when {
                score >= 70 -> StatusSuccess
                score >= 50 -> StatusWarning
                else -> StatusError
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$correctAnswers of $totalQuestions correct",
            style = MaterialTheme.typography.titleMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = QuantumYellow.copy(alpha = 0.15f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "⭐", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "+$xpEarned XP",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = QuantumYellow
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onRetry,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Try Again")
            }
            Button(
                onClick = onFinish,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = QuantumBlue),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Finish")
            }
        }
    }
}
