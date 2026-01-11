# K-QuantumNative

K-QuantumNative is an Android application for learning quantum computing, ported from the iOS app QuantumNative. It provides an interactive platform for exploring quantum concepts, building circuits, and connecting to real IBM Quantum hardware.

## Features

### Learning Platform
- **Structured Learning Tracks**: Progress through quantum computing topics from fundamentals to advanced algorithms
- **Interactive Lessons**: Engaging content with visual explanations
- **Practice Sessions**: Test your knowledge with quizzes and exercises
- **Achievement System**: Earn XP, unlock achievements, and track your progress

### Quantum Bridge
- **IBM Quantum Integration**: Connect to real quantum hardware via QuantumBridge API
- **Circuit Builder**: Create and run OpenQASM circuits
- **Real-time Noise Data**: Monitor quantum processor noise levels
- **Job Management**: Submit, monitor, and retrieve results from quantum jobs

### Subscription Tiers
- **Free**: Basic learning content and limited practice
- **Pro**: All learning tracks, unlimited practice, 10 bridge jobs/day
- **Premium**: Unlimited bridge access, real-time noise data, advanced features

## Tech Stack

- **Language**: Kotlin 1.9.0
- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: MVVM with Clean Architecture
- **Dependency Injection**: Hilt
- **Networking**: Retrofit + OkHttp + Kotlinx Serialization
- **Local Storage**: DataStore, EncryptedSharedPreferences
- **Billing**: Google Play Billing Library 7.0
- **Navigation**: Jetpack Navigation Compose
- **Async**: Kotlin Coroutines + Flow

## Project Structure

```
app/src/main/java/com/kquantum/native/
├── KQuantumNativeApp.kt          # Application class with Hilt
├── MainActivity.kt               # Main activity entry point
├── data/
│   ├── models/                   # Data models
│   │   ├── AuthModels.kt
│   │   ├── SubscriptionModels.kt
│   │   ├── LearningModels.kt
│   │   ├── AchievementModels.kt
│   │   ├── QuantumModels.kt
│   │   ├── BridgeModels.kt
│   │   └── UserProgress.kt
│   └── remote/
│       ├── ApiClient.kt          # Retrofit API client
│       └── TokenManager.kt       # Secure token storage
├── services/
│   ├── auth/
│   │   └── AuthService.kt        # Authentication
│   ├── billing/
│   │   └── BillingService.kt     # Google Play Billing
│   ├── bridge/
│   │   └── QuantumBridgeService.kt # IBM Quantum integration
│   ├── progress/
│   │   └── ProgressService.kt    # User progress tracking
│   ├── learning/
│   │   └── LearningService.kt    # Learning content
│   └── achievement/
│       └── AchievementService.kt # Achievement tracking
├── di/
│   └── AppModule.kt              # Hilt DI module
└── presentation/
    ├── theme/
    │   ├── Color.kt              # Color palette
    │   ├── Type.kt               # Typography
    │   └── Theme.kt              # Material theme
    ├── navigation/
    │   └── NavGraph.kt           # Navigation setup
    ├── viewmodels/
    │   ├── AuthViewModel.kt
    │   ├── HomeViewModel.kt
    │   ├── LearnViewModel.kt
    │   ├── PracticeViewModel.kt
    │   ├── ExploreViewModel.kt
    │   ├── BridgeViewModel.kt
    │   ├── AchievementViewModel.kt
    │   ├── ProfileViewModel.kt
    │   └── SubscriptionViewModel.kt
    └── screens/
        ├── SplashScreen.kt
        ├── OnboardingScreen.kt
        ├── LoginScreen.kt
        ├── SignUpScreen.kt
        ├── HomeScreen.kt
        ├── LearnScreen.kt
        ├── PracticeScreen.kt
        ├── ExploreScreen.kt
        ├── BridgeScreen.kt
        ├── AchievementsScreen.kt
        ├── ProfileScreen.kt
        ├── SubscriptionScreen.kt
        └── PassportScreen.kt
```

## Requirements

- Android Studio Hedgehog (2023.1.1) or later
- Android SDK 34
- Kotlin 1.9.0+
- JDK 17

## Setup

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Build and run on an emulator or device

### Configuration

Update the API endpoints in `app/build.gradle.kts`:

```kotlin
buildConfigField("String", "API_BASE_URL", "\"https://your-api-url.com/\"")
buildConfigField("String", "BRIDGE_API_URL", "\"https://your-bridge-api.com/\"")
```

### Google Play Billing Setup

1. Create your app in Google Play Console
2. Add subscription products with IDs:
   - `pro_monthly`
   - `pro_yearly`
   - `premium_monthly`
   - `premium_yearly`
3. Configure license testing

## Building

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Run tests
./gradlew testDebugUnitTest
```

## Architecture

The app follows MVVM architecture with Clean Architecture principles:

- **Presentation Layer**: Compose UI + ViewModels
- **Domain Layer**: Use cases and business logic (in services)
- **Data Layer**: Repositories, API clients, local storage

### Key Design Patterns

- **Repository Pattern**: Data access abstraction
- **StateFlow**: Reactive state management
- **Hilt DI**: Dependency injection for testability
- **Single Activity**: Navigation Compose for screen management

## Related Projects

- **K-Quantum**: Android quantum computing library (port of SwiftQuantum)
- **SwiftQuantum**: iOS quantum computing library
- **QuantumNative**: iOS app (original)
- **SwiftQuantumBackend**: Backend API server
- **QuantumBridge**: IBM Quantum hardware integration

## License

Copyright (c) 2025 Eunmin Park. All rights reserved.

## Author

Eunmin Park
