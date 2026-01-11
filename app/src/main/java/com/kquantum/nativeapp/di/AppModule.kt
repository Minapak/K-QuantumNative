/**
 * K-QuantumNative - Hilt Dependency Injection Module
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.di

import android.content.Context
import com.kquantum.nativeapp.data.remote.ApiClient
import com.kquantum.nativeapp.data.remote.TokenManager
import com.kquantum.nativeapp.services.achievement.AchievementService
import com.kquantum.nativeapp.services.auth.AuthService
import com.kquantum.nativeapp.services.billing.BillingService
import com.kquantum.nativeapp.services.bridge.QuantumBridgeService
import com.kquantum.nativeapp.services.learning.LearningService
import com.kquantum.nativeapp.services.progress.ProgressService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTokenManager(
        @ApplicationContext context: Context
    ): TokenManager {
        return TokenManager(context)
    }

    @Provides
    @Singleton
    fun provideApiClient(
        tokenManager: TokenManager
    ): ApiClient {
        return ApiClient(tokenManager)
    }

    @Provides
    @Singleton
    fun provideAuthService(
        apiClient: ApiClient,
        tokenManager: TokenManager
    ): AuthService {
        return AuthService(apiClient, tokenManager)
    }

    @Provides
    @Singleton
    fun provideBillingService(
        @ApplicationContext context: Context,
        apiClient: ApiClient
    ): BillingService {
        return BillingService(context, apiClient)
    }

    @Provides
    @Singleton
    fun provideQuantumBridgeService(): QuantumBridgeService {
        return QuantumBridgeService()
    }

    @Provides
    @Singleton
    fun provideProgressService(
        @ApplicationContext context: Context,
        apiClient: ApiClient
    ): ProgressService {
        return ProgressService(context, apiClient)
    }

    @Provides
    @Singleton
    fun provideLearningService(
        apiClient: ApiClient
    ): LearningService {
        return LearningService(apiClient)
    }

    @Provides
    @Singleton
    fun provideAchievementService(
        @ApplicationContext context: Context,
        apiClient: ApiClient
    ): AchievementService {
        return AchievementService(context, apiClient)
    }
}
