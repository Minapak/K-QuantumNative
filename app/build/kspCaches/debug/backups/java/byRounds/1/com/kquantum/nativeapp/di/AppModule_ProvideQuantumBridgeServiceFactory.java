package com.kquantum.nativeapp.di;

import com.kquantum.nativeapp.services.bridge.QuantumBridgeService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideQuantumBridgeServiceFactory implements Factory<QuantumBridgeService> {
  @Override
  public QuantumBridgeService get() {
    return provideQuantumBridgeService();
  }

  public static AppModule_ProvideQuantumBridgeServiceFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static QuantumBridgeService provideQuantumBridgeService() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideQuantumBridgeService());
  }

  private static final class InstanceHolder {
    private static final AppModule_ProvideQuantumBridgeServiceFactory INSTANCE = new AppModule_ProvideQuantumBridgeServiceFactory();
  }
}
