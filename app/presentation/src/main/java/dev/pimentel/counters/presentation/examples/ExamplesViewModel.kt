package dev.pimentel.counters.presentation.examples

import dev.pimentel.counters.R
import dev.pimentel.counters.di.NavigatorRouterQualifier
import dev.pimentel.counters.domain.usecase.CreateCounter
import dev.pimentel.counters.presentation.examples.data.ExamplesIntention
import dev.pimentel.counters.presentation.examples.data.ExamplesState
import dev.pimentel.counters.presentation.examples.mappers.ExamplesViewDataMapper
import dev.pimentel.counters.shared.dispatchers.DispatchersProvider
import dev.pimentel.counters.shared.mvi.StateViewModelImpl
import dev.pimentel.counters.shared.mvi.toEvent
import dev.pimentel.counters.shared.navigator.NavigatorRouter
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
        navigator.pop(R.id.countersFragment)
    }
}
