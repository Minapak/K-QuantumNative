/**
 * K-QuantumNative - Authentication Models
 * Port of QuantumNative/Models/AuthModels.swift
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val email: String,
    val username: String,
    val password: String
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class AuthResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("token_type")
    val tokenType: String,
    @SerialName("user_id")
    val userId: String,
    val username: String,
    val email: String,
    @SerialName("expires_in")
    val expiresIn: Int? = null
)

@Serializable
data class UserResponse(
    val id: String,
    val email: String,
    val username: String,
    @SerialName("display_name")
    val displayName: String? = null,
    @SerialName("subscription_type")
    val subscriptionType: String? = null,
    @SerialName("total_xp")
    val totalXp: Int = 0,
    @SerialName("current_level")
    val currentLevel: Int = 1,
    @SerialName("current_streak")
    val currentStreak: Int = 0,
    @SerialName("longest_streak")
    val longestStreak: Int = 0,
    @SerialName("lessons_completed")
    val lessonsCompleted: Int = 0,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("is_active")
    val isActive: Boolean = true,
    @SerialName("is_premium")
    val isPremium: Boolean = false,
    @SerialName("subscription_tier")
    val subscriptionTier: String? = null,
    @SerialName("subscription_expires_at")
    val subscriptionExpiresAt: String? = null
)

@Serializable
data class UserStatsResponse(
    @SerialName("total_xp")
    val totalXp: Int = 0,
    @SerialName("current_level")
    val currentLevel: Int = 1,
    @SerialName("current_streak")
    val currentStreak: Int = 0,
    @SerialName("longest_streak")
    val longestStreak: Int = 0,
    @SerialName("levels_completed")
    val levelsCompleted: Int = 0,
    @SerialName("lessons_completed")
    val lessonsCompleted: Int = 0,
    @SerialName("total_study_time_minutes")
    val totalStudyTimeMinutes: Int = 0,
    @SerialName("xp_until_next_level")
    val xpUntilNextLevel: Int = 100,
    @SerialName("level_progress")
    val levelProgress: Double = 0.0
)

@Serializable
data class UpdateProfileRequest(
    val username: String? = null,
    val email: String? = null
)

@Serializable
data class ChangePasswordRequest(
    @SerialName("current_password")
    val currentPassword: String,
    @SerialName("new_password")
    val newPassword: String
)

@Serializable
data class ResetPasswordRequest(
    val email: String
)

@Serializable
data class ResetPasswordConfirmRequest(
    val token: String,
    @SerialName("new_password")
    val newPassword: String
)

@Serializable
data class VerifyEmailRequest(
    val token: String
)

@Serializable
data class ApiErrorResponse(
    val detail: String? = null,
    val message: String? = null
)
