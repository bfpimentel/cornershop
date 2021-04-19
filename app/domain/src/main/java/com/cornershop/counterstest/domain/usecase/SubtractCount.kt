package com.cornershop.counterstest.domain.usecase

import com.cornershop.counterstest.domain.repository.CountersRepository

class SubtractCount(private val repository: CountersRepository) : SuspendedUseCase<SubtractCount.Params, Unit> {

    override suspend fun invoke(params: Params) = repository.subtractCount(params.counterId)

    data class Params(val counterId: String)
}
