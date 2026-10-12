package com.kaylakautai.ui.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.kaylakautai.ui.theme.PīkauTheme

/** Light and dark themes, plus a range of system font sizes. */
@PreviewLightDark
@PreviewFontScale
internal annotation class PikauPreviews

/** Theme plus a themed background, so dark previews are readable. */
@Composable
internal fun PikauPreviewSurface(content: @Composable () -> Unit) {
    PīkauTheme(dynamicColor = false) {
        Surface(color = MaterialTheme.colorScheme.surface, content = content)
    }
}