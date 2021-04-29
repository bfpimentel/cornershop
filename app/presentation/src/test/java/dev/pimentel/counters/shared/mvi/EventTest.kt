package dev.pimentel.counters.shared.mvi

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class EventTest {

    @Test
    fun `event value must not be null when first accessing it`() {
        val event = "".toEvent()

        assertNotNull(event.value)
    }

    @Test
    fun `event value must be null when accessing it for the second time`() {
        val event = "".toEvent()

        assertNotNull(event.value)
        assertNull(event.value)
    }

    @Test
    fun `handle event must execute block when first calling it`() {
        val event = "".toEvent()

        var expectedResult: String? = null

        event.handleEvent { expectedResult = it }

        assertNotNull(expectedResult)
    }

    @Test
    fun `handle event must not execute block when calling it for the second time`() {
        val event = "".toEvent()

        var expectedResult: String? = null

        event.handleEvent { expectedResult = it }
        event.handleEvent { expectedResult = null }

        assertNotNull(expectedResult)
    }

    @Test
    fun `null event must not execute any block`() {
        val event: Event<String>? = null

        val nonNullString = ""
        var expectedResult: String? = null

        event.handleEvent { expectedResult = nonNullString }

        assertNull(expectedResult)
    }
}
