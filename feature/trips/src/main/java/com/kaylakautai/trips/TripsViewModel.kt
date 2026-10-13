package com.kaylakautai.trips

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaylakautai.data.repo.TripRepository
import com.kaylakautai.model.Trip
import com.kaylakautai.ui.lce.UiState
import com.kaylakautai.ui.lce.asUiState
import com.kaylakautai.ui.model.TripListItem
import com.kaylakautai.ui.util.formatDateRangeToDisplay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TripsViewModel @Inject constructor(
    tripsRepository: TripRepository,
    clock: Clock
) : ViewModel() {
    private val _selectedIndex = MutableStateFlow(0)
    private val today = LocalDate.now(clock)

    val uiState: StateFlow<UiState<TripsScreenData>> = combine(
        tripsRepository.getUpcomingTrips(today),
        tripsRepository.getPastTrips(today),
        _selectedIndex
    ) { upcoming, past, index ->
        TripsScreenData(
            upcomingTrips = upcoming.toListItems(),
            pastTrips = past.toListItems(),
            selectedOptionIndex = index
        )
    }
        .asUiState(errorMessage = R.string.trips_failed_to_load_trips)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

    fun onOptionSelected(index: Int) {
        _selectedIndex.value = index
    }

    fun onNewTripClicked() {
        // TODO
        Log.i("TRIPS_SCREEN", "onNewTripClicked() triggered")
    }

    private fun List<Trip>.toListItems() = map {
        with(it) {
            TripListItem(
                id = id,
                name = name,
                dates = formatDateRangeToDisplay(startDate, endDate),
                location = location,
                // TODO progress
            )
        }
    }
}
