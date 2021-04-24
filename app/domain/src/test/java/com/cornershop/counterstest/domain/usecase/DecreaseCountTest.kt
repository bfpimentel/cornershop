package com.cornershop.counterstest.domain.usecase

import com.cornershop.counterstest.domain.repository.CountersRepository
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

class DecreaseCountTest {

    private val countersRepository = mockk<CountersRepository>()
    private val useCase = DecreaseCount(countersRepository)

    @Test
    fun `should decrease count`() = runBlockingTest {
        val counterId = "counterId"

        coJustRun { countersRepository.decreaseCount(counterId) }

        useCase(DecreaseCount.Params(counterId))

        coVerify(exactly = 1) { countersRepository.decreaseCount(counterId) }
        confirmVerified(countersRepository)
    }
}
