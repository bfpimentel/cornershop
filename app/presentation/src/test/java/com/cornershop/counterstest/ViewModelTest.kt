package com.cornershop.counterstest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cornershop.counterstest.shared.dispatchers.DispatchersProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class ViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var coroutineDispatcher: TestCoroutineDispatcher
    private lateinit var dispatchersProvider: DispatchersProvider

    abstract fun `setup subject`(dispatchersProvider: DispatchersProvider)

    @BeforeEach
    fun `setup subject`() {
        coroutineDispatcher = TestCoroutineDispatcher()
        dispatchersProvider = TestDispatchersProvider(coroutineDispatcher)

        Dispatchers.setMain(coroutineDispatcher)

        `setup subject`(dispatchersProvider)
    }

    @AfterEach
    fun `tear down`() {
        Dispatchers.resetMain()
        coroutineDispatcher.cleanupTestCoroutines()
    }

    protected fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) =
        coroutineDispatcher.runBlockingTest(block)
}
