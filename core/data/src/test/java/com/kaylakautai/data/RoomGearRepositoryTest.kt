package com.kaylakautai.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.kaylakautai.data.repo.GearRepository
import com.kaylakautai.data.repo.RoomGearRepository
import com.kaylakautai.database.PikauDatabase
import com.kaylakautai.database.model.GearCategoryEntity
import com.kaylakautai.database.model.TripCategoryEntity
import com.kaylakautai.database.model.asModel
import com.kaylakautai.database.model.testGearCategoryEntity
import com.kaylakautai.database.model.testGearEntity
import com.kaylakautai.database.model.testTripCategoryEntity
import com.kaylakautai.model.Gear
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RoomGearRepositoryTest {

    private lateinit var database: PikauDatabase
    private lateinit var repository: GearRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PikauDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        repository = RoomGearRepository(
            gearDao = database.gearDao(),
            categoryDao = database.tripCategoryDao(),
            gearCategoryDao = database.gearCategoryDao()
        )
    }

    @After
    fun teardown() = database.close()

    //region Helpers
    private suspend fun seedDefaultGearCategory(): GearCategoryEntity {
        val category = testGearCategoryEntity()
        database.gearCategoryDao().insertAll(listOf(category))
        return category
    }

    private suspend fun seedDefaultTripCategory(): TripCategoryEntity {
        val category = testTripCategoryEntity()
        database.tripCategoryDao().insertAll(listOf(category))
        return category
    }
    //endRegion

    @Test
    fun `getAllGear - no gear exists - returns empty list`() = runTest {
        repository.getAllGear().test {
            assertEquals(emptyList<Gear>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getAllGear - one gear item - returns hydrated gear`() = runTest {
        val gearCategory = seedDefaultGearCategory()
        val suggestedCategory = seedDefaultTripCategory()

        val gearEntity = testGearEntity(
            categoryId = gearCategory.id,
            suggestedForIds = listOf(suggestedCategory.id)
        )
        database.gearDao().insertGear(gearEntity)

        repository.getAllGear().test {
            val gear = awaitItem()
            assertEquals(1, gear.size)
            assertEquals(gearEntity.id, gear.first().id)
            assertEquals(gearCategory.asModel(), gear.first().category)
            assertEquals(listOf(suggestedCategory.asModel()), gear.first().suggestedFor)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getAllGear - gear references missing category - excluded from list`() = runTest {
        // No category seeded at all
        val gearEntity = testGearEntity(categoryId = "nonexistent")
        database.gearDao().insertGear(gearEntity)

        repository.getAllGear().test {
            assertEquals(emptyList<Gear>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getAllGear - gear category is archived - still resolves`() = runTest {
        val archivedCategory = testGearCategoryEntity(isArchived = true)
        database.gearCategoryDao().insertAll(listOf(archivedCategory))

        val gearEntity = testGearEntity(categoryId = archivedCategory.id)
        database.gearDao().insertGear(gearEntity)

        repository.getAllGear().test {
            val gear = awaitItem()
            assertEquals(1, gear.size)
            assertEquals(archivedCategory.asModel(), gear.first().category)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getGearById - gear does not exist - returns null`() = runTest {
        repository.getGearById("nonexistent").test {
            assertEquals(null, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getGearById - gear exists - returns hydrated gear`() = runTest {
        val gearCategory = seedDefaultGearCategory()
        val gearEntity = testGearEntity(categoryId = gearCategory.id)
        database.gearDao().insertGear(gearEntity)

        repository.getGearById(gearEntity.id).test {
            val gear = awaitItem()
            assertEquals(gearEntity.id, gear?.id)
            assertEquals(gearCategory.asModel(), gear?.category)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getGearById - category missing - returns null`() = runTest {
        val gearEntity = testGearEntity(categoryId = "nonexistent")
        database.gearDao().insertGear(gearEntity)

        repository.getGearById(gearEntity.id).test {
            assertEquals(null, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `addGear - then getGearById - returns stored gear`() = runTest {
        val gearCategory = seedDefaultGearCategory().asModel()
        val gear = com.kaylakautai.model.testGear(category = gearCategory)

        repository.addGear(gear)

        database.gearDao().getGearById(gear.id).test {
            assertEquals(gear.id, awaitItem()?.id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updateGear - weight changed - persists change`() = runTest {
        val gearCategory = seedDefaultGearCategory().asModel()
        val gear = com.kaylakautai.model.testGear(category = gearCategory, weight = 500)
        repository.addGear(gear)

        repository.updateGear(gear.copy(weight = 750))

        database.gearDao().getGearById(gear.id).test {
            assertEquals(750, awaitItem()?.weight)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `deleteGear - gear exists - removed from repository`() = runTest {
        val gearCategory = seedDefaultGearCategory().asModel()
        val gear = com.kaylakautai.model.testGear(category = gearCategory)
        repository.addGear(gear)

        repository.deleteGear(gear.id)

        database.gearDao().getGearById(gear.id).test {
            assertEquals(null, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}