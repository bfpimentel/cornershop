package dev.pimentel.counters.data.sources.local

import android.content.SharedPreferences
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PreferencesLocalDataSourceTest {

    private val sharedPreferences = mockk<SharedPreferences>(relaxed = true)
    private val dataSource: PreferencesLocalDataSource = PreferencesLocalDataSourceImpl(sharedPreferences)

    @Test
    fun `should return shared preferences value when asking if has fetched counters`() = runBlockingTest {
        val hasFetchedCounters = true

        every { sharedPreferences.getBoolean(HAS_FETCHED_COUNTERS_KEY, false) } returns hasFetchedCounters

        assertEquals(dataSource.hasFetchedCounters(), hasFetchedCounters)

        coVerify(exactly = 1) { sharedPreferences.getBoolean(HAS_FETCHED_COUNTERS_KEY, false) }
        confirmVerified(sharedPreferences)
    }

    @Test
    fun `should put true value on fetched key`() = runBlockingTest {
        val editor = mockk<SharedPreferences.Editor>()

        every { sharedPreferences.edit() } returns editor
        every { editor.putBoolean(HAS_FETCHED_COUNTERS_KEY, true) } returns editor
        justRun { editor.apply() }

        dataSource.setHasFetchedCounters()

        coVerify(exactly = 1) {
            sharedPreferences.edit()
            editor.putBoolean(HAS_FETCHED_COUNTERS_KEY, true)
            editor.apply()
        }
        confirmVerified(sharedPreferences, editor)
    }

    private companion object {
        const val HAS_FETCHED_COUNTERS_KEY = "HAS_FETCHED_COUNTERS"
    }
}
