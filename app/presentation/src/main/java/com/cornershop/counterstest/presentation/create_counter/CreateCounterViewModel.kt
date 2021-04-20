package com.cornershop.counterstest.presentation.create_counter

import com.cornershop.counterstest.di.NavigatorRouterQualifier
import com.cornershop.counterstest.presentation.create_counter.data.CreateCounterIntention
import com.cornershop.counterstest.presentation.create_counter.data.CreateCounterState
import com.cornershop.counterstest.shared.dispatchers.DispatchersProvider
import com.cornershop.counterstest.shared.mvi.StateViewModelImpl
import com.cornershop.counterstest.shared.navigator.NavigatorRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateCounterViewModel @Inject constructor(
    @NavigatorRouterQualifier private val navigator: NavigatorRouter,
    dispatchersProvider: DispatchersProvider,
    @CreateCounterStateQualifier initialState: CreateCounterState
) : StateViewModelImpl<CreateCounterState, CreateCounterIntention>(
    dispatchersProvider = dispatchersProvider,
    initialState = initialState
), CreateCounterContract.ViewModel {

    private lateinit var name: String

    override suspend fun handleIntentions(intention: CreateCounterIntention) {
        when (intention) {
            is CreateCounterIntention.SetName -> {
                this.name = intention.name
                updateState { copy(canSave = name.isNotEmpty()) }
            }
            is CreateCounterIntention.Save -> Unit
            is CreateCounterIntention.Close -> navigator.pop()
        }
    }
}
