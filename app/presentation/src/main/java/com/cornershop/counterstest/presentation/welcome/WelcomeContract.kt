package com.cornershop.counterstest.presentation.welcome

import com.cornershop.counterstest.presentation.welcome.data.WelcomeIntention
import com.cornershop.counterstest.presentation.welcome.data.WelcomeState
import com.cornershop.counterstest.shared.mvi.StateViewModel

interface WelcomeContract {

    interface ViewModel : StateViewModel<WelcomeState, WelcomeIntention>
}
