package com.cornershop.counterstest.data.repository

import com.cornershop.counterstest.data.sources.local.PreferencesLocalDataSource
import com.cornershop.counterstest.domain.repository.PreferencesRepository

class PreferencesRepositoryImpl(
    private val preferencesLocalDataSource: PreferencesLocalDataSource
) : PreferencesRepository {

    override suspend fun hasFetchedCounters(): Boolean = preferencesLocalDataSource.hasFetchedCounters()

    override suspend fun setHasFetchedCounters() = preferencesLocalDataSource.setHasFetchedCounters()
}
