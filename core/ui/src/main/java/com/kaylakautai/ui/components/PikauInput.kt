package com.kaylakautai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import com.kaylakautai.ui.theme.PikauShapes
import com.kaylakautai.ui.theme.PīkauTheme
import com.kaylakautai.ui.theme.Spacing

@Composable
fun PikauInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = ""
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.secondary, shape = PikauShapes.medium)
            .padding(horizontal = Spacing.medium, vertical = Spacing.small),
        textStyle = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onSecondary),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        singleLine = true,
        decorationBox = { innerTextField ->
            Box(contentAlignment = Alignment.CenterStart) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.6f)
                    )
                }
                innerTextField()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PikauInputPreview() {
    PīkauTheme(dynamicColor = false) {
        Column(
            modifier = Modifier
                .safeDrawingPadding()
                .padding(Spacing.xlarge),
            verticalArrangement = Arrangement.spacedBy(Spacing.xlarge)
        ) {
            // Input Field
            var text by remember { mutableStateOf("") }
            PikauInput(
                value = text,
                onValueChange = { text = it },
                placeholder = "eg., Taranaki",
                modifier = Modifier.fillMaxWidth(0.8f)
            )
        }
    }
}