package dev.pimentel.counters.domain.usecase

import dev.pimentel.counters.domain.repository.PreferencesRepository

class SetHasFetchedCounters(private val repository: PreferencesRepository) : SuspendedUseCase<NoParams, Unit> {

    override suspend fun invoke(params: NoParams) = repository.setHasFetchedCounters()
}
