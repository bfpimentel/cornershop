package com.cornershop.counterstest.data.repository

import android.util.Log
import com.cornershop.counterstest.data.dto.CounterDTO
import com.cornershop.counterstest.data.model.CounterModelImpl
import com.cornershop.counterstest.data.sources.local.CountersLocalDataSource
import com.cornershop.counterstest.data.sources.remote.CountersRemoteDataSource
import com.cornershop.counterstest.domain.model.CounterModel
import com.cornershop.counterstest.domain.repository.CountersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
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

    private val _syncFlow = MutableSharedFlow<Unit>(replay = 0)

    private var isFirstAccess = true // TODO: Remove after persisting user information

    init {
        externalScope.launch {
            _syncFlow.debounce(5000L).collect {
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
        _syncFlow.emit(Unit)
    }

    override suspend fun subtractCount(counterId: String) {
        localDataSource.subtractCount(counterId)
        _syncFlow.emit(Unit)
    }

    private suspend fun synchronizeCounters() {
        val unsynchronizedCounters = localDataSource.getUnsynchronizedCounters()
        Log.d("SYNC", "STARTED SYNCING: $unsynchronizedCounters")
        delay(1000L)
        localDataSource.synchronizeCounters(counterIds = unsynchronizedCounters.map(CounterDTO::id))
        Log.d("SYNC", "FINISHED SYNCING")
    }
}
