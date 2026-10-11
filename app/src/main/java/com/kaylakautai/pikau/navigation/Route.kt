package com.kaylakautai.pikau.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object HomeTab : Route

    @Serializable
    data object GearTab : Route

    @Serializable
    data object TripsTab : Route

    @Serializable
    data object MeTab : Route

    @Serializable
    data class GearDetail(val gearId: String) : Route

    @Serializable
    data class TripDetail(val tripId: String) : Route
}