package com.cornershop.counterstest.data.repository

import com.cornershop.counterstest.data.dto.CounterDTO
import com.cornershop.counterstest.data.model.CounterModelImpl
import com.cornershop.counterstest.data.body.CounterBody
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
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CountersRepositoryTest {

    private val remoteDataSource = mockk<CountersRemoteDataSource>()
    private val localDataSource = mockk<CountersLocalDataSource>()

    private val repository: CountersRepository = CountersRepositoryImpl(
        remoteDataSource,
        localDataSource
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
}
