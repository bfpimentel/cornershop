package com.cornershop.counterstest.presentation.di

import com.cornershop.counterstest.presentation.shared.dispatchers.AppDispatchersProvider
import com.cornershop.counterstest.presentation.shared.dispatchers.DispatchersProvider
import com.cornershop.counterstest.presentation.shared.navigator.Navigator
import com.cornershop.counterstest.presentation.shared.navigator.NavigatorImpl
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
