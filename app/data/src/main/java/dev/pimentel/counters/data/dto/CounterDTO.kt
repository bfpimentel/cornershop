package dev.pimentel.counters.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Counters")
data class CounterDTO(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "count") val count: Int,
    @ColumnInfo(name = "isSynchronized") val isSynchronized: Boolean? = true,
    @ColumnInfo(name = "hasBeenDeleted") val hasBeenDeleted: Boolean? = false
) {

    constructor(
        id: String,
        title: String
    ) : this(
        id = id,
        title = title,
        count = 0,
        isSynchronized = false
    )
}
