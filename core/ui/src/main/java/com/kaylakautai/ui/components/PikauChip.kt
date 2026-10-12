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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kaylakautai.pikau.ui.R
import com.kaylakautai.ui.theme.PīkauTheme
import com.kaylakautai.ui.theme.Spacing

enum class PikauChipSize { Small, Large }

@Composable
fun PikauChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: PikauChipSize = PikauChipSize.Small,
    enabled: Boolean = true,
    @DrawableRes leadingIcon: Int? = null
) {
    val colors = MaterialTheme.colorScheme
    val height = if (size == PikauChipSize.Small) 26.dp else 40.dp
    val iconSize = if (size == PikauChipSize.Small) 16.dp else 24.dp

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
            containerColor = colors.secondary,
            labelColor = colors.onSecondary,
            iconColor = colors.onSecondary,
            selectedContainerColor = colors.primary,
            selectedLabelColor = colors.onPrimary,
            selectedLeadingIconColor = colors.onPrimary
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun PikauChipPreview() {
    PīkauTheme(dynamicColor = false) {
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