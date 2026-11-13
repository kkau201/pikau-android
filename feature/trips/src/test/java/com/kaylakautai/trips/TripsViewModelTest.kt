package com.kaylakautai.trips

import app.cash.turbine.test
import com.kaylakautai.data.testing.FakePikauLogger
import com.kaylakautai.data.testing.FakeTripRepository
import com.kaylakautai.model.Trip
import com.kaylakautai.model.testTrip
import com.kaylakautai.testing.MainDispatcherRule
import com.kaylakautai.ui.lce.DisplayError
import com.kaylakautai.ui.lce.UiState
import com.kaylakautai.ui.model.TripListItem
import com.kaylakautai.ui.util.formatDateRangeToDisplay
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Locale
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TripsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val today: LocalDate = LocalDate.of(2026, 6, 15)
    private val clock: Clock = Clock.fixed(
        today.atStartOfDay(ZoneOffset.UTC).toInstant(),
        ZoneOffset.UTC,
    )
    private val repository = FakeTripRepository()
    private val logger = FakePikauLogger()
    private lateinit var defaultLocale: Locale

    private val olderPast = testTrip(
        id = "past-old",
        name = "Routeburn",
        location = "Fiordland",
        startDate = today.minusDays(20),
        endDate = today.minusDays(17),
    )
    private val newerPast = testTrip(
        id = "past-new",
        name = "Kepler",
        location = "Te Anau",
        startDate = today.minusDays(4),
        endDate = today.minusDays(1),
    )
    private val upcoming = testTrip(
        id = "next",
        name = "Abel Tasman",
        location = "Marahau",
        startDate = today.plusDays(3),
        endDate = today.plusDays(6),
    )
    private val startsToday = testTrip(
        id = "today",
        name = "Day walk",
        location = "Wellington",
        startDate = today,
        endDate = today,
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
    fun `loads upcoming trips on the first tab and past trips on the second`() = runTest {
        repository.emit(listOf(olderPast, upcoming, newerPast, startsToday))
        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())

            val upcomingTab = awaitItem().content()
            assertEquals(0, upcomingTab.selectedOptionIndex)
            assertEquals(listOf(startsToday, upcoming).map { it.toListItem() }, upcomingTab.tripsList)

            viewModel.onOptionSelected(1)

            val pastTab = awaitItem().content()
            assertEquals(1, pastTab.selectedOptionIndex)
            assertEquals(listOf(newerPast, olderPast).map { it.toListItem() }, pastTab.tripsList)
        }
    }

    @Test
    fun `empty repository is content with no trips`() = runTest {
        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            val content = awaitItem().content()
            assertTrue(content.tripsList.isEmpty())
        }
    }

    @Test
    fun `repository update replaces the list without returning to loading`() = runTest {
        repository.emit(listOf(upcoming))
        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(listOf("Abel Tasman"), awaitItem().content().tripsList.map { it.name })

            repository.emit(listOf(upcoming, startsToday))

            val updated = awaitItem()
            assertTrue(updated is UiState.Content)
            assertEquals(
                listOf("Day walk", "Abel Tasman"),
                updated.content().tripsList.map { it.name },
            )
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
            repository.emit(listOf(upcoming))
            viewModel.onRetryClick()

            assertEquals(UiState.Loading, awaitItem())
            assertEquals(listOf("Abel Tasman"), awaitItem().content().tripsList.map { it.name })
        }
    }

    @Test
    fun `new trip click is logged`() {
        val viewModel = viewModel()

        viewModel.onNewTripClick()

        assertEquals(listOf("TRIPS_SCREEN" to "onNewTripClick() triggered"), logger.info)
    }

    private fun viewModel() = TripsViewModel(
        tripsRepository = repository,
        clock = clock,
        logger = logger,
    )

    private fun Trip.toListItem() = TripListItem(
        id = id,
        name = name,
        dates = formatDateRangeToDisplay(startDate, endDate),
        location = location,
    )
}

private fun UiState<TripsScreenData>.content(): TripsScreenData {
    assertTrue("Expected content but was $this", this is UiState.Content)
    return (this as UiState.Content).data
}

private fun UiState<TripsScreenData>.error(): UiState.Error {
    assertTrue("Expected error but was $this", this is UiState.Error)
    return this as UiState.Error
}
