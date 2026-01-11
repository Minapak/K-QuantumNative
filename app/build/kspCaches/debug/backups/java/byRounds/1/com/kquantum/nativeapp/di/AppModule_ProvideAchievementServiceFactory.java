package com.kquantum.nativeapp.di;

import android.content.Context;
import com.kquantum.nativeapp.data.remote.ApiClient;
import com.kquantum.nativeapp.services.achievement.AchievementService;
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
public final class AppModule_ProvideAchievementServiceFactory implements Factory<AchievementService> {
  private final Provider<Context> contextProvider;

  private final Provider<ApiClient> apiClientProvider;

  public AppModule_ProvideAchievementServiceFactory(Provider<Context> contextProvider,
      Provider<ApiClient> apiClientProvider) {
    this.contextProvider = contextProvider;
    this.apiClientProvider = apiClientProvider;
  }

  @Override
  public AchievementService get() {
    return provideAchievementService(contextProvider.get(), apiClientProvider.get());
  }

  public static AppModule_ProvideAchievementServiceFactory create(Provider<Context> contextProvider,
      Provider<ApiClient> apiClientProvider) {
    return new AppModule_ProvideAchievementServiceFactory(contextProvider, apiClientProvider);
  }

  public static AchievementService provideAchievementService(Context context, ApiClient apiClient) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideAchievementService(context, apiClient));
  }
}
