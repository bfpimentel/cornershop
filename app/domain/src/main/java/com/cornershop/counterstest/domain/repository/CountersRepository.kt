package com.cornershop.counterstest.domain.repository

import com.cornershop.counterstest.domain.model.CounterModel
import kotlinx.coroutines.flow.Flow

interface CountersRepository {
    fun getCounters(filter: String?): Flow<List<CounterModel>>
    suspend fun addCount(counterId: String)
    suspend fun subtractCount(counterId: String)
}
