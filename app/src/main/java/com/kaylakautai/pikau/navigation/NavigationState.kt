package com.kaylakautai.pikau.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.serialization.NavKeySerializer
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer

/**
 * State holder for managing navigation within the application, specifically designed to handle
 * multiple backstacks associated with top-level routes (such as bottom navigation tabs).
 *
 * @property startRoute The initial [NavKey] used as the entry point of the navigation
 * @property backStacks A mapping of top-level [NavKey]s to their respective [NavBackStack]
 * @property topLevelRoute The currently active top-level [NavKey]
 */
class NavigationState(
    val startRoute: NavKey,
    val backStacks: Map<NavKey, NavBackStack<NavKey>>,
    topLevelRouteState: MutableState<NavKey>
) {
    var topLevelRoute: NavKey by topLevelRouteState

    /**
     * Converts the navigation backstacks into a list of decorated [NavEntry] objects
     */
    @Composable
    fun toDecoratedEntries(
        entryProvider: (NavKey) -> NavEntry<NavKey>
    ): List<NavEntry<NavKey>> {
        val decoratedEntries = backStacks.mapValues { (_, stack) ->
            rememberDecoratedNavEntries(
                backStack = stack,
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
                ),
                entryProvider = entryProvider
            )
        }

        return getTopLevelRoutesInUse().flatMap { decoratedEntries[it] ?: emptyList() }
    }

    /**
     * Returns the keys for stacks that must remain active, ensuring the [startRoute]
     * stack is preserved even when [topLevelRoute] changes
     */
    private fun getTopLevelRoutesInUse(): List<NavKey> =
        listOf(startRoute, topLevelRoute).distinct()
}

/**
 * Creates and remembers a [NavigationState] for managing multi-stack navigation.
 *
 * This function initialises individual [NavBackStack]s for each of the provided [topLevelRoutes]
 * and ensures that the current navigation position is preserved across config changes.
 *
 * @param startRoute The initial [NavKey] to be displayed when the application starts
 * @param topLevelRoutes The set of all [NavKey]s that represent the root destinations of the app (e.g., bototm nav tabs)
 * @return A [NavigationState] instance that tracks the active tab and its associated back stacks
 */
@Composable
fun rememberNavigationState(
    startRoute: NavKey,
    topLevelRoutes: Set<NavKey>
): NavigationState {
    // Ensure the top level route survives config changes
    val topLevelRouteState = rememberSerializable(
        startRoute,
        topLevelRoutes,
        serializer = MutableStateSerializer(NavKeySerializer())
    ) { mutableStateOf(startRoute) }

    // Creates a backstack for each tab (top-level routes e.g. Home, Gear)
    val backStacks = topLevelRoutes.associateWith { tab -> rememberNavBackStack(tab) }

    return remember(startRoute, topLevelRoutes) {
        NavigationState(startRoute, backStacks, topLevelRouteState)
    }
}