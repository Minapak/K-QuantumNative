/**
 * K-QuantumNative - Subscription Screen
 * Port of QuantumNative SubscriptionView
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.presentation.screens

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kquantum.nativeapp.data.models.SubscriptionProductId
import com.kquantum.nativeapp.data.models.SubscriptionTier
import com.kquantum.nativeapp.presentation.theme.*
import com.kquantum.nativeapp.presentation.viewmodels.SubscriptionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionScreen(
    onNavigateBack: () -> Unit,
    viewModel: SubscriptionViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val selectedProductId by viewModel.selectedProductId.collectAsState()
    val showSuccessDialog by viewModel.showSuccessDialog.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upgrade", fontWeight = FontWeight.Bold) },
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
            // Header
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "👑", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Unlock Your Potential",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Get access to premium features and accelerate your quantum learning journey",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // PRO Plan
            item {
                SubscriptionPlanCard(
                    tier = SubscriptionTier.PRO,
                    title = "Pro",
                    monthlyPrice = viewModel.getProductPrice(SubscriptionProductId.PRO_MONTHLY),
                    yearlyPrice = viewModel.getProductPrice(SubscriptionProductId.PRO_YEARLY),
                    features = listOf(
                        "All learning tracks",
                        "Unlimited practice sessions",
                        "Bridge access (10 jobs/day)",
                        "Priority support"
                    ),
                    color = TierPro,
                    selectedProductId = selectedProductId,
                    onSelectMonthly = { viewModel.selectProduct(SubscriptionProductId.PRO_MONTHLY) },
                    onSelectYearly = { viewModel.selectProduct(SubscriptionProductId.PRO_YEARLY) }
                )
            }

            // Premium Plan
            item {
                SubscriptionPlanCard(
                    tier = SubscriptionTier.PREMIUM,
                    title = "Premium",
                    monthlyPrice = viewModel.getProductPrice(SubscriptionProductId.PREMIUM_MONTHLY),
                    yearlyPrice = viewModel.getProductPrice(SubscriptionProductId.PREMIUM_YEARLY),
                    features = listOf(
                        "Everything in Pro",
                        "Unlimited Bridge access",
                        "Real-time noise data",
                        "Advanced algorithms",
                        "1-on-1 mentoring sessions",
                        "Early access to new features"
                    ),
                    color = TierPremium,
                    isPopular = true,
                    selectedProductId = selectedProductId,
                    onSelectMonthly = { viewModel.selectProduct(SubscriptionProductId.PREMIUM_MONTHLY) },
                    onSelectYearly = { viewModel.selectProduct(SubscriptionProductId.PREMIUM_YEARLY) }
                )
            }

            // Subscribe button
            item {
                Button(
                    onClick = {
                        activity?.let { viewModel.purchase(it) }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !isLoading && selectedProductId != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = QuantumPurple
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = TextPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Subscribe Now",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Restore purchases
            item {
                TextButton(
                    onClick = { viewModel.restorePurchases() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Restore Purchases",
                        color = TextSecondary
                    )
                }
            }

            // Error message
            errorMessage?.let { error ->
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = StatusError.copy(alpha = 0.15f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = null,
                                tint = StatusError
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = error,
                                color = StatusError,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { viewModel.clearError() }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Dismiss",
                                    tint = StatusError
                                )
                            }
                        }
                    }
                }
            }

            // Terms
            item {
                Text(
                    text = "Subscriptions automatically renew unless cancelled at least 24 hours before the end of the current period. Your account will be charged within 24 hours prior to renewal. Manage your subscription in Google Play Store settings.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextTertiary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }

    // Success dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissSuccessDialog() },
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "🎉", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Welcome!", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Text(
                    text = "Your subscription is now active. Enjoy all the premium features!",
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.dismissSuccessDialog()
                        onNavigateBack()
                    }
                ) {
                    Text("Get Started")
                }
            },
            containerColor = DarkSurface
        )
    }
}

@Composable
private fun SubscriptionPlanCard(
    tier: SubscriptionTier,
    title: String,
    monthlyPrice: String?,
    yearlyPrice: String?,
    features: List<String>,
    color: Color,
    isPopular: Boolean = false,
    selectedProductId: SubscriptionProductId?,
    onSelectMonthly: () -> Unit,
    onSelectYearly: () -> Unit
) {
    val monthlyProductId = if (tier == SubscriptionTier.PRO)
        SubscriptionProductId.PRO_MONTHLY else SubscriptionProductId.PREMIUM_MONTHLY
    val yearlyProductId = if (tier == SubscriptionTier.PRO)
        SubscriptionProductId.PRO_YEARLY else SubscriptionProductId.PREMIUM_YEARLY

    val isMonthlySelected = selectedProductId == monthlyProductId
    val isYearlySelected = selectedProductId == yearlyProductId
    val isSelected = isMonthlySelected || isYearlySelected

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isSelected) Modifier.border(
                    width = 2.dp,
                    brush = Brush.horizontalGradient(listOf(color, color.copy(alpha = 0.6f))),
                    shape = RoundedCornerShape(20.dp)
                ) else Modifier
            ),
        shape = RoundedCornerShape(20.dp),
        color = DarkCard
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (tier == SubscriptionTier.PRO) "⭐" else "👑",
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }
                if (isPopular) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = color.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "POPULAR",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = color,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pricing options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Monthly
                PricingOption(
                    label = "Monthly",
                    price = monthlyPrice ?: "...",
                    period = "/month",
                    isSelected = isMonthlySelected,
                    color = color,
                    modifier = Modifier.weight(1f),
                    onClick = onSelectMonthly
                )

                // Yearly
                PricingOption(
                    label = "Yearly",
                    price = yearlyPrice ?: "...",
                    period = "/year",
                    isSelected = isYearlySelected,
                    color = color,
                    badge = "Save 40%",
                    modifier = Modifier.weight(1f),
                    onClick = onSelectYearly
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = DarkCardElevated)
            Spacer(modifier = Modifier.height(16.dp))

            // Features
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                features.forEach { feature ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = color
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = feature,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PricingOption(
    label: String,
    price: String,
    period: String,
    isSelected: Boolean,
    color: Color,
    badge: String? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) color.copy(alpha = 0.15f) else DarkCardElevated,
        border = if (isSelected) BorderStroke(1.dp, color) else null
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (badge != null) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = StatusSuccess.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = badge,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = StatusSuccess,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary
            )
            Text(
                text = price,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) color else TextPrimary
            )
            Text(
                text = period,
                style = MaterialTheme.typography.labelSmall,
                color = TextTertiary
            )
        }
    }
}
