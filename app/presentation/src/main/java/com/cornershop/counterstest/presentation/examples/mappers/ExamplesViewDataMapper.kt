package com.cornershop.counterstest.presentation.examples.mappers

import android.content.Context
import com.cornershop.counterstest.R
import com.cornershop.counterstest.presentation.examples.data.ExampleViewData

interface ExamplesViewDataMapper {
    fun getExamples(): List<ExampleViewData>
}

class ExamplesViewDataMapperImpl(private val context: Context) : ExamplesViewDataMapper {

    override fun getExamples(): List<ExampleViewData> = examples.map { example ->
        ExampleViewData(
            title = context.getString(example.key),
            examples = context.resources.getStringArray(example.value).toList()
        )
    }

    private companion object {
        val examples = mapOf(
            R.string.examples_drinks_title to R.array.examples_drinks_array,
            R.string.examples_food_title to R.array.examples_food_array,
            R.string.examples_misc_title to R.array.examples_misc_array
        )
    }
}
