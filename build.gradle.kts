// K-QuantumNative - Top-level build file
// Android/Kotlin implementation of QuantumNative (iOS)
// Copyright (c) 2025 Eunmin Park. All rights reserved.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
}
