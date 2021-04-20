package com.cornershop.counterstest.presentation.counters

import com.cornershop.counterstest.ViewModelTest
import com.cornershop.counterstest.domain.entity.Counter
import com.cornershop.counterstest.domain.usecase.AddCount
import com.cornershop.counterstest.domain.usecase.GetCounters
import com.cornershop.counterstest.domain.usecase.NoParams
import com.cornershop.counterstest.domain.usecase.SearchCounters
import com.cornershop.counterstest.domain.usecase.SubtractCount
import com.cornershop.counterstest.presentation.counters.data.CounterViewData
import com.cornershop.counterstest.presentation.counters.data.CountersIntention
import com.cornershop.counterstest.presentation.counters.data.CountersState
import com.cornershop.counterstest.shared.dispatchers.DispatchersProvider
import com.cornershop.counterstest.shared.navigator.NavigatorRouter
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CountersViewModelTest : ViewModelTest() {

    private val navigator = mockk<NavigatorRouter>()
    private val getCounters = mockk<GetCounters>()
    private val searchCounters = mockk<SearchCounters>()
    private val addCount = mockk<AddCount>()
    private val subtractCount = mockk<SubtractCount>()
    private lateinit var viewModel: CountersContract.ViewModel

    override fun `setup subject`(dispatchersProvider: DispatchersProvider) {
        mockInit()

        viewModel = CountersViewModel(
            navigator = navigator,
            getCounters = getCounters,
            searchCounters = searchCounters,
            addCount = addCount,
            subtractCount = subtractCount,
            dispatchersProvider = dispatchersProvider,
            initialState = CountersState()
        )
    }

    @Test
    fun `should get counters when creating view model`() = runBlockingTest {
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

        val countersStateValues = arrayListOf<CountersState>()
        val countersStateJob = launch { viewModel.state.toList(countersStateValues) }

        val firstCountersState = countersStateValues[0]
        assertEquals(firstCountersState.countersEvent!!.value, countersViewData)
        assertEquals(firstCountersState.totalItemCount, 2)
        assertEquals(firstCountersState.totalTimesCount, 3)

        coVerify(exactly = 1) { getCounters(NoParams) }
        confirmEverythingVerified()

        countersStateJob.cancel()
    }

    @Test
    fun `should search counters`() = runBlockingTest {
        val query = "query"

        val searchCountersParams = SearchCounters.Params(query)

        coJustRun { searchCounters(searchCountersParams) }

        viewModel.publish(CountersIntention.SearchCounters(query))

        coVerify(exactly = 1) {
            getCounters(NoParams)
            searchCounters(searchCountersParams)
        }
        confirmEverythingVerified()
    }

    @Test
    fun `should add count`() = runBlockingTest {
        val counterId = "counterId"

        val addCountParams = AddCount.Params(counterId)

        coJustRun { addCount(addCountParams) }

        viewModel.publish(CountersIntention.Add(counterId))

        coVerify(exactly = 1) {
            getCounters(NoParams)
            addCount(addCountParams)
        }
        confirmEverythingVerified()
    }

    @Test
    fun `should subtract count`() = runBlockingTest {
        val counterId = "counterId"

        val subtractCountParams = SubtractCount.Params(counterId)

        coJustRun { subtractCount(subtractCountParams) }

        viewModel.publish(CountersIntention.Subtract(counterId))

        coVerify(exactly = 1) {
            getCounters(NoParams)
            subtractCount(subtractCountParams)
        }
        confirmEverythingVerified()
    }

    @Test
    fun `should navigate to counter creation`() = runBlockingTest {
        val directions = CountersFragmentDirections.toCreateCounterFragment()

        coJustRun { navigator.navigate(directions) }

        viewModel.publish(CountersIntention.NavigateToCreateCounter)

        coVerify(exactly = 1) {
            getCounters(NoParams)
            navigator.navigate(directions)
        }
        confirmEverythingVerified()
    }

    private fun mockInit() {
        coEvery { getCounters(NoParams) } returns flowOf(counters)
    }

    private fun confirmEverythingVerified() {
        confirmVerified(
            navigator,
            getCounters,
            searchCounters,
            addCount,
            subtractCount
        )
    }

    private companion object {
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
    }
}
