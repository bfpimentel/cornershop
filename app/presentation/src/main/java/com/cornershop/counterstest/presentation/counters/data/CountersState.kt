package com.cornershop.counterstest.presentation.counters.data

import com.cornershop.counterstest.shared.mvi.Event
import com.cornershop.counterstest.shared.mvi.toEvent

data class CountersState(
    val countersEvent: Event<List<CounterViewData>>? = null,
    val totalItemCount: Int = 0,
    val totalTimesCount: Int = 0,
    val numberOfSelectedCounters: Int = 0,
    val areMenusEnabled: Boolean = false,
    val layoutEvent: Event<Layout> = Layout.Default.toEvent(),
    val deleteConfirmationEvent: Event<String>? = null,
    val shareEvent: Event<String>? = null
) {

    sealed class Layout(
        val isSearchInputVisible: Boolean,
        val isToolbarVisible: Boolean
    ) {

        object Default : Layout(isSearchInputVisible = true, isToolbarVisible = false)

        object Editing : Layout(isSearchInputVisible = false, isToolbarVisible = true)
    }
}
