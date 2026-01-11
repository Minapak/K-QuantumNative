/**
 * K-QuantumNative - Application Class
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KQuantumNativeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize any app-wide configurations here
    }
}
