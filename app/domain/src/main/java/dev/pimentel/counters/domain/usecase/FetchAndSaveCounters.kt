package dev.pimentel.counters.domain.usecase

import dev.pimentel.counters.domain.repository.CountersRepository

class FetchAndSaveCounters(
    private val repository: CountersRepository,
    private val setHasFetchedCounters: SetHasFetchedCounters
) : SuspendedUseCase<NoParams, Unit> {

    override suspend fun invoke(params: NoParams) {
        repository.fetchAndSaveCounters()
        setHasFetchedCounters(NoParams)
    }
}
