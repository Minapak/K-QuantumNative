package com.kquantum.nativeapp;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.kquantum.nativeapp.data.remote.ApiClient;
import com.kquantum.nativeapp.data.remote.TokenManager;
import com.kquantum.nativeapp.di.AppModule_ProvideAchievementServiceFactory;
import com.kquantum.nativeapp.di.AppModule_ProvideApiClientFactory;
import com.kquantum.nativeapp.di.AppModule_ProvideAuthServiceFactory;
import com.kquantum.nativeapp.di.AppModule_ProvideBillingServiceFactory;
import com.kquantum.nativeapp.di.AppModule_ProvideLearningServiceFactory;
import com.kquantum.nativeapp.di.AppModule_ProvideProgressServiceFactory;
import com.kquantum.nativeapp.di.AppModule_ProvideQuantumBridgeServiceFactory;
import com.kquantum.nativeapp.di.AppModule_ProvideTokenManagerFactory;
import com.kquantum.nativeapp.presentation.viewmodels.AchievementViewModel;
import com.kquantum.nativeapp.presentation.viewmodels.AchievementViewModel_HiltModules;
import com.kquantum.nativeapp.presentation.viewmodels.AuthViewModel;
import com.kquantum.nativeapp.presentation.viewmodels.AuthViewModel_HiltModules;
import com.kquantum.nativeapp.presentation.viewmodels.BridgeViewModel;
import com.kquantum.nativeapp.presentation.viewmodels.BridgeViewModel_HiltModules;
import com.kquantum.nativeapp.presentation.viewmodels.ExploreViewModel;
import com.kquantum.nativeapp.presentation.viewmodels.ExploreViewModel_HiltModules;
import com.kquantum.nativeapp.presentation.viewmodels.HomeViewModel;
import com.kquantum.nativeapp.presentation.viewmodels.HomeViewModel_HiltModules;
import com.kquantum.nativeapp.presentation.viewmodels.LearnViewModel;
import com.kquantum.nativeapp.presentation.viewmodels.LearnViewModel_HiltModules;
import com.kquantum.nativeapp.presentation.viewmodels.PracticeViewModel;
import com.kquantum.nativeapp.presentation.viewmodels.PracticeViewModel_HiltModules;
import com.kquantum.nativeapp.presentation.viewmodels.ProfileViewModel;
import com.kquantum.nativeapp.presentation.viewmodels.ProfileViewModel_HiltModules;
import com.kquantum.nativeapp.presentation.viewmodels.SubscriptionViewModel;
import com.kquantum.nativeapp.presentation.viewmodels.SubscriptionViewModel_HiltModules;
import com.kquantum.nativeapp.services.achievement.AchievementService;
import com.kquantum.nativeapp.services.auth.AuthService;
import com.kquantum.nativeapp.services.billing.BillingService;
import com.kquantum.nativeapp.services.bridge.QuantumBridgeService;
import com.kquantum.nativeapp.services.learning.LearningService;
import com.kquantum.nativeapp.services.progress.ProgressService;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerKQuantumNativeApp_HiltComponents_SingletonC {
  private DaggerKQuantumNativeApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public KQuantumNativeApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements KQuantumNativeApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public KQuantumNativeApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements KQuantumNativeApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public KQuantumNativeApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements KQuantumNativeApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public KQuantumNativeApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements KQuantumNativeApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public KQuantumNativeApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements KQuantumNativeApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public KQuantumNativeApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements KQuantumNativeApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public KQuantumNativeApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements KQuantumNativeApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public KQuantumNativeApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends KQuantumNativeApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends KQuantumNativeApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends KQuantumNativeApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends KQuantumNativeApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(9).put(LazyClassKeyProvider.com_kquantum_nativeapp_presentation_viewmodels_AchievementViewModel, AchievementViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_kquantum_nativeapp_presentation_viewmodels_AuthViewModel, AuthViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_kquantum_nativeapp_presentation_viewmodels_BridgeViewModel, BridgeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_kquantum_nativeapp_presentation_viewmodels_ExploreViewModel, ExploreViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_kquantum_nativeapp_presentation_viewmodels_HomeViewModel, HomeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_kquantum_nativeapp_presentation_viewmodels_LearnViewModel, LearnViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_kquantum_nativeapp_presentation_viewmodels_PracticeViewModel, PracticeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_kquantum_nativeapp_presentation_viewmodels_ProfileViewModel, ProfileViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_kquantum_nativeapp_presentation_viewmodels_SubscriptionViewModel, SubscriptionViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_kquantum_nativeapp_presentation_viewmodels_SubscriptionViewModel = "com.kquantum.nativeapp.presentation.viewmodels.SubscriptionViewModel";

      static String com_kquantum_nativeapp_presentation_viewmodels_AuthViewModel = "com.kquantum.nativeapp.presentation.viewmodels.AuthViewModel";

      static String com_kquantum_nativeapp_presentation_viewmodels_LearnViewModel = "com.kquantum.nativeapp.presentation.viewmodels.LearnViewModel";

      static String com_kquantum_nativeapp_presentation_viewmodels_PracticeViewModel = "com.kquantum.nativeapp.presentation.viewmodels.PracticeViewModel";

      static String com_kquantum_nativeapp_presentation_viewmodels_ExploreViewModel = "com.kquantum.nativeapp.presentation.viewmodels.ExploreViewModel";

      static String com_kquantum_nativeapp_presentation_viewmodels_ProfileViewModel = "com.kquantum.nativeapp.presentation.viewmodels.ProfileViewModel";

      static String com_kquantum_nativeapp_presentation_viewmodels_AchievementViewModel = "com.kquantum.nativeapp.presentation.viewmodels.AchievementViewModel";

      static String com_kquantum_nativeapp_presentation_viewmodels_BridgeViewModel = "com.kquantum.nativeapp.presentation.viewmodels.BridgeViewModel";

      static String com_kquantum_nativeapp_presentation_viewmodels_HomeViewModel = "com.kquantum.nativeapp.presentation.viewmodels.HomeViewModel";

      @KeepFieldType
      SubscriptionViewModel com_kquantum_nativeapp_presentation_viewmodels_SubscriptionViewModel2;

      @KeepFieldType
      AuthViewModel com_kquantum_nativeapp_presentation_viewmodels_AuthViewModel2;

      @KeepFieldType
      LearnViewModel com_kquantum_nativeapp_presentation_viewmodels_LearnViewModel2;

      @KeepFieldType
      PracticeViewModel com_kquantum_nativeapp_presentation_viewmodels_PracticeViewModel2;

      @KeepFieldType
      ExploreViewModel com_kquantum_nativeapp_presentation_viewmodels_ExploreViewModel2;

      @KeepFieldType
      ProfileViewModel com_kquantum_nativeapp_presentation_viewmodels_ProfileViewModel2;

      @KeepFieldType
      AchievementViewModel com_kquantum_nativeapp_presentation_viewmodels_AchievementViewModel2;

      @KeepFieldType
      BridgeViewModel com_kquantum_nativeapp_presentation_viewmodels_BridgeViewModel2;

      @KeepFieldType
      HomeViewModel com_kquantum_nativeapp_presentation_viewmodels_HomeViewModel2;
    }
  }

  private static final class ViewModelCImpl extends KQuantumNativeApp_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AchievementViewModel> achievementViewModelProvider;

    private Provider<AuthViewModel> authViewModelProvider;

    private Provider<BridgeViewModel> bridgeViewModelProvider;

    private Provider<ExploreViewModel> exploreViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<LearnViewModel> learnViewModelProvider;

    private Provider<PracticeViewModel> practiceViewModelProvider;

    private Provider<ProfileViewModel> profileViewModelProvider;

    private Provider<SubscriptionViewModel> subscriptionViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.achievementViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.authViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.bridgeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.exploreViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.learnViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.practiceViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.profileViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.subscriptionViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(9).put(LazyClassKeyProvider.com_kquantum_nativeapp_presentation_viewmodels_AchievementViewModel, ((Provider) achievementViewModelProvider)).put(LazyClassKeyProvider.com_kquantum_nativeapp_presentation_viewmodels_AuthViewModel, ((Provider) authViewModelProvider)).put(LazyClassKeyProvider.com_kquantum_nativeapp_presentation_viewmodels_BridgeViewModel, ((Provider) bridgeViewModelProvider)).put(LazyClassKeyProvider.com_kquantum_nativeapp_presentation_viewmodels_ExploreViewModel, ((Provider) exploreViewModelProvider)).put(LazyClassKeyProvider.com_kquantum_nativeapp_presentation_viewmodels_HomeViewModel, ((Provider) homeViewModelProvider)).put(LazyClassKeyProvider.com_kquantum_nativeapp_presentation_viewmodels_LearnViewModel, ((Provider) learnViewModelProvider)).put(LazyClassKeyProvider.com_kquantum_nativeapp_presentation_viewmodels_PracticeViewModel, ((Provider) practiceViewModelProvider)).put(LazyClassKeyProvider.com_kquantum_nativeapp_presentation_viewmodels_ProfileViewModel, ((Provider) profileViewModelProvider)).put(LazyClassKeyProvider.com_kquantum_nativeapp_presentation_viewmodels_SubscriptionViewModel, ((Provider) subscriptionViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_kquantum_nativeapp_presentation_viewmodels_ProfileViewModel = "com.kquantum.nativeapp.presentation.viewmodels.ProfileViewModel";

      static String com_kquantum_nativeapp_presentation_viewmodels_SubscriptionViewModel = "com.kquantum.nativeapp.presentation.viewmodels.SubscriptionViewModel";

      static String com_kquantum_nativeapp_presentation_viewmodels_AchievementViewModel = "com.kquantum.nativeapp.presentation.viewmodels.AchievementViewModel";

      static String com_kquantum_nativeapp_presentation_viewmodels_BridgeViewModel = "com.kquantum.nativeapp.presentation.viewmodels.BridgeViewModel";

      static String com_kquantum_nativeapp_presentation_viewmodels_HomeViewModel = "com.kquantum.nativeapp.presentation.viewmodels.HomeViewModel";

      static String com_kquantum_nativeapp_presentation_viewmodels_AuthViewModel = "com.kquantum.nativeapp.presentation.viewmodels.AuthViewModel";

      static String com_kquantum_nativeapp_presentation_viewmodels_ExploreViewModel = "com.kquantum.nativeapp.presentation.viewmodels.ExploreViewModel";

      static String com_kquantum_nativeapp_presentation_viewmodels_LearnViewModel = "com.kquantum.nativeapp.presentation.viewmodels.LearnViewModel";

      static String com_kquantum_nativeapp_presentation_viewmodels_PracticeViewModel = "com.kquantum.nativeapp.presentation.viewmodels.PracticeViewModel";

      @KeepFieldType
      ProfileViewModel com_kquantum_nativeapp_presentation_viewmodels_ProfileViewModel2;

      @KeepFieldType
      SubscriptionViewModel com_kquantum_nativeapp_presentation_viewmodels_SubscriptionViewModel2;

      @KeepFieldType
      AchievementViewModel com_kquantum_nativeapp_presentation_viewmodels_AchievementViewModel2;

      @KeepFieldType
      BridgeViewModel com_kquantum_nativeapp_presentation_viewmodels_BridgeViewModel2;

      @KeepFieldType
      HomeViewModel com_kquantum_nativeapp_presentation_viewmodels_HomeViewModel2;

      @KeepFieldType
      AuthViewModel com_kquantum_nativeapp_presentation_viewmodels_AuthViewModel2;

      @KeepFieldType
      ExploreViewModel com_kquantum_nativeapp_presentation_viewmodels_ExploreViewModel2;

      @KeepFieldType
      LearnViewModel com_kquantum_nativeapp_presentation_viewmodels_LearnViewModel2;

      @KeepFieldType
      PracticeViewModel com_kquantum_nativeapp_presentation_viewmodels_PracticeViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.kquantum.nativeapp.presentation.viewmodels.AchievementViewModel 
          return (T) new AchievementViewModel(singletonCImpl.provideAchievementServiceProvider.get());

          case 1: // com.kquantum.nativeapp.presentation.viewmodels.AuthViewModel 
          return (T) new AuthViewModel(singletonCImpl.provideAuthServiceProvider.get());

          case 2: // com.kquantum.nativeapp.presentation.viewmodels.BridgeViewModel 
          return (T) new BridgeViewModel(singletonCImpl.provideQuantumBridgeServiceProvider.get());

          case 3: // com.kquantum.nativeapp.presentation.viewmodels.ExploreViewModel 
          return (T) new ExploreViewModel();

          case 4: // com.kquantum.nativeapp.presentation.viewmodels.HomeViewModel 
          return (T) new HomeViewModel(singletonCImpl.provideAuthServiceProvider.get(), singletonCImpl.provideProgressServiceProvider.get(), singletonCImpl.provideLearningServiceProvider.get());

          case 5: // com.kquantum.nativeapp.presentation.viewmodels.LearnViewModel 
          return (T) new LearnViewModel(singletonCImpl.provideLearningServiceProvider.get(), singletonCImpl.provideProgressServiceProvider.get());

          case 6: // com.kquantum.nativeapp.presentation.viewmodels.PracticeViewModel 
          return (T) new PracticeViewModel(singletonCImpl.provideLearningServiceProvider.get(), singletonCImpl.provideProgressServiceProvider.get());

          case 7: // com.kquantum.nativeapp.presentation.viewmodels.ProfileViewModel 
          return (T) new ProfileViewModel(singletonCImpl.provideAuthServiceProvider.get(), singletonCImpl.provideProgressServiceProvider.get(), singletonCImpl.provideAchievementServiceProvider.get(), singletonCImpl.provideBillingServiceProvider.get());

          case 8: // com.kquantum.nativeapp.presentation.viewmodels.SubscriptionViewModel 
          return (T) new SubscriptionViewModel(singletonCImpl.provideBillingServiceProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends KQuantumNativeApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends KQuantumNativeApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends KQuantumNativeApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<TokenManager> provideTokenManagerProvider;

    private Provider<ApiClient> provideApiClientProvider;

    private Provider<AchievementService> provideAchievementServiceProvider;

    private Provider<AuthService> provideAuthServiceProvider;

    private Provider<QuantumBridgeService> provideQuantumBridgeServiceProvider;

    private Provider<ProgressService> provideProgressServiceProvider;

    private Provider<LearningService> provideLearningServiceProvider;

    private Provider<BillingService> provideBillingServiceProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideTokenManagerProvider = DoubleCheck.provider(new SwitchingProvider<TokenManager>(singletonCImpl, 2));
      this.provideApiClientProvider = DoubleCheck.provider(new SwitchingProvider<ApiClient>(singletonCImpl, 1));
      this.provideAchievementServiceProvider = DoubleCheck.provider(new SwitchingProvider<AchievementService>(singletonCImpl, 0));
      this.provideAuthServiceProvider = DoubleCheck.provider(new SwitchingProvider<AuthService>(singletonCImpl, 3));
      this.provideQuantumBridgeServiceProvider = DoubleCheck.provider(new SwitchingProvider<QuantumBridgeService>(singletonCImpl, 4));
      this.provideProgressServiceProvider = DoubleCheck.provider(new SwitchingProvider<ProgressService>(singletonCImpl, 5));
      this.provideLearningServiceProvider = DoubleCheck.provider(new SwitchingProvider<LearningService>(singletonCImpl, 6));
      this.provideBillingServiceProvider = DoubleCheck.provider(new SwitchingProvider<BillingService>(singletonCImpl, 7));
    }

    @Override
    public void injectKQuantumNativeApp(KQuantumNativeApp kQuantumNativeApp) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.kquantum.nativeapp.services.achievement.AchievementService 
          return (T) AppModule_ProvideAchievementServiceFactory.provideAchievementService(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideApiClientProvider.get());

          case 1: // com.kquantum.nativeapp.data.remote.ApiClient 
          return (T) AppModule_ProvideApiClientFactory.provideApiClient(singletonCImpl.provideTokenManagerProvider.get());

          case 2: // com.kquantum.nativeapp.data.remote.TokenManager 
          return (T) AppModule_ProvideTokenManagerFactory.provideTokenManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.kquantum.nativeapp.services.auth.AuthService 
          return (T) AppModule_ProvideAuthServiceFactory.provideAuthService(singletonCImpl.provideApiClientProvider.get(), singletonCImpl.provideTokenManagerProvider.get());

          case 4: // com.kquantum.nativeapp.services.bridge.QuantumBridgeService 
          return (T) AppModule_ProvideQuantumBridgeServiceFactory.provideQuantumBridgeService();

          case 5: // com.kquantum.nativeapp.services.progress.ProgressService 
          return (T) AppModule_ProvideProgressServiceFactory.provideProgressService(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideApiClientProvider.get());

          case 6: // com.kquantum.nativeapp.services.learning.LearningService 
          return (T) AppModule_ProvideLearningServiceFactory.provideLearningService(singletonCImpl.provideApiClientProvider.get());

          case 7: // com.kquantum.nativeapp.services.billing.BillingService 
          return (T) AppModule_ProvideBillingServiceFactory.provideBillingService(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideApiClientProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
