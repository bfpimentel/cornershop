package dev.pimentel.counters.presentation.counters.data

import dev.pimentel.counters.shared.mvi.Event
import dev.pimentel.counters.shared.mvi.toEvent

data class CountersState(
    val countersEvent: Event<List<CounterViewData>>? = null,
    val totalItemCount: Int = 0,
    val totalTimesCount: Int = 0,
    val numberOfSelectedCounters: Int = 0,
    val areMenusEnabled: Boolean = false,
    val isSearchEnabled: Boolean = false,
    val topLayoutEvent: Event<TopLayout> = TopLayout.Default.toEvent(),
    val mainLayoutEvent: Event<MainLayout> = MainLayout.Default.toEvent(),
    val deleteConfirmationEvent: Event<String>? = null,
    val shareEvent: Event<String>? = null
) {

    enum class TopLayout(
        val isSearchInputVisible: Boolean,
        val isToolbarVisible: Boolean
    ) {
        Default(isSearchInputVisible = true, isToolbarVisible = false),
        Editing(isSearchInputVisible = false, isToolbarVisible = true),
    }

    enum class MainLayout(
        val isDefaultContainerVisible: Boolean,
        val isNoCountersVisible: Boolean,
        val isNoResultsVisible: Boolean
    ) {
        Default(isDefaultContainerVisible = true, isNoCountersVisible = false, isNoResultsVisible = false),
        NoCounters(isDefaultContainerVisible = false, isNoCountersVisible = true, isNoResultsVisible = false),
        NoResults(isDefaultContainerVisible = false, isNoCountersVisible = false, isNoResultsVisible = true),
    }
}
