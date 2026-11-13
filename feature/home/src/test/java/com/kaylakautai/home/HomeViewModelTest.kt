package com.kaylakautai.home

import app.cash.turbine.test
import com.kaylakautai.data.testing.FakeTripRepository
import com.kaylakautai.model.testTrip
import com.kaylakautai.testing.MainDispatcherRule
import com.kaylakautai.ui.lce.DisplayError
import com.kaylakautai.ui.lce.UiState
import com.kaylakautai.ui.util.formatDateRangeToDisplay
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Locale
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val today: LocalDate = LocalDate.of(2026, 6, 15)
    private val clock: Clock = Clock.fixed(
        today.atStartOfDay(ZoneOffset.UTC).toInstant(),
        ZoneOffset.UTC,
    )
    private val repository = FakeTripRepository()
    private lateinit var defaultLocale: Locale

    private val later = testTrip(
        id = "later",
        name = "Kepler",
        location = "Te Anau",
        startDate = today.plusDays(20),
        endDate = today.plusDays(23),
    )
    private val next = testTrip(
        id = "next",
        name = "Mt Taranaki",
        location = "Taranaki",
        startDate = today.plusDays(3),
        endDate = today.plusDays(5),
    )

    @Before
    fun setUp() {
        defaultLocale = Locale.getDefault()
        Locale.setDefault(Locale.US)
    }

    @After
    fun tearDown() {
        Locale.setDefault(defaultLocale)
    }

    @Test
    fun `shows the soonest upcoming trip`() = runTest {
        repository.emit(listOf(later, next))
        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            val trip = awaitItem().content().upcomingTrip
            assertEquals("next", trip?.id)
            assertEquals("Mt Taranaki", trip?.name)
            assertEquals("Taranaki", trip?.location)
            assertEquals(formatDateRangeToDisplay(next.startDate, next.endDate), trip?.dates)
        }
    }

    @Test
    fun `empty repository is content with no upcoming trip`() = runTest {
        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            assertNull(awaitItem().content().upcomingTrip)
        }
    }

    @Test
    fun `retry shows loading then recovers after the repository stops failing`() = runTest {
        repository.failure = IllegalStateException("database closed")
        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            val error = awaitItem().error()
            assertEquals(DisplayError.NoTripsFound, error.error)
            assertEquals("database closed", error.cause?.message)

            repository.failure = null
            repository.emit(listOf(next))
            viewModel.onRetryClick()

            assertEquals(UiState.Loading, awaitItem())
            assertEquals("Mt Taranaki", awaitItem().content().upcomingTrip?.name)
        }
    }

    private fun viewModel() = HomeViewModel(
        tripRepository = repository,
        clock = clock,
    )

    private fun UiState<HomeScreenData>.content(): HomeScreenData {
        return (this as UiState.Content).data
    }

    private fun UiState<HomeScreenData>.error(): UiState.Error {
        return this as UiState.Error
    }
}
