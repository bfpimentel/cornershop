package com.cornershop.counterstest.presentation.welcome

import com.cornershop.counterstest.di.NavigatorRouterQualifier
import com.cornershop.counterstest.presentation.welcome.data.WelcomeIntention
import com.cornershop.counterstest.presentation.welcome.data.WelcomeState
import com.cornershop.counterstest.shared.dispatchers.DispatchersProvider
import com.cornershop.counterstest.shared.mvi.StateViewModelImpl
import com.cornershop.counterstest.shared.navigator.NavigatorRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    @NavigatorRouterQualifier private val navigator: NavigatorRouter,
    dispatchersProvider: DispatchersProvider,
    @WelcomeStateQualifier initialState: WelcomeState
) : StateViewModelImpl<WelcomeState, WelcomeIntention>(
    dispatchersProvider = dispatchersProvider,
    initialState = initialState
), WelcomeContract.ViewModel {

    override suspend fun handleIntentions(intention: WelcomeIntention) {
        when (intention) {
            WelcomeIntention.NavigateToCounters -> navigator.navigate(WelcomeFragmentDirections.toCountersFragment())
        }
    }
}
