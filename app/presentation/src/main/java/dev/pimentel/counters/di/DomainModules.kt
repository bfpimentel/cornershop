package dev.pimentel.counters.di

import dev.pimentel.counters.domain.repository.CountersRepository
import dev.pimentel.counters.domain.repository.PreferencesRepository
import dev.pimentel.counters.domain.usecase.IncreaseCount
import dev.pimentel.counters.domain.usecase.CreateCounter
import dev.pimentel.counters.domain.usecase.DeleteCounters
import dev.pimentel.counters.domain.usecase.FetchAndSaveCounters
import dev.pimentel.counters.domain.usecase.GetCounters
import dev.pimentel.counters.domain.usecase.HasFetchedCounters
import dev.pimentel.counters.domain.usecase.SearchCounters
import dev.pimentel.counters.domain.usecase.SetHasFetchedCounters
import dev.pimentel.counters.domain.usecase.DecreaseCount
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
    fun provideAddCount(countersRepository: CountersRepository): IncreaseCount =
        IncreaseCount(repository = countersRepository)

    @Provides
    @ViewModelScoped
    fun provideSubtractCount(countersRepository: CountersRepository): DecreaseCount =
        DecreaseCount(repository = countersRepository)

    @Provides
    @ViewModelScoped
    fun provideDeleteCounters(countersRepository: CountersRepository): DeleteCounters =
        DeleteCounters(repository = countersRepository)
}
