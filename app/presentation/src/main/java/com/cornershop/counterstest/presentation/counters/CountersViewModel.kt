package com.cornershop.counterstest.presentation.counters

import androidx.lifecycle.viewModelScope
import com.cornershop.counterstest.di.NavigatorRouterQualifier
import com.cornershop.counterstest.domain.entity.Counter
import com.cornershop.counterstest.domain.usecase.AddCount
import com.cornershop.counterstest.domain.usecase.DeleteCounters
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
    private val deleteCounters: DeleteCounters,
    dispatchersProvider: DispatchersProvider,
    @CountersStateQualifier initialState: CountersState
) : StateViewModelImpl<CountersState, CountersIntention>(
    dispatchersProvider = dispatchersProvider,
    initialState = initialState
), CountersContract.ViewModel {

    private lateinit var counters: List<Counter>

    private var isEditing: Boolean = false
    private val selectedCountersIds: MutableList<String> = mutableListOf()

    init {
        viewModelScope.launch(dispatchersProvider.io) { getCounters() }
    }

    override suspend fun handleIntentions(intention: CountersIntention) {
        when (intention) {
            is CountersIntention.SearchCounters -> searchCounters(SearchCounters.Params(intention.query))
            is CountersIntention.Add -> addCount(AddCount.Params(intention.counterId))
            is CountersIntention.Subtract -> subtractCount(SubtractCount.Params(intention.counterId))
            is CountersIntention.StartEditing -> startEditing()
            is CountersIntention.SelectOrDeselectCounter -> selectOrDeselectCounter(intention.counterId)
            is CountersIntention.DeleteSelectedCounters -> deleteSelectedCounters()
            is CountersIntention.ShareSelectedCounters -> shareSelectedCounters()
            is CountersIntention.FinishEditing -> finishEditing()
            is CountersIntention.NavigateToCreateCounter -> navigateToCreateCounter()
        }
    }

    private suspend fun getCounters() {
        getCounters(NoParams).collect { counters ->
            this.counters = counters
            updateScreenState()
        }
    }

    private suspend fun startEditing() {
        this.isEditing = true
        updateScreenState()
    }

    private suspend fun selectOrDeselectCounter(counterId: String) {
        if (selectedCountersIds.contains(counterId)) selectedCountersIds.remove(counterId)
        else selectedCountersIds.add(counterId)
        updateScreenState()
    }

    private suspend fun deleteSelectedCounters() {
        deleteCounters(DeleteCounters.Params(selectedCountersIds))
        selectedCountersIds.removeAll { true }
    }

    private suspend fun shareSelectedCounters() {
        // TODO
    }

    private suspend fun finishEditing() {
        this.isEditing = false
        updateScreenState()
    }

    private suspend fun navigateToCreateCounter() {
        val directions = CountersFragmentDirections.toCreateCounterFragment()
        navigator.navigate(directions)
    }

    private suspend fun updateScreenState() {
        val countersViewData =
            if (isEditing) {
                this.counters.map { counter ->
                    CounterViewData.Item(
                        id = counter.id,
                        title = counter.title,
                        count = counter.count
                    )
                }
            } else {
                this.counters.map { counter ->
                    CounterViewData.Edit(
                        id = counter.id,
                        title = counter.title,
                        isSelected = selectedCountersIds.contains(counter.id),
                        count = counter.count
                    )
                }
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
