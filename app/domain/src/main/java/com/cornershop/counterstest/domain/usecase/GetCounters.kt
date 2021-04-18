package com.cornershop.counterstest.domain.usecase

import com.cornershop.counterstest.domain.entity.Counter
import com.cornershop.counterstest.domain.repository.CountersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCounters(private val repository: CountersRepository) : UseCase<GetCounters.Params, Flow<List<Counter>>> {

    override fun invoke(params: Params): Flow<List<Counter>> =
        repository.getCounters(filter = params.filter).map { list ->
            list.map { counterModel ->
                Counter(
                    id = counterModel.id,
                    title = counterModel.title,
                    count = counterModel.count
                )
            }
        }

    data class Params(val filter: String?)
}
