package com.kaylakautai.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kaylakautai.ui.theme.PikauShapes
import com.kaylakautai.ui.theme.Spacing
import com.kaylakautai.ui.util.PikauPreviewSurface
import com.kaylakautai.ui.util.PikauPreviews

@Composable
fun PikauTabs(
    modifier: Modifier = Modifier,
    @StringRes optionsRes: List<Int>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
) {
    PikauTabs(
        modifier = modifier,
        options = optionsRes.map {
            stringResource(it)
        },
        selectedIndex = selectedIndex,
        onOptionSelected = onOptionSelected,
    )
}

@Composable
fun PikauTabs(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = PikauShapes.medium
            )
            .height(35.dp)
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
                icon = {},
                contentPadding = PaddingValues.Zero
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                )
            }
        }
    }
}

@PikauPreviews
@Composable
fun PikauTabsPreview() {
    PikauPreviewSurface {
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