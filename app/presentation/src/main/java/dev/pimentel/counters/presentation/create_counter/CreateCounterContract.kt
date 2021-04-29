package dev.pimentel.counters.presentation.create_counter

import dev.pimentel.counters.presentation.create_counter.data.CreateCounterIntention
import dev.pimentel.counters.presentation.create_counter.data.CreateCounterState
import dev.pimentel.counters.shared.mvi.StateViewModel

interface CreateCounterContract {

    interface ViewModel : StateViewModel<CreateCounterState, CreateCounterIntention>
}
