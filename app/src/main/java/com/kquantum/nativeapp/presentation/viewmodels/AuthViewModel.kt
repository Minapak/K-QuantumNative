/**
 * K-QuantumNative - Auth ViewModel
 * Port of QuantumNative/ViewModels/AuthViewModel.swift
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kquantum.nativeapp.data.models.UserResponse
import com.kquantum.nativeapp.data.remote.ApiResult
import com.kquantum.nativeapp.services.auth.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean> = authService.isLoggedIn
    val isAuthenticated: StateFlow<Boolean> = authService.isLoggedIn
    val currentUser: StateFlow<UserResponse?> = authService.currentUser
    val isAdmin: StateFlow<Boolean> = authService.isAdmin
    val isLoading: StateFlow<Boolean> = authService.isLoading
    val errorMessage: StateFlow<String?> = authService.errorMessage

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _signUpSuccess = MutableStateFlow(false)
    val signUpSuccess: StateFlow<Boolean> = _signUpSuccess.asStateFlow()

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess.asStateFlow()

    fun updateEmail(value: String) {
        _email.value = value
    }

    fun updateUsername(value: String) {
        _username.value = value
    }

    fun updatePassword(value: String) {
        _password.value = value
    }

    fun updateConfirmPassword(value: String) {
        _confirmPassword.value = value
    }

    fun signUp() {
        if (!validateSignUp()) return

        viewModelScope.launch {
            val result = authService.signUp(_email.value, _username.value, _password.value)
            if (result is ApiResult.Success) {
                _signUpSuccess.value = true
                clearFields()
            }
        }
    }

    fun login() {
        if (!validateLogin()) return

        viewModelScope.launch {
            val result = authService.login(_email.value, _password.value)
            if (result is ApiResult.Success) {
                _loginSuccess.value = true
                clearFields()
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = authService.login(email, password)
            if (result is ApiResult.Success) {
                _loginSuccess.value = true
            }
        }
    }

    fun signUp(email: String, password: String, displayName: String) {
        viewModelScope.launch {
            val result = authService.signUp(email, displayName, password)
            if (result is ApiResult.Success) {
                _signUpSuccess.value = true
            }
        }
    }

    fun enableAdminMode() {
        viewModelScope.launch {
            authService.enableAdminMode()
            _loginSuccess.value = true
        }
    }

    fun logout() {
        authService.logout()
        _loginSuccess.value = false
        _signUpSuccess.value = false
    }

    fun clearError() {
        authService.clearError()
    }

    fun clearSuccess() {
        _signUpSuccess.value = false
        _loginSuccess.value = false
    }

    private fun validateSignUp(): Boolean {
        if (_email.value.isBlank() || !isValidEmail(_email.value)) {
            return false
        }
        if (_username.value.length < 3) {
            return false
        }
        if (_password.value.length < 6) {
            return false
        }
        if (_password.value != _confirmPassword.value) {
            return false
        }
        return true
    }

    private fun validateLogin(): Boolean {
        if (_email.value.isBlank()) return false
        if (_password.value.isBlank()) return false
        return true
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun clearFields() {
        _email.value = ""
        _username.value = ""
        _password.value = ""
        _confirmPassword.value = ""
    }

    val isPremium: Boolean
        get() = authService.isPremium
}
