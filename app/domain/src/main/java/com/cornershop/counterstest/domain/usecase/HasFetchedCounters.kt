package com.cornershop.counterstest.domain.usecase

import com.cornershop.counterstest.domain.repository.PreferencesRepository

class HasFetchedCounters(private val repository: PreferencesRepository) : SuspendedUseCase<NoParams, Boolean> {

    override suspend fun invoke(params: NoParams): Boolean = repository.hasFetchedCounters()
}
