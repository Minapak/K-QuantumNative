/**
 * K-QuantumNative - API Client
 * Port of QuantumNative/Services/APIClient.swift
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kquantum.nativeapp.BuildConfig
import com.kquantum.nativeapp.data.models.*
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

interface QuantumApiService {

    // Auth endpoints
    @POST("api/v1/auth/signup")
    suspend fun signUp(@Body request: SignUpRequest): Response<AuthResponse>

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("api/v1/users/me")
    suspend fun getCurrentUser(): Response<UserResponse>

    @PUT("api/v1/users/me")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<UserResponse>

    @POST("api/v1/auth/verify-email")
    suspend fun verifyEmail(@Body request: VerifyEmailRequest): Response<Unit>

    @POST("api/v1/auth/reset-password-request")
    suspend fun requestPasswordReset(@Body request: ResetPasswordRequest): Response<Unit>

    @POST("api/v1/auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordConfirmRequest): Response<Unit>

    @POST("api/v1/auth/change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<Unit>

    // User stats
    @GET("api/v1/users/me/stats")
    suspend fun getUserStats(): Response<UserStatsResponse>

    // Learning endpoints
    @GET("api/v1/learning/tracks")
    suspend fun getLearningTracks(): Response<List<LearningTrack>>

    @GET("api/v1/learning/levels/{track}")
    suspend fun getLevelsForTrack(@Path("track") track: String): Response<List<LearningLevel>>

    @POST("api/v1/learning/progress/complete/{levelId}")
    suspend fun completeLevel(
        @Path("levelId") levelId: String,
        @Body request: LevelCompleteRequest
    ): Response<Unit>

    // Progress endpoints
    @POST("api/v1/progress/xp")
    suspend fun addXp(@Body request: ProgressUpdateRequest): Response<UserProgress>

    // Achievements endpoints
    @GET("api/v1/achievements/")
    suspend fun getAchievements(): Response<List<AchievementResponse>>

    @POST("api/v1/achievements/{id}/unlock")
    suspend fun unlockAchievement(@Path("id") id: String): Response<Achievement>

    // Payment/Subscription endpoints
    @POST("api/v1/payment/verify")
    suspend fun verifyPurchase(@Body request: VerifyPurchaseRequest): Response<VerifyPurchaseResponse>

    @GET("api/v1/payment/subscription/status")
    suspend fun getSubscriptionStatus(): Response<SubscriptionSyncResponse>
}

@Singleton
class ApiClient @Inject constructor(
    private val tokenManager: TokenManager
) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        coerceInputValues = true
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG_MODE) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val authInterceptor = okhttp3.Interceptor { chain ->
        val originalRequest = chain.request()
        val token = tokenManager.getToken()

        val newRequest = if (token != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build()
        } else {
            originalRequest.newBuilder()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build()
        }

        val response = chain.proceed(newRequest)

        // Clear token on 401 Unauthorized
        if (response.code == 401) {
            tokenManager.clearToken()
        }

        response
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL + "/")
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val api: QuantumApiService = retrofit.create(QuantumApiService::class.java)
}

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val code: Int? = null) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()

    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
}

suspend fun <T> safeApiCall(call: suspend () -> Response<T>): ApiResult<T> {
    return try {
        val response = call()
        if (response.isSuccessful) {
            response.body()?.let {
                ApiResult.Success(it)
            } ?: ApiResult.Error("Empty response body")
        } else {
            val errorBody = response.errorBody()?.string()
            val message = try {
                Json.decodeFromString<ApiErrorResponse>(errorBody ?: "").detail
                    ?: Json.decodeFromString<ApiErrorResponse>(errorBody ?: "").message
                    ?: "Unknown error"
            } catch (e: Exception) {
                errorBody ?: "Unknown error"
            }
            ApiResult.Error(message, response.code())
        }
    } catch (e: Exception) {
        ApiResult.Error(e.message ?: "Network error")
    }
}
