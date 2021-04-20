package com.cornershop.counterstest.presentation.create_counter.data

sealed class CreateCounterIntention {

    data class SetName(val name: String) : CreateCounterIntention()

    object Save : CreateCounterIntention()

    object Close : CreateCounterIntention()
}
