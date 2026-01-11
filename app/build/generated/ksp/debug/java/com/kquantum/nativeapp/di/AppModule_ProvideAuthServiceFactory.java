package com.kquantum.nativeapp.di;

import com.kquantum.nativeapp.data.remote.ApiClient;
import com.kquantum.nativeapp.data.remote.TokenManager;
import com.kquantum.nativeapp.services.auth.AuthService;
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
public final class AppModule_ProvideAuthServiceFactory implements Factory<AuthService> {
  private final Provider<ApiClient> apiClientProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  public AppModule_ProvideAuthServiceFactory(Provider<ApiClient> apiClientProvider,
      Provider<TokenManager> tokenManagerProvider) {
    this.apiClientProvider = apiClientProvider;
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public AuthService get() {
    return provideAuthService(apiClientProvider.get(), tokenManagerProvider.get());
  }

  public static AppModule_ProvideAuthServiceFactory create(Provider<ApiClient> apiClientProvider,
      Provider<TokenManager> tokenManagerProvider) {
    return new AppModule_ProvideAuthServiceFactory(apiClientProvider, tokenManagerProvider);
  }

  public static AuthService provideAuthService(ApiClient apiClient, TokenManager tokenManager) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideAuthService(apiClient, tokenManager));
  }
}
