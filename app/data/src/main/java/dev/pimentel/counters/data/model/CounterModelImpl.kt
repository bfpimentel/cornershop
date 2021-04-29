package dev.pimentel.counters.data.model

import dev.pimentel.counters.domain.model.CounterModel

data class CounterModelImpl(
    override val id: String,
    override val title: String,
    override val count: Int
) : CounterModel
