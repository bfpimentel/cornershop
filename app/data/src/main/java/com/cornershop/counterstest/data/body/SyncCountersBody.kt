package com.cornershop.counterstest.data.body

import com.squareup.moshi.Json

data class SyncCountersBody(
    @Json(name = "deletedCounterIds") val deletedCountersIds: List<String>,
    @Json(name = "counters") val counters: List<CounterBody>
)
