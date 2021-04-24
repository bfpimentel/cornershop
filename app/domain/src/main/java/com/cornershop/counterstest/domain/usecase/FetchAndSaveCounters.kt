package com.cornershop.counterstest.domain.usecase

import com.cornershop.counterstest.domain.repository.CountersRepository

class FetchAndSaveCounters(
    private val repository: CountersRepository,
    private val setHasFetchedCounters: SetHasFetchedCounters
) : SuspendedUseCase<NoParams, Unit> {

    override suspend fun invoke(params: NoParams) {
        repository.fetchAndSaveCounters()
        setHasFetchedCounters(NoParams)
    }
}
