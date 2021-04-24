package com.cornershop.counterstest.presentation.create_counter

import com.cornershop.counterstest.ViewModelTest
import com.cornershop.counterstest.domain.usecase.CreateCounter
import com.cornershop.counterstest.presentation.create_counter.data.CreateCounterIntention
import com.cornershop.counterstest.presentation.create_counter.data.CreateCounterState
import com.cornershop.counterstest.shared.navigator.NavigatorRouter
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateCounterViewModelTest : ViewModelTest() {

    private val navigator = mockk<NavigatorRouter>()
    private val createCounter = mockk<CreateCounter>()
    private lateinit var viewModel: CreateCounterContract.ViewModel

    @BeforeEach
    fun `setup subject`() {
        viewModel = CreateCounterViewModel(
            navigator = navigator,
            createCounter = createCounter,
            dispatchersProvider = dispatchersProvider,
            initialState = initialState
        )
    }

    @Test
    fun `should create counter`() = runBlockingTest {
        val counterName = "counterName"

        val createCounterParams = CreateCounter.Params(counterName)

        coJustRun { createCounter(createCounterParams) }
        coJustRun { navigator.pop() }

        val createCounterStateValues = arrayListOf<CreateCounterState>()
        val createCounterStateJob = launch { viewModel.state.toList(createCounterStateValues) }

        viewModel.publish(CreateCounterIntention.SetName(counterName))
        viewModel.publish(CreateCounterIntention.Save)

        val firstCountersState = createCounterStateValues[0]
        assertEquals(firstCountersState, initialState)

        val secondCreateCounterState = createCounterStateValues[1]
        assertTrue(secondCreateCounterState.canSave)

        coVerify(exactly = 1) {
            createCounter(createCounterParams)
            navigator.pop()
        }
        confirmEverythingVerified()

        createCounterStateJob.cancel()
    }

    @Test
    fun `should enable save button when is not empty and disable when it is`() = runBlockingTest {
        val createCounterStateValues = arrayListOf<CreateCounterState>()
        val createCounterStateJob = launch { viewModel.state.toList(createCounterStateValues) }

        viewModel.publish(CreateCounterIntention.SetName("notAnEmptyName"))
        viewModel.publish(CreateCounterIntention.SetName(""))

        val firstCountersState = createCounterStateValues[0]
        assertEquals(firstCountersState, initialState)

        val secondCreateCounterState = createCounterStateValues[1]
        assertTrue(secondCreateCounterState.canSave)

        val thirdCreateCounterState = createCounterStateValues[2]
        assertFalse(thirdCreateCounterState.canSave)

        confirmEverythingVerified()

        createCounterStateJob.cancel()
    }

    @Test
    fun `should just close`() = runBlockingTest {
        coJustRun { navigator.pop() }

        viewModel.publish(CreateCounterIntention.Close)

        coVerify(exactly = 1) {
            navigator.pop()
        }
        confirmEverythingVerified()
    }

    @Test
    fun `should navigate to examples`() = runBlockingTest {
        val directions = CreateCounterFragmentDirections.toExamplesFragment()

        coJustRun { navigator.navigate(directions) }

        viewModel.publish(CreateCounterIntention.NavigateToExamples)

        coVerify(exactly = 1) {
            navigator.navigate(directions)
        }
        confirmEverythingVerified()
    }

    private fun confirmEverythingVerified() {
        confirmVerified(
            navigator,
            createCounter
        )
    }

    private companion object {
        val initialState = CreateCounterState()
    }
}
