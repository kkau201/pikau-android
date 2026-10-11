package com.kaylakautai.pikau.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Represents the primary navigation destinations (for now the tabs in bottom navigation bar).
 *
 * @property route The navigation route/key corresponding to the destination
 * @property title The user-facing display name for the destination
 */
enum class TopLevelDestination(
    val route: Route,
    val title: String,
    // TODO add icons later
) {
    HOME(Route.HomeTab, "Home"),
    GEAR(Route.GearTab, "Gear"),
    TRIPS(Route.TripsTab, "Trips"),
    ME(Route.MeTab, "Me");

    companion object {
        val routes: Set<NavKey> = entries.map { it.route }.toSet()
    }
}