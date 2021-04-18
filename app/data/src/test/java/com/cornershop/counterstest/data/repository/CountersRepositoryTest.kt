package com.cornershop.counterstest.data.repository

import com.cornershop.counterstest.data.model.CounterModelImpl
import com.cornershop.counterstest.data.responses.CounterResponse
import com.cornershop.counterstest.data.sources.remote.CountersRemoteDataSource
import com.cornershop.counterstest.domain.repository.CountersRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CountersRepositoryTest {

    private val remoteDataSource = mockk<CountersRemoteDataSource>()
    private val repository: CountersRepository = CountersRepositoryImpl(remoteDataSource)

    @Test
    fun `should get counters`() = runBlockingTest {
        val countersResponse = listOf(
            CounterResponse(
                id = "id1",
                title = "title1",
                count = 1,
            ),
            CounterResponse(
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

        coEvery { remoteDataSource.getCounters() } returns countersResponse

        assertEquals(
            repository.getCounters(null),
            countersModels
        )

        coVerify(exactly = 1) {
            remoteDataSource.getCounters()
        }
        confirmVerified(remoteDataSource)
    }
}
