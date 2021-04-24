package com.cornershop.counterstest.presentation.counters

import com.cornershop.counterstest.ViewModelTest
import com.cornershop.counterstest.domain.entity.Counter
import com.cornershop.counterstest.domain.usecase.AddCount
import com.cornershop.counterstest.domain.usecase.DeleteCounters
import com.cornershop.counterstest.domain.usecase.GetCounters
import com.cornershop.counterstest.domain.usecase.NoParams
import com.cornershop.counterstest.domain.usecase.SearchCounters
import com.cornershop.counterstest.domain.usecase.SubtractCount
import com.cornershop.counterstest.presentation.counters.data.CounterViewData
import com.cornershop.counterstest.presentation.counters.data.CountersIntention
import com.cornershop.counterstest.presentation.counters.data.CountersState
import com.cornershop.counterstest.presentation.counters.mappers.CountersDeletionMapper
import com.cornershop.counterstest.presentation.counters.mappers.CountersSharingMapper
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
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CountersViewModelTest : ViewModelTest() {

    private val navigator = mockk<NavigatorRouter>()
    private val getCounters = mockk<GetCounters>()
    private val searchCounters = mockk<SearchCounters>()
    private val addCount = mockk<AddCount>()
    private val subtractCount = mockk<SubtractCount>()
    private val deleteCounters = mockk<DeleteCounters>()
    private val deletionMapper = mockk<CountersDeletionMapper>()
    private val sharingMapper = mockk<CountersSharingMapper>()
    private lateinit var viewModel: CountersContract.ViewModel

    @BeforeEach
    fun `setup subject`() {
        coEvery { getCounters(NoParams) } returns flowOf(counters)

        viewModel = CountersViewModel(
            navigator = navigator,
            getCounters = getCounters,
            searchCounters = searchCounters,
            addCount = addCount,
            subtractCount = subtractCount,
            deleteCounters = deleteCounters,
            deletionMapper = deletionMapper,
            sharingMapper = sharingMapper,
            dispatchersProvider = dispatchersProvider,
            initialState = CountersState()
        )
    }

    @Test
    fun `should get counters when creating view model`() = runBlockingTest {
        val countersViewData = listOf(
            CounterViewData.Counter(
                id = "id1",
                title = "title1",
                count = 1,
            ),
            CounterViewData.Counter(
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
        assertEquals(firstCountersState.numberOfSelectedCounters, 0)
        assertFalse(firstCountersState.areMenusEnabled)
        assertTrue(firstCountersState.layoutEvent.value is CountersState.Layout.Default)

        coVerify(exactly = 1) { getCounters(NoParams) }
        confirmEverythingVerified()

        countersStateJob.cancel()
    }

    @Test
    fun `should edit counters`() = runBlockingTest {
        val countersViewData = listOf(
            CounterViewData.Edit(
                id = "id1",
                title = "title1",
                count = 1,
                isSelected = false
            ),
            CounterViewData.Edit(
                id = "id2",
                title = "title2",
                count = 2,
                isSelected = true
            )
        )

        val countersStateValues = arrayListOf<CountersState>()
        val countersStateJob = launch { viewModel.state.toList(countersStateValues) }

        viewModel.publish(CountersIntention.StartEditing("id1"))
        viewModel.publish(CountersIntention.SelectOrDeselectCounter("id2"))
        viewModel.publish(CountersIntention.SelectOrDeselectCounter("id1"))

        val lastCountersState = countersStateValues.last()
        assertEquals(lastCountersState.countersEvent!!.value, countersViewData)
        assertEquals(lastCountersState.totalItemCount, 2)
        assertEquals(lastCountersState.totalTimesCount, 3)
        assertEquals(lastCountersState.numberOfSelectedCounters, 1)
        assertTrue(lastCountersState.areMenusEnabled)
        assertTrue(lastCountersState.layoutEvent.value is CountersState.Layout.Editing)

        coVerify(exactly = 1) { getCounters(NoParams) }
        confirmEverythingVerified()

        countersStateJob.cancel()
    }

    @Test
    fun `should delete selected counters`() = runBlockingTest {
        val deleteCountersParams = DeleteCounters.Params(listOf("id2"))
        val itemsToBeDeleted = listOf(Counter(id = "id2", title = "title2", count = 2))
        val itemsToBeDeletedText = "itemsToBeDeletedText"

        coEvery { deletionMapper.map(itemsToBeDeleted) } returns itemsToBeDeletedText
        coJustRun { deleteCounters(deleteCountersParams) }

        val countersStateValues = arrayListOf<CountersState>()
        val countersStateJob = launch { viewModel.state.toList(countersStateValues) }

        viewModel.publish(CountersIntention.StartEditing("id1"))
        viewModel.publish(CountersIntention.SelectOrDeselectCounter("id2"))
        viewModel.publish(CountersIntention.SelectOrDeselectCounter("id1"))
        viewModel.publish(CountersIntention.TryDeleting)
        viewModel.publish(CountersIntention.DeleteSelectedCounters)

        val lastCountersState = countersStateValues.last()
        assertTrue(lastCountersState.areMenusEnabled)
        assertEquals(lastCountersState.deleteConfirmationEvent!!.value, itemsToBeDeletedText)

        coVerify(exactly = 1) {
            getCounters(NoParams)
            deletionMapper.map(itemsToBeDeleted)
            deleteCounters(deleteCountersParams)
        }
        confirmEverythingVerified()

        countersStateJob.cancel()
    }

    @Test
    fun `should share selected counters`() = runBlockingTest {
        val itemsToBeShared = listOf(Counter(id = "id2", title = "title2", count = 2))
        val itemsToBeSharedText = "itemsToBeSharedText"

        coEvery { sharingMapper.map(itemsToBeShared) } returns itemsToBeSharedText

        val countersStateValues = arrayListOf<CountersState>()
        val countersStateJob = launch { viewModel.state.toList(countersStateValues) }

        viewModel.publish(CountersIntention.StartEditing("id1"))
        viewModel.publish(CountersIntention.SelectOrDeselectCounter("id2"))
        viewModel.publish(CountersIntention.SelectOrDeselectCounter("id1"))
        viewModel.publish(CountersIntention.ShareSelectedCounters)

        val lastCountersState = countersStateValues.last()
        assertTrue(lastCountersState.areMenusEnabled)
        assertEquals(lastCountersState.shareEvent!!.value, itemsToBeSharedText)

        coVerify(exactly = 1) {
            getCounters(NoParams)
            sharingMapper.map(itemsToBeShared)
        }
        confirmEverythingVerified()

        countersStateJob.cancel()
    }

    @Test
    fun `should stop editing counters`() = runBlockingTest {
        val countersViewData = listOf(
            CounterViewData.Counter(
                id = "id1",
                title = "title1",
                count = 1,
            ),
            CounterViewData.Counter(
                id = "id2",
                title = "title2",
                count = 2,
            )
        )

        val countersViewDataWhileEditing = listOf(
            CounterViewData.Edit(
                id = "id1",
                title = "title1",
                count = 1,
                isSelected = true
            ),
            CounterViewData.Edit(
                id = "id2",
                title = "title2",
                count = 2,
                isSelected = false
            )
        )

        val countersStateValues = arrayListOf<CountersState>()
        val countersStateJob = launch { viewModel.state.toList(countersStateValues) }

        viewModel.publish(CountersIntention.StartEditing("id1"))
        viewModel.publish(CountersIntention.FinishEditing)

        val firstCountersState = countersStateValues[0]
        assertEquals(firstCountersState.countersEvent!!.value, countersViewData)
        assertEquals(firstCountersState.totalItemCount, 2)
        assertEquals(firstCountersState.totalTimesCount, 3)
        assertEquals(firstCountersState.numberOfSelectedCounters, 0)
        assertTrue(firstCountersState.layoutEvent.value is CountersState.Layout.Default)

        val secondCountersState = countersStateValues[1]
        assertEquals(secondCountersState.countersEvent!!.value, countersViewDataWhileEditing)
        assertEquals(secondCountersState.totalItemCount, 2)
        assertEquals(secondCountersState.totalTimesCount, 3)
        assertEquals(secondCountersState.numberOfSelectedCounters, 1)
        assertTrue(secondCountersState.layoutEvent.value is CountersState.Layout.Editing)

        val thirdCountersState = countersStateValues[2]
        assertEquals(thirdCountersState.countersEvent!!.value, countersViewData)
        assertEquals(thirdCountersState.totalItemCount, 2)
        assertEquals(thirdCountersState.totalTimesCount, 3)
        assertEquals(thirdCountersState.numberOfSelectedCounters, 0)
        assertFalse(thirdCountersState.areMenusEnabled)
        assertTrue(thirdCountersState.layoutEvent.value is CountersState.Layout.Default)

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

    private fun confirmEverythingVerified() {
        confirmVerified(
            navigator,
            getCounters,
            searchCounters,
            addCount,
            subtractCount,
            deleteCounters,
            deletionMapper,
            sharingMapper
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
