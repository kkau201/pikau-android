package com.kaylakautai.pikau.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Handles navigation events by managing the application's [NavigationState].
 *
 * This class facilitates switching between top-level routes and managing the back stack
 * for each destination. It provides methods to navigate to new routes and handle
 * back-button behaviour.
 *
 * @property state The current state of navigation, including all back stacks and the active route
 */
class Navigator(val state: NavigationState) {

    /**
     * Navigates to the specified [route].
     *
     * If the route is a top-level destination, it switches the current active stack.
     * Otherwise, it pushes the route onto the current top-level back stack.
     *
     * @param route The destination [NavKey] to navigate to.
     */
    fun navigate(route: NavKey) {
        if (route in state.backStacks.keys) {
            // This is a top level route, just switch to it
            state.topLevelRoute = route
        } else {
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    /**
     * Navigates back in the current stack or returns to the start route.
     *
     * If the current route is the root of a top-level stack, this function switches the
     * active stack to the [NavigationState.startRoute]. Otherwise, it removes the
     * top-most route from the current stack.
     *
     * @throws IllegalStateException if the stack for the current top-level route cannot be found
     */
    fun goBack() {
        val currentStack = state.backStacks[state.topLevelRoute]
            ?: error("Stack for ${state.topLevelRoute} not found")
        val currentRoute = currentStack.last()

        // If we're at a top-level route, go back to the start route stack
        if (currentRoute == state.topLevelRoute) {
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeLastOrNull()
        }
    }

}