package com.cornershop.counterstest.initializers

import android.content.Context
import androidx.startup.Initializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.startKoin

@Suppress("Unused")
class KoinInitializer : Initializer<KoinApplication> {

    override fun create(context: Context): KoinApplication = startKoin { androidContext(context) }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
