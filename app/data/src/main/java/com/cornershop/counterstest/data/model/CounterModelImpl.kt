package com.cornershop.counterstest.data.model

import com.cornershop.counterstest.domain.model.CounterModel

data class CounterModelImpl(
    override val id: String,
    override val title: String,
    override val count: Int
) : CounterModel
