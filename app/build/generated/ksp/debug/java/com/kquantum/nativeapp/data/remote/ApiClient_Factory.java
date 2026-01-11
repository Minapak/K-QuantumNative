package com.kquantum.nativeapp.data.remote;

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
public final class ApiClient_Factory implements Factory<ApiClient> {
  private final Provider<TokenManager> tokenManagerProvider;

  public ApiClient_Factory(Provider<TokenManager> tokenManagerProvider) {
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public ApiClient get() {
    return newInstance(tokenManagerProvider.get());
  }

  public static ApiClient_Factory create(Provider<TokenManager> tokenManagerProvider) {
    return new ApiClient_Factory(tokenManagerProvider);
  }

  public static ApiClient newInstance(TokenManager tokenManager) {
    return new ApiClient(tokenManager);
  }
}
