package com.cornershop.counterstest.presentation.counters

import com.cornershop.counterstest.presentation.counters.data.CountersState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
object CountersViewModelModule {

    @Provides
    @ViewModelScoped
    @CountersStateQualifier
    fun provideInitialState(): CountersState = CountersState()
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CountersStateQualifier
