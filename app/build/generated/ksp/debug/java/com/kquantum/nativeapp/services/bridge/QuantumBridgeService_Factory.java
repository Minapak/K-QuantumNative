package com.kquantum.nativeapp.services.bridge;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class QuantumBridgeService_Factory implements Factory<QuantumBridgeService> {
  @Override
  public QuantumBridgeService get() {
    return newInstance();
  }

  public static QuantumBridgeService_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static QuantumBridgeService newInstance() {
    return new QuantumBridgeService();
  }

  private static final class InstanceHolder {
    private static final QuantumBridgeService_Factory INSTANCE = new QuantumBridgeService_Factory();
  }
}
