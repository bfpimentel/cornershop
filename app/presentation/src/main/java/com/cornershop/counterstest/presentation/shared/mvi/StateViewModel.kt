package com.cornershop.counterstest.presentation.shared.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornershop.counterstest.presentation.shared.dispatchers.DispatchersProvider
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

    private val publisher = MutableSharedFlow<Intention>()

    init {
        publisher.onEach { intention ->
            viewModelScope.launch(dispatchersProvider.io) { handleIntentions(intention) }
        }.shareIn(viewModelScope, SharingStarted.Eagerly)
    }

    final override val state: MutableStateFlow<State> = MutableStateFlow(initialState)

    final override fun publish(intention: Intention) {
        viewModelScope.launch(dispatchersProvider.io) { publisher.emit(intention) }
    }

    abstract suspend fun handleIntentions(intention: Intention)

    protected suspend fun updateState(handler: suspend State.() -> State) {
        state.value = handler(state.value)
    }
}
