package com.kaylakautai.testing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Installs a [StandardTestDispatcher] as [Dispatchers.Main] for one test.
 *
 * `viewModelScope` always uses `Dispatchers.Main.immediate`. Unit tests have no
 * Android main looper, so this rule has to run before the ViewModel is created.
 *
 * [kotlinx.coroutines.test.runTest] reuses the scheduler of [Dispatchers.Main]
 * when it is already a [TestDispatcher]. Create the rule as a `@get:Rule`, then
 * call `runTest { viewModel.uiState.test { ... } }` and both scopes share one
 * virtual clock.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
