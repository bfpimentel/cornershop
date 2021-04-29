package dev.pimentel.counters.presentation.create_counter

import dev.pimentel.counters.presentation.create_counter.data.CreateCounterState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
class CreateCounterViewModelModule {

    @Provides
    @ViewModelScoped
    @CreateCounterStateQualifier
    fun provideInitialState(): CreateCounterState = CreateCounterState()
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CreateCounterStateQualifier
