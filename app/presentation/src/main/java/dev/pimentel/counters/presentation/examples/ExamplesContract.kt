package dev.pimentel.counters.presentation.examples

import dev.pimentel.counters.presentation.examples.data.ExamplesIntention
import dev.pimentel.counters.presentation.examples.data.ExamplesState
import dev.pimentel.counters.shared.mvi.StateViewModel

interface ExamplesContract {

    interface ViewModel : StateViewModel<ExamplesState, ExamplesIntention>

    fun interface ItemListener {
        fun onExampleClick(name: String)
    }
}
