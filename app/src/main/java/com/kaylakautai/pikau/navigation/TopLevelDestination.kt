package com.kaylakautai.pikau.navigation

import androidx.navigation3.runtime.NavKey
import com.kaylakautai.pikau.R

/**
 * Represents the primary navigation destinations (for now the tabs in bottom navigation bar).
 *
 * @property route The navigation route/key corresponding to the destination
 * @property title The user-facing display name for the destination
 * @property icon The resource ID representing the user-facing icon
 */
enum class TopLevelDestination(
    val route: Route,
    val title: String,
    val icon: Int
) {
    HOME(Route.HomeTab, "Home", R.drawable.ic_tab_home),
    GEAR(Route.GearTab, "Gear", R.drawable.ic_tab_gear),
    TRIPS(Route.TripsTab, "Trips", R.drawable.ic_tab_trips),
    ME(Route.MeTab, "Me", R.drawable.ic_tab_me);

    companion object {
        val routes: Set<NavKey> = entries.map { it.route }.toSet()
    }
}