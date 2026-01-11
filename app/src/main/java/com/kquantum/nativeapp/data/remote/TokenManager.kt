/**
 * K-QuantumNative - Token Manager
 * Port of QuantumNative/Services/KeychainService.swift
 * Uses EncryptedSharedPreferences for secure token storage
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.data.remote

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val PREFS_NAME = "kquantum_secure_prefs"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_IS_ADMIN = "is_admin"
    }

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedPrefs: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveToken(token: String) {
        encryptedPrefs.edit().putString(KEY_AUTH_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return encryptedPrefs.getString(KEY_AUTH_TOKEN, null)
    }

    fun clearToken() {
        encryptedPrefs.edit().remove(KEY_AUTH_TOKEN).apply()
    }

    fun hasToken(): Boolean {
        return getToken() != null
    }

    fun saveUserInfo(userId: String, username: String, email: String) {
        encryptedPrefs.edit()
            .putString(KEY_USER_ID, userId)
            .putString(KEY_USERNAME, username)
            .putString(KEY_EMAIL, email)
            .apply()
    }

    fun getUserId(): String? = encryptedPrefs.getString(KEY_USER_ID, null)
    fun getUsername(): String? = encryptedPrefs.getString(KEY_USERNAME, null)
    fun getEmail(): String? = encryptedPrefs.getString(KEY_EMAIL, null)

    fun setAdmin(isAdmin: Boolean) {
        encryptedPrefs.edit().putBoolean(KEY_IS_ADMIN, isAdmin).apply()
    }

    fun isAdmin(): Boolean {
        return encryptedPrefs.getBoolean(KEY_IS_ADMIN, false)
    }

    fun clearAll() {
        encryptedPrefs.edit().clear().apply()
    }
}
