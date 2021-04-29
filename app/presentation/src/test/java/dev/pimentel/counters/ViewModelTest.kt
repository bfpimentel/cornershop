package dev.pimentel.counters

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.pimentel.counters.shared.dispatchers.DispatchersProvider
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
    private val coroutineDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
    protected val dispatchersProvider: DispatchersProvider = TestDispatchersProvider(coroutineDispatcher)

    @BeforeEach
    fun `setup dispatcher and subject`() {
        Dispatchers.setMain(coroutineDispatcher)
    }

    @AfterEach
    fun `tear down`() {
        Dispatchers.resetMain()
        coroutineDispatcher.cleanupTestCoroutines()
    }

    protected fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) =
        coroutineDispatcher.runBlockingTest(block)
}
