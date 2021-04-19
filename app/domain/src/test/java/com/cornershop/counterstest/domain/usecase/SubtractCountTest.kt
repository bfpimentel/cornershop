package com.cornershop.counterstest.domain.usecase

import com.cornershop.counterstest.domain.repository.CountersRepository
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

class SubtractCountTest {

    private val countersRepository = mockk<CountersRepository>()
    private val useCase = SubtractCount(countersRepository)

    @Test
    fun `should search counters`() = runBlockingTest {
        val counterId = "counterId"

        coJustRun { countersRepository.subtractCount(counterId) }

        useCase(SubtractCount.Params(counterId))

        coVerify(exactly = 1) { countersRepository.subtractCount(counterId) }
        confirmVerified(countersRepository)
    }
}
