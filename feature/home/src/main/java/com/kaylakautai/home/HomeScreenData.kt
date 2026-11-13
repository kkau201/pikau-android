package com.kaylakautai.home

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.kaylakautai.ui.model.TripListItem

@Immutable
data class HomeScreenData(
    @StringRes val title: Int = R.string.home_title,
    @StringRes val tagline: Int = R.string.home_tagline,
    val upcomingTrip: TripListItem? = null,
)
