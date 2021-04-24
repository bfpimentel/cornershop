package com.cornershop.counterstest.di

import com.cornershop.counterstest.domain.repository.CountersRepository
import com.cornershop.counterstest.domain.repository.PreferencesRepository
import com.cornershop.counterstest.domain.usecase.AddCount
import com.cornershop.counterstest.domain.usecase.CreateCounter
import com.cornershop.counterstest.domain.usecase.DeleteCounters
import com.cornershop.counterstest.domain.usecase.FetchAndSaveCounters
import com.cornershop.counterstest.domain.usecase.GetCounters
import com.cornershop.counterstest.domain.usecase.HasFetchedCounters
import com.cornershop.counterstest.domain.usecase.SearchCounters
import com.cornershop.counterstest.domain.usecase.SetHasFetchedCounters
import com.cornershop.counterstest.domain.usecase.SubtractCount
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object DomainUseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideHasFetchedCounters(preferencesRepository: PreferencesRepository): HasFetchedCounters =
        HasFetchedCounters(repository = preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideSetHasFetchedCounters(preferencesRepository: PreferencesRepository): SetHasFetchedCounters =
        SetHasFetchedCounters(repository = preferencesRepository)

    @Provides
    @ViewModelScoped
    fun provideFetchAndSaveCounters(
        countersRepository: CountersRepository,
        setHasFetchedCounters: SetHasFetchedCounters
    ): FetchAndSaveCounters = FetchAndSaveCounters(
        repository = countersRepository,
        setHasFetchedCounters = setHasFetchedCounters
    )

    @Provides
    @ViewModelScoped
    fun provideGetCounters(countersRepository: CountersRepository): GetCounters =
        GetCounters(repository = countersRepository)

    @Provides
    @ViewModelScoped
    fun provideSearchCounters(countersRepository: CountersRepository): SearchCounters =
        SearchCounters(repository = countersRepository)

    @Provides
    @ViewModelScoped
    fun provideCreateCounter(countersRepository: CountersRepository): CreateCounter =
        CreateCounter(repository = countersRepository)

    @Provides
    @ViewModelScoped
    fun provideAddCount(countersRepository: CountersRepository): AddCount =
        AddCount(repository = countersRepository)

    @Provides
    @ViewModelScoped
    fun provideSubtractCount(countersRepository: CountersRepository): SubtractCount =
        SubtractCount(repository = countersRepository)

    @Provides
    @ViewModelScoped
    fun provideDeleteCounters(countersRepository: CountersRepository): DeleteCounters =
        DeleteCounters(repository = countersRepository)
}
