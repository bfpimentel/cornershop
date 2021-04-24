package com.cornershop.counterstest.presentation.examples.mappers

import android.content.Context
import android.content.res.Resources
import com.cornershop.counterstest.R
import com.cornershop.counterstest.presentation.examples.data.ExampleViewData
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ExamplesViewDataMapperTest {

    private val context = mockk<Context>(relaxed = true)
    private val mapper: ExamplesViewDataMapper = ExamplesViewDataMapperImpl(context)

    @Test
    fun `should map resources to view data`() {
        val resources = mockk<Resources>(relaxed = true)

        every { context.getString(R.string.examples_drinks_title) } returns "drinks"
        every { context.getString(R.string.examples_food_title) } returns "food"
        every { context.getString(R.string.examples_misc_title) } returns "misc"

        every { context.resources } returns resources

        every { resources.getStringArray(R.array.examples_drinks_array) } returns arrayOf("drinks1", "drinks2")
        every { resources.getStringArray(R.array.examples_food_array) } returns arrayOf("food1", "food2")
        every { resources.getStringArray(R.array.examples_misc_array) } returns arrayOf("misc1", "misc2")

        val expected = listOf(
            ExampleViewData("drinks", listOf("drinks1", "drinks2")),
            ExampleViewData("food", listOf("food1", "food2")),
            ExampleViewData("misc", listOf("misc1", "misc2")),
        )

        assertEquals(expected, mapper.getExamples())

        coVerify(exactly = 1) {
            context.getString(R.string.examples_drinks_title)
            context.getString(R.string.examples_food_title)
            context.getString(R.string.examples_misc_title)
            resources.getStringArray(R.array.examples_drinks_array)
            resources.getStringArray(R.array.examples_food_array)
            resources.getStringArray(R.array.examples_misc_array)
        }
        coVerify(exactly = 3) {
            context.resources
        }
        confirmVerified(context)
    }
}
