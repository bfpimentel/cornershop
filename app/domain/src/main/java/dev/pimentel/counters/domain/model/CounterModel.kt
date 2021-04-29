package dev.pimentel.counters.domain.model

interface CounterModel {
    val id: String
    val title: String
    val count: Int
}
