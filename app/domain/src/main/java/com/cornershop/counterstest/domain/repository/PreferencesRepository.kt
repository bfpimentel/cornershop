package com.cornershop.counterstest.domain.repository

interface PreferencesRepository {
    suspend fun isFirstAccess(): Boolean
}
