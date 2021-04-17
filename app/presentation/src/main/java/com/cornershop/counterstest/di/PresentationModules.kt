package com.cornershop.counterstest.di

import com.cornershop.counterstest.shared.dispatchers.AppDispatchersProvider
import com.cornershop.counterstest.shared.dispatchers.DispatchersProvider
import com.cornershop.counterstest.shared.navigator.Navigator
import com.cornershop.counterstest.shared.navigator.NavigatorImpl
import org.koin.dsl.module

private val dispatchersModule = module {
    factory<DispatchersProvider> { AppDispatchersProvider() }
}

private val navigatorModule = module {
    single<Navigator> { NavigatorImpl(dispatchersProvider = get()) }
}

val presentationModules = listOf(
    dispatchersModule,
    navigatorModule
)
