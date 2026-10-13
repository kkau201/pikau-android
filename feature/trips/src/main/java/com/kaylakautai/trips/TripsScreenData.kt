package com.kaylakautai.trips

import androidx.annotation.StringRes
import com.kaylakautai.ui.model.TripListItem

data class TripsScreenData(
    @StringRes val title: Int = R.string.trips_title,
    val selectedOptionIndex: Int = 0,

    private val upcomingTrips: List<TripListItem> = emptyList(),
    private val pastTrips: List<TripListItem> = emptyList()
) {
    val tripsList: List<TripListItem>
        get() = (if (selectedOptionIndex == 0) upcomingTrips else pastTrips)
}

enum class TripOption(
    @StringRes val labelRes: Int,
    @StringRes val emptyStateTitleRes: Int,
    @StringRes val emptyStateMessageRes: Int
) {
    UPCOMING(
        labelRes = R.string.trips_tab_upcoming,
        emptyStateTitleRes = R.string.trips_no_trips_title_upcoming,
        emptyStateMessageRes = R.string.trips_no_trips_message_upcoming
    ),
    PAST(
        labelRes = R.string.trips_tab_past,
        emptyStateTitleRes = R.string.trips_no_trips_title_past,
        emptyStateMessageRes = R.string.trips_no_trips_message_past
    );

    companion object {
        val labels: List<Int> = TripOption.entries.map { it.labelRes }
    }
}