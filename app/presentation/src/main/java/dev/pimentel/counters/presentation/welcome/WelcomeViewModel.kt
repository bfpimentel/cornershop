package dev.pimentel.counters.presentation.welcome

import androidx.lifecycle.viewModelScope
import dev.pimentel.counters.di.NavigatorRouterQualifier
import dev.pimentel.counters.domain.usecase.FetchAndSaveCounters
import dev.pimentel.counters.domain.usecase.HasFetchedCounters
import dev.pimentel.counters.domain.usecase.NoParams
import dev.pimentel.counters.presentation.welcome.data.WelcomeIntention
import dev.pimentel.counters.presentation.welcome.data.WelcomeState
import dev.pimentel.counters.shared.dispatchers.DispatchersProvider
import dev.pimentel.counters.shared.mvi.StateViewModelImpl
import dev.pimentel.counters.shared.mvi.toEvent
import dev.pimentel.counters.shared.navigator.NavigatorRouter
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
