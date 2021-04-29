package dev.pimentel.counters.presentation.counters.mappers

import android.content.Context
import dev.pimentel.counters.R
import dev.pimentel.counters.domain.entity.Counter

interface CountersSharingMapper {
    fun map(itemsToBeShared: List<Counter>): String
}

class CountersSharingMapperImpl(private val context: Context) : CountersSharingMapper {

    override fun map(itemsToBeShared: List<Counter>): String =
        if (itemsToBeShared.isEmpty()) throw IllegalArgumentException("List can't be empty")
        else itemsToBeShared.joinToString(separator = "\n") { counter ->
            context.getString(R.string.counters_share_item, counter.count, counter.title)
        }
}
