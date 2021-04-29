package dev.pimentel.counters.presentation.welcome

import dev.pimentel.counters.ViewModelTest
import dev.pimentel.counters.domain.usecase.FetchAndSaveCounters
import dev.pimentel.counters.domain.usecase.HasFetchedCounters
import dev.pimentel.counters.domain.usecase.NoParams
import dev.pimentel.counters.presentation.welcome.data.WelcomeIntention
import dev.pimentel.counters.presentation.welcome.data.WelcomeState
import dev.pimentel.counters.shared.navigator.NavigatorRouter
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class WelcomeViewModelTest : ViewModelTest() {

    private val navigator = mockk<NavigatorRouter>()
    private val hasFetchedCounters = mockk<HasFetchedCounters>()
    private val fetchAndSaveCounters = mockk<FetchAndSaveCounters>()

    @Test
    fun `should just navigate to counters when first fetching is successful`() = runBlockingTest {
        val directions = WelcomeFragmentDirections.toCountersFragment()

        coEvery { hasFetchedCounters(NoParams) } returns false andThen true
        coJustRun { fetchAndSaveCounters(NoParams) }
        coJustRun { navigator.navigate(directions) }

        val viewModel = getViewModelInstance()

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

        val viewModel = getViewModelInstance()

        viewModel.publish(WelcomeIntention.NavigateToCounters)

        coVerify(exactly = 1) { navigator.navigate(directions) }
        coVerify(exactly = 2) { hasFetchedCounters(NoParams) }
        confirmEverythingVerified()
    }

    @Test
    fun `should show error when fetching comes from intention and it fails`() = runBlockingTest {
        coEvery { hasFetchedCounters(NoParams) } returns false andThen false
        coEvery { fetchAndSaveCounters(NoParams) } throws IllegalStateException() andThenThrows IllegalStateException()

        val viewModel = getViewModelInstance()

        val welcomeStateValues = arrayListOf<WelcomeState>()
        val welcomeStateJob = launch { viewModel.state.toList(welcomeStateValues) }

        viewModel.publish(WelcomeIntention.NavigateToCounters)

        val firstWelcomeState = welcomeStateValues[0]
        assertTrue(firstWelcomeState.isButtonEnabled)
        assertNull(firstWelcomeState.errorEvent)
        val secondWelcomeState = welcomeStateValues[1]
        assertFalse(secondWelcomeState.isButtonEnabled)
        assertNull(secondWelcomeState.errorEvent)
        val thirdWelcomeState = welcomeStateValues[2]
        assertTrue(thirdWelcomeState.isButtonEnabled)
        assertNotNull(thirdWelcomeState.errorEvent)

        coVerify(exactly = 2) {
            fetchAndSaveCounters(NoParams)
            hasFetchedCounters(NoParams)
        }
        confirmEverythingVerified()

        welcomeStateJob.cancel()
    }

    private fun getViewModelInstance(): WelcomeContract.ViewModel =
        WelcomeViewModel(
            navigator = navigator,
            dispatchersProvider = dispatchersProvider,
            hasFetchedCounters = this@WelcomeViewModelTest.hasFetchedCounters,
            fetchAndSaveCounters = fetchAndSaveCounters,
            initialState = initialState
        )

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
