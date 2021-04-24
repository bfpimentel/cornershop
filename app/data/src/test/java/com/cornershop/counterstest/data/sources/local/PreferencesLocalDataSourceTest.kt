package com.cornershop.counterstest.data.sources.local

import android.content.SharedPreferences
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PreferencesLocalDataSourceTest {

    private val sharedPreferences = mockk<SharedPreferences>()
    private val dataSource: PreferencesLocalDataSource = PreferencesLocalDataSourceImpl(sharedPreferences)

    @Test
    fun `should return true if there key does not exist and save it as false later`() = runBlockingTest {
        val isFirstAccess = true

        every { sharedPreferences.getBoolean(IS_FIRST_ACCESS_KEY, true) } returns isFirstAccess
        justRun { sharedPreferences.edit().putBoolean(IS_FIRST_ACCESS_KEY, false).apply() }

        assertEquals(dataSource.hasFetchedCounters(), isFirstAccess)

        coVerify(exactly = 1) {
            sharedPreferences.getBoolean(IS_FIRST_ACCESS_KEY, true)
            sharedPreferences.edit().putBoolean(IS_FIRST_ACCESS_KEY, false).apply()
        }
        confirmVerified(sharedPreferences)
    }

    @Test
    fun `should just return false when key exists`() = runBlockingTest {
        val isFirstAccess = false

        coEvery { sharedPreferences.getBoolean(IS_FIRST_ACCESS_KEY, true) } returns isFirstAccess

        assertEquals(dataSource.hasFetchedCounters(), isFirstAccess)

        coVerify(exactly = 1) { sharedPreferences.getBoolean(IS_FIRST_ACCESS_KEY, true) }
        confirmVerified(sharedPreferences)
    }

    private companion object {
        const val IS_FIRST_ACCESS_KEY = "IS_FIRST_ACCESS"
    }
}
