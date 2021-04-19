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
        WHERE hasBeenDeleted = '0'
        """
    )
    fun getCounters(): Flow<List<CounterDTO>>

    @Query(
        """
        SELECT id, title, count 
        FROM Counters 
        WHERE title LIKE :filter 
        AND hasBeenDeleted = '0'
        """
    )
    fun getCounters(filter: String): Flow<List<CounterDTO>>

    @Query(
        """
        UPDATE Counters SET
            count = count + 1,
            isSynchronized = '0'
        WHERE id = :counterId
        """
    )
    suspend fun addCount(counterId: String)

    @Query(
        """
        UPDATE Counters SET
            count = count - 1,
            isSynchronized = '0'
        WHERE id = :counterId
        """
    )
    suspend fun subtractCount(counterId: String)

    @Query(
        """
        UPDATE Counters SET
            hasBeenDeleted = '1'
        WHERE id = :counterId 
        """
    )
    suspend fun deleteCounter(counterId: String)

    @Query(
        """
        SELECT id, title, count, hasBeenDeleted
        FROM Counters 
        WHERE isSynchronized = '0'
        """
    )
    suspend fun getUnsynchronizedCounters(): List<CounterDTO>

    @Query(
        """
        UPDATE Counters SET
            isSynchronized = '1'
        WHERE id IN (:counterIds)
        """
    )
    suspend fun synchronizeCounters(counterIds: List<String>)

    @Query(
        """
        DELETE FROM Counters
        WHERE id IN (:deletedCounterIds)
        """
    )
    suspend fun removeDeletedCounters(deletedCounterIds: List<String>)
}
