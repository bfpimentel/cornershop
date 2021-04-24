package com.cornershop.counterstest.presentation.examples

import android.content.Context
import com.cornershop.counterstest.presentation.examples.data.ExamplesState
import com.cornershop.counterstest.presentation.examples.mappers.ExamplesViewDataMapper
import com.cornershop.counterstest.presentation.examples.mappers.ExamplesViewDataMapperImpl
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
