package dev.pimentel.counters.presentation.examples.data

import dev.pimentel.counters.shared.mvi.Event

data class ExamplesState(
    val examplesEvent: Event<List<ExampleViewData>>? = null
)
