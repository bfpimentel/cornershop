package com.cornershop.counterstest.presentation.counters

import android.util.Log
import com.cornershop.counterstest.domain.usecase.GetCounters
import com.cornershop.counterstest.presentation.counters.data.CountersIntention
import com.cornershop.counterstest.presentation.counters.data.CountersState
import com.cornershop.counterstest.shared.dispatchers.DispatchersProvider
import com.cornershop.counterstest.shared.mvi.StateViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CountersViewModel @Inject constructor(
    private val getCounters: GetCounters,
    dispatchersProvider: DispatchersProvider,
    @CountersStateQualifier initialState: CountersState
) : StateViewModelImpl<CountersState, CountersIntention>(
    dispatchersProvider = dispatchersProvider,
    initialState = initialState
), CountersContract.ViewModel {

    override suspend fun handleIntentions(intention: CountersIntention) {
        Log.d("GET_COUNTERS", "TEST")
        val counters = getCounters(GetCounters.Params(null))
        Log.d("GET_COUNTERS", counters.toString())
    }
}
