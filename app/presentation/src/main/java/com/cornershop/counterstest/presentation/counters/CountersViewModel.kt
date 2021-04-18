package com.cornershop.counterstest.presentation.counters

import androidx.lifecycle.viewModelScope
import com.cornershop.counterstest.domain.usecase.GetCounters
import com.cornershop.counterstest.presentation.counters.data.CounterViewData
import com.cornershop.counterstest.presentation.counters.data.CountersIntention
import com.cornershop.counterstest.presentation.counters.data.CountersState
import com.cornershop.counterstest.shared.dispatchers.DispatchersProvider
import com.cornershop.counterstest.shared.mvi.StateViewModelImpl
import com.cornershop.counterstest.shared.mvi.toEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountersViewModel @Inject constructor(
    private val getCounters: GetCounters,
    private val dispatchersProvider: DispatchersProvider,
    @CountersStateQualifier initialState: CountersState
) : StateViewModelImpl<CountersState, CountersIntention>(
    dispatchersProvider = dispatchersProvider,
    initialState = initialState
), CountersContract.ViewModel {

    override suspend fun handleIntentions(intention: CountersIntention) {
        when (intention) {
            is CountersIntention.GetCounters -> watchCounters()
            is CountersIntention.Add -> Unit
            is CountersIntention.Subtract -> Unit
        }
    }

    private suspend fun watchCounters() {
        try {
            getCounters(GetCounters.Params(null)).collect { counters ->
                val countersViewData = counters
                    .map { counter ->
                        CounterViewData(
                            id = counter.id,
                            title = counter.title,
                            count = counter.count
                        )
                    }

                updateState {
                    copy(
                        countersEvent = countersViewData.toEvent(),
                        totalItemCount = countersViewData.size,
                        totalTimesCount = countersViewData.sumBy(CounterViewData::count)
                    )
                }
            }
        } catch (error: Exception) {
        }
    }
}
