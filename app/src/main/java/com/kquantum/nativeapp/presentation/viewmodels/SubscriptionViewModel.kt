/**
 * K-QuantumNative - Subscription ViewModel
 * Port of QuantumNative StoreKitService UI integration
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.presentation.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.ProductDetails
import com.kquantum.nativeapp.data.models.*
import com.kquantum.nativeapp.services.billing.BillingService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val billingService: BillingService
) : ViewModel() {

    val products: StateFlow<List<ProductDetails>> = billingService.products
    val subscriptionInfo: StateFlow<SubscriptionInfo> = billingService.subscriptionInfo
    val isLoading: StateFlow<Boolean> = billingService.isLoading
    val errorMessage: StateFlow<String?> = billingService.errorMessage

    private val _selectedProductId = MutableStateFlow<SubscriptionProductId?>(null)
    val selectedProductId: StateFlow<SubscriptionProductId?> = _selectedProductId.asStateFlow()

    private val _purchaseResult = MutableStateFlow<PurchaseResult?>(null)
    val purchaseResult: StateFlow<PurchaseResult?> = _purchaseResult.asStateFlow()

    private val _showSuccessDialog = MutableStateFlow(false)
    val showSuccessDialog: StateFlow<Boolean> = _showSuccessDialog.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            billingService.loadProducts()
        }
    }

    fun selectProduct(productId: SubscriptionProductId) {
        _selectedProductId.value = productId
    }

    fun purchase(activity: Activity) {
        val productId = _selectedProductId.value ?: return
        val productDetails = billingService.getProduct(productId) ?: return

        viewModelScope.launch {
            val result = billingService.purchase(activity, productDetails)
            _purchaseResult.value = result

            when (result) {
                is PurchaseResult.Success -> {
                    _showSuccessDialog.value = true
                }
                is PurchaseResult.Failed -> {
                    // Error is shown via errorMessage
                }
                else -> {}
            }
        }
    }

    fun restorePurchases() {
        viewModelScope.launch {
            val restored = billingService.restorePurchases()
            if (restored) {
                _showSuccessDialog.value = true
            }
        }
    }

    fun dismissSuccessDialog() {
        _showSuccessDialog.value = false
    }

    fun clearPurchaseResult() {
        _purchaseResult.value = null
    }

    fun clearError() {
        billingService.clearError()
    }

    val isPremium: Boolean
        get() = billingService.isPremium

    val currentTier: SubscriptionTier
        get() = billingService.currentTier

    fun getProductPrice(productId: SubscriptionProductId): String? {
        return billingService.getProductPrice(productId)
    }

    // Group products by tier for display
    val proProducts: List<ProductDetails>
        get() = products.value.filter {
            SubscriptionProductId.fromProductId(it.productId)?.tier == SubscriptionTier.PRO
        }

    val premiumProducts: List<ProductDetails>
        get() = products.value.filter {
            SubscriptionProductId.fromProductId(it.productId)?.tier == SubscriptionTier.PREMIUM
        }
}
