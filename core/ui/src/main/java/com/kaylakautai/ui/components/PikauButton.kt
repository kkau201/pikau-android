package com.kaylakautai.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kaylakautai.pikau.ui.R
import com.kaylakautai.ui.theme.PikauShapes
import com.kaylakautai.ui.theme.Spacing
import com.kaylakautai.ui.util.PikauPreviewSurface
import com.kaylakautai.ui.util.PikauPreviews

enum class PikauButtonStyle { Primary, Secondary, Tertiary }

private val ButtonIconSize = 24.dp
private val TertiaryStrokeWidth = 3.dp
private const val DISABLED_CONTAINER_ALPHA = 0.45f
private const val DISABLED_CONTENT_ALPHA = 0.6f

@Composable
fun PikauButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: PikauButtonStyle = PikauButtonStyle.Primary,
    enabled: Boolean = true,
    @DrawableRes leadingIcon: Int? = null,
    @DrawableRes trailingIcon: Int? = null
) {
    val colors = MaterialTheme.colorScheme

    when (style) {
        PikauButtonStyle.Primary -> Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = PikauShapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.onPrimary,
                disabledContainerColor = colors.primary.copy(alpha = DISABLED_CONTAINER_ALPHA),
                disabledContentColor = colors.onPrimary.copy(alpha = DISABLED_CONTENT_ALPHA)
            )
        ) { ButtonContent(text, leadingIcon, trailingIcon) }

        PikauButtonStyle.Secondary -> FilledTonalButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = PikauShapes.medium,
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = colors.secondary,
                contentColor = colors.onSecondary,
                disabledContainerColor = colors.secondary.copy(alpha = DISABLED_CONTAINER_ALPHA),
                disabledContentColor = colors.onSecondary.copy(alpha = DISABLED_CONTENT_ALPHA)
            )
        ) { ButtonContent(text, leadingIcon, trailingIcon) }

        PikauButtonStyle.Tertiary -> OutlinedButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = PikauShapes.medium,
            border = BorderStroke(
                width = TertiaryStrokeWidth,
                color = if (enabled) colors.primary
                else colors.primary.copy(alpha = DISABLED_CONTAINER_ALPHA)
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = colors.primary,
                disabledContentColor = colors.primary.copy(alpha = DISABLED_CONTENT_ALPHA)
            )
        ) { ButtonContent(text, leadingIcon, trailingIcon) }
    }
}

@Composable
private fun ButtonContent(
    text: String,
    @DrawableRes leadingIcon: Int?,
    @DrawableRes trailingIcon: Int?
) {
    Row(
        modifier = Modifier.padding(vertical = Spacing.xSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ButtonDefaults.IconSpacing)
    ) {
        if (leadingIcon != null) {
            Icon(
                painter = painterResource(id = leadingIcon),
                contentDescription = null,
                modifier = Modifier.size(ButtonIconSize)
            )
        }

        Text(text = text, style = MaterialTheme.typography.titleMedium)

        if (trailingIcon != null) {
            Icon(
                painter = painterResource(id = trailingIcon),
                contentDescription = null,
                modifier = Modifier.size(ButtonIconSize)
            )
        }
    }
}

@PikauPreviews
@Composable
private fun PikauButtonPreview() {
    val modifier = Modifier.widthIn(min = 250.dp)
    PikauPreviewSurface {
        Column(
            modifier = Modifier
                .safeDrawingPadding()
                .padding(Spacing.medium)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Spacing.xlarge)
        ) {
            PikauButtonStyle.entries.forEach { style ->
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.small)) {
                    PikauButton(
                        text = style.name,
                        onClick = {},
                        style = style,
                        modifier = modifier
                    )
                    PikauButton(
                        text = style.name,
                        onClick = {},
                        style = style,
                        enabled = false,
                        modifier = modifier
                    )
                    PikauButton(
                        text = style.name,
                        onClick = {},
                        style = style,
                        leadingIcon = R.drawable.ic_add,
                        modifier = modifier
                    )
                    PikauButton(
                        text = style.name,
                        onClick = {},
                        style = style,
                        trailingIcon = R.drawable.ic_add,
                        modifier = modifier
                    )
                }
            }
        }
    }
}