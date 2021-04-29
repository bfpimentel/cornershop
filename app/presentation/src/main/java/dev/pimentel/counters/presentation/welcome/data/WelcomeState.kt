package dev.pimentel.counters.presentation.welcome.data

import dev.pimentel.counters.shared.mvi.Event

data class WelcomeState(
    val isButtonEnabled: Boolean = true,
    val errorEvent: Event<Unit>? = null
)
