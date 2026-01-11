/**
 * K-QuantumNative - Google Play Billing Service
 * Port of QuantumNative/Services/StoreKitService.swift
 * Implements Google Play Billing Library for Android
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.services.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import com.kquantum.nativeapp.BuildConfig
import com.kquantum.nativeapp.data.models.*
import com.kquantum.nativeapp.data.remote.ApiClient
import com.kquantum.nativeapp.data.remote.safeApiCall
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class BillingService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiClient: ApiClient
) : PurchasesUpdatedListener {

    private val scope = CoroutineScope(Dispatchers.IO)

    private var billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases()
        .build()

    private val _products = MutableStateFlow<List<ProductDetails>>(emptyList())
    val products: StateFlow<List<ProductDetails>> = _products.asStateFlow()

    private val _subscriptionInfo = MutableStateFlow(SubscriptionInfo())
    val subscriptionInfo: StateFlow<SubscriptionInfo> = _subscriptionInfo.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var pendingPurchaseCallback: ((PurchaseResult) -> Unit)? = null

    init {
        connectBillingClient()
    }

    private fun connectBillingClient() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    scope.launch {
                        loadProducts()
                        updateSubscriptionStatus()
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                // Retry connection
                scope.launch {
                    kotlinx.coroutines.delay(3000)
                    connectBillingClient()
                }
            }
        })
    }

    suspend fun loadProducts() {
        _isLoading.value = true

        val productIds = SubscriptionProductId.entries.map { it.productId }

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                productIds.map {
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(it)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                }
            )
            .build()

        val result = suspendCancellableCoroutine<List<ProductDetails>> { continuation ->
            billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    continuation.resume(productDetailsList)
                } else {
                    continuation.resume(emptyList())
                }
            }
        }

        // Sort by subscription product order
        _products.value = result.sortedBy { productDetails ->
            SubscriptionProductId.fromProductId(productDetails.productId)?.sortOrder ?: 99
        }

        _isLoading.value = false
    }

    suspend fun purchase(activity: Activity, productDetails: ProductDetails): PurchaseResult {
        if (!billingClient.isReady) {
            return PurchaseResult.Failed(BillingError.BillingUnavailable)
        }

        val offerToken = productDetails.subscriptionOfferDetails?.firstOrNull()?.offerToken
            ?: return PurchaseResult.Failed(BillingError.ProductNotFound)

        val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(productDetails)
            .setOfferToken(offerToken)
            .build()

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productDetailsParams))
            .build()

        return suspendCancellableCoroutine { continuation ->
            pendingPurchaseCallback = { result ->
                continuation.resume(result)
            }

            val billingResult = billingClient.launchBillingFlow(activity, billingFlowParams)

            if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                pendingPurchaseCallback = null
                continuation.resume(
                    PurchaseResult.Failed(
                        BillingError.Unknown("Billing flow failed: ${billingResult.debugMessage}")
                    )
                )
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases?.forEach { purchase ->
                    scope.launch {
                        handlePurchase(purchase)
                    }
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                pendingPurchaseCallback?.invoke(PurchaseResult.UserCancelled)
                pendingPurchaseCallback = null
            }
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                scope.launch { updateSubscriptionStatus() }
            }
            else -> {
                pendingPurchaseCallback?.invoke(
                    PurchaseResult.Failed(
                        BillingError.Unknown(billingResult.debugMessage)
                    )
                )
                pendingPurchaseCallback = null
            }
        }
    }

    private suspend fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            // Verify with server
            val verifyResult = safeApiCall {
                apiClient.api.verifyPurchase(
                    VerifyPurchaseRequest(
                        purchaseToken = purchase.purchaseToken,
                        productId = purchase.products.firstOrNull() ?: ""
                    )
                )
            }

            when (verifyResult) {
                is com.kquantum.nativeapp.data.remote.ApiResult.Success -> {
                    // Acknowledge the purchase
                    if (!purchase.isAcknowledged) {
                        val acknowledgeParams = AcknowledgePurchaseParams.newBuilder()
                            .setPurchaseToken(purchase.purchaseToken)
                            .build()

                        billingClient.acknowledgePurchase(acknowledgeParams) { }
                    }

                    updateSubscriptionStatus()

                    pendingPurchaseCallback?.invoke(
                        PurchaseResult.Success(
                            productId = purchase.products.firstOrNull() ?: "",
                            purchaseToken = purchase.purchaseToken
                        )
                    )
                }
                is com.kquantum.nativeapp.data.remote.ApiResult.Error -> {
                    pendingPurchaseCallback?.invoke(
                        PurchaseResult.Failed(BillingError.VerificationFailed)
                    )
                }
                else -> {}
            }
        } else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
            pendingPurchaseCallback?.invoke(PurchaseResult.Pending)
        }

        pendingPurchaseCallback = null
    }

    suspend fun updateSubscriptionStatus() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        val result = billingClient.queryPurchasesAsync(params)

        if (result.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            val activePurchase = result.purchasesList.firstOrNull { purchase ->
                purchase.purchaseState == Purchase.PurchaseState.PURCHASED
            }

            if (activePurchase != null) {
                _subscriptionInfo.value = SubscriptionInfo(
                    status = "active",
                    productId = activePurchase.products.firstOrNull(),
                    purchaseDate = activePurchase.purchaseTime,
                    originalTransactionId = activePurchase.orderId,
                    isAutoRenewEnabled = activePurchase.isAutoRenewing
                )
            } else {
                _subscriptionInfo.value = SubscriptionInfo()
            }
        }
    }

    suspend fun restorePurchases(): Boolean {
        updateSubscriptionStatus()
        return _subscriptionInfo.value.isActive
    }

    val isPremium: Boolean
        get() = BuildConfig.DEBUG_MODE || _subscriptionInfo.value.isActive

    val currentTier: SubscriptionTier
        get() = _subscriptionInfo.value.tier

    fun getProductPrice(productId: SubscriptionProductId): String? {
        return _products.value.find { it.productId == productId.productId }
            ?.subscriptionOfferDetails?.firstOrNull()
            ?.pricingPhases?.pricingPhaseList?.firstOrNull()
            ?.formattedPrice
    }

    fun getProduct(productId: SubscriptionProductId): ProductDetails? {
        return _products.value.find { it.productId == productId.productId }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}

// Extension functions for ProductDetails
val ProductDetails.monthlyPrice: String?
    get() {
        val pricingPhase = subscriptionOfferDetails?.firstOrNull()
            ?.pricingPhases?.pricingPhaseList?.firstOrNull()
        val priceAmountMicros = pricingPhase?.priceAmountMicros ?: return null
        val billingPeriod = pricingPhase.billingPeriod

        // If yearly, divide by 12 for monthly equivalent
        val monthlyMicros = if (billingPeriod == "P1Y") {
            priceAmountMicros / 12
        } else {
            priceAmountMicros
        }

        val currency = pricingPhase.priceCurrencyCode
        val amount = monthlyMicros / 1_000_000.0

        return java.text.NumberFormat.getCurrencyInstance().apply {
            this.currency = java.util.Currency.getInstance(currency)
        }.format(amount)
    }

val ProductDetails.billingPeriodDisplay: String
    get() {
        val period = subscriptionOfferDetails?.firstOrNull()
            ?.pricingPhases?.pricingPhaseList?.firstOrNull()
            ?.billingPeriod

        return when (period) {
            "P1M" -> "Monthly"
            "P1Y" -> "Yearly"
            "P3M" -> "Quarterly"
            "P6M" -> "6 Months"
            else -> period ?: "Unknown"
        }
    }
