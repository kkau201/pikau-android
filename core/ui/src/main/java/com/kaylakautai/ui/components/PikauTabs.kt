package com.kaylakautai.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kaylakautai.ui.theme.PikauShapes
import com.kaylakautai.ui.theme.PīkauTheme
import com.kaylakautai.ui.theme.Spacing

@Composable
fun PikauTabs(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.secondary,
            shape = PikauShapes.medium
        )
    ) {
        options.forEachIndexed { index, text ->
            SegmentedButton(
                selected = index == selectedIndex,
                onClick = { onOptionSelected(index) },
                shape = PikauShapes.medium,
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = MaterialTheme.colorScheme.primary,
                    activeContentColor = MaterialTheme.colorScheme.onPrimary,
                    inactiveContainerColor = MaterialTheme.colorScheme.secondary,
                    inactiveContentColor = MaterialTheme.colorScheme.onSecondary
                ),
                // Remove default borders
                border = BorderStroke(0.dp, Color.Transparent),
                // Remove default icon
                icon = {}
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = Spacing.xSmall)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PikauTabsPreview() {
    PīkauTheme(dynamicColor = false) {
        Column(
            modifier = Modifier
                .safeDrawingPadding()
                .padding(Spacing.xlarge),
            verticalArrangement = Arrangement.spacedBy(Spacing.xlarge)
        ) {
            var selectedTabIndex1 by remember { mutableIntStateOf(0) }
            val twoTabOptions = listOf("Upcoming", "Past")

            PikauTabs(
                options = twoTabOptions,
                selectedIndex = selectedTabIndex1,
                onOptionSelected = { selectedTabIndex1 = it }
            )

            var selectedTabIndex2 by remember { mutableIntStateOf(0) }
            val fourTabOptions = listOf("Overview", "Packing", "Food", "Notes")

            PikauTabs(
                options = fourTabOptions,
                selectedIndex = selectedTabIndex2,
                onOptionSelected = { selectedTabIndex2 = it }
            )
        }
    }
}