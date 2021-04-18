package com.cornershop.counterstest.domain.repository

import com.cornershop.counterstest.domain.model.CounterModel

interface CountersRepository {
    suspend fun getCounters(filter: String?): List<CounterModel>
}
