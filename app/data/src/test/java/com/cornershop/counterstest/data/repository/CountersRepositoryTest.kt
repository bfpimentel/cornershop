package com.cornershop.counterstest.data.repository

import com.cornershop.counterstest.data.body.CounterBody
import com.cornershop.counterstest.data.body.SyncCountersBody
import com.cornershop.counterstest.data.dto.CounterDTO
import com.cornershop.counterstest.data.model.CounterModelImpl
import com.cornershop.counterstest.data.sources.local.CountersLocalDataSource
import com.cornershop.counterstest.data.sources.remote.CountersRemoteDataSource
import com.cornershop.counterstest.domain.repository.CountersRepository
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CountersRepositoryTest {

    private val remoteDataSource = mockk<CountersRemoteDataSource>()
    private val localDataSource = mockk<CountersLocalDataSource>()
    private val dispatcher = TestCoroutineDispatcher()
    private val scope = TestCoroutineScope(dispatcher)

    private val repository: CountersRepository = CountersRepositoryImpl(
        remoteDataSource = remoteDataSource,
        localDataSource = localDataSource,
        externalScope = scope
    )

    @Test
    fun `should get counters`() = runBlockingTest {
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

        coEvery { remoteDataSource.getCounters() } returns remoteCounters
        coJustRun { localDataSource.insertCounters(localCounters) }
        coEvery { localDataSource.getCounters() } returns flowOf(localCounters)

        assertEquals(repository.getCounters(null).first(), countersModels)

        coVerify(exactly = 1) {
            remoteDataSource.getCounters()
            localDataSource.insertCounters(localCounters)
            localDataSource.getCounters()
        }
        confirmVerified(remoteDataSource, localDataSource)
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

        coJustRun { localDataSource.addCount(counterId) }
        coEvery { localDataSource.getUnsynchronizedCounters() } returns unsynchronizedCounters
        coJustRun { remoteDataSource.syncCounters(syncBody) }
        coJustRun { localDataSource.synchronizeCounters(countersToBeSynchronizedIds) }
        coJustRun { localDataSource.removeDeletedCounters(deletedCounterIds) }

        repository.addCount(counterId)
        advanceTimeBy(5000L)

        coVerify(exactly = 1) {
            localDataSource.addCount(counterId)
            localDataSource.getUnsynchronizedCounters()
            remoteDataSource.syncCounters(syncBody)
            localDataSource.synchronizeCounters(countersToBeSynchronizedIds)
            localDataSource.removeDeletedCounters(deletedCounterIds)
        }
        confirmVerified(remoteDataSource, localDataSource)
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

        coJustRun { localDataSource.subtractCount(counterId) }
        coEvery { localDataSource.getUnsynchronizedCounters() } returns unsynchronizedCounters
        coJustRun { remoteDataSource.syncCounters(syncBody) }
        coJustRun { localDataSource.synchronizeCounters(countersToBeSynchronizedIds) }
        coJustRun { localDataSource.removeDeletedCounters(deletedCounterIds) }

        repository.subtractCount(counterId)
        advanceTimeBy(5000L)

        coVerify(exactly = 1) {
            localDataSource.subtractCount(counterId)
            localDataSource.getUnsynchronizedCounters()
            remoteDataSource.syncCounters(syncBody)
            localDataSource.synchronizeCounters(countersToBeSynchronizedIds)
            localDataSource.removeDeletedCounters(deletedCounterIds)
        }
        confirmVerified(remoteDataSource, localDataSource)
    }

    @Test
    fun `should delete counter and sync`() = dispatcher.runBlockingTest {
        val deletedCounterId = "deletedCounterId"

        val unsynchronizedCounters = listOf(
            CounterDTO(id = deletedCounterId, title = "title1", count = 1, hasBeenDeleted = true),
            CounterDTO(id = "id2", title = "title2", count = 1, hasBeenDeleted = false)
        )

        val deletedCounterIds = listOf(deletedCounterId)
        val countersToBeSynchronized = listOf(CounterBody(id = "id2", title = "title2", count = 1))

        val syncBody = SyncCountersBody(
            deletedCountersIds = deletedCounterIds,
            counters = countersToBeSynchronized
        )

        val countersToBeSynchronizedIds = listOf("id2")

        coJustRun { localDataSource.deleteCounter(deletedCounterId) }
        coEvery { localDataSource.getUnsynchronizedCounters() } returns unsynchronizedCounters
        coJustRun { remoteDataSource.syncCounters(syncBody) }
        coJustRun { localDataSource.synchronizeCounters(countersToBeSynchronizedIds) }
        coJustRun { localDataSource.removeDeletedCounters(deletedCounterIds) }

        repository.deleteCounter(deletedCounterId)
        advanceTimeBy(5000L)

        coVerify(exactly = 1) {
            localDataSource.deleteCounter(deletedCounterId)
            localDataSource.getUnsynchronizedCounters()
            remoteDataSource.syncCounters(syncBody)
            localDataSource.synchronizeCounters(countersToBeSynchronizedIds)
            localDataSource.removeDeletedCounters(deletedCounterIds)
        }
        confirmVerified(remoteDataSource, localDataSource)
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

        coJustRun { localDataSource.addCount(counterId) }
        coEvery { localDataSource.getUnsynchronizedCounters() } returns unsynchronizedCounters
        coJustRun { remoteDataSource.syncCounters(syncBody) }
        coJustRun { localDataSource.synchronizeCounters(countersToBeSynchronizedIds) }
        coJustRun { localDataSource.removeDeletedCounters(deletedCounterIds) }

        repeat((1..additions).count()) { repository.addCount(counterId) }
        advanceTimeBy(5000L)

        coVerify(exactly = additions) {
            localDataSource.addCount(counterId)
        }
        coVerify(exactly = 1) {
            localDataSource.getUnsynchronizedCounters()
            remoteDataSource.syncCounters(syncBody)
            localDataSource.synchronizeCounters(countersToBeSynchronizedIds)
            localDataSource.removeDeletedCounters(deletedCounterIds)
        }
        confirmVerified(remoteDataSource, localDataSource)
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

            coJustRun { localDataSource.addCount(counterId) }
            coEvery { localDataSource.getUnsynchronizedCounters() } returns unsynchronizedCounters
            coJustRun { remoteDataSource.syncCounters(syncBody) }
            coJustRun { localDataSource.synchronizeCounters(countersToBeSynchronizedIds) }
            coJustRun { localDataSource.removeDeletedCounters(deletedCounterIds) }

            repeat((1..additions).count()) { repository.addCount(counterId) }
            advanceTimeBy(5000L)
            repository.addCount(counterId)
            advanceTimeBy(5000L)

            coVerify(exactly = additions + 1) {
                localDataSource.addCount(counterId)
            }
            coVerify(exactly = 2) {
                localDataSource.getUnsynchronizedCounters()
                remoteDataSource.syncCounters(syncBody)
                localDataSource.synchronizeCounters(countersToBeSynchronizedIds)
                localDataSource.removeDeletedCounters(deletedCounterIds)
            }
            confirmVerified(remoteDataSource, localDataSource)
        }
}
