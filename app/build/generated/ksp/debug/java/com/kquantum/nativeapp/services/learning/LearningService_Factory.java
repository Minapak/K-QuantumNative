package com.kquantum.nativeapp.services.learning;

import com.kquantum.nativeapp.data.remote.ApiClient;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class LearningService_Factory implements Factory<LearningService> {
  private final Provider<ApiClient> apiClientProvider;

  public LearningService_Factory(Provider<ApiClient> apiClientProvider) {
    this.apiClientProvider = apiClientProvider;
  }

  @Override
  public LearningService get() {
    return newInstance(apiClientProvider.get());
  }

  public static LearningService_Factory create(Provider<ApiClient> apiClientProvider) {
    return new LearningService_Factory(apiClientProvider);
  }

  public static LearningService newInstance(ApiClient apiClient) {
    return new LearningService(apiClient);
  }
}
