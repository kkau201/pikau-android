package com.kaylakautai.pikau

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.kaylakautai.pikau.navigation.Navigator
import com.kaylakautai.pikau.navigation.PikauBottomBar
import com.kaylakautai.pikau.navigation.Route
import com.kaylakautai.pikau.navigation.TopLevelDestination
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
            Text("Gear Screen")
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
    }

    Box(Modifier.fillMaxSize()) {
        PikauBackground()
        Scaffold(
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0),
            bottomBar = { PikauBottomBar(navigationState) { navigator.navigate(it) } }
        ) { innerPadding ->
            NavDisplay(
                entries = navigationState.toDecoratedEntries(entryProvider),
                onBack = { navigator.goBack() },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}