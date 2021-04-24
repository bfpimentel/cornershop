package com.cornershop.counterstest.presentation.welcome

import com.cornershop.counterstest.ViewModelTest
import com.cornershop.counterstest.domain.usecase.FetchAndSaveCounters
import com.cornershop.counterstest.domain.usecase.HasFetchedCounters
import com.cornershop.counterstest.domain.usecase.NoParams
import com.cornershop.counterstest.presentation.welcome.data.WelcomeIntention
import com.cornershop.counterstest.presentation.welcome.data.WelcomeState
import com.cornershop.counterstest.shared.navigator.NavigatorRouter
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.jupiter.api.Test

class WelcomeViewModelTest : ViewModelTest() {

    private val navigator = mockk<NavigatorRouter>()
    private val hasFetchedCounters = mockk<HasFetchedCounters>()
    private val fetchAndSaveCounters = mockk<FetchAndSaveCounters>()
    private val viewModel: WelcomeContract.ViewModel
        get() = WelcomeViewModel(
            navigator = navigator,
            dispatchersProvider = dispatchersProvider,
            hasFetchedCounters = this@WelcomeViewModelTest.hasFetchedCounters,
            fetchAndSaveCounters = fetchAndSaveCounters,
            initialState = initialState
        )

    @Test
    fun `should navigate to counters when first fetching is successful`() = runBlockingTest {
        val directions = WelcomeFragmentDirections.toCountersFragment()

        coEvery { hasFetchedCounters(NoParams) } returns false andThen true
        coJustRun { fetchAndSaveCounters(NoParams) }
        coJustRun { navigator.navigate(directions) }

        viewModel.publish(WelcomeIntention.NavigateToCounters)

        coVerify(exactly = 1) {
            fetchAndSaveCounters(NoParams)
            navigator.navigate(directions)
        }
        coVerify(exactly = 2) { hasFetchedCounters(NoParams) }
        confirmEverythingVerified()
    }

    @Test
    fun `should navigate to counters when fetching is not needed`() = runBlockingTest {
        val directions = WelcomeFragmentDirections.toCountersFragment()

        coEvery { hasFetchedCounters(NoParams) } returns true andThen true
        coJustRun { navigator.navigate(directions) }

        viewModel.publish(WelcomeIntention.NavigateToCounters)

        coVerify(exactly = 1) { navigator.navigate(directions) }
        coVerify(exactly = 2) { hasFetchedCounters(NoParams) }
        confirmEverythingVerified()
    }

    @Test
    fun `should try again when first fetching was unsuccessful`() = runBlockingTest {
        val directions = WelcomeFragmentDirections.toCountersFragment()

        coEvery { hasFetchedCounters(NoParams) } returns false andThen false andThen true
        coEvery { fetchAndSaveCounters(NoParams) } throws IllegalArgumentException() andThen Unit
        coJustRun { navigator.navigate(directions) }

        viewModel.publish(WelcomeIntention.NavigateToCounters)

        coVerify(exactly = 1) { navigator.navigate(directions) }
        coVerify(exactly = 2) { fetchAndSaveCounters(NoParams) }
        coVerify(exactly = 3) { hasFetchedCounters(NoParams) }
        confirmEverythingVerified()
    }

    private fun confirmEverythingVerified() {
        confirmVerified(
            navigator,
            hasFetchedCounters,
            fetchAndSaveCounters
        )
    }

    private companion object {
        val initialState = WelcomeState()
    }
}
