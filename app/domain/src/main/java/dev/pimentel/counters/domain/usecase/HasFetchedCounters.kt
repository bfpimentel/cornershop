package dev.pimentel.counters.domain.usecase

import dev.pimentel.counters.domain.repository.PreferencesRepository

class HasFetchedCounters(private val repository: PreferencesRepository) : SuspendedUseCase<NoParams, Boolean> {

    override suspend fun invoke(params: NoParams): Boolean = repository.hasFetchedCounters()
}
