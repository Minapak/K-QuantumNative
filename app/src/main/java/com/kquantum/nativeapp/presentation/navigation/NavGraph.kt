/**
 * K-QuantumNative - Navigation Graph
 * Port of QuantumNative Navigation
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kquantum.nativeapp.presentation.screens.*

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object Learn : Screen("learn")
    object LearnDetail : Screen("learn/{trackId}") {
        fun createRoute(trackId: String) = "learn/$trackId"
    }
    object Lesson : Screen("lesson/{lessonId}") {
        fun createRoute(lessonId: String) = "lesson/$lessonId"
    }
    object Practice : Screen("practice")
    object PracticeSession : Screen("practice/session")
    object Explore : Screen("explore")
    object ExploreDetail : Screen("explore/{itemId}") {
        fun createRoute(itemId: String) = "explore/$itemId"
    }
    object Bridge : Screen("bridge")
    object Achievements : Screen("achievements")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object Subscription : Screen("subscription")
    object Passport : Screen("passport")
}

@Composable
fun KQuantumNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onSignUpSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToLearn = { navController.navigate(Screen.Learn.route) },
                onNavigateToPractice = { navController.navigate(Screen.Practice.route) },
                onNavigateToExplore = { navController.navigate(Screen.Explore.route) },
                onNavigateToBridge = { navController.navigate(Screen.Bridge.route) },
                onNavigateToAchievements = { navController.navigate(Screen.Achievements.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                onNavigateToSubscription = { navController.navigate(Screen.Subscription.route) }
            )
        }

        composable(Screen.Learn.route) {
            LearnScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTrack = { trackId ->
                    navController.navigate(Screen.LearnDetail.createRoute(trackId))
                }
            )
        }

        composable(Screen.LearnDetail.route) { backStackEntry ->
            val trackId = backStackEntry.arguments?.getString("trackId") ?: return@composable
            LearnDetailScreen(
                trackId = trackId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLesson = { lessonId ->
                    navController.navigate(Screen.Lesson.createRoute(lessonId))
                }
            )
        }

        composable(Screen.Lesson.route) { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: return@composable
            LessonScreen(
                lessonId = lessonId,
                onNavigateBack = { navController.popBackStack() },
                onLessonComplete = { navController.popBackStack() }
            )
        }

        composable(Screen.Practice.route) {
            PracticeScreen(
                onNavigateBack = { navController.popBackStack() },
                onStartPractice = {
                    navController.navigate(Screen.PracticeSession.route)
                }
            )
        }

        composable(Screen.PracticeSession.route) {
            PracticeSessionScreen(
                onNavigateBack = { navController.popBackStack() },
                onComplete = { navController.popBackStack() }
            )
        }

        composable(Screen.Explore.route) {
            ExploreScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToItem = { itemId ->
                    navController.navigate(Screen.ExploreDetail.createRoute(itemId))
                }
            )
        }

        composable(Screen.ExploreDetail.route) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: return@composable
            ExploreDetailScreen(
                itemId = itemId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Bridge.route) {
            BridgeScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Achievements.route) {
            AchievementsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToSubscription = { navController.navigate(Screen.Subscription.route) },
                onNavigateToPassport = { navController.navigate(Screen.Passport.route) },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Subscription.route) {
            SubscriptionScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Passport.route) {
            PassportScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
