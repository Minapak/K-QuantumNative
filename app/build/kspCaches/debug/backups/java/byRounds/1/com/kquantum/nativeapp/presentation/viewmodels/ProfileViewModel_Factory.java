package com.kquantum.nativeapp.presentation.viewmodels;

import com.kquantum.nativeapp.services.achievement.AchievementService;
import com.kquantum.nativeapp.services.auth.AuthService;
import com.kquantum.nativeapp.services.billing.BillingService;
import com.kquantum.nativeapp.services.progress.ProgressService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class ProfileViewModel_Factory implements Factory<ProfileViewModel> {
  private final Provider<AuthService> authServiceProvider;

  private final Provider<ProgressService> progressServiceProvider;

  private final Provider<AchievementService> achievementServiceProvider;

  private final Provider<BillingService> billingServiceProvider;

  public ProfileViewModel_Factory(Provider<AuthService> authServiceProvider,
      Provider<ProgressService> progressServiceProvider,
      Provider<AchievementService> achievementServiceProvider,
      Provider<BillingService> billingServiceProvider) {
    this.authServiceProvider = authServiceProvider;
    this.progressServiceProvider = progressServiceProvider;
    this.achievementServiceProvider = achievementServiceProvider;
    this.billingServiceProvider = billingServiceProvider;
  }

  @Override
  public ProfileViewModel get() {
    return newInstance(authServiceProvider.get(), progressServiceProvider.get(), achievementServiceProvider.get(), billingServiceProvider.get());
  }

  public static ProfileViewModel_Factory create(Provider<AuthService> authServiceProvider,
      Provider<ProgressService> progressServiceProvider,
      Provider<AchievementService> achievementServiceProvider,
      Provider<BillingService> billingServiceProvider) {
    return new ProfileViewModel_Factory(authServiceProvider, progressServiceProvider, achievementServiceProvider, billingServiceProvider);
  }

  public static ProfileViewModel newInstance(AuthService authService,
      ProgressService progressService, AchievementService achievementService,
      BillingService billingService) {
    return new ProfileViewModel(authService, progressService, achievementService, billingService);
  }
}
