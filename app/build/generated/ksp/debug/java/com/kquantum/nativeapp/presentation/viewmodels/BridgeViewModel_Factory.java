package com.kquantum.nativeapp.presentation.viewmodels;

import com.kquantum.nativeapp.services.bridge.QuantumBridgeService;
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
public final class BridgeViewModel_Factory implements Factory<BridgeViewModel> {
  private final Provider<QuantumBridgeService> bridgeServiceProvider;

  public BridgeViewModel_Factory(Provider<QuantumBridgeService> bridgeServiceProvider) {
    this.bridgeServiceProvider = bridgeServiceProvider;
  }

  @Override
  public BridgeViewModel get() {
    return newInstance(bridgeServiceProvider.get());
  }

  public static BridgeViewModel_Factory create(
      Provider<QuantumBridgeService> bridgeServiceProvider) {
    return new BridgeViewModel_Factory(bridgeServiceProvider);
  }

  public static BridgeViewModel newInstance(QuantumBridgeService bridgeService) {
    return new BridgeViewModel(bridgeService);
  }
}
