package dev.pimentel.counters.data.sources.local

import android.content.SharedPreferences

interface PreferencesLocalDataSource {
    suspend fun hasFetchedCounters(): Boolean
    suspend fun setHasFetchedCounters()
}

class PreferencesLocalDataSourceImpl(private val sharedPreferences: SharedPreferences) : PreferencesLocalDataSource {

    override suspend fun hasFetchedCounters(): Boolean =
        sharedPreferences.getBoolean(HAS_FETCHED_COUNTERS_KEY, false)

    override suspend fun setHasFetchedCounters() {
        sharedPreferences.edit().putBoolean(HAS_FETCHED_COUNTERS_KEY, true).apply()
    }

    private companion object {
        const val HAS_FETCHED_COUNTERS_KEY = "HAS_FETCHED_COUNTERS"
    }
}
