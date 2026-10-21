package com.kaylakautai.pikau.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Swipeable top-level tabs. Each tab keeps its own back stack, and moving between tabs slides
 * the current screen aside.
 */
@Composable
fun PikauTabPager(
    navigationState: NavigationState,
    navigator: Navigator,
    entryProvider: (NavKey) -> NavEntry<NavKey>,
    modifier: Modifier = Modifier,
) {
    val tabs = TopLevelDestination.entries
    val entriesByTab = tabs.associate { destination ->
        destination.route to navigationState.decoratedEntries(destination.route, entryProvider)
    }
    val initialPage =
        tabs.indexOfFirst { it.route == navigationState.topLevelRoute }.coerceAtLeast(0)
    val pagerState = rememberPagerState(initialPage = initialPage) { tabs.size }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .distinctUntilChanged()
            .collect { page ->
                val route = tabs[page].route
                if (navigationState.topLevelRoute != route) {
                    navigator.navigate(route)
                }
            }
    }

    LaunchedEffect(navigationState.topLevelRoute) {
        val page = tabs.indexOfFirst { it.route == navigationState.topLevelRoute }
        if (page >= 0 && pagerState.settledPage != page && !pagerState.isScrollInProgress) {
            pagerState.animateScrollToPage(page)
        }
    }

    val settledRoute = tabs[pagerState.settledPage].route
    val canSwipeTabs = navigationState.backStacks[settledRoute]?.size == 1

    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        userScrollEnabled = canSwipeTabs,
        key = { tabs[it].name },
    ) { page ->
        NavDisplay(
            entries = entriesByTab.getValue(tabs[page].route),
            onBack = { navigator.goBack() },
            modifier = Modifier.fillMaxSize(),
        )
    }
}
