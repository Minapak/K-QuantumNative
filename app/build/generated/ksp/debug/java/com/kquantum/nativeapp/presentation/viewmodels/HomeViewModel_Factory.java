package com.kquantum.nativeapp.presentation.viewmodels;

import com.kquantum.nativeapp.services.auth.AuthService;
import com.kquantum.nativeapp.services.learning.LearningService;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<AuthService> authServiceProvider;

  private final Provider<ProgressService> progressServiceProvider;

  private final Provider<LearningService> learningServiceProvider;

  public HomeViewModel_Factory(Provider<AuthService> authServiceProvider,
      Provider<ProgressService> progressServiceProvider,
      Provider<LearningService> learningServiceProvider) {
    this.authServiceProvider = authServiceProvider;
    this.progressServiceProvider = progressServiceProvider;
    this.learningServiceProvider = learningServiceProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(authServiceProvider.get(), progressServiceProvider.get(), learningServiceProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<AuthService> authServiceProvider,
      Provider<ProgressService> progressServiceProvider,
      Provider<LearningService> learningServiceProvider) {
    return new HomeViewModel_Factory(authServiceProvider, progressServiceProvider, learningServiceProvider);
  }

  public static HomeViewModel newInstance(AuthService authService, ProgressService progressService,
      LearningService learningService) {
    return new HomeViewModel(authService, progressService, learningService);
  }
}
