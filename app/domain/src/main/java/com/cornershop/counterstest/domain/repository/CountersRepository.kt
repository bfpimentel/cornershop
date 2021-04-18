package com.cornershop.counterstest.domain.repository

import com.cornershop.counterstest.domain.model.CounterModel
import kotlinx.coroutines.flow.Flow

interface CountersRepository {
    fun getCounters(filter: String?): Flow<List<CounterModel>>
}
