package com.cornershop.counterstest.di

import com.cornershop.counterstest.shared.dispatchers.AppDispatchersProvider
import com.cornershop.counterstest.shared.dispatchers.DispatchersProvider
import com.cornershop.counterstest.shared.navigator.Navigator
import com.cornershop.counterstest.shared.navigator.NavigatorBinder
import com.cornershop.counterstest.shared.navigator.NavigatorImpl
import com.cornershop.counterstest.shared.navigator.NavigatorRouter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PresentationModule {

    @Provides
    @Singleton
    fun provideDispatchersProvider(): DispatchersProvider = AppDispatchersProvider()

    @Provides
    @Singleton
    fun provideNavigator(
        dispatchersProvider: DispatchersProvider
    ): Navigator = NavigatorImpl(dispatchersProvider = dispatchersProvider)

    @NavigatorBinderQualifier
    @Provides
    fun provideNavigatorBinder(navigator: Navigator): NavigatorBinder = navigator

    @NavigatorRouterQualifier
    @Provides
    fun provideNavigatorRouter(navigator: Navigator): NavigatorRouter = navigator
}


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NavigatorBinderQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NavigatorRouterQualifier
