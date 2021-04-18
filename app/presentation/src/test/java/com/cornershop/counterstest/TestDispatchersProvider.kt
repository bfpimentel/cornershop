package com.cornershop.counterstest

import com.cornershop.counterstest.shared.dispatchers.DispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher

class TestDispatchersProvider(testCoroutineDispatcher: TestCoroutineDispatcher) : DispatchersProvider {

    override val ui: CoroutineDispatcher = testCoroutineDispatcher
    override val io: CoroutineDispatcher = testCoroutineDispatcher
}
