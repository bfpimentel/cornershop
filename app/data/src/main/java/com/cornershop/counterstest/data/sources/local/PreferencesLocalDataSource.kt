package com.cornershop.counterstest.data.sources.local

import android.content.SharedPreferences

interface PreferencesLocalDataSource {
    suspend fun isFirstAccess(): Boolean
}

class PreferencesLocalDataSourceImpl(private val sharedPreferences: SharedPreferences) : PreferencesLocalDataSource {

    override suspend fun isFirstAccess(): Boolean =
        sharedPreferences.getBoolean(IS_FIRST_ACCESS_KEY, true).also { isFirstAccess ->
            if (isFirstAccess) sharedPreferences.edit().putBoolean(IS_FIRST_ACCESS_KEY, false).apply()
        }

    private companion object {
        const val IS_FIRST_ACCESS_KEY = "IS_FIRST_ACCESS"
    }
}
