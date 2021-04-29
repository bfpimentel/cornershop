package dev.pimentel.counters.data.repository

import dev.pimentel.counters.data.sources.local.PreferencesLocalDataSource
import dev.pimentel.counters.domain.repository.PreferencesRepository

class PreferencesRepositoryImpl(
    private val preferencesLocalDataSource: PreferencesLocalDataSource
) : PreferencesRepository {

    override suspend fun hasFetchedCounters(): Boolean = preferencesLocalDataSource.hasFetchedCounters()

    override suspend fun setHasFetchedCounters() = preferencesLocalDataSource.setHasFetchedCounters()
}
