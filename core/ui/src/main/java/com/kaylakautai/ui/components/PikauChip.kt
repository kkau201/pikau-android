package com.kaylakautai.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kaylakautai.ui.theme.Spacing
import com.kaylakautai.ui.util.PikauPreviewSurface
import com.kaylakautai.ui.util.PikauPreviews

enum class PikauChipSize { Small, Large }

@Composable
fun PikauChip(
    label: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    size: PikauChipSize = PikauChipSize.Small,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    textAlign: TextAlign = TextAlign.Center,
    @DrawableRes leadingIcon: Int? = null,
    selectedContentColour: Color = MaterialTheme.colorScheme.onPrimary,
    selectedBgColour: Color = MaterialTheme.colorScheme.primary,
    unselectedContentColour: Color = MaterialTheme.colorScheme.onSecondary,
    unselectedBgColour: Color = MaterialTheme.colorScheme.secondary,
) {
    val minHeight = if (size == PikauChipSize.Small) 20.dp else 32.dp
    val textStyle = if (size == PikauChipSize.Small) MaterialTheme.typography.labelSmall else MaterialTheme.typography.bodyMedium

    if (onClick != null) {
        FilterChip(
            selected = selected,
            onClick = onClick,
            enabled = enabled,
            modifier = modifier.heightIn(min = minHeight),
            shape = CircleShape,
            border = null,
            label = {
                ChipLabel(
                    label = label,
                    textStyle = textStyle,
                    textAlign = textAlign,
                    singleLine = singleLine,
                )
            },
            leadingIcon = leadingIcon?.let { iconRes ->
                { ChipLeadingIcon(iconRes = iconRes, isFilterChip = true) }
            },
            colors = FilterChipDefaults.filterChipColors(
                containerColor = unselectedBgColour,
                labelColor = unselectedContentColour,
                iconColor = unselectedContentColour,
                selectedContainerColor = selectedBgColour,
                selectedLabelColor = selectedContentColour,
                selectedLeadingIconColor = selectedContentColour
            )
        )
    } else {
        val containerColor = if (selected) selectedBgColour else unselectedBgColour
        val contentColor = if (selected) selectedContentColour else unselectedContentColour

        Surface(
            modifier = modifier.heightIn(min = minHeight),
            shape = CircleShape,
            color = containerColor,
            contentColor = contentColor,
        ) {
            Row(
                modifier = Modifier.padding(horizontal = Spacing.small, vertical = Spacing.xSmall),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.xSmall),
            ) {
                if (leadingIcon != null) {
                    ChipLeadingIcon(iconRes = leadingIcon, isFilterChip = false)
                }
                ChipLabel(
                    label = label,
                    textStyle = textStyle,
                    textAlign = textAlign,
                    singleLine = singleLine,
                )
            }
        }
    }
}

@Composable
private fun ChipLabel(
    label: String,
    textStyle: TextStyle,
    textAlign: TextAlign,
    singleLine: Boolean,
) {
    Text(
        text = label,
        style = textStyle,
        textAlign = textAlign,
        maxLines = if (singleLine) 1 else Int.MAX_VALUE,
        overflow = if (singleLine) TextOverflow.Ellipsis else TextOverflow.Clip,
    )
}

@Composable
private fun ChipLeadingIcon(
    @DrawableRes iconRes: Int,
    isFilterChip: Boolean,
) {
    Icon(
        painter = painterResource(id = iconRes),
        contentDescription = null,
        modifier = if (isFilterChip) {
            Modifier
                .padding(start = Spacing.xSmall)
                .padding(vertical = 4.dp)
        } else {
            Modifier.size(16.dp)
        }
    )
}

@PikauPreviews
@Composable
private fun PikauChipPreview() {
    PikauPreviewSurface {
        Row(
            modifier = Modifier.padding(Spacing.medium),
            horizontalArrangement = Arrangement.spacedBy(Spacing.large)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.small)) {
                PikauChip(
                    "Small selected",
                    selected = true,
                    onClick = {}
                )
                PikauChip(
                    "Small unselected",
                    selected = false,
                    onClick = {}
                )
                PikauChip(
                    "Small static badge",
                    selected = true,
                    onClick = null
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.small)) {
                PikauChip(
                    "Large selected",
                    selected = true,
                    onClick = {},
                    size = PikauChipSize.Large
                )
                PikauChip(
                    "Large unselected",
                    selected = false,
                    onClick = {},
                    size = PikauChipSize.Large
                )
            }
        }
    }
}
