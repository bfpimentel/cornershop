package com.cornershop.counterstest.data.responses

import com.squareup.moshi.Json

data class CounterResponse(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "count") val count: Int
)
