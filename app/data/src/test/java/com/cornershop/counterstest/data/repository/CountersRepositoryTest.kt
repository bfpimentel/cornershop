package com.cornershop.counterstest.data.repository

import com.cornershop.counterstest.data.body.CounterBody
import com.cornershop.counterstest.data.body.SyncCountersBody
import com.cornershop.counterstest.data.dto.CounterDTO
import com.cornershop.counterstest.data.generator.IdGenerator
import com.cornershop.counterstest.data.model.CounterModelImpl
import com.cornershop.counterstest.data.sources.local.CountersLocalDataSource
import com.cornershop.counterstest.data.sources.remote.CountersRemoteDataSource
import com.cornershop.counterstest.domain.repository.CountersRepository
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CountersRepositoryTest {

    private val countersRemoteDataSource = mockk<CountersRemoteDataSource>()
    private val countersLocalDataSource = mockk<CountersLocalDataSource>()
    private val idGenerator = mockk<IdGenerator>()
    private val dispatcher = TestCoroutineDispatcher()
    private val scope = TestCoroutineScope(dispatcher)

    private lateinit var repository: CountersRepository

    @BeforeEach
    fun `setup subject`() {
        Dispatchers.setMain(dispatcher)

        repository = CountersRepositoryImpl(
            countersRemoteDataSource = countersRemoteDataSource,
            countersLocalDataSource = countersLocalDataSource,
            externalScope = scope,
            idGenerator = idGenerator
        )
    }

    @AfterEach
    fun `tear down`() {
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `should fetch and save counters`() = dispatcher.runBlockingTest {
        val remoteCounters = listOf(
            CounterBody(
                id = "id1",
                title = "title1",
                count = 1,
            ),
            CounterBody(
                id = "id2",
                title = "title2",
                count = 2,
            )
        )

        val localCounters = listOf(
            CounterDTO(
                id = "id1",
                title = "title1",
                count = 1,
            ),
            CounterDTO(
                id = "id2",
                title = "title2",
                count = 2,
            )
        )

        coEvery { countersRemoteDataSource.getCounters() } returns remoteCounters
        coJustRun { countersLocalDataSource.insertCounters(localCounters) }

        repository.fetchAndSaveCounters()

        coVerify(exactly = 1) {
            countersRemoteDataSource.getCounters()
            countersLocalDataSource.insertCounters(localCounters)
        }
        confirmEverythingVerified()
    }

    @Test
    fun `should get counters when searching for them`() = dispatcher.runBlockingTest {
        val query = "query"

        val localCounters = listOf(
            CounterDTO(
                id = "id1",
                title = "title1",
                count = 1,
            ),
            CounterDTO(
                id = "id2",
                title = "title2",
                count = 2,
            )
        )

        val countersModels = listOf(
            CounterModelImpl(
                id = "id1",
                title = "title1",
                count = 1,
            ),
            CounterModelImpl(
                id = "id2",
                title = "title2",
                count = 2,
            )
        )

        coEvery { countersLocalDataSource.getCounters(query) } returns flowOf(localCounters)

        repository.searchCounters(query)
        advanceTimeBy(1000L)

        assertEquals(repository.getCounters().first(), countersModels)

        coVerify(exactly = 1) {
            countersLocalDataSource.getCounters(query)
        }
        confirmEverythingVerified()
    }

    @Test
    fun `should create counter and sync`() = dispatcher.runBlockingTest {
        val newCounterId = "newCounterId"
        val newCounterName = "newCounterName"

        val deletedCounterId = "deletedCounterId"

        val newCounter = CounterDTO(
            id = newCounterId,
            title = newCounterName,
            count = 0,
            isSynchronized = false,
            hasBeenDeleted = false
        )

        val unsynchronizedCounters = listOf(
            newCounter,
            CounterDTO(id = deletedCounterId, title = "title2", count = 1, hasBeenDeleted = true)
        )

        val deletedCounterIds = listOf(deletedCounterId)
        val countersToBeSynchronized = listOf(CounterBody(id = newCounterId, title = newCounterName, count = 0))

        val syncBody = SyncCountersBody(
            deletedCountersIds = deletedCounterIds,
            counters = countersToBeSynchronized
        )

        val countersToBeSynchronizedIds = listOf(newCounterId)

        coEvery { idGenerator.generateId(instant = any()) } returns newCounterId
        coJustRun { countersLocalDataSource.createCounter(newCounter) }
        coEvery { countersLocalDataSource.getUnsynchronizedCounters() } returns unsynchronizedCounters
        coJustRun { countersRemoteDataSource.syncCounters(syncBody) }
        coJustRun { countersLocalDataSource.synchronizeCounters(countersToBeSynchronizedIds) }
        coJustRun { countersLocalDataSource.removeDeletedCounters(deletedCounterIds) }

        repository.createCounter(newCounterName)
        advanceTimeBy(5000L)

        coVerify(exactly = 1) {
            idGenerator.generateId(instant = any())
            countersLocalDataSource.createCounter(newCounter)
            countersLocalDataSource.getUnsynchronizedCounters()
            countersRemoteDataSource.syncCounters(syncBody)
            countersLocalDataSource.synchronizeCounters(countersToBeSynchronizedIds)
            countersLocalDataSource.removeDeletedCounters(deletedCounterIds)
        }
        confirmEverythingVerified()
    }

    @Test
    fun `should add to counter and sync`() = dispatcher.runBlockingTest {
        val counterId = "counterId"
        val deletedCounterId = "deletedCounterId"

        val unsynchronizedCounters = listOf(
            CounterDTO(id = counterId, title = "title1", count = 1, hasBeenDeleted = false),
            CounterDTO(id = deletedCounterId, title = "title2", count = 1, hasBeenDeleted = true)
        )

        val deletedCounterIds = listOf(deletedCounterId)
        val countersToBeSynchronized = listOf(CounterBody(id = counterId, title = "title1", count = 1))

        val syncBody = SyncCountersBody(
            deletedCountersIds = deletedCounterIds,
            counters = countersToBeSynchronized
        )

        val countersToBeSynchronizedIds = listOf(counterId)

        coJustRun { countersLocalDataSource.addCount(counterId) }
        coEvery { countersLocalDataSource.getUnsynchronizedCounters() } returns unsynchronizedCounters
        coJustRun { countersRemoteDataSource.syncCounters(syncBody) }
        coJustRun { countersLocalDataSource.synchronizeCounters(countersToBeSynchronizedIds) }
        coJustRun { countersLocalDataSource.removeDeletedCounters(deletedCounterIds) }

        repository.addCount(counterId)
        advanceTimeBy(5000L)

        coVerify(exactly = 1) {
            countersLocalDataSource.addCount(counterId)
            countersLocalDataSource.getUnsynchronizedCounters()
            countersRemoteDataSource.syncCounters(syncBody)
            countersLocalDataSource.synchronizeCounters(countersToBeSynchronizedIds)
            countersLocalDataSource.removeDeletedCounters(deletedCounterIds)
        }
        confirmEverythingVerified()
    }

    @Test
    fun `should subtract to counter and sync`() = dispatcher.runBlockingTest {
        val counterId = "counterId"
        val deletedCounterId = "deletedCounterId"

        val unsynchronizedCounters = listOf(
            CounterDTO(id = counterId, title = "title1", count = 1, hasBeenDeleted = false),
            CounterDTO(id = deletedCounterId, title = "title2", count = 1, hasBeenDeleted = true)
        )

        val deletedCounterIds = listOf(deletedCounterId)
        val countersToBeSynchronized = listOf(CounterBody(id = counterId, title = "title1", count = 1))

        val syncBody = SyncCountersBody(
            deletedCountersIds = deletedCounterIds,
            counters = countersToBeSynchronized
        )

        val countersToBeSynchronizedIds = listOf(counterId)

        coJustRun { countersLocalDataSource.subtractCount(counterId) }
        coEvery { countersLocalDataSource.getUnsynchronizedCounters() } returns unsynchronizedCounters
        coJustRun { countersRemoteDataSource.syncCounters(syncBody) }
        coJustRun { countersLocalDataSource.synchronizeCounters(countersToBeSynchronizedIds) }
        coJustRun { countersLocalDataSource.removeDeletedCounters(deletedCounterIds) }

        repository.subtractCount(counterId)
        advanceTimeBy(5000L)

        coVerify(exactly = 1) {
            countersLocalDataSource.subtractCount(counterId)
            countersLocalDataSource.getUnsynchronizedCounters()
            countersRemoteDataSource.syncCounters(syncBody)
            countersLocalDataSource.synchronizeCounters(countersToBeSynchronizedIds)
            countersLocalDataSource.removeDeletedCounters(deletedCounterIds)
        }
        confirmEverythingVerified()
    }

    @Test
    fun `should delete counter and sync`() = dispatcher.runBlockingTest {
        val deletedCountersIds = listOf("deletedCounterId")

        val unsynchronizedCounters = listOf(
            CounterDTO(id = deletedCountersIds.first(), title = "title1", count = 1, hasBeenDeleted = true),
            CounterDTO(id = "id2", title = "title2", count = 1, hasBeenDeleted = false)
        )

        val countersToBeSynchronized = listOf(CounterBody(id = "id2", title = "title2", count = 1))

        val syncBody = SyncCountersBody(
            deletedCountersIds = deletedCountersIds,
            counters = countersToBeSynchronized
        )

        val countersToBeSynchronizedIds = listOf("id2")

        coJustRun { countersLocalDataSource.deleteCounters(deletedCountersIds) }
        coEvery { countersLocalDataSource.getUnsynchronizedCounters() } returns unsynchronizedCounters
        coJustRun { countersRemoteDataSource.syncCounters(syncBody) }
        coJustRun { countersLocalDataSource.synchronizeCounters(countersToBeSynchronizedIds) }
        coJustRun { countersLocalDataSource.removeDeletedCounters(deletedCountersIds) }

        repository.deleteCounters(deletedCountersIds)
        advanceTimeBy(5000L)

        coVerify(exactly = 1) {
            countersLocalDataSource.deleteCounters(deletedCountersIds)
            countersLocalDataSource.getUnsynchronizedCounters()
            countersRemoteDataSource.syncCounters(syncBody)
            countersLocalDataSource.synchronizeCounters(countersToBeSynchronizedIds)
            countersLocalDataSource.removeDeletedCounters(deletedCountersIds)
        }
        confirmEverythingVerified()
    }

    @Test
    fun `should add multiple times in debounce interval, syncing just one time`() = dispatcher.runBlockingTest {
        val additions = 5

        val counterId = "counterId"
        val deletedCounterId = "deletedCounterId"

        val unsynchronizedCounters = listOf(
            CounterDTO(id = counterId, title = "title1", count = 1, hasBeenDeleted = false),
            CounterDTO(id = deletedCounterId, title = "title2", count = 1, hasBeenDeleted = true)
        )

        val deletedCounterIds = listOf(deletedCounterId)
        val countersToBeSynchronized = listOf(CounterBody(id = counterId, title = "title1", count = 1))

        val syncBody = SyncCountersBody(
            deletedCountersIds = deletedCounterIds,
            counters = countersToBeSynchronized
        )

        val countersToBeSynchronizedIds = listOf(counterId)

        coJustRun { countersLocalDataSource.addCount(counterId) }
        coEvery { countersLocalDataSource.getUnsynchronizedCounters() } returns unsynchronizedCounters
        coJustRun { countersRemoteDataSource.syncCounters(syncBody) }
        coJustRun { countersLocalDataSource.synchronizeCounters(countersToBeSynchronizedIds) }
        coJustRun { countersLocalDataSource.removeDeletedCounters(deletedCounterIds) }

        repeat((1..additions).count()) { repository.addCount(counterId) }
        advanceTimeBy(5000L)

        coVerify(exactly = additions) {
            countersLocalDataSource.addCount(counterId)
        }
        coVerify(exactly = 1) {
            countersLocalDataSource.getUnsynchronizedCounters()
            countersRemoteDataSource.syncCounters(syncBody)
            countersLocalDataSource.synchronizeCounters(countersToBeSynchronizedIds)
            countersLocalDataSource.removeDeletedCounters(deletedCounterIds)
        }
        confirmEverythingVerified()
    }

    @Test
    fun `should add multiple times in debounce interval and one after it, syncing two times`() =
        dispatcher.runBlockingTest {
            val additions = 5

            val counterId = "counterId"
            val deletedCounterId = "deletedCounterId"

            val unsynchronizedCounters = listOf(
                CounterDTO(id = counterId, title = "title1", count = 1, hasBeenDeleted = false),
                CounterDTO(id = deletedCounterId, title = "title2", count = 1, hasBeenDeleted = true)
            )

            val deletedCounterIds = listOf(deletedCounterId)
            val countersToBeSynchronized = listOf(CounterBody(id = counterId, title = "title1", count = 1))

            val syncBody = SyncCountersBody(
                deletedCountersIds = deletedCounterIds,
                counters = countersToBeSynchronized
            )

            val countersToBeSynchronizedIds = listOf(counterId)

            coJustRun { countersLocalDataSource.addCount(counterId) }
            coEvery { countersLocalDataSource.getUnsynchronizedCounters() } returns unsynchronizedCounters
            coJustRun { countersRemoteDataSource.syncCounters(syncBody) }
            coJustRun { countersLocalDataSource.synchronizeCounters(countersToBeSynchronizedIds) }
            coJustRun { countersLocalDataSource.removeDeletedCounters(deletedCounterIds) }

            repeat((1..additions).count()) { repository.addCount(counterId) }
            advanceTimeBy(5000L)
            repository.addCount(counterId)
            advanceTimeBy(5000L)

            coVerify(exactly = additions + 1) {
                countersLocalDataSource.addCount(counterId)
            }
            coVerify(exactly = 2) {
                countersLocalDataSource.getUnsynchronizedCounters()
                countersRemoteDataSource.syncCounters(syncBody)
                countersLocalDataSource.synchronizeCounters(countersToBeSynchronizedIds)
                countersLocalDataSource.removeDeletedCounters(deletedCounterIds)
            }
            confirmEverythingVerified()
        }

    @Test
    fun `should do nothing after failing to sync`() = dispatcher.runBlockingTest {
        val counterId = "counterId"
        val deletedCounterId = "deletedCounterId"

        val unsynchronizedCounters = listOf(
            CounterDTO(id = counterId, title = "title1", count = 1, hasBeenDeleted = false),
            CounterDTO(id = deletedCounterId, title = "title2", count = 1, hasBeenDeleted = true)
        )

        val deletedCounterIds = listOf(deletedCounterId)
        val countersToBeSynchronized = listOf(CounterBody(id = counterId, title = "title1", count = 1))

        val syncBody = SyncCountersBody(
            deletedCountersIds = deletedCounterIds,
            counters = countersToBeSynchronized
        )

        coJustRun { countersLocalDataSource.addCount(counterId) }
        coEvery { countersLocalDataSource.getUnsynchronizedCounters() } returns unsynchronizedCounters
        coEvery { countersRemoteDataSource.syncCounters(syncBody) } throws IllegalStateException()

        repository.addCount(counterId)
        advanceTimeBy(5000L)

        coVerify(exactly = 1) {
            countersLocalDataSource.addCount(counterId)
            countersLocalDataSource.getUnsynchronizedCounters()
            countersRemoteDataSource.syncCounters(syncBody)
        }
        confirmEverythingVerified()
    }

    private fun confirmEverythingVerified() {
        confirmVerified(
            countersRemoteDataSource,
            countersLocalDataSource,
            idGenerator
        )
    }
}
