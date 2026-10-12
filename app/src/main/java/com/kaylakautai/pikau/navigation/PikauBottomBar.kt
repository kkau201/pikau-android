package com.kaylakautai.pikau.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kaylakautai.ui.theme.ShapeCornerRadii
import com.kaylakautai.ui.theme.Spacing

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