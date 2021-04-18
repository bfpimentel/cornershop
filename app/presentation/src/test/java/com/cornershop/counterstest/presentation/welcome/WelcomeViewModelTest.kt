package com.cornershop.counterstest.presentation.welcome

import com.cornershop.counterstest.ViewModelTest
import com.cornershop.counterstest.presentation.welcome.data.WelcomeIntention
import com.cornershop.counterstest.presentation.welcome.data.WelcomeState
import com.cornershop.counterstest.shared.dispatchers.DispatchersProvider
import com.cornershop.counterstest.shared.navigator.NavigatorRouter
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.jupiter.api.Test

class WelcomeViewModelTest : ViewModelTest() {

    private val navigator = mockk<NavigatorRouter>()
    private lateinit var viewModel: WelcomeContract.ViewModel

    override fun `setup subject`(dispatchersProvider: DispatchersProvider) {
        viewModel = WelcomeViewModel(
            navigator = navigator,
            dispatchersProvider = dispatchersProvider,
            initialState = WelcomeState()
        )
    }

    @Test
    fun `should navigate to counters`() = runBlockingTest {
        val directions = WelcomeFragmentDirections.toCountersFragment()

        coJustRun { navigator.navigate(directions) }

        viewModel.publish(WelcomeIntention.NavigateToCounters)

        coVerify(exactly = 1) { navigator.navigate(directions) }
        confirmVerified(navigator)
    }
}
