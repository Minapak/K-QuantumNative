package com.kquantum.nativeapp.presentation.viewmodels;

import com.kquantum.nativeapp.services.achievement.AchievementService;
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
public final class AchievementViewModel_Factory implements Factory<AchievementViewModel> {
  private final Provider<AchievementService> achievementServiceProvider;

  public AchievementViewModel_Factory(Provider<AchievementService> achievementServiceProvider) {
    this.achievementServiceProvider = achievementServiceProvider;
  }

  @Override
  public AchievementViewModel get() {
    return newInstance(achievementServiceProvider.get());
  }

  public static AchievementViewModel_Factory create(
      Provider<AchievementService> achievementServiceProvider) {
    return new AchievementViewModel_Factory(achievementServiceProvider);
  }

  public static AchievementViewModel newInstance(AchievementService achievementService) {
    return new AchievementViewModel(achievementService);
  }
}
