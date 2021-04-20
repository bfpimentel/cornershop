package com.cornershop.counterstest.presentation.counters.data

sealed class CountersIntention {

    data class SearchCounters(val query: String? = null) : CountersIntention()

    data class Add(val counterId: String) : CountersIntention()

    data class Subtract(val counterId: String) : CountersIntention()

    object NavigateToCreateCounter : CountersIntention()
}
