package dev.pimentel.counters.presentation.create_counter.data

sealed class CreateCounterIntention {

    data class SetName(val name: String) : CreateCounterIntention()

    object Save : CreateCounterIntention()

    object NavigateToExamples : CreateCounterIntention()

    object Close : CreateCounterIntention()
}
