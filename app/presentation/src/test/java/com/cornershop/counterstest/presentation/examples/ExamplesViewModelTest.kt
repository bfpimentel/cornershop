package com.cornershop.counterstest.presentation.examples

import com.cornershop.counterstest.R
import com.cornershop.counterstest.ViewModelTest
import com.cornershop.counterstest.domain.usecase.CreateCounter
import com.cornershop.counterstest.presentation.examples.data.ExampleViewData
import com.cornershop.counterstest.presentation.examples.data.ExamplesIntention
import com.cornershop.counterstest.presentation.examples.data.ExamplesState
import com.cornershop.counterstest.presentation.examples.mappers.ExamplesViewDataMapper
import com.cornershop.counterstest.shared.navigator.NavigatorRouter
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ExamplesViewModelTest : ViewModelTest() {

    private val navigator = mockk<NavigatorRouter>()
    private val createCounter = mockk<CreateCounter>()
    private val examplesViewDataMapper = mockk<ExamplesViewDataMapper>()
    private lateinit var viewModel: ExamplesContract.ViewModel

    @BeforeEach
    fun `setup subject`() {
        viewModel = ExamplesViewModel(
            navigator = navigator,
            createCounter = createCounter,
            mapper = examplesViewDataMapper,
            dispatchersProvider = dispatchersProvider,
            initialState = initialState
        )
    }

    @Test
    fun `should get mappers`() = runBlockingTest {
        val examples = listOf(
            ExampleViewData(title = "title1", examples = listOf("example1", "example2"))
        )

        val examplesStateValues = arrayListOf<ExamplesState>()
        val examplesStateJob = launch { viewModel.state.toList(examplesStateValues) }

        coEvery { examplesViewDataMapper.getExamples() } returns examples

        viewModel.publish(ExamplesIntention.GetExamples)

        val firstExamplesState = examplesStateValues[0]
        assertEquals(firstExamplesState, initialState)
        val secondExamplesState = examplesStateValues[1]
        assertEquals(secondExamplesState.examplesEvent!!.value, examples)

        coVerify(exactly = 1) { examplesViewDataMapper.getExamples() }
        confirmVerified(examplesViewDataMapper, createCounter, navigator)

        examplesStateJob.cancel()
    }

    @Test
    fun `should create selected example`() = runBlockingTest {
        val counterName = "counterName"
        val destinationId = R.id.countersFragment

        val createCounterParams = CreateCounter.Params(counterName)

        coJustRun { createCounter(createCounterParams) }
        coJustRun { navigator.pop(destinationId, false) }

        viewModel.publish(ExamplesIntention.SelectExample(counterName))

        coVerify(exactly = 1) {
            createCounter(createCounterParams)
            navigator.pop(destinationId, false)
        }
        confirmVerified(examplesViewDataMapper, createCounter, navigator)
    }

    @Test
    fun `should just pop navigation when closing`() = runBlockingTest {
        coJustRun { navigator.pop() }

        viewModel.publish(ExamplesIntention.Close)

        coVerify(exactly = 1) { navigator.pop() }
        confirmVerified(examplesViewDataMapper, createCounter, navigator)
    }

    private companion object {
        val initialState = ExamplesState()
    }
}
