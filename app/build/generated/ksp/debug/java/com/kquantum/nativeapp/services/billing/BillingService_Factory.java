package com.kquantum.nativeapp.services.billing;

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
public final class BillingService_Factory implements Factory<BillingService> {
  private final Provider<Context> contextProvider;

  private final Provider<ApiClient> apiClientProvider;

  public BillingService_Factory(Provider<Context> contextProvider,
      Provider<ApiClient> apiClientProvider) {
    this.contextProvider = contextProvider;
    this.apiClientProvider = apiClientProvider;
  }

  @Override
  public BillingService get() {
    return newInstance(contextProvider.get(), apiClientProvider.get());
  }

  public static BillingService_Factory create(Provider<Context> contextProvider,
      Provider<ApiClient> apiClientProvider) {
    return new BillingService_Factory(contextProvider, apiClientProvider);
  }

  public static BillingService newInstance(Context context, ApiClient apiClient) {
    return new BillingService(context, apiClient);
  }
}
