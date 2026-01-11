package com.kquantum.nativeapp.di;

import com.kquantum.nativeapp.data.remote.ApiClient;
import com.kquantum.nativeapp.data.remote.TokenManager;
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
public final class AppModule_ProvideApiClientFactory implements Factory<ApiClient> {
  private final Provider<TokenManager> tokenManagerProvider;

  public AppModule_ProvideApiClientFactory(Provider<TokenManager> tokenManagerProvider) {
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public ApiClient get() {
    return provideApiClient(tokenManagerProvider.get());
  }

  public static AppModule_ProvideApiClientFactory create(
      Provider<TokenManager> tokenManagerProvider) {
    return new AppModule_ProvideApiClientFactory(tokenManagerProvider);
  }

  public static ApiClient provideApiClient(TokenManager tokenManager) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideApiClient(tokenManager));
  }
}
