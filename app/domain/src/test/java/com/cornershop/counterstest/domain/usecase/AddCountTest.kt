package com.cornershop.counterstest.domain.usecase

import com.cornershop.counterstest.domain.repository.CountersRepository
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

class AddCountTest {

    private val countersRepository = mockk<CountersRepository>()
    private val useCase = AddCount(countersRepository)

    @Test
    fun `should search counters`() = runBlockingTest {
        val counterId = "counterId"

        coJustRun { countersRepository.addCount(counterId) }

        useCase(AddCount.Params(counterId))

        coVerify(exactly = 1) { countersRepository.addCount(counterId) }
        confirmVerified(countersRepository)
    }
}
