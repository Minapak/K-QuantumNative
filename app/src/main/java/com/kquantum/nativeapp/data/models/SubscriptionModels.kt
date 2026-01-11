/**
 * K-QuantumNative - Subscription Models
 * Port of QuantumNative/Models/Subscription.swift
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.temporal.ChronoUnit

enum class SubscriptionTier(val displayName: String) {
    FREE("Free"),
    PRO("Pro"),
    PREMIUM("Premium")
}

enum class SubscriptionProductId(
    val productId: String,
    val displayName: String,
    val description: String,
    val tier: SubscriptionTier,
    val isYearly: Boolean,
    val features: List<String>,
    val isBestValue: Boolean,
    val sortOrder: Int
) {
    PRO_MONTHLY(
        productId = "com.kquantum.nativeapp.pro.monthly",
        displayName = "Pro Monthly",
        description = "Unlock advanced quantum simulations",
        tier = SubscriptionTier.PRO,
        isYearly = false,
        features = listOf(
            "64 qubit simulations",
            "100 monthly QPU credits",
            "Advanced noise visualization",
            "Continuous operation mode"
        ),
        isBestValue = false,
        sortOrder = 1
    ),
    PRO_YEARLY(
        productId = "com.kquantum.nativeapp.pro.yearly",
        displayName = "Pro Yearly",
        description = "Save 33% with annual billing",
        tier = SubscriptionTier.PRO,
        isYearly = true,
        features = listOf(
            "64 qubit simulations",
            "100 monthly QPU credits",
            "Advanced noise visualization",
            "Continuous operation mode",
            "2 months free"
        ),
        isBestValue = true,
        sortOrder = 2
    ),
    PREMIUM_MONTHLY(
        productId = "com.kquantum.nativeapp.premium.monthly",
        displayName = "Premium Monthly",
        description = "Full quantum computing access",
        tier = SubscriptionTier.PREMIUM,
        isYearly = false,
        features = listOf(
            "256 qubit simulations",
            "1000 monthly QPU credits",
            "Fault-tolerant quantum computing",
            "Priority queue access",
            "Unlimited error correction",
            "Career Passport O1 Evidence"
        ),
        isBestValue = false,
        sortOrder = 3
    ),
    PREMIUM_YEARLY(
        productId = "com.kquantum.nativeapp.premium.yearly",
        displayName = "Premium Yearly",
        description = "Best value for serious researchers",
        tier = SubscriptionTier.PREMIUM,
        isYearly = true,
        features = listOf(
            "256 qubit simulations",
            "1000 monthly QPU credits",
            "Fault-tolerant quantum computing",
            "Priority queue access",
            "Unlimited error correction",
            "Career Passport O1 Evidence",
            "2 months free"
        ),
        isBestValue = true,
        sortOrder = 4
    );

    companion object {
        fun fromProductId(id: String): SubscriptionProductId? =
            entries.find { it.productId == id }
    }
}

enum class SubscriptionStatus(val displayName: String) {
    FREE("Free"),
    ACTIVE("Active"),
    EXPIRED("Expired"),
    CANCELLED("Cancelled"),
    GRACE_PERIOD("Grace Period");

    val isPremium: Boolean
        get() = this == ACTIVE || this == GRACE_PERIOD
}

@Serializable
data class SubscriptionInfo(
    val status: String = "free",
    val productId: String? = null,
    val purchaseDate: Long? = null,
    val expirationDate: Long? = null,
    val originalTransactionId: String? = null,
    val isAutoRenewEnabled: Boolean = false
) {
    val subscriptionStatus: SubscriptionStatus
        get() = when (status.lowercase()) {
            "active" -> SubscriptionStatus.ACTIVE
            "expired" -> SubscriptionStatus.EXPIRED
            "cancelled" -> SubscriptionStatus.CANCELLED
            "grace_period" -> SubscriptionStatus.GRACE_PERIOD
            else -> SubscriptionStatus.FREE
        }

    val isActive: Boolean
        get() = subscriptionStatus.isPremium &&
                (expirationDate == null || expirationDate > System.currentTimeMillis())

    val daysRemaining: Int?
        get() {
            val expDate = expirationDate ?: return null
            if (expDate <= System.currentTimeMillis()) return 0
            return ChronoUnit.DAYS.between(
                Instant.now(),
                Instant.ofEpochMilli(expDate)
            ).toInt()
        }

    val tier: SubscriptionTier
        get() = productId?.let { SubscriptionProductId.fromProductId(it)?.tier }
            ?: SubscriptionTier.FREE
}

sealed class PurchaseResult {
    data class Success(val productId: String, val purchaseToken: String) : PurchaseResult()
    object Pending : PurchaseResult()
    object UserCancelled : PurchaseResult()
    data class Failed(val error: BillingError) : PurchaseResult()
}

sealed class BillingError(val message: String) {
    object ProductNotFound : BillingError("Product not found")
    object PurchaseFailed : BillingError("Purchase failed")
    object VerificationFailed : BillingError("Verification failed")
    object NetworkError : BillingError("Network error")
    object BillingUnavailable : BillingError("Billing service unavailable")
    object ServiceDisconnected : BillingError("Service disconnected")
    data class Unknown(val errorMessage: String) : BillingError(errorMessage)
}

@Serializable
data class SubscriptionSyncResponse(
    @SerialName("is_premium")
    val isPremium: Boolean = false,
    @SerialName("subscription_tier")
    val subscriptionTier: String? = null,
    @SerialName("expires_at")
    val expiresAt: String? = null,
    @SerialName("is_auto_renew")
    val isAutoRenew: Boolean = false
)

@Serializable
data class VerifyPurchaseRequest(
    @SerialName("purchase_token")
    val purchaseToken: String,
    @SerialName("product_id")
    val productId: String,
    val platform: String = "android"
)

@Serializable
data class VerifyPurchaseResponse(
    val success: Boolean,
    val message: String? = null,
    @SerialName("subscription_info")
    val subscriptionInfo: SubscriptionSyncResponse? = null
)
