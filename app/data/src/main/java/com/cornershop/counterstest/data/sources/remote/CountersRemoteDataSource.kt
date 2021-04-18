package com.cornershop.counterstest.data.sources.remote

import com.cornershop.counterstest.data.responses.CounterResponse
import retrofit2.http.GET

interface CountersRemoteDataSource {

    @GET("v1/counters")
    suspend fun getCounters(): List<CounterResponse>
}
