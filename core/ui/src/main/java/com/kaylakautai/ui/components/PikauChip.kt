package com.kaylakautai.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kaylakautai.pikau.ui.R
import com.kaylakautai.ui.theme.Spacing
import com.kaylakautai.ui.util.PikauPreviewSurface
import com.kaylakautai.ui.util.PikauPreviews

enum class PikauChipSize { Small, Large }

@Composable
fun PikauChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: PikauChipSize = PikauChipSize.Small,
    enabled: Boolean = true,
    @DrawableRes leadingIcon: Int? = null,
    selectedContentColour: Color = MaterialTheme.colorScheme.onPrimary,
    selectedBgColour: Color = MaterialTheme.colorScheme.primary,
    unselectedContentColour: Color = MaterialTheme.colorScheme.onSecondary,
    unselectedBgColour: Color = MaterialTheme.colorScheme.secondary,
) {
    val height = if (size == PikauChipSize.Small) 20.dp else 32.dp

    FilterChip(
        selected = selected,
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(height),
        shape = CircleShape,
        border = null,
        label = { Text(text = label, style = MaterialTheme.typography.bodyMedium) },
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = Spacing.xSmall)
                        .padding(vertical = 6.dp)
                )
            }
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
                    "Small selected",
                    selected = false,
                    onClick = {},
                    leadingIcon = R.drawable.ic_add
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
                PikauChip(
                    "Large unselected",
                    selected = false,
                    onClick = {},
                    size = PikauChipSize.Large,
                    leadingIcon = R.drawable.ic_add
                )
            }
        }
    }
}