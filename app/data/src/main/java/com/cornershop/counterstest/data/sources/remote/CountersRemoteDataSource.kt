package com.cornershop.counterstest.data.sources.remote

import com.cornershop.counterstest.data.body.CounterBody
import com.cornershop.counterstest.data.body.SyncCountersBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface CountersRemoteDataSource {

    @GET("v1/counters")
    suspend fun getCounters(): List<CounterBody>

    @PUT("v1/counters/sync")
    suspend fun syncCounters(@Body sync: SyncCountersBody)
}
