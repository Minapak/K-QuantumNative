package com.kquantum.nativeapp.presentation.viewmodels;

import com.kquantum.nativeapp.services.billing.BillingService;
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
public final class SubscriptionViewModel_Factory implements Factory<SubscriptionViewModel> {
  private final Provider<BillingService> billingServiceProvider;

  public SubscriptionViewModel_Factory(Provider<BillingService> billingServiceProvider) {
    this.billingServiceProvider = billingServiceProvider;
  }

  @Override
  public SubscriptionViewModel get() {
    return newInstance(billingServiceProvider.get());
  }

  public static SubscriptionViewModel_Factory create(
      Provider<BillingService> billingServiceProvider) {
    return new SubscriptionViewModel_Factory(billingServiceProvider);
  }

  public static SubscriptionViewModel newInstance(BillingService billingService) {
    return new SubscriptionViewModel(billingService);
  }
}
