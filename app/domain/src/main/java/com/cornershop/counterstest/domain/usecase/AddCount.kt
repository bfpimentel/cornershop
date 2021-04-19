package com.cornershop.counterstest.domain.usecase

import com.cornershop.counterstest.domain.repository.CountersRepository

class AddCount(private val repository: CountersRepository) : SuspendedUseCase<AddCount.Params, Unit> {

    override suspend fun invoke(params: Params) = repository.addCount(params.counterId)

    data class Params(val counterId: String)
}
