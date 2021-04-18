package com.cornershop.counterstest.di

import com.cornershop.counterstest.domain.repository.CountersRepository
import com.cornershop.counterstest.domain.usecase.GetCounters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainUseCaseModule {

    @Provides
    @Singleton
    fun provideGetCounters(countersRepository: CountersRepository): GetCounters =
        GetCounters(repository = countersRepository)
}
