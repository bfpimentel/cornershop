package com.cornershop.counterstest.data.repository

import com.cornershop.counterstest.data.model.CounterModelImpl
import com.cornershop.counterstest.data.sources.remote.CountersRemoteDataSource
import com.cornershop.counterstest.domain.model.CounterModel
import com.cornershop.counterstest.domain.repository.CountersRepository

class CountersRepositoryImpl(private val remoteDataSource: CountersRemoteDataSource) : CountersRepository {

    override suspend fun getCounters(filter: String?): List<CounterModel> =
        remoteDataSource.getCounters().map { counterResponse ->
            CounterModelImpl(
                id = counterResponse.id,
                title = counterResponse.title,
                count = counterResponse.count
            )
        }
}
