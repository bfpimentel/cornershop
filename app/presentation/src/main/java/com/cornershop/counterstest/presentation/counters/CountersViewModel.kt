package com.cornershop.counterstest.presentation.counters

import com.cornershop.counterstest.domain.entity.Counter
import com.cornershop.counterstest.domain.usecase.GetCounters
import com.cornershop.counterstest.presentation.counters.data.CounterViewData
import com.cornershop.counterstest.presentation.counters.data.CountersIntention
import com.cornershop.counterstest.presentation.counters.data.CountersState
import com.cornershop.counterstest.shared.dispatchers.DispatchersProvider
import com.cornershop.counterstest.shared.mvi.StateViewModelImpl
import com.cornershop.counterstest.shared.mvi.toEvent
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
        when (intention) {
            is CountersIntention.GetCounters -> getCounters()
            is CountersIntention.Add -> Unit
            is CountersIntention.Subtract -> Unit
        }
    }

    private suspend fun getCounters() {
        try {
            val counters = getCounters(GetCounters.Params(null))

            val countersViewData = counters.map { counter ->
                CounterViewData(
                    id = counter.id,
                    title = counter.title,
                    count = counter.count
                )
            }

            updateState {
                copy(
                    countersEvent = countersViewData.toEvent(),
                    totalItemCount = counters.size,
                    totalTimesCount = counters.sumBy(Counter::count)
                )
            }
        } catch (error: Exception) {
        }
    }
}
