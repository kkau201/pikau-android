package com.kaylakautai.ui.components

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun PikauText(
    @StringRes textRes: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = MaterialTheme.colorScheme.primary,
    textAlign: TextAlign = TextAlign.Center,
    minLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Clip
) = PikauText(
    text = stringResource(textRes),
    modifier = modifier,
    style = style,
    color = color,
    textAlign = textAlign,
    minLines = minLines,
    overflow = overflow
)

@Composable
fun PikauText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = MaterialTheme.colorScheme.primary,
    textAlign: TextAlign = TextAlign.Center,
    minLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Clip
) = Text(
    text = text,
    modifier = modifier,
    style = style,
    color = color,
    textAlign = textAlign,
    minLines = minLines,
    overflow = overflow
)