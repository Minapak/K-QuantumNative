/**
 * K-QuantumNative - Authentication Service
 * Port of QuantumNative/Services/AuthService.swift
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.services.auth

import com.kquantum.nativeapp.BuildConfig
import com.kquantum.nativeapp.data.models.*
import com.kquantum.nativeapp.data.remote.ApiClient
import com.kquantum.nativeapp.data.remote.ApiResult
import com.kquantum.nativeapp.data.remote.TokenManager
import com.kquantum.nativeapp.data.remote.safeApiCall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthService @Inject constructor(
    private val apiClient: ApiClient,
    private val tokenManager: TokenManager
) {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentUser = MutableStateFlow<UserResponse?>(null)
    val currentUser: StateFlow<UserResponse?> = _currentUser.asStateFlow()

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        val hasToken = tokenManager.hasToken()
        val savedIsAdmin = tokenManager.isAdmin()
        _isLoggedIn.value = hasToken || savedIsAdmin
        _isAdmin.value = savedIsAdmin
    }

    suspend fun signUp(email: String, username: String, password: String): ApiResult<AuthResponse> {
        _isLoading.value = true
        _errorMessage.value = null

        val result = safeApiCall {
            apiClient.api.signUp(SignUpRequest(email, username, password))
        }

        when (result) {
            is ApiResult.Success -> {
                handleAuthSuccess(result.data)
            }
            is ApiResult.Error -> {
                _errorMessage.value = result.message
            }
            else -> {}
        }

        _isLoading.value = false
        return result
    }

    suspend fun login(email: String, password: String): ApiResult<AuthResponse> {
        _isLoading.value = true
        _errorMessage.value = null

        // Check for admin login
        if (email == BuildConfig.ADMIN_EMAIL && password == BuildConfig.ADMIN_PASSWORD) {
            handleAdminLogin()
            _isLoading.value = false
            return ApiResult.Success(
                AuthResponse(
                    accessToken = "admin_token",
                    tokenType = "Bearer",
                    userId = "admin",
                    username = "Admin",
                    email = email
                )
            )
        }

        val result = safeApiCall {
            apiClient.api.login(LoginRequest(email, password))
        }

        when (result) {
            is ApiResult.Success -> {
                handleAuthSuccess(result.data)
            }
            is ApiResult.Error -> {
                _errorMessage.value = result.message
            }
            else -> {}
        }

        _isLoading.value = false
        return result
    }

    private fun handleAuthSuccess(response: AuthResponse) {
        tokenManager.saveToken(response.accessToken)
        tokenManager.saveUserInfo(response.userId, response.username, response.email)
        _isLoggedIn.value = true
        _currentUser.value = UserResponse(
            id = response.userId,
            email = response.email,
            username = response.username
        )
    }

    private fun handleAdminLogin() {
        tokenManager.setAdmin(true)
        _isAdmin.value = true
        _isLoggedIn.value = true
        _currentUser.value = UserResponse(
            id = "admin",
            email = BuildConfig.ADMIN_EMAIL,
            username = "Admin",
            isPremium = true,
            subscriptionTier = "premium"
        )
    }

    suspend fun fetchCurrentUser(): ApiResult<UserResponse> {
        if (_isAdmin.value) {
            return ApiResult.Success(_currentUser.value!!)
        }

        val result = safeApiCall { apiClient.api.getCurrentUser() }

        when (result) {
            is ApiResult.Success -> {
                _currentUser.value = result.data
            }
            is ApiResult.Error -> {
                if (result.code == 401) {
                    logout()
                }
            }
            else -> {}
        }

        return result
    }

    suspend fun updateProfile(username: String?, email: String?): ApiResult<UserResponse> {
        _isLoading.value = true

        val result = safeApiCall {
            apiClient.api.updateProfile(UpdateProfileRequest(username, email))
        }

        when (result) {
            is ApiResult.Success -> {
                _currentUser.value = result.data
            }
            is ApiResult.Error -> {
                _errorMessage.value = result.message
            }
            else -> {}
        }

        _isLoading.value = false
        return result
    }

    suspend fun changePassword(currentPassword: String, newPassword: String): ApiResult<Unit> {
        _isLoading.value = true

        val result = safeApiCall {
            apiClient.api.changePassword(ChangePasswordRequest(currentPassword, newPassword))
        }

        when (result) {
            is ApiResult.Error -> {
                _errorMessage.value = result.message
            }
            else -> {}
        }

        _isLoading.value = false
        return result
    }

    suspend fun requestPasswordReset(email: String): ApiResult<Unit> {
        return safeApiCall {
            apiClient.api.requestPasswordReset(ResetPasswordRequest(email))
        }
    }

    fun logout() {
        tokenManager.clearAll()
        _isLoggedIn.value = false
        _isAdmin.value = false
        _currentUser.value = null
        _errorMessage.value = null
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun enableAdminMode() {
        handleAdminLogin()
    }

    val isPremium: Boolean
        get() = _isAdmin.value || (_currentUser.value?.isPremium ?: false) || BuildConfig.DEBUG_MODE
}
