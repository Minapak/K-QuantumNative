/**
 * K-QuantumNative - Profile & Settings Screens
 * Port of QuantumNative ProfileView and SettingsView
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kquantum.nativeapp.data.models.SubscriptionTier
import com.kquantum.nativeapp.presentation.theme.*
import com.kquantum.nativeapp.presentation.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToSubscription: () -> Unit,
    onNavigateToPassport: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val userProgress by viewModel.userProgress.collectAsState()
    val profileStats by viewModel.profileStats.collectAsState()
    val showLogoutConfirmation by viewModel.showLogoutConfirmation.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = TextPrimary,
                    navigationIconContentColor = TextPrimary,
                    actionIconContentColor = TextPrimary
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile header
            item {
                ProfileHeader(
                    displayName = currentUser?.displayName ?: "Quantum Explorer",
                    email = currentUser?.email ?: "",
                    level = profileStats.level,
                    tier = viewModel.subscriptionTier
                )
            }

            // Stats cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "XP",
                        value = "${profileStats.totalXp}",
                        icon = "⭐",
                        color = QuantumYellow,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Streak",
                        value = "${profileStats.currentStreak}",
                        icon = "🔥",
                        color = QuantumOrange,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Achievements",
                        value = "${profileStats.achievementsUnlocked}",
                        icon = "🏆",
                        color = QuantumPurple,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Level progress
            item {
                LevelProgressCard(
                    level = profileStats.level,
                    progress = viewModel.levelProgress,
                    xpToNext = viewModel.xpToNextLevel
                )
            }

            // Menu items
            item {
                MenuSection(
                    title = "Account"
                ) {
                    MenuItem(
                        icon = Icons.Default.CardMembership,
                        title = "Subscription",
                        subtitle = viewModel.subscriptionTier.name,
                        onClick = onNavigateToSubscription
                    )
                    MenuItem(
                        icon = Icons.Default.Badge,
                        title = "Quantum Passport",
                        subtitle = "View your achievements",
                        onClick = onNavigateToPassport
                    )
                }
            }

            item {
                MenuSection(
                    title = "Learning"
                ) {
                    MenuItem(
                        icon = Icons.Default.School,
                        title = "Lessons Completed",
                        subtitle = "${profileStats.lessonsCompleted} lessons",
                        onClick = {}
                    )
                    MenuItem(
                        icon = Icons.Default.Timer,
                        title = "Study Time",
                        subtitle = viewModel.formattedStudyTime,
                        onClick = {}
                    )
                }
            }

            item {
                MenuSection(
                    title = "App"
                ) {
                    MenuItem(
                        icon = Icons.Default.Info,
                        title = "About",
                        subtitle = "Version 1.0.0",
                        onClick = {}
                    )
                    MenuItem(
                        icon = Icons.Default.Logout,
                        title = "Log Out",
                        subtitle = "",
                        color = StatusError,
                        onClick = { viewModel.showLogoutConfirmation() }
                    )
                }
            }
        }
    }

    // Logout confirmation dialog
    if (showLogoutConfirmation) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissLogoutConfirmation() },
            title = { Text("Log Out", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.logout()
                        onLogout()
                    }
                ) {
                    Text("Log Out", color = StatusError)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissLogoutConfirmation() }) {
                    Text("Cancel")
                }
            },
            containerColor = DarkSurface
        )
    }
}

@Composable
private fun ProfileHeader(
    displayName: String,
    email: String,
    level: Int,
    tier: SubscriptionTier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = DarkCard
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(QuantumBlue.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Lv$level",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = QuantumBlue
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    if (tier != SubscriptionTier.FREE) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (tier == SubscriptionTier.PREMIUM)
                                TierPremium.copy(alpha = 0.2f) else TierPro.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = tier.name,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (tier == SubscriptionTier.PREMIUM)
                                    TierPremium else TierPro,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                if (email.isNotEmpty()) {
                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun LevelProgressCard(
    level: Int,
    progress: Float,
    xpToNext: Int
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DarkCard
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Level $level",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "$xpToNext XP to Level ${level + 1}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = QuantumBlue,
                trackColor = DarkCardElevated,
            )
        }
    }
}

@Composable
private fun MenuSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = TextSecondary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DarkCard
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
private fun MenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color = TextPrimary,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = color
                )
                if (subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextTertiary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                MenuSection(title = "Appearance") {
                    SettingsToggle(
                        icon = Icons.Default.DarkMode,
                        title = "Dark Mode",
                        subtitle = "Use dark theme",
                        isEnabled = true,
                        onToggle = {}
                    )
                }
            }

            item {
                MenuSection(title = "Notifications") {
                    SettingsToggle(
                        icon = Icons.Default.Notifications,
                        title = "Push Notifications",
                        subtitle = "Receive learning reminders",
                        isEnabled = true,
                        onToggle = {}
                    )
                    SettingsToggle(
                        icon = Icons.Default.Email,
                        title = "Email Updates",
                        subtitle = "Weekly progress reports",
                        isEnabled = false,
                        onToggle = {}
                    )
                }
            }

            item {
                MenuSection(title = "Privacy") {
                    MenuItem(
                        icon = Icons.Default.Security,
                        title = "Privacy Policy",
                        subtitle = "",
                        onClick = {}
                    )
                    MenuItem(
                        icon = Icons.Default.Description,
                        title = "Terms of Service",
                        subtitle = "",
                        onClick = {}
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsToggle(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = TextPrimary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )
                if (subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }
        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = QuantumBlue,
                checkedTrackColor = QuantumBlue.copy(alpha = 0.5f)
            )
        )
    }
}
