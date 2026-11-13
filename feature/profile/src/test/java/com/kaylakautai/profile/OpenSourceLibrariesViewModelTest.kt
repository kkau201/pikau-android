package com.kaylakautai.profile

import app.cash.turbine.test
import com.kaylakautai.testing.MainDispatcherRule
import com.kaylakautai.ui.lce.DisplayError
import com.kaylakautai.ui.lce.UiState
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class OpenSourceLibrariesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val library = OpenSourceLibrary(
        name = "Kotlin",
        detail = "org.jetbrains.kotlin",
        version = "2.4.20",
        license = "Apache-2.0",
        licenseUrl = "https://www.apache.org/licenses/LICENSE-2.0",
    )
    private val source = FakeOpenSourceLibrariesSource()

    @Test
    fun `loads libraries from the source`() = runTest {
        source.libraries = listOf(library)
        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(listOf(library), awaitItem().content().libraries)
            assertEquals(1, source.readCount)
        }
    }

    @Test
    fun `retry reads the source again and recovers from a failure`() = runTest {
        source.failure = IllegalStateException("bad json")
        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            val error = awaitItem().error()
            assertEquals(DisplayError.Generic, error.error)
            assertEquals("bad json", error.cause?.message)
            assertEquals(1, source.readCount)

            source.failure = null
            source.libraries = listOf(library)
            viewModel.onRetryClick()

            assertEquals(UiState.Loading, awaitItem())
            assertEquals(listOf(library), awaitItem().content().libraries)
            assertEquals(2, source.readCount)
        }
    }

    private fun viewModel() = OpenSourceLibrariesViewModel(
        source = source,
        ioDispatcher = mainDispatcherRule.testDispatcher,
    )
}

private class FakeOpenSourceLibrariesSource : OpenSourceLibrariesSource {
    var libraries: List<OpenSourceLibrary> = emptyList()
    var failure: Throwable? = null
    var readCount: Int = 0

    override fun read(): List<OpenSourceLibrary> {
        readCount += 1
        failure?.let { throw it }
        return libraries
    }
}

private fun UiState<OpenSourceLibrariesScreenData>.content(): OpenSourceLibrariesScreenData {
    assertTrue("Expected content but was $this", this is UiState.Content)
    return (this as UiState.Content).data
}

private fun UiState<OpenSourceLibrariesScreenData>.error(): UiState.Error {
    assertTrue("Expected error but was $this", this is UiState.Error)
    return this as UiState.Error
}
