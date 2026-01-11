package com.kquantum.nativeapp.di;

import android.content.Context;
import com.kquantum.nativeapp.data.remote.ApiClient;
import com.kquantum.nativeapp.services.progress.ProgressService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideProgressServiceFactory implements Factory<ProgressService> {
  private final Provider<Context> contextProvider;

  private final Provider<ApiClient> apiClientProvider;

  public AppModule_ProvideProgressServiceFactory(Provider<Context> contextProvider,
      Provider<ApiClient> apiClientProvider) {
    this.contextProvider = contextProvider;
    this.apiClientProvider = apiClientProvider;
  }

  @Override
  public ProgressService get() {
    return provideProgressService(contextProvider.get(), apiClientProvider.get());
  }

  public static AppModule_ProvideProgressServiceFactory create(Provider<Context> contextProvider,
      Provider<ApiClient> apiClientProvider) {
    return new AppModule_ProvideProgressServiceFactory(contextProvider, apiClientProvider);
  }

  public static ProgressService provideProgressService(Context context, ApiClient apiClient) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideProgressService(context, apiClient));
  }
}
