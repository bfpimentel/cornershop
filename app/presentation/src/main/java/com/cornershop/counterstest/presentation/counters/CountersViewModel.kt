package com.cornershop.counterstest.presentation.counters

import androidx.lifecycle.viewModelScope
import com.cornershop.counterstest.di.NavigatorRouterQualifier
import com.cornershop.counterstest.domain.usecase.AddCount
import com.cornershop.counterstest.domain.usecase.GetCounters
import com.cornershop.counterstest.domain.usecase.NoParams
import com.cornershop.counterstest.domain.usecase.SearchCounters
import com.cornershop.counterstest.domain.usecase.SubtractCount
import com.cornershop.counterstest.presentation.counters.data.CounterViewData
import com.cornershop.counterstest.presentation.counters.data.CountersIntention
import com.cornershop.counterstest.presentation.counters.data.CountersState
import com.cornershop.counterstest.shared.dispatchers.DispatchersProvider
import com.cornershop.counterstest.shared.mvi.StateViewModelImpl
import com.cornershop.counterstest.shared.mvi.toEvent
import com.cornershop.counterstest.shared.navigator.NavigatorRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountersViewModel @Inject constructor(
    @NavigatorRouterQualifier private val navigator: NavigatorRouter,
    private val getCounters: GetCounters,
    private val searchCounters: SearchCounters,
    private val addCount: AddCount,
    private val subtractCount: SubtractCount,
    dispatchersProvider: DispatchersProvider,
    @CountersStateQualifier initialState: CountersState
) : StateViewModelImpl<CountersState, CountersIntention>(
    dispatchersProvider = dispatchersProvider,
    initialState = initialState
), CountersContract.ViewModel {

    init {
        viewModelScope.launch(dispatchersProvider.io) { getCounters() }
    }

    override suspend fun handleIntentions(intention: CountersIntention) {
        when (intention) {
            is CountersIntention.SearchCounters -> searchCounters(SearchCounters.Params(intention.query))
            is CountersIntention.Add -> addCount(AddCount.Params(intention.counterId))
            is CountersIntention.Subtract -> subtractCount(SubtractCount.Params(intention.counterId))
            is CountersIntention.NavigateToCreateCounter ->
                navigator.navigate(CountersFragmentDirections.toCreateCounterFragment())
        }
    }

    private suspend fun getCounters() {
        getCounters(NoParams).collect { counters ->
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
                    totalItemCount = countersViewData.size,
                    totalTimesCount = countersViewData.sumBy(CounterViewData::count)
                )
            }
        }
    }
}
