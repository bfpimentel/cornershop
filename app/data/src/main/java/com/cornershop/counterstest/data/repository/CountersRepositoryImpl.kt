package com.cornershop.counterstest.data.repository

import com.cornershop.counterstest.data.dto.CounterDTO
import com.cornershop.counterstest.data.model.CounterModelImpl
import com.cornershop.counterstest.data.sources.local.CountersLocalDataSource
import com.cornershop.counterstest.data.sources.remote.CountersRemoteDataSource
import com.cornershop.counterstest.domain.model.CounterModel
import com.cornershop.counterstest.domain.repository.CountersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class CountersRepositoryImpl(
    private val remoteDataSource: CountersRemoteDataSource,
    private val localDataSource: CountersLocalDataSource
) : CountersRepository {

    private var isFirstAccess = true

    override fun getCounters(filter: String?): Flow<List<CounterModel>> =
        localDataSource.getCounters()
            .map {
                if (isFirstAccess) {
                    remoteDataSource.getCounters()
                        .map { CounterDTO(id = it.id, count = it.count, title = it.title) }
                        .also { localDataSource.insertCounters(it) }
                    isFirstAccess = false
                }
                it
            }
            .distinctUntilChanged()
            .map { response ->
                response.map {
                    CounterModelImpl(
                        id = it.id,
                        title = it.title,
                        count = it.count
                    )
                }
            }
}
