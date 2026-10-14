package com.kaylakautai.ui.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.kaylakautai.ui.components.PikauBackground
import com.kaylakautai.ui.theme.PikauTheme

/** Light and dark themes, plus a range of system font sizes. */
@PreviewLightDark
@PreviewFontScale
annotation class PikauPreviews

/** Theme plus a themed background, so dark previews are readable. */
@Composable
fun PikauPreviewSurface(content: @Composable () -> Unit) {
    PikauTheme(dynamicColor = false) {
        Surface(color = MaterialTheme.colorScheme.surface, content = content)
    }
}

/** For previews of full tab screens, which expect PikauApp to draw the background behind them. */
@Composable
fun PikauScreenPreviewSurface(content: @Composable () -> Unit) {
    PikauPreviewSurface {
        Box(Modifier.fillMaxSize()) {
            PikauBackground()
            content()
        }
    }
}