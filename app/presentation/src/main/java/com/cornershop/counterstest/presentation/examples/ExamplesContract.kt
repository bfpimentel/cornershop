package com.cornershop.counterstest.presentation.examples

import com.cornershop.counterstest.presentation.examples.data.ExamplesIntention
import com.cornershop.counterstest.presentation.examples.data.ExamplesState
import com.cornershop.counterstest.shared.mvi.StateViewModel

interface ExamplesContract {

    interface ViewModel : StateViewModel<ExamplesState, ExamplesIntention>

    fun interface ItemListener {
        fun onExampleClick(name: String)
    }
}
