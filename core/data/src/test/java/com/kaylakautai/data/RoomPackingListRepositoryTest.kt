package com.kaylakautai.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.kaylakautai.data.repo.PackingListRepository
import com.kaylakautai.data.repo.RoomGearRepository
import com.kaylakautai.data.repo.RoomPackingListRepository
import com.kaylakautai.database.PikauDatabase
import com.kaylakautai.database.model.PackingListItemEntity
import com.kaylakautai.database.model.testGearCategoryEntity
import com.kaylakautai.database.model.testGearEntity
import com.kaylakautai.database.model.testPackingListItemEntity
import com.kaylakautai.database.model.testTripEntity
import com.kaylakautai.model.PackingListItem
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RoomPackingListRepositoryTest {

    private lateinit var database: PikauDatabase
    private lateinit var repository: PackingListRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PikauDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        val gearRepository = RoomGearRepository(
            gearDao = database.gearDao(),
            categoryDao = database.tripCategoryDao(),
            gearCategoryDao = database.gearCategoryDao()
        )

        repository = RoomPackingListRepository(
            packingListItemDao = database.packingListItemDao(),
            gearRepository = gearRepository
        )

        runBlocking {
            database.tripDao().insertTrip(testTripEntity(id = "trip-1"))
        }
    }

    @After
    fun teardown() = database.close()

    @Test
    fun `getPackingList - no items - returns empty list`() = runTest {
        repository.getPackingList("trip-1").test {
            assertEquals(emptyList<PackingListItem>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getPackingList - one item - returns gear with categories`() = runTest {
        val gearCategory = testGearCategoryEntity()
        database.gearCategoryDao().insertAll(listOf(gearCategory))
        val gearEntity = testGearEntity(categoryId = gearCategory.id)
        database.gearDao().insertGear(gearEntity)
        val packListItemEntity = testPackingListItemEntity()
        database.packingListItemDao().insertItem(packListItemEntity)

        repository.getPackingList(packListItemEntity.tripId).test {
            val items = awaitItem()
            assertEquals(1, items.size)
            assertEquals(gearEntity.id, items.first().gear.id)
            assertEquals(false, items.first().isPacked)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getPackingList - referenced gear was deleted - item excluded`() = runTest {
        val gearCategory = testGearCategoryEntity()
        database.gearCategoryDao().insertAll(listOf(gearCategory))
        val gearEntity = testGearEntity(categoryId = gearCategory.id)
        database.gearDao().insertGear(gearEntity)
        val packListItemEntity = testPackingListItemEntity()
        database.packingListItemDao().insertItem(packListItemEntity)

        database.gearDao().deleteGear(gearEntity.id)

        repository.getPackingList(packListItemEntity.tripId).test {
            assertEquals(emptyList<PackingListItem>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `setPacked - item exists - toggles packed state`() = runTest {
        val gearCategory = testGearCategoryEntity()
        database.gearCategoryDao().insertAll(listOf(gearCategory))
        val gearEntity = testGearEntity(categoryId = gearCategory.id)
        database.gearDao().insertGear(gearEntity)
        val packListItemEntity = testPackingListItemEntity()
        database.packingListItemDao().insertItem(packListItemEntity)

        repository.setPacked(tripId = "trip-1", gearId = gearEntity.id, isPacked = true)

        database.packingListItemDao().getForTrip(packListItemEntity.tripId).test {
            assertEquals(true, awaitItem().first().isPacked)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `removeItem - item exists - removed from packing list`() = runTest {
        val gearCategory = testGearCategoryEntity()
        database.gearCategoryDao().insertAll(listOf(gearCategory))
        val gearEntity = testGearEntity(categoryId = gearCategory.id)
        database.gearDao().insertGear(gearEntity)
        val packListItemEntity = testPackingListItemEntity()
        database.packingListItemDao().insertItem(packListItemEntity)

        repository.removeItem(tripId = "trip-1", gearId = gearEntity.id)

        database.packingListItemDao().getForTrip(packListItemEntity.tripId).test {
            assertEquals(emptyList<PackingListItemEntity>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}