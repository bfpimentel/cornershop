package dev.pimentel.counters.data.sources.remote

import dev.pimentel.counters.data.body.CounterBody
import dev.pimentel.counters.data.body.SyncCountersBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface CountersRemoteDataSource {

    @GET("v1/counters")
    suspend fun getCounters(): List<CounterBody>

    @PUT("v1/counters/sync")
    suspend fun syncCounters(@Body sync: SyncCountersBody)
}
