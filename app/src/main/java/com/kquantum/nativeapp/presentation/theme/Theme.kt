/**
 * K-QuantumNative - App Theme
 * Port of QuantumNative SwiftUI Theme
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = QuantumBlue,
    onPrimary = Color.White,
    primaryContainer = QuantumPurple,
    onPrimaryContainer = Color.White,
    secondary = QuantumCyan,
    onSecondary = Color.Black,
    secondaryContainer = DarkCard,
    onSecondaryContainer = TextPrimary,
    tertiary = QuantumPink,
    onTertiary = Color.White,
    tertiaryContainer = DarkCardElevated,
    onTertiaryContainer = TextPrimary,
    error = StatusError,
    onError = Color.White,
    errorContainer = StatusError.copy(alpha = 0.2f),
    onErrorContainer = StatusError,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkCard,
    onSurfaceVariant = TextSecondary,
    outline = TextTertiary,
    outlineVariant = DarkCardElevated,
    inverseSurface = LightSurface,
    inverseOnSurface = TextPrimaryLight,
    inversePrimary = QuantumBlue,
    surfaceTint = QuantumBlue
)

private val LightColorScheme = lightColorScheme(
    primary = QuantumBlue,
    onPrimary = Color.White,
    primaryContainer = QuantumBlue.copy(alpha = 0.1f),
    onPrimaryContainer = QuantumBlue,
    secondary = QuantumPurple,
    onSecondary = Color.White,
    secondaryContainer = LightCard,
    onSecondaryContainer = TextPrimaryLight,
    tertiary = QuantumPink,
    onTertiary = Color.White,
    tertiaryContainer = LightCard,
    onTertiaryContainer = TextPrimaryLight,
    error = StatusError,
    onError = Color.White,
    errorContainer = StatusError.copy(alpha = 0.1f),
    onErrorContainer = StatusError,
    background = LightBackground,
    onBackground = TextPrimaryLight,
    surface = LightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = LightCard,
    onSurfaceVariant = TextSecondaryLight,
    outline = TextSecondaryLight,
    outlineVariant = LightCard,
    inverseSurface = DarkSurface,
    inverseOnSurface = TextPrimary,
    inversePrimary = QuantumCyan,
    surfaceTint = QuantumBlue
)

@Composable
fun KQuantumNativeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disable dynamic color to maintain quantum theme
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
