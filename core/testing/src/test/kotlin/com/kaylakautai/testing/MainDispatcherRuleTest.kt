package com.kaylakautai.testing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MainDispatcherRuleTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `runTest shares the scheduler installed as Dispatchers Main`() = runTest {
        var ranOnMain = false
        val job = launch(Dispatchers.Main) {
            ranOnMain = true
        }
        job.join()
        assertTrue(ranOnMain)
    }
}
