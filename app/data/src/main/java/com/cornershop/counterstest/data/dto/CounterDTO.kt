package com.cornershop.counterstest.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Counters")
data class CounterDTO(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "count") val count: Int,
    @ColumnInfo(name = "isSynchronized") val isSynchronized: Boolean? = true
)
