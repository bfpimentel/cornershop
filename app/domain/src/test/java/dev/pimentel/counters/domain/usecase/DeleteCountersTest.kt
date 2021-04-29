package dev.pimentel.counters.domain.usecase

import dev.pimentel.counters.domain.repository.CountersRepository
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

class DeleteCountersTest {

    private val countersRepository = mockk<CountersRepository>()
    private val useCase = DeleteCounters(countersRepository)

    @Test
    fun `should delete counters`() = runBlockingTest {
        val countersToBeDeletedIds = listOf("id")

        coJustRun { countersRepository.deleteCounters(countersToBeDeletedIds) }

        useCase(DeleteCounters.Params(countersToBeDeletedIds = countersToBeDeletedIds))

        coVerify(exactly = 1) { countersRepository.deleteCounters(countersToBeDeletedIds) }
        confirmVerified(countersRepository)
    }
}
