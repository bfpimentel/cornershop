package com.cornershop.counterstest.data.sources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cornershop.counterstest.data.dto.CounterDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface CountersLocalDataSource {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    @Insert // TODO : Remove comment when ready. OnConflict will not be needed later.
    suspend fun insertCounters(counter: List<CounterDTO>)

    @Query(
        """
        SELECT id, title, count 
        FROM Counters
        """
    )
    fun getCounters(): Flow<List<CounterDTO>>

    @Query(
        """
        SELECT id, title, count 
        FROM Counters 
        WHERE title LIKE :filter
        """
    )
    fun getCounters(filter: String): Flow<List<CounterDTO>>

    @Query(
        """
        UPDATE Counters SET
            count = count + 1,
            isSynchronized = 'false'
        WHERE id = :counterId
        """
    )
    suspend fun addCount(counterId: String)

    @Query(
        """
        UPDATE Counters SET
            count = count - 1,
            isSynchronized = 'false'
        WHERE id = :counterId
        """
    )
    suspend fun subtractCount(counterId: String)

    @Query(
        """
        SELECT id, title, count 
        FROM Counters 
        WHERE isSynchronized = 'false'
        """
    )
    suspend fun getUnsynchronizedCounters(): List<CounterDTO>

    @Query(
        """
        UPDATE Counters SET
            isSynchronized = 'true'
        WHERE id IN (:counterIds)
        """
    )
    suspend fun synchronizeCounters(counterIds: List<String>)
}
