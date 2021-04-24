package com.cornershop.counterstest.domain.repository

interface PreferencesRepository {
    suspend fun hasFetchedCounters(): Boolean
    suspend fun setHasFetchedCounters()
}
