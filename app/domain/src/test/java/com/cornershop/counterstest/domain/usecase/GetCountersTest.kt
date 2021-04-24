package com.cornershop.counterstest.domain.usecase

import com.cornershop.counterstest.domain.entity.Counter
import com.cornershop.counterstest.domain.model.CounterModel
import com.cornershop.counterstest.domain.repository.CountersRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetCountersTest {

    private val countersRepository = mockk<CountersRepository>()
    private val useCase = GetCounters(countersRepository)

    @Test
    fun `should get counters and map them to entity`() = runBlockingTest {
        val countersModels = listOf(
            object : CounterModel {
                override val id: String = "id1"
                override val title: String = "title1"
                override val count: Int = 1
            },
            object : CounterModel {
                override val id: String = "id2"
                override val title: String = "title2"
                override val count: Int = 2
            }
        )

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

        coEvery { countersRepository.getCounters() } returns flowOf(countersModels)

        assertEquals(useCase(NoParams).first(), counters)

        coVerify(exactly = 1) { countersRepository.getCounters() }
        confirmVerified(countersRepository)
    }
}
