package com.kaylakautai.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.kaylakautai.pikau.ui.R
import com.kaylakautai.ui.theme.Spacing
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Renders the design-system components. Pixel comparison is not part of the normal test run.
 * Record or verify on one OS with `:core:ui:recordRoborazziDebug` or `:core:ui:verifyRoborazziDebug`.
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = "en-rUS-w360dp-h1600dp")
class PikauComponentScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun pikauButton_styles() {
        composeRule.setScreenshotContent {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.small)) {
                PikauButtonStyle.entries.forEach { style ->
                    PikauButton(
                        text = style.name,
                        onClick = {},
                        style = style,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    PikauButton(
                        text = "Disabled",
                        onClick = {},
                        style = style,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    PikauButton(
                        text = style.name,
                        onClick = {},
                        style = style,
                        leadingIcon = R.drawable.ic_add,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
        composeRule.captureScreenshot()
    }

    @Test
    fun pikauChip_sizesAndSelection() {
        composeRule.setScreenshotContent {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.small)) {
                PikauChip(label = "Small selected", selected = true, onClick = {})
                PikauChip(label = "Small unselected", selected = false, onClick = {})
                PikauChip(label = "Small static", selected = true)
                PikauChip(
                    label = "Large selected",
                    selected = true,
                    onClick = {},
                    size = PikauChipSize.Large,
                )
                PikauChip(
                    label = "Large unselected",
                    selected = false,
                    onClick = {},
                    size = PikauChipSize.Large,
                )
                PikauChip(label = "Disabled", selected = false, onClick = {}, enabled = false)
            }
        }
        composeRule.captureScreenshot()
    }

    @Test
    fun pikauTabs_twoAndFourOptions() {
        composeRule.setScreenshotContent {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.large)) {
                PikauTabs(
                    options = listOf("Upcoming", "Past"),
                    selectedIndex = 0,
                    onOptionSelected = {},
                    modifier = Modifier.fillMaxWidth(),
                )
                PikauTabs(
                    options = listOf("Overview", "Packing", "Food", "Notes"),
                    selectedIndex = 2,
                    onOptionSelected = {},
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        composeRule.captureScreenshot()
    }
}
