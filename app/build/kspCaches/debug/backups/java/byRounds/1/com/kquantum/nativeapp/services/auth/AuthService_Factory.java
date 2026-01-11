package com.kquantum.nativeapp.services.auth;

import com.kquantum.nativeapp.data.remote.ApiClient;
import com.kquantum.nativeapp.data.remote.TokenManager;
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
public final class AuthService_Factory implements Factory<AuthService> {
  private final Provider<ApiClient> apiClientProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  public AuthService_Factory(Provider<ApiClient> apiClientProvider,
      Provider<TokenManager> tokenManagerProvider) {
    this.apiClientProvider = apiClientProvider;
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public AuthService get() {
    return newInstance(apiClientProvider.get(), tokenManagerProvider.get());
  }

  public static AuthService_Factory create(Provider<ApiClient> apiClientProvider,
      Provider<TokenManager> tokenManagerProvider) {
    return new AuthService_Factory(apiClientProvider, tokenManagerProvider);
  }

  public static AuthService newInstance(ApiClient apiClient, TokenManager tokenManager) {
    return new AuthService(apiClient, tokenManager);
  }
}
