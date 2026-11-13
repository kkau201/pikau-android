package com.kaylakautai.profile

import app.cash.turbine.test
import com.kaylakautai.data.testing.FakePikauLogger
import com.kaylakautai.data.testing.FakeTripRepository
import com.kaylakautai.model.testTrip
import com.kaylakautai.testing.MainDispatcherRule
import com.kaylakautai.ui.lce.DisplayError
import com.kaylakautai.ui.lce.UiState
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneOffset
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val today: LocalDate = LocalDate.of(2026, 6, 15)
    private val clock: Clock = Clock.fixed(
        today.atStartOfDay(ZoneOffset.UTC).toInstant(),
        ZoneOffset.UTC,
    )
    private val repository = FakeTripRepository()
    private val logger = FakePikauLogger()

    @Test
    fun `trips completed counts past trips only`() = runTest {
        repository.emit(
            listOf(
                testTrip(id = "past-1", startDate = today.minusDays(10), endDate = today.minusDays(8)),
                testTrip(id = "past-2", startDate = today.minusDays(3), endDate = today.minusDays(1)),
                testTrip(id = "next", startDate = today.plusDays(2), endDate = today.plusDays(4)),
                testTrip(id = "today", startDate = today, endDate = today),
            )
        )
        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(2, awaitItem().content().tripsCompleted)
        }
    }

    @Test
    fun `retry shows loading then recovers after the repository stops failing`() = runTest {
        repository.failure = IllegalStateException("database closed")
        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            val error = awaitItem().error()
            assertEquals(DisplayError.Generic, error.error)
            assertEquals("database closed", error.cause?.message)

            repository.failure = null
            repository.emit(
                listOf(testTrip(startDate = today.minusDays(2), endDate = today.minusDays(1)))
            )
            viewModel.onRetryClick()

            assertEquals(UiState.Loading, awaitItem())
            assertEquals(1, awaitItem().content().tripsCompleted)
        }
    }

    @Test
    fun `profile actions are logged`() {
        val viewModel = viewModel()

        viewModel.onEditProfileClick()
        viewModel.onLinkClick(ProfileLink.OpenSource)

        assertEquals(
            listOf(
                "PROFILE_SCREEN" to "onEditProfileClick() triggered",
                "PROFILE_SCREEN" to "onLinkClick(OpenSource) triggered",
            ),
            logger.info,
        )
    }

    private fun viewModel() = ProfileViewModel(
        tripRepository = repository,
        clock = clock,
        logger = logger,
    )
}

private fun UiState<ProfileScreenData>.content(): ProfileScreenData {
    assertTrue("Expected content but was $this", this is UiState.Content)
    return (this as UiState.Content).data
}

private fun UiState<ProfileScreenData>.error(): UiState.Error {
    assertTrue("Expected error but was $this", this is UiState.Error)
    return this as UiState.Error
}
