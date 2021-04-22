package com.cornershop.counterstest.presentation.examples

import com.cornershop.counterstest.di.NavigatorRouterQualifier
import com.cornershop.counterstest.domain.usecase.CreateCounter
import com.cornershop.counterstest.presentation.examples.data.ExamplesIntention
import com.cornershop.counterstest.presentation.examples.data.ExamplesState
import com.cornershop.counterstest.presentation.examples.mappers.ExamplesViewDataMapper
import com.cornershop.counterstest.shared.dispatchers.DispatchersProvider
import com.cornershop.counterstest.shared.mvi.StateViewModelImpl
import com.cornershop.counterstest.shared.mvi.toEvent
import com.cornershop.counterstest.shared.navigator.NavigatorRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExamplesViewModel @Inject constructor(
    @NavigatorRouterQualifier private val navigator: NavigatorRouter,
    private val createCounter: CreateCounter,
    private val mapper: ExamplesViewDataMapper,
    dispatchersProvider: DispatchersProvider,
    @ExamplesStateQualifier initialState: ExamplesState
) : StateViewModelImpl<ExamplesState, ExamplesIntention>(
    dispatchersProvider = dispatchersProvider,
    initialState = initialState
), ExamplesContract.ViewModel {

    override suspend fun handleIntentions(intention: ExamplesIntention) {
        when (intention) {
            is ExamplesIntention.GetExamples -> getExamples()
            is ExamplesIntention.SelectExample -> selectExample(intention.name)
            is ExamplesIntention.Close -> navigator.pop()
        }
    }

    private suspend fun getExamples() {
        updateState { copy(examplesEvent = mapper.getExamples().toEvent()) }
    }

    private suspend fun selectExample(name: String) {
        createCounter(CreateCounter.Params(name))

        val directions = ExamplesFragmentDirections.backToCountersFragment()
        navigator.navigate(directions)
    }
}
