package com.cornershop.counterstest.presentation.counters.mapper

import android.content.Context
import com.cornershop.counterstest.R
import com.cornershop.counterstest.domain.entity.Counter
import com.cornershop.counterstest.presentation.counters.mappers.CountersDeletionMapper
import com.cornershop.counterstest.presentation.counters.mappers.CountersDeletionMapperImpl
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CountersDeletionMapperTest {

    private val context = mockk<Context>()
    private val mapper: CountersDeletionMapper = CountersDeletionMapperImpl(context)

    @Test
    fun `should throw exception when list is empty`() {
        assertThrows<IllegalArgumentException> { mapper.map(emptyList()) }

        confirmVerified(context)
    }

    @Test
    fun `should map one item list`() {
        val items = listOf(Counter(id = "id1", "title1", count = 1))

        val text = "Delete title1?"

        every {
            context.getString(
                R.string.counters_delete_confirmation_question_one_item,
                items[0].title
            )
        } returns text

        assertEquals(mapper.map(items), text)

        verify(exactly = 1) {
            context.getString(
                R.string.counters_delete_confirmation_question_one_item,
                items[0].title
            )
        }
        confirmVerified(context)
    }

    @Test
    fun `should map more than one item list`() {
        val items = listOf(
            Counter(id = "id1", "title1", count = 1),
            Counter(id = "id2", "title2", count = 2),
            Counter(id = "id3", "title3", count = 3),
        )

        val text = "Delete title1, title2 and title3?"

        every {
            context.getString(
                R.string.counters_delete_confirmation_question_more_items,
                "${items[0].title}, ${items[1].title}",
                items[2].title
            )
        } returns text

        assertEquals(mapper.map(items), text)

        verify(exactly = 1) {
            context.getString(
                R.string.counters_delete_confirmation_question_more_items,
                "${items[0].title}, ${items[1].title}",
                items[2].title
            )
        }
        confirmVerified(context)
    }
}
