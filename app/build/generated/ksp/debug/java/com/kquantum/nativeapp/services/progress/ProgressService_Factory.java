package com.kquantum.nativeapp.services.progress;

import android.content.Context;
import com.kquantum.nativeapp.data.remote.ApiClient;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class ProgressService_Factory implements Factory<ProgressService> {
  private final Provider<Context> contextProvider;

  private final Provider<ApiClient> apiClientProvider;

  public ProgressService_Factory(Provider<Context> contextProvider,
      Provider<ApiClient> apiClientProvider) {
    this.contextProvider = contextProvider;
    this.apiClientProvider = apiClientProvider;
  }

  @Override
  public ProgressService get() {
    return newInstance(contextProvider.get(), apiClientProvider.get());
  }

  public static ProgressService_Factory create(Provider<Context> contextProvider,
      Provider<ApiClient> apiClientProvider) {
    return new ProgressService_Factory(contextProvider, apiClientProvider);
  }

  public static ProgressService newInstance(Context context, ApiClient apiClient) {
    return new ProgressService(context, apiClient);
  }
}
