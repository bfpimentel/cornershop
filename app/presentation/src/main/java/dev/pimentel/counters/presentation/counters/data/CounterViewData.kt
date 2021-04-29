package dev.pimentel.counters.presentation.counters.data

sealed class CounterViewData {
    abstract val id: String
    abstract val title: String
    abstract val count: Int

    data class Counter(
        override val id: String,
        override val title: String,
        override val count: Int
    ) : CounterViewData() {

        val canDecrease: Boolean
            get() = count > 0

        companion object {
            const val IDENTIFIER = 0
        }
    }

    data class Edit(
        override val id: String,
        override val title: String,
        override val count: Int,
        val isSelected: Boolean
    ) : CounterViewData() {

        companion object {
            const val IDENTIFIER = 1
        }
    }
}
