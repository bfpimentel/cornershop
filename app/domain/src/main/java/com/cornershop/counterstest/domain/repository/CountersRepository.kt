package com.cornershop.counterstest.domain.repository

import com.cornershop.counterstest.domain.model.CounterModel
import kotlinx.coroutines.flow.Flow

interface CountersRepository {
    suspend fun fetchAndSaveCounters()
    fun getCounters(): Flow<List<CounterModel>>
    suspend fun searchCounters(query: String?)
    suspend fun createCounter(name: String)
    suspend fun increaseCount(counterId: String)
    suspend fun decreaseCount(counterId: String)
    suspend fun deleteCounters(countersToBeDeletedIds: List<String>)
}
