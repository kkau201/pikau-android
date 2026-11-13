package com.kaylakautai.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.kaylakautai.ui.theme.PikauTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = "en-rUS-w360dp-h640dp")
class PikauComponentUiTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun button_clickInvokesCallback_andDisabledButtonDoesNot() {
        var clicks = 0
        composeRule.setContent {
            PikauTheme(dynamicColor = false) {
                Column {
                    PikauButton(text = "Save", onClick = { clicks += 1 })
                    PikauButton(text = "Disabled", onClick = { clicks += 1 }, enabled = false)
                }
            }
        }

        composeRule.onNodeWithText("Save").assertHasClickAction().performClick()
        composeRule.onNodeWithText("Disabled").assertIsNotEnabled()
        assertEquals(1, clicks)
    }

    @Test
    fun chip_clickInvokesCallback_andStaticChipIsNotClickable() {
        var clicks = 0
        composeRule.setContent {
            PikauTheme(dynamicColor = false) {
                Column {
                    PikauChip(label = "Shelter", selected = true, onClick = { clicks += 1 })
                    PikauChip(label = "Badge", selected = true, onClick = null)
                }
            }
        }

        composeRule.onNodeWithText("Shelter").assertIsSelected().performClick()
        composeRule.onNodeWithText("Badge").assertHasNoClickAction()
        assertEquals(1, clicks)
    }

    @Test
    fun tabs_selectingAnOptionReportsItsIndex() {
        var selected = 0
        composeRule.setContent {
            PikauTheme(dynamicColor = false) {
                PikauTabs(
                    options = listOf("Upcoming", "Past"),
                    selectedIndex = selected,
                    onOptionSelected = { selected = it },
                )
            }
        }

        composeRule.onNodeWithText("Upcoming").assertIsSelected()
        composeRule.onNodeWithText("Past").assertIsNotSelected().performClick()
        assertEquals(1, selected)
    }
}
