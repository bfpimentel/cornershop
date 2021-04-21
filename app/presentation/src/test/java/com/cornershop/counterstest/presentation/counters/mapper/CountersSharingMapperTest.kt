package com.cornershop.counterstest.presentation.counters.mapper

import android.content.Context
import com.cornershop.counterstest.R
import com.cornershop.counterstest.domain.entity.Counter
import com.cornershop.counterstest.presentation.counters.mappers.CountersSharingMapper
import com.cornershop.counterstest.presentation.counters.mappers.CountersSharingMapperImpl
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CountersSharingMapperTest {

    private val context = mockk<Context>()
    private val mapper: CountersSharingMapper = CountersSharingMapperImpl(context)

    @Test
    fun `should throw exception when list is empty`() {
        assertThrows<IllegalArgumentException> { mapper.map(emptyList()) }

        confirmVerified(context)
    }

    @Test
    fun `should map items for sharing`() {
        val items = listOf(
            Counter(id = "id1", "title1", count = 1),
            Counter(id = "id2", "title2", count = 2),
            Counter(id = "id3", "title3", count = 3),
        )

        val counter1Text = "1 x title1"
        val counter2Text = "2 x title2"
        val counter3Text = "3 x title3"

        val text = "$counter1Text\n$counter2Text\n$counter3Text"

        every { context.getString(R.string.counters_share_item, 1, "title1") } returns counter1Text
        every { context.getString(R.string.counters_share_item, 2, "title2") } returns counter2Text
        every { context.getString(R.string.counters_share_item, 3, "title3") } returns counter3Text

        assertEquals(mapper.map(items), text)

        verify(exactly = 1) {
            context.getString(R.string.counters_share_item, 1, "title1")
            context.getString(R.string.counters_share_item, 2, "title2")
            context.getString(R.string.counters_share_item, 3, "title3")
        }
        confirmVerified(context)
    }
}
