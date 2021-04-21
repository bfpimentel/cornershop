package com.cornershop.counterstest.domain.repository

import com.cornershop.counterstest.domain.model.CounterModel
import kotlinx.coroutines.flow.Flow

interface CountersRepository {
    fun getCounters(): Flow<List<CounterModel>>
    suspend fun searchCounters(query: String?)
    suspend fun createCounter(name: String)
    suspend fun addCount(counterId: String)
    suspend fun subtractCount(counterId: String)
    suspend fun deleteCounters(countersToBeDeletedIds: List<String>)
}
