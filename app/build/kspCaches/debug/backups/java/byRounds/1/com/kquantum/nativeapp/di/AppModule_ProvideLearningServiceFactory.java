package com.kquantum.nativeapp.di;

import com.kquantum.nativeapp.data.remote.ApiClient;
import com.kquantum.nativeapp.services.learning.LearningService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AppModule_ProvideLearningServiceFactory implements Factory<LearningService> {
  private final Provider<ApiClient> apiClientProvider;

  public AppModule_ProvideLearningServiceFactory(Provider<ApiClient> apiClientProvider) {
    this.apiClientProvider = apiClientProvider;
  }

  @Override
  public LearningService get() {
    return provideLearningService(apiClientProvider.get());
  }

  public static AppModule_ProvideLearningServiceFactory create(
      Provider<ApiClient> apiClientProvider) {
    return new AppModule_ProvideLearningServiceFactory(apiClientProvider);
  }

  public static LearningService provideLearningService(ApiClient apiClient) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideLearningService(apiClient));
  }
}
