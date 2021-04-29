package dev.pimentel.counters.presentation.counters

import dev.pimentel.counters.presentation.counters.data.CountersIntention
import dev.pimentel.counters.presentation.counters.data.CountersState
import dev.pimentel.counters.shared.mvi.StateViewModel

interface CountersContract {

    interface ViewModel : StateViewModel<CountersState, CountersIntention>

    interface ItemListener {
        fun onCounterLongClick(counterId: String)
        fun onCounterClick(counterId: String)
        fun onIncreaseClick(counterId: String)
        fun onDecreaseClick(counterId: String)
    }
}
