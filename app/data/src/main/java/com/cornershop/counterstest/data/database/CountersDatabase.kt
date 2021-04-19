package com.cornershop.counterstest.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cornershop.counterstest.data.dto.CounterDTO
import com.cornershop.counterstest.data.sources.local.CountersLocalDataSource

@Database(
    entities = [CounterDTO::class],
    version = 1,
    exportSchema = false
)
abstract class CountersDatabase : RoomDatabase() {

    abstract fun createCountersLocalDataSource(): CountersLocalDataSource
}
