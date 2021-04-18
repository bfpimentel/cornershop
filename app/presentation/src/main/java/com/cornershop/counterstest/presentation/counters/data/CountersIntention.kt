package com.cornershop.counterstest.presentation.counters.data

sealed class CountersIntention {

    object GetCounters : CountersIntention()

    data class Add(val counterId: String) : CountersIntention()

    data class Subtract(val counterId: String) : CountersIntention()
}
