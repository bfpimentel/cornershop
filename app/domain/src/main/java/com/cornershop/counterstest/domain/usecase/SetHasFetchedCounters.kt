package com.cornershop.counterstest.domain.usecase

import com.cornershop.counterstest.domain.repository.PreferencesRepository

class SetHasFetchedCounters(private val repository: PreferencesRepository) : SuspendedUseCase<NoParams, Unit> {

    override suspend fun invoke(params: NoParams) = repository.setHasFetchedCounters()
}
