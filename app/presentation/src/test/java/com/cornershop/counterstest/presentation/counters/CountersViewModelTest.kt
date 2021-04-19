package com.cornershop.counterstest.presentation.counters

import com.cornershop.counterstest.ViewModelTest
import com.cornershop.counterstest.domain.entity.Counter
import com.cornershop.counterstest.domain.usecase.AddCount
import com.cornershop.counterstest.domain.usecase.GetCounters
import com.cornershop.counterstest.domain.usecase.SubtractCount
import com.cornershop.counterstest.presentation.counters.data.CounterViewData
import com.cornershop.counterstest.presentation.counters.data.CountersIntention
import com.cornershop.counterstest.presentation.counters.data.CountersState
import com.cornershop.counterstest.shared.dispatchers.DispatchersProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CountersViewModelTest : ViewModelTest() {

    private val getCounters = mockk<GetCounters>()
    private val addCount = mockk<AddCount>()
    private val subtractCount = mockk<SubtractCount>()
    private lateinit var viewModel: CountersContract.ViewModel

    override fun `setup subject`(dispatchersProvider: DispatchersProvider) {
        viewModel = CountersViewModel(
            getCounters = getCounters,
            addCount = addCount,
            subtractCount = subtractCount,
            dispatchersProvider = dispatchersProvider,
            initialState = initialState
        )
    }

    @Test
    fun `should get counters`() = runBlockingTest {
        val counters = listOf(
            Counter(
                id = "id1",
                title = "title1",
                count = 1,
            ),
            Counter(
                id = "id2",
                title = "title2",
                count = 2,
            )
        )

        val countersViewData = listOf(
            CounterViewData(
                id = "id1",
                title = "title1",
                count = 1,
            ),
            CounterViewData(
                id = "id2",
                title = "title2",
                count = 2,
            )
        )

        val getCountersParams = GetCounters.Params(null)

        coEvery { getCounters(getCountersParams) } returns flowOf(counters)

        val countersStateValues = arrayListOf<CountersState>()
        val countersStateJob = launch { viewModel.state.toList(countersStateValues) }

        viewModel.publish(CountersIntention.GetCounters)

        val firstCountersState = countersStateValues[0]
        assertEquals(firstCountersState, initialState)

        val secondCountersState = countersStateValues[1]
        assertEquals(secondCountersState.countersEvent!!.value, countersViewData)
        assertEquals(secondCountersState.totalItemCount, 2)
        assertEquals(secondCountersState.totalTimesCount, 3)

        coVerify(exactly = 1) {
            getCounters(getCountersParams)
        }
        confirmVerified(getCounters)

        countersStateJob.cancel()
    }

    private companion object {
        val initialState = CountersState()
    }
}
