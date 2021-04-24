package com.cornershop.counterstest.domain.usecase

import com.cornershop.counterstest.domain.repository.CountersRepository

class DecreaseCount(private val repository: CountersRepository) : SuspendedUseCase<DecreaseCount.Params, Unit> {

    override suspend fun invoke(params: Params) = repository.decreaseCount(params.counterId)

    data class Params(val counterId: String)
}
