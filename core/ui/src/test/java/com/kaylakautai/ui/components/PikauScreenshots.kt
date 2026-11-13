package com.kaylakautai.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import com.github.takahirom.roborazzi.captureRoboImage
import com.kaylakautai.ui.theme.PikauTheme
import com.kaylakautai.ui.theme.Spacing

internal const val ScreenshotTag = "pikau-screenshot"

internal fun ComposeContentTestRule.setScreenshotContent(content: @Composable () -> Unit) {
    setContent {
        PikauTheme(darkTheme = false, dynamicColor = false) {
            Surface(
                modifier = Modifier
                    .testTag(ScreenshotTag)
                    .width(320.dp),
                color = MaterialTheme.colorScheme.surface,
            ) {
                Box(Modifier.padding(Spacing.medium)) {
                    content()
                }
            }
        }
    }
    waitForIdle()
}

internal fun ComposeContentTestRule.captureScreenshot() {
    onNodeWithTag(ScreenshotTag).captureRoboImage()
}
