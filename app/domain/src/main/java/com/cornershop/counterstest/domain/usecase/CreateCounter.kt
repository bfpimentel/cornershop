package com.cornershop.counterstest.domain.usecase

import com.cornershop.counterstest.domain.repository.CountersRepository

class CreateCounter(private val repository: CountersRepository) : SuspendedUseCase<CreateCounter.Params, Unit> {

    override suspend fun invoke(params: Params) =
        repository.createCounter(params.name)

    data class Params(val name: String)
}
