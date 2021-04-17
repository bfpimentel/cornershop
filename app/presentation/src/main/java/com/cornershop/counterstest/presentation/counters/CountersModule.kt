package com.cornershop.counterstest.presentation.counters

import com.cornershop.counterstest.presentation.counters.data.CountersState
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val countersModule = module {
    viewModel {
        CountersViewModel(
            dispatchersProvider = get(),
            initialState = CountersState()
        )
    }
}
