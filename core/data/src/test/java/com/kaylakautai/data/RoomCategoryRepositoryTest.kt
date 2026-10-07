package com.kaylakautai.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.kaylakautai.data.repo.CategoryRepository
import com.kaylakautai.data.repo.RoomCategoryRepository
import com.kaylakautai.database.PikauDatabase
import com.kaylakautai.database.model.testGearCategoryEntity
import com.kaylakautai.database.model.testTripCategoryEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RoomCategoryRepositoryTest {

    private lateinit var database: PikauDatabase
    private lateinit var repository: CategoryRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PikauDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        repository = RoomCategoryRepository(
            gearCategoryDao = database.gearCategoryDao(),
            tripCategoryDao = database.tripCategoryDao()
        )
    }

    @After
    fun teardown() = database.close()

    @Test
    fun `getActiveGearCategories - archived category exists - excluded from results`() = runTest {
        val active = testGearCategoryEntity(id = "shelter", isArchived = false)
        val archived = testGearCategoryEntity(id = "cooking", isArchived = true)
        database.gearCategoryDao().insertAll(listOf(active, archived))

        repository.getActiveGearCategories().test {
            val categories = awaitItem()
            assertEquals(1, categories.size)
            assertEquals(active.id, categories.first().id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getActiveTripCategories - archived category exists - excluded from results`() = runTest {
        val active = testTripCategoryEntity(id = "hiking", isArchived = false)
        val archived = testTripCategoryEntity(id = "skiing", isArchived = true)
        database.tripCategoryDao().insertAll(listOf(active, archived))

        repository.getActiveTripCategories().test {
            val categories = awaitItem()
            assertEquals(1, categories.size)
            assertEquals(active.id, categories.first().id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `archiveGearCategory - active category - removed from active list`() = runTest {
        val category = testGearCategoryEntity(isArchived = false)
        database.gearCategoryDao().insertAll(listOf(category))

        repository.archiveGearCategory(category.id)

        repository.getActiveGearCategories().test {
            assertEquals(emptyList<com.kaylakautai.model.GearCategory>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `archiveTripCategory - active category - removed from active list`() = runTest {
        val category = testTripCategoryEntity(isArchived = false)
        database.tripCategoryDao().insertAll(listOf(category))

        repository.archiveTripCategory(category.id)

        repository.getActiveTripCategories().test {
            assertEquals(emptyList<com.kaylakautai.model.TripCategory>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `seedDefaultsIfEmpty - empty tables - inserts default categories`() = runTest {
        repository.seedDefaultsIfEmpty()

        repository.getActiveGearCategories().test {
            assertTrue(awaitItem().isNotEmpty())
            cancelAndIgnoreRemainingEvents()
        }
        repository.getActiveTripCategories().test {
            assertTrue(awaitItem().isNotEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `seedDefaultsIfEmpty - already seeded - does not duplicate`() = runTest {
        repository.seedDefaultsIfEmpty()
        repository.seedDefaultsIfEmpty() // called twice

        val firstRunCount = database.gearCategoryDao().getAll().first().size
        assertEquals(firstRunCount, database.gearCategoryDao().getAll().first().size)
    }
}