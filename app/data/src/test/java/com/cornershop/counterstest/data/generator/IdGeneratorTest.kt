package com.cornershop.counterstest.data.generator

import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class IdGeneratorTest {

    private val idGenerator: IdGenerator = IdGeneratorImpl()

    @Test
    fun `all ids with same timestamp must be different`() = runBlockingTest {
        val timestamp = 1468418909016L
        val tsAsString = "-KMZ_aCN"

        val ids = (1..100000).map { idGenerator.generateId(instant = timestamp) }

        assertEquals(ids.sorted(), ids)
        assertEquals(ids.map { it.substring(0..7) }, ids.map { tsAsString })
        assertEquals(ids.distinct().size, ids.size)
    }

    @Test
    fun `all ids with same timestamps and different states must be different`() = runBlockingTest {
        val initialState = IdGenerator.State()

        val timestamp = 1468418909016L
        val result = idGenerator.generateNextId(initialState, timestamp)

        assertEquals(result.nextState.lastInstant, timestamp)
        assertNotEquals(result.nextState.lastRandChars, initialState.lastRandChars)
    }
}
