package com.kaylakautai.pikau

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import com.kaylakautai.pikau.navigation.Navigator
import com.kaylakautai.pikau.navigation.PikauBottomBar
import com.kaylakautai.pikau.navigation.PikauTabPager
import com.kaylakautai.pikau.navigation.Route
import com.kaylakautai.pikau.navigation.TopLevelDestination
import com.kaylakautai.gear.GearScreen
import com.kaylakautai.pikau.navigation.rememberNavigationState
import com.kaylakautai.trips.TripsScreen
import com.kaylakautai.ui.components.PikauBackground

@Composable
fun PikauApp() {

    val navigationState = rememberNavigationState(
        startRoute = Route.HomeTab,
        topLevelRoutes = TopLevelDestination.routes
    )

    val navigator = remember(navigationState) { Navigator(navigationState) }

    // TODO Temp placeholders
    val entryProvider = entryProvider<NavKey> {
        entry<Route.HomeTab> {
            Text("Home Screen")
        }
        entry<Route.GearTab> {
            GearScreen(
                onGearClick = { gearId ->
                    navigator.navigate(Route.GearDetail(gearId))
                }
            )
        }
        entry<Route.TripsTab> {
            TripsScreen(
                onTripClick = { tripId ->
                    navigator.navigate(Route.TripDetail(tripId))
                }
            )
        }
        entry<Route.MeTab> {
            Text("Me Screen")
        }

        entry<Route.TripDetail> { key ->
            // TODO replace placeholder
            Text("Trip detail: ${key.tripId}")
        }
        entry<Route.GearDetail> { key ->
            // TODO replace placeholder
            Text("Gear detail: ${key.gearId}")
        }
    }

    Box(Modifier.fillMaxSize()) {
        PikauBackground()
        val showBottomBar = navigationState.isAtTabRoot
        Scaffold(
            containerColor = Color.Transparent,
            contentWindowInsets = if (showBottomBar) {
                WindowInsets(0)
            } else {
                WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)
            },
            bottomBar = {
                if (showBottomBar) {
                    PikauBottomBar(navigationState) { navigator.navigate(it) }
                }
            }
        ) { innerPadding ->
            PikauTabPager(
                navigationState = navigationState,
                navigator = navigator,
                entryProvider = entryProvider,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}