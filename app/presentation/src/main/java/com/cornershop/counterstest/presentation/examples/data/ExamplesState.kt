package com.cornershop.counterstest.presentation.examples.data

import com.cornershop.counterstest.shared.mvi.Event

data class ExamplesState(
    val examplesEvent: Event<List<ExampleViewData>>? = null
)
