package com.cornershop.counterstest.presentation.counters

import com.cornershop.counterstest.presentation.counters.data.CountersIntention
import com.cornershop.counterstest.presentation.counters.data.CountersState
import com.cornershop.counterstest.shared.mvi.StateViewModel

interface CountersContract {

    interface ViewModel : StateViewModel<CountersState, CountersIntention>

    interface ItemListener {
        fun onCounterLongClick(counterId: String)
        fun onCounterClick(counterId: String)
        fun onAddClick(counterId: String)
        fun onSubtractClick(counterId: String)
    }
}
