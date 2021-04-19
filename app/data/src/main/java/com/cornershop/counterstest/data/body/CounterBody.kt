package com.cornershop.counterstest.data.body

import com.squareup.moshi.Json

data class CounterBody(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "count") val count: Int
)
