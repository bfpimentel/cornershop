package com.cornershop.counterstest.presentation.welcome

import androidx.lifecycle.viewModelScope
import com.cornershop.counterstest.di.NavigatorRouterQualifier
import com.cornershop.counterstest.domain.usecase.FetchAndSaveCounters
import com.cornershop.counterstest.domain.usecase.HasFetchedCounters
import com.cornershop.counterstest.domain.usecase.NoParams
import com.cornershop.counterstest.presentation.welcome.data.WelcomeIntention
import com.cornershop.counterstest.presentation.welcome.data.WelcomeState
import com.cornershop.counterstest.shared.dispatchers.DispatchersProvider
import com.cornershop.counterstest.shared.mvi.StateViewModelImpl
import com.cornershop.counterstest.shared.mvi.toEvent
import com.cornershop.counterstest.shared.navigator.NavigatorRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    @NavigatorRouterQualifier private val navigator: NavigatorRouter,
    private val hasFetchedCounters: HasFetchedCounters,
    private val fetchAndSaveCounters: FetchAndSaveCounters,
    dispatchersProvider: DispatchersProvider,
    @WelcomeStateQualifier initialState: WelcomeState
) : StateViewModelImpl<WelcomeState, WelcomeIntention>(
    dispatchersProvider = dispatchersProvider,
    initialState = initialState
), WelcomeContract.ViewModel {

    init {
        viewModelScope.launch(dispatchersProvider.io) {
            val hasFetchedCounters = hasFetchedCounters(NoParams)
            fetchAndSaveCounters(hasFetchedCounters)
        }
    }

    override suspend fun handleIntentions(intention: WelcomeIntention) {
        when (intention) {
            WelcomeIntention.NavigateToCounters -> navigateToCounters()
        }
    }

    private suspend fun navigateToCounters() {
        val navigate = suspend { navigator.navigate(WelcomeFragmentDirections.toCountersFragment()) }

        val hasFetchedCounters = hasFetchedCounters(NoParams)

        if (hasFetchedCounters) {
            navigate()
            return
        }

        fetchAndSaveCounters(hasFetchedCounters, navigate::invoke)
    }

    private suspend fun fetchAndSaveCounters(
        hasFetchedCounters: Boolean,
        continuation: (suspend () -> Unit)? = null
    ) {
        updateState { copy(isButtonEnabled = false) }

        try {
            if (hasFetchedCounters) {
                updateState { copy(isButtonEnabled = true) }
                return
            }

            fetchAndSaveCounters(NoParams)

            updateState { copy(isButtonEnabled = true) }

            continuation?.invoke()
        } catch (error: Exception) {
            updateState {
                copy(
                    isButtonEnabled = true,
                    errorEvent = continuation?.let { Unit.toEvent() }
                )
            }
        }
    }
}
