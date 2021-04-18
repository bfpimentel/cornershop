package com.cornershop.counterstest.domain.usecase

import com.cornershop.counterstest.domain.entity.Counter
import com.cornershop.counterstest.domain.repository.CountersRepository

class GetCounters(private val repository: CountersRepository) : UseCase<GetCounters.Params, List<Counter>> {

    override suspend fun invoke(params: Params): List<Counter> =
        repository.getCounters(filter = params.filter).map { counterModel ->
            Counter(
                id = counterModel.id,
                title = counterModel.title,
                count = counterModel.count
            )
        }

    data class Params(val filter: String?)
}
