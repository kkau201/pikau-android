package com.kaylakautai.pikau

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.kaylakautai.pikau.navigation.Navigator
import com.kaylakautai.pikau.navigation.Route
import com.kaylakautai.pikau.navigation.TopLevelDestination
import com.kaylakautai.pikau.navigation.rememberNavigationState

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
            Text("Trips Screen")
        }
        entry<Route.MeTab> {
            Text("Me Screen")
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                TopLevelDestination.entries.forEach { destination ->
                    val isSelected = destination.route == navigationState.topLevelRoute
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navigator.navigate(destination.route)
                        },
                        icon = {}, // TODO add icons
                        label = { Text(destination.title) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavDisplay(
            entries = navigationState.toDecoratedEntries(entryProvider),
            onBack = { navigator.goBack() },
            modifier = Modifier.padding(paddingValues)
        )
    }
}