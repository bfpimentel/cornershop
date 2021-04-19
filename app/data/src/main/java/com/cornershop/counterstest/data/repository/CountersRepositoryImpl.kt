package com.cornershop.counterstest.data.repository

import android.util.Log
import com.cornershop.counterstest.data.body.CounterBody
import com.cornershop.counterstest.data.body.SyncCountersBody
import com.cornershop.counterstest.data.dto.CounterDTO
import com.cornershop.counterstest.data.model.CounterModelImpl
import com.cornershop.counterstest.data.sources.local.CountersLocalDataSource
import com.cornershop.counterstest.data.sources.remote.CountersRemoteDataSource
import com.cornershop.counterstest.domain.model.CounterModel
import com.cornershop.counterstest.domain.repository.CountersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CountersRepositoryImpl(
    private val remoteDataSource: CountersRemoteDataSource,
    private val localDataSource: CountersLocalDataSource,
    externalScope: CoroutineScope = CoroutineScope(SupervisorJob())
) : CountersRepository {

    private val syncFlow = MutableSharedFlow<Unit>(replay = 0)

    private var isFirstAccess = true // TODO: Remove after persisting user information

    init {
        externalScope.launch {
            syncFlow.debounce(5000L).collect {
                synchronizeCounters()
            }
        }
    }

    override fun getCounters(filter: String?): Flow<List<CounterModel>> =
        localDataSource.getCounters()
            .map {
                if (isFirstAccess) {
                    remoteDataSource.getCounters()
                        .map { counterResponse ->
                            CounterDTO(
                                id = counterResponse.id,
                                count = counterResponse.count,
                                title = counterResponse.title
                            )
                        }
                        .run { localDataSource.insertCounters(this) }
                    isFirstAccess = false
                }
                it
            }
            .distinctUntilChanged()
            .map { response ->
                response.map {
                    CounterModelImpl(
                        id = it.id,
                        title = it.title,
                        count = it.count
                    )
                }
            }

    override suspend fun addCount(counterId: String) {
        localDataSource.addCount(counterId)
        syncFlow.emit(Unit)
    }

    override suspend fun subtractCount(counterId: String) {
        localDataSource.subtractCount(counterId)
        syncFlow.emit(Unit)
    }

    override suspend fun deleteCounter(counterId: String) {
        localDataSource.deleteCounter(counterId)
        syncFlow.emit(Unit)
    }

    private suspend fun synchronizeCounters() {
        try {
            val unsynchronizedCounters = localDataSource.getUnsynchronizedCounters()

            val deletedCountersIds = unsynchronizedCounters
                .filter { it.hasBeenDeleted == true }
                .map(CounterDTO::id)

            val countersToBeSynchronized = unsynchronizedCounters
                .filter { it.hasBeenDeleted == false }
                .map { counter ->
                    CounterBody(
                        id = counter.id,
                        title = counter.title,
                        count = counter.count
                    )
                }

            remoteDataSource.syncCounters(
                SyncCountersBody(
                    deletedCountersIds = deletedCountersIds,
                    counters = countersToBeSynchronized
                )
            )

            localDataSource.synchronizeCounters(counterIds = countersToBeSynchronized.map(CounterBody::id))
            localDataSource.removeDeletedCounters(deletedCountersIds)
        } catch (error: Exception) {
            Log.d("SYNC_ERROR", "Couldn't sync database", error)
        }
    }
}
