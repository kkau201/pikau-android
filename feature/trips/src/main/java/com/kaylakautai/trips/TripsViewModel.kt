package com.kaylakautai.trips

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaylakautai.data.repo.TripRepository
import com.kaylakautai.model.Trip
import com.kaylakautai.ui.lce.DisplayError
import com.kaylakautai.ui.lce.UiState
import com.kaylakautai.ui.lce.asUiState
import com.kaylakautai.ui.model.TripListItem
import com.kaylakautai.ui.util.formatDateRangeToDisplay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TripsViewModel @Inject constructor(
    tripsRepository: TripRepository,
    clock: Clock
) : ViewModel() {
    private val _selectedIndex = MutableStateFlow(0)
    private val retryTrigger = MutableStateFlow(0)

    val uiState: StateFlow<UiState<TripsScreenData>> = retryTrigger
        .flatMapLatest {
            val today = LocalDate.now(clock)
            combine(
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
                .asUiState(displayError = DisplayError.NoTripsFound)
                .onStart { emit(UiState.Loading) }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

    fun onOptionSelected(index: Int) {
        _selectedIndex.value = index
    }

    fun onNewTripClick() {
        // TODO
        Log.i("TRIPS_SCREEN", "onNewTripClick() triggered")
    }

    fun onRetryClick() = retryTrigger.update { it + 1 }

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
