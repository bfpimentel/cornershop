package com.cornershop.counterstest.shared.mvi

import com.cornershop.counterstest.ViewModelTest
import com.cornershop.counterstest.domain.usecase.UseCase
import com.cornershop.counterstest.shared.dispatchers.DispatchersProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.BeforeClass
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class StateViewModelTest : ViewModelTest() {

    private val getStringFromInteger = mockk<GetStringFromInteger>()
    private lateinit var viewModel: GenericContract.ViewModel

    @BeforeEach
    fun `setup subject`() {
        viewModel = GenericViewModel(
            getStringFromInteger = getStringFromInteger,
            dispatchersProvider = dispatchersProvider,
            initialState = ""
        )
    }

    @Test
    fun `should put string`() = runBlockingTest {
        val value = "stringValue"

        viewModel.publish(GenericIntention.PutString(value))
        assertEquals(viewModel.state.value, value)

        confirmVerified(getStringFromInteger)
    }

    @Test
    fun `should put integer`() = runBlockingTest {
        val value = 12345
        val stringValue = "12345"

        coEvery { getStringFromInteger(value) } returns stringValue

        viewModel.publish(GenericIntention.PutInt(value))
        assertEquals(viewModel.state.value, stringValue)

        coVerify(exactly = 1) { getStringFromInteger(value) }
        confirmVerified(getStringFromInteger)
    }

    @Test
    fun `should retry putting int when use case throws error`() = runBlockingTest {
        val value = 12345
        val stringValue = "12345"

        coEvery { getStringFromInteger(value) } throws IllegalArgumentException() andThen stringValue

        viewModel.publish(GenericIntention.PutInt(value))
        assertEquals(viewModel.state.value, "ERROR")

        viewModel.publish(GenericIntention.PutInt(value))
        assertEquals(viewModel.state.value, stringValue)

        coVerify(exactly = 2) { getStringFromInteger(value) }
        confirmVerified(getStringFromInteger)
    }
}

private interface GetStringFromInteger : UseCase<Int, String>

private sealed class GenericIntention {

    data class PutString(val value: String) : GenericIntention()

    data class PutInt(val value: Int) : GenericIntention()
}

private interface GenericContract {

    interface ViewModel : StateViewModel<String, GenericIntention>
}

private class GenericViewModel(
    private val getStringFromInteger: GetStringFromInteger,
    dispatchersProvider: DispatchersProvider,
    initialState: String
) : StateViewModelImpl<String, GenericIntention>(
    dispatchersProvider = dispatchersProvider,
    initialState = initialState
), GenericContract.ViewModel {

    override suspend fun handleIntentions(intention: GenericIntention) {
        when (intention) {
            is GenericIntention.PutInt -> updateState {
                try {
                    getStringFromInteger(intention.value)
                } catch (error: Exception) {
                    "ERROR"
                }
            }
            is GenericIntention.PutString -> updateState { intention.value }
        }
    }
}
