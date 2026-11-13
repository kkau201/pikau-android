@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kaylakautai.gear

import app.cash.turbine.test
import com.kaylakautai.data.testing.FakeCategoryRepository
import com.kaylakautai.data.testing.FakeGearRepository
import com.kaylakautai.data.testing.FakePikauLogger
import com.kaylakautai.model.testGear
import com.kaylakautai.model.testGearCategory
import com.kaylakautai.testing.MainDispatcherRule
import com.kaylakautai.ui.lce.DisplayError
import com.kaylakautai.ui.lce.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class GearViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val shelter = testGearCategory(id = "shelter", displayText = "Shelter")
    private val cook = testGearCategory(id = "cook", displayText = "Cook")
    private val tent = testGear(id = "tent", name = "Tent", category = shelter, weight = 1200)
    private val tarp = testGear(id = "tarp", name = "Ultralight Tarp", category = shelter, weight = 400)
    private val stove = testGear(id = "stove", name = "Stove", category = cook, weight = 75)

    private val gearRepository = FakeGearRepository()
    private val categoryRepository = FakeCategoryRepository()
    private val logger = FakePikauLogger()

    @Test
    fun `loads gear and categories`() = runTest {
        emitInventory()
        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            val content = awaitItem().content()
            assertEquals(listOf(shelter, cook), content.categories)
            assertEquals(listOf("Tent", "Ultralight Tarp", "Stove"), content.gearList.map { it.name })
            assertEquals(listOf(1200, 400, 75), content.gearList.map { it.weightGrams })
            assertTrue(content.hasGear)
            assertTrue(content.selectedCategoryIds.isEmpty())
        }
    }

    @Test
    fun `category toggle filters then clears`() = runTest {
        emitInventory()
        val viewModel = viewModel()

        viewModel.uiState.test {
            skipItems(1)
            awaitItem()

            viewModel.onCategoryToggled(shelter.id)
            val shelterOnly = awaitItem().content()
            assertEquals(setOf(shelter.id), shelterOnly.selectedCategoryIds)
            assertEquals(listOf("Tent", "Ultralight Tarp"), shelterOnly.gearList.map { it.name })

            viewModel.onCategoryToggled(cook.id)
            val both = awaitItem().content()
            assertEquals(setOf(shelter.id, cook.id), both.selectedCategoryIds)
            assertEquals(listOf("Tent", "Ultralight Tarp", "Stove"), both.gearList.map { it.name })

            viewModel.onCategoryToggled(shelter.id)
            val cookOnly = awaitItem().content()
            assertEquals(setOf(cook.id), cookOnly.selectedCategoryIds)
            assertEquals(listOf("Stove"), cookOnly.gearList.map { it.name })
        }
    }

    @Test
    fun `search matches name ignoring case and trims whitespace`() = runTest {
        emitInventory()
        val viewModel = viewModel()

        viewModel.uiState.test {
            skipItems(1)
            awaitItem()

            viewModel.onSearchQueryChange("  TARP ")
            val matched = awaitItem().content()
            assertEquals("  TARP ", matched.searchQuery)
            assertEquals(listOf("Ultralight Tarp"), matched.gearList.map { it.name })

            viewModel.onSearchQueryChange("   ")
            val blank = awaitItem().content()
            assertEquals("   ", blank.searchQuery)
            assertEquals(3, blank.gearList.size)
        }
    }

    @Test
    fun `search does not match category names`() = runTest {
        emitInventory()
        val viewModel = viewModel()

        viewModel.uiState.test {
            skipItems(1)
            awaitItem()

            viewModel.onSearchQueryChange("shelter")
            assertTrue(awaitItem().content().gearList.isEmpty())
        }
    }

    @Test
    fun `category and search apply together and an empty match still counts as having gear`() = runTest {
        emitInventory()
        val viewModel = viewModel()

        viewModel.uiState.test {
            skipItems(1)
            awaitItem()

            viewModel.onCategoryToggled(shelter.id)
            awaitItem()
            viewModel.onSearchQueryChange("stove")

            val content = awaitItem().content()
            assertTrue(content.gearList.isEmpty())
            assertTrue(content.hasGear)
        }
    }

    @Test
    fun `unknown category ids are ignored and a removed category clears that filter`() = runTest {
        emitInventory()
        val viewModel = viewModel()

        viewModel.uiState.test {
            skipItems(1)
            awaitItem()

            viewModel.onCategoryToggled("missing")
            this@runTest.advanceUntilIdle()
            expectNoEvents()

            viewModel.onCategoryToggled(shelter.id)
            val selected = awaitItem().content()
            assertEquals(setOf(shelter.id), selected.selectedCategoryIds)
            assertEquals(listOf("Tent", "Ultralight Tarp"), selected.gearList.map { it.name })

            viewModel.onCategoryToggled("missing")
            this@runTest.advanceUntilIdle()
            expectNoEvents()

            categoryRepository.emitGearCategories(listOf(cook))

            val dropped = awaitItem().content()
            assertEquals(emptySet<String>(), dropped.selectedCategoryIds)
            assertEquals(listOf("Tent", "Ultralight Tarp", "Stove"), dropped.gearList.map { it.name })
            assertTrue(dropped.hasGear)
        }
    }

    @Test
    fun `empty inventory is content with no gear`() = runTest {
        categoryRepository.emitGearCategories(listOf(shelter))
        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            val content = awaitItem().content()
            assertTrue(content.gearList.isEmpty())
            assertFalse(content.hasGear)
        }
    }

    @Test
    fun `retry shows loading then recovers after the repository stops failing`() = runTest {
        gearRepository.failure = IllegalStateException("database closed")
        categoryRepository.emitGearCategories(listOf(shelter))
        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            val error = awaitItem().error()
            assertEquals(DisplayError.NoGearFound, error.error)
            assertEquals("database closed", error.cause?.message)

            gearRepository.failure = null
            gearRepository.emit(listOf(tent))
            viewModel.onRetryClick()

            assertEquals(UiState.Loading, awaitItem())
            val content = awaitItem().content()
            assertEquals(listOf("Tent"), content.gearList.map { it.name })
            assertTrue(content.hasGear)
        }
    }

    @Test
    fun `new item click is logged`() {
        val viewModel = viewModel()

        viewModel.onNewItemClick()

        assertEquals(listOf("GEAR_SCREEN" to "onNewItemClick() triggered"), logger.info)
    }

    private fun emitInventory() {
        gearRepository.emit(listOf(tent, tarp, stove))
        categoryRepository.emitGearCategories(listOf(shelter, cook))
    }

    private fun viewModel() = GearViewModel(
        gearRepository = gearRepository,
        categoryRepository = categoryRepository,
        logger = logger,
    )
}

private fun UiState<GearScreenData>.content(): GearScreenData {
    assertTrue("Expected content but was $this", this is UiState.Content)
    return (this as UiState.Content).data
}

private fun UiState<GearScreenData>.error(): UiState.Error {
    assertTrue("Expected error but was $this", this is UiState.Error)
    return this as UiState.Error
}
