package com.cornershop.counterstest.presentation.counters.data

import com.cornershop.counterstest.shared.mvi.Event

data class CountersState(
    val countersEvent: Event<List<CounterViewData>>? = null,
    val totalItemCount: Int = 0,
    val totalTimesCount: Int = 0,
)
