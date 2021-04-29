package dev.pimentel.counters.presentation.examples

import android.content.Context
import dev.pimentel.counters.presentation.examples.data.ExamplesState
import dev.pimentel.counters.presentation.examples.mappers.ExamplesViewDataMapper
import dev.pimentel.counters.presentation.examples.mappers.ExamplesViewDataMapperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
class ExamplesModules {

    @Provides
    @ViewModelScoped
    fun provideViewDataMapper(@ApplicationContext context: Context): ExamplesViewDataMapper =
        ExamplesViewDataMapperImpl(context = context)

    @Provides
    @ViewModelScoped
    @ExamplesStateQualifier
    fun provideInitialState(): ExamplesState = ExamplesState()
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ExamplesStateQualifier
