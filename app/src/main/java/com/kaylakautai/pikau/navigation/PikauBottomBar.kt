package com.kaylakautai.pikau.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.kaylakautai.ui.theme.ShapeCornerRadii
import com.kaylakautai.ui.theme.Spacing

private const val BottomBarAnimationMillis = 300

/**
 * Slides the bottom tab bar off the bottom of the screen. The navigation-bar inset stays in the
 * layout, so the screen padding eases down to the gesture area instead of jumping when the
 * bar finishes leaving.
 */
@Composable
fun AnimatedPikauBottomBar(
    visible: Boolean,
    navigationState: NavigationState,
    onTabClick: (Route) -> Unit,
) {
    val barColor = MaterialTheme.colorScheme.primary
    val insetColor by animateColorAsState(
        targetValue = if (visible) barColor else Color.Transparent,
        animationSpec = tween(BottomBarAnimationMillis),
        label = "bottomBarInset",
    )
    val motion = tween<IntOffset>(BottomBarAnimationMillis)
    val heightMotion = tween<IntSize>(BottomBarAnimationMillis)

    Column {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(BottomBarAnimationMillis)) +
                slideInVertically(motion) { it } +
                expandVertically(heightMotion, expandFrom = Alignment.Bottom),
            exit = fadeOut(tween(BottomBarAnimationMillis)) +
                slideOutVertically(motion) { it } +
                shrinkVertically(heightMotion, shrinkTowards = Alignment.Bottom),
        ) {
            PikauBottomBar(navigationState, onTabClick)
        }
        Box(
            Modifier
                .fillMaxWidth()
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .background(insetColor)
        )
    }
}

/**
 * Bottom navigation bar that displays top-level app destinations.
 *
 * @param navigationState The current state used to determine the active tab
 * @param onTabClick Callback triggered when a destination is selected
 */
@Composable
fun PikauBottomBar(
    navigationState: NavigationState,
    onTabClick: (Route) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        // The gesture inset is drawn by [AnimatedPikauBottomBar] so this height can animate away
        windowInsets = WindowInsets(0),
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStart = ShapeCornerRadii.medium,
                    topEnd = ShapeCornerRadii.medium
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacing.small),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TopLevelDestination.entries.forEach { destination ->
                val isSelected = destination.route == navigationState.topLevelRoute
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onTabClick(destination.route) },
                    icon = {
                        Icon(
                            painter = painterResource(destination.icon),
                            contentDescription = null // no desc required as label is provided
                        )
                    },
                    label = { Text(stringResource(destination.title)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.secondary,
                        selectedTextColor = MaterialTheme.colorScheme.secondary,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = MaterialTheme.colorScheme.tertiary,
                        unselectedTextColor = MaterialTheme.colorScheme.tertiary
                    )
                )
            }
        }
    }
}