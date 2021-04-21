package com.cornershop.counterstest.presentation.counters

import android.content.Context
import com.cornershop.counterstest.presentation.counters.data.CountersState
import com.cornershop.counterstest.presentation.counters.mappers.CountersDeletionMapper
import com.cornershop.counterstest.presentation.counters.mappers.CountersDeletionMapperImpl
import com.cornershop.counterstest.presentation.counters.mappers.CountersSharingMapper
import com.cornershop.counterstest.presentation.counters.mappers.CountersSharingMapperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
object CountersViewModelModule {

    @Provides
    @ViewModelScoped
    @CountersStateQualifier
    fun provideInitialState(): CountersState = CountersState()

    @Provides
    @ViewModelScoped
    fun provideCountersDeletionMapper(@ApplicationContext context: Context): CountersDeletionMapper =
        CountersDeletionMapperImpl(context = context)

    @Provides
    @ViewModelScoped
    fun provideCountersSharingMapper(@ApplicationContext context: Context): CountersSharingMapper =
        CountersSharingMapperImpl(context = context)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CountersStateQualifier
