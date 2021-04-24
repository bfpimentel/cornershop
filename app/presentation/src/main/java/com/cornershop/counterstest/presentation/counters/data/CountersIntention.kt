package com.cornershop.counterstest.presentation.counters.data

sealed class CountersIntention {

    data class SearchCounters(val query: String? = null) : CountersIntention()

    data class Increase(val counterId: String) : CountersIntention()

    data class Decrease(val counterId: String) : CountersIntention()

    data class StartEditing(val counterId: String) : CountersIntention()

    data class SelectOrDeselectCounter(val counterId: String) : CountersIntention()

    object TryDeleting : CountersIntention()

    object DeleteSelectedCounters : CountersIntention()

    object ShareSelectedCounters : CountersIntention()

    object FinishEditing : CountersIntention()

    object NavigateToCreateCounter : CountersIntention()
}
