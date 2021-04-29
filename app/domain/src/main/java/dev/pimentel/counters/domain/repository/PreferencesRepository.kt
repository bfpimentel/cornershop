package dev.pimentel.counters.domain.repository

interface PreferencesRepository {
    suspend fun hasFetchedCounters(): Boolean
    suspend fun setHasFetchedCounters()
}
