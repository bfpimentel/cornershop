package com.cornershop.counterstest.presentation.create_counter

import com.cornershop.counterstest.presentation.create_counter.data.CreateCounterIntention
import com.cornershop.counterstest.presentation.create_counter.data.CreateCounterState
import com.cornershop.counterstest.shared.mvi.StateViewModel

interface CreateCounterContract {

    interface ViewModel : StateViewModel<CreateCounterState, CreateCounterIntention>
}
