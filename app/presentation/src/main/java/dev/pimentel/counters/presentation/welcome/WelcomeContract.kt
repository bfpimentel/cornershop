package dev.pimentel.counters.presentation.welcome

import dev.pimentel.counters.presentation.welcome.data.WelcomeIntention
import dev.pimentel.counters.presentation.welcome.data.WelcomeState
import dev.pimentel.counters.shared.mvi.StateViewModel

interface WelcomeContract {

    interface ViewModel : StateViewModel<WelcomeState, WelcomeIntention>
}
