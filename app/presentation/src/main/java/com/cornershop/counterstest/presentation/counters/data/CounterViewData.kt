package com.cornershop.counterstest.presentation.counters.data

sealed class CounterViewData {
    abstract val count: Int

    data class Item(
        val id: String,
        val title: String,
        override val count: Int
    ) : CounterViewData()

    data class Edit(
        val id: String,
        val title: String,
        val isSelected: Boolean,
        override val count: Int
    ) : CounterViewData()
}
