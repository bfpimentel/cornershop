package com.cornershop.counterstest.presentation.welcome.data

import com.cornershop.counterstest.shared.mvi.Event

data class WelcomeState(
    val isButtonEnabled: Boolean = true,
    val errorEvent: Event<Unit>? = null
)
