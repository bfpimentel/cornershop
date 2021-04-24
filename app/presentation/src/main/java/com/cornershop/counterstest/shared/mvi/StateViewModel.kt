package com.cornershop.counterstest.shared.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornershop.counterstest.shared.dispatchers.DispatchersProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

interface StateViewModel<State, Intention> {
    val state: StateFlow<State>
    fun publish(intention: Intention)
}

abstract class StateViewModelImpl<State, Intention>(
    private val dispatchersProvider: DispatchersProvider,
    initialState: State
) : ViewModel(), StateViewModel<State, Intention> {

    final override val state: MutableStateFlow<State> = MutableStateFlow(initialState)

    private val publisher = MutableSharedFlow<Intention>()

    init {
        publisher
            .onEach(::handleIntentions)
            .shareIn(viewModelScope, SharingStarted.Eagerly)
    }

    final override fun publish(intention: Intention) {
        viewModelScope.launch(dispatchersProvider.io) { publisher.emit(intention) }
    }

    abstract suspend fun handleIntentions(intention: Intention)

    protected suspend fun updateState(handler: suspend State.() -> State) {
        state.value = handler(state.value)
    }
}
