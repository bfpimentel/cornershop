package com.cornershop.counterstest.presentation.counters.mappers

import android.content.Context
import com.cornershop.counterstest.R
import com.cornershop.counterstest.domain.entity.Counter

interface CountersDeletionMapper {
    fun map(itemsToBeDeleted: List<Counter>): String
}

class CountersDeletionMapperImpl(private val context: Context) : CountersDeletionMapper {

    override fun map(itemsToBeDeleted: List<Counter>): String =
        when (itemsToBeDeleted.size) {
            0 -> throw IllegalArgumentException("List can't be empty")
            1 -> context.getString(
                R.string.counters_delete_confirmation_question_one_item,
                itemsToBeDeleted.first().toString()
            )
            else -> context.getString(
                R.string.counters_delete_confirmation_question_more_items,
                itemsToBeDeleted.dropLast(1).joinToString { ", " },
                itemsToBeDeleted.last()
            )
        }
}
