package com.kquantum.nativeapp.presentation.viewmodels;

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
public final class LearnViewModel_Factory implements Factory<LearnViewModel> {
  private final Provider<LearningService> learningServiceProvider;

  private final Provider<ProgressService> progressServiceProvider;

  public LearnViewModel_Factory(Provider<LearningService> learningServiceProvider,
      Provider<ProgressService> progressServiceProvider) {
    this.learningServiceProvider = learningServiceProvider;
    this.progressServiceProvider = progressServiceProvider;
  }

  @Override
  public LearnViewModel get() {
    return newInstance(learningServiceProvider.get(), progressServiceProvider.get());
  }

  public static LearnViewModel_Factory create(Provider<LearningService> learningServiceProvider,
      Provider<ProgressService> progressServiceProvider) {
    return new LearnViewModel_Factory(learningServiceProvider, progressServiceProvider);
  }

  public static LearnViewModel newInstance(LearningService learningService,
      ProgressService progressService) {
    return new LearnViewModel(learningService, progressService);
  }
}
