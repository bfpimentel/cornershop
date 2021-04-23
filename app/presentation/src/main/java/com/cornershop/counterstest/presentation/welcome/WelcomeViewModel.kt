package com.cornershop.counterstest.presentation.welcome

import androidx.lifecycle.viewModelScope
import com.cornershop.counterstest.di.NavigatorRouterQualifier
import com.cornershop.counterstest.domain.usecase.FetchAndSaveCounters
import com.cornershop.counterstest.domain.usecase.IsFirstAccess
import com.cornershop.counterstest.domain.usecase.NoParams
import com.cornershop.counterstest.presentation.welcome.data.WelcomeIntention
import com.cornershop.counterstest.presentation.welcome.data.WelcomeState
import com.cornershop.counterstest.shared.dispatchers.DispatchersProvider
import com.cornershop.counterstest.shared.mvi.StateViewModelImpl
import com.cornershop.counterstest.shared.navigator.NavigatorRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    @NavigatorRouterQualifier private val navigator: NavigatorRouter,
    private val isFirstAccess: IsFirstAccess,
    private val fetchAndSaveCounters: FetchAndSaveCounters,
    dispatchersProvider: DispatchersProvider,
    @WelcomeStateQualifier initialState: WelcomeState
) : StateViewModelImpl<WelcomeState, WelcomeIntention>(
    dispatchersProvider = dispatchersProvider,
    initialState = initialState
), WelcomeContract.ViewModel {

    private var countersHaveBeenSaved: Boolean = false

    init {
        viewModelScope.launch(dispatchersProvider.io) { fetchAndSaveCounters() }
    }

    override suspend fun handleIntentions(intention: WelcomeIntention) {
        when (intention) {
            WelcomeIntention.NavigateToCounters -> navigateToCounters()
        }
    }

    private suspend fun navigateToCounters() {
        if (this.countersHaveBeenSaved) {
            navigator.navigate(WelcomeFragmentDirections.toCountersFragment())
            return
        }

        fetchAndSaveCounters(::navigateToCounters)
    }

    private suspend fun fetchAndSaveCounters(continuation: (suspend () -> Unit)? = null) {
        try {
            val isFirstAccess = isFirstAccess(NoParams)

            if (!isFirstAccess) {
                this.countersHaveBeenSaved = true
                updateState { copy(isLoading = false) }
                return
            }

            fetchAndSaveCounters(NoParams)

            continuation?.let { nullSafeContinuation ->
                this.countersHaveBeenSaved = true
                nullSafeContinuation()
            } ?: run {
                this.countersHaveBeenSaved = true
            }

            updateState { copy(isLoading = false) }
        } catch (error: Exception) {
            // TODO: Need to show error
            updateState { copy(isLoading = false) }
        }
    }
}
