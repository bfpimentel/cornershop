package com.cornershop.counterstest.domain.usecase

import com.cornershop.counterstest.domain.entity.Counter
import com.cornershop.counterstest.domain.repository.CountersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCounters(private val repository: CountersRepository) : UseCase<NoParams, Flow<List<Counter>>> {

    override fun invoke(params: NoParams): Flow<List<Counter>> =
        repository.getCounters().map { list ->
            list.map { counterModel ->
                Counter(
                    id = counterModel.id,
                    title = counterModel.title,
                    count = counterModel.count
                )
            }
        }
}
