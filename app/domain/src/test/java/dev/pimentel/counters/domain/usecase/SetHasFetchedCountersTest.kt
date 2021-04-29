package dev.pimentel.counters.domain.usecase

import dev.pimentel.counters.domain.repository.PreferencesRepository
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

class SetHasFetchedCountersTest {

    private val repository = mockk<PreferencesRepository>()
    private val useCase = SetHasFetchedCounters(repository)

    @Test
    fun `should just call repository`() = runBlockingTest {
        coJustRun { repository.setHasFetchedCounters() }

        useCase(NoParams)

        coVerify(exactly = 1) { repository.setHasFetchedCounters() }
        confirmVerified(repository)
    }
}
