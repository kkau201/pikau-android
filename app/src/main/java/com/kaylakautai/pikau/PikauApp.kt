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
import com.kaylakautai.pikau.navigation.AnimatedPikauBottomBar
import com.kaylakautai.pikau.navigation.Navigator
import com.kaylakautai.pikau.navigation.PikauTabPager
import com.kaylakautai.pikau.navigation.Route
import com.kaylakautai.pikau.navigation.TopLevelDestination
import com.kaylakautai.gear.GearScreen
import com.kaylakautai.home.HomeScreen
import com.kaylakautai.profile.OpenSourceLibrariesScreen
import com.kaylakautai.profile.ProfileScreen
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

    val entryProvider = entryProvider<NavKey> {
        entry<Route.HomeTab> {
            HomeScreen(
                onTripClick = { tripId ->
                    navigator.navigate(Route.TripDetail(tripId))
                }
            )
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
            ProfileScreen(
                onOpenSourceClick = {
                    navigator.navigate(Route.OpenSourceLibraries)
                }
            )
        }
        entry<Route.OpenSourceLibraries> {
            OpenSourceLibrariesScreen(onBackClick = navigator::goBack)
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
        Scaffold(
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0),
            bottomBar = {
                AnimatedPikauBottomBar(
                    visible = navigationState.isAtTabRoot,
                    navigationState = navigationState,
                    onTabClick = navigator::navigate,
                )
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