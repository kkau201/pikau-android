package com.kaylakautai.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.kaylakautai.data.repo.FoodRepository
import com.kaylakautai.data.repo.RoomFoodRepository
import com.kaylakautai.database.PikauDatabase
import com.kaylakautai.database.model.FoodItemEntity
import com.kaylakautai.database.model.testFoodItemEntity
import com.kaylakautai.database.model.testTripEntity
import com.kaylakautai.model.FoodListItem
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RoomFoodRepositoryTest {

    private lateinit var database: PikauDatabase
    private lateinit var repository: FoodRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PikauDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        repository = RoomFoodRepository(foodItemDao = database.foodItemDao())

        runBlocking {
            database.tripDao().insertTrip(testTripEntity(id = "trip-1"))
        }
    }

    @After
    fun teardown() = database.close()

    @Test
    fun `getFoodList - no items - returns empty list`() = runTest {
        repository.getFoodList("trip-1").test {
            assertEquals(emptyList<FoodListItem>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `addItem - item added - appears in food list unpacked`() = runTest {
        repository.addItem(tripId = "trip-1", name = "Trail mix")

        database.foodItemDao().getForTrip("trip-1").test {
            val items = awaitItem()
            assertEquals(1, items.size)
            assertEquals("Trail mix", items.first().name)
            assertEquals(false, items.first().isPacked)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `setPacked - item exists - toggles packed state`() = runTest {
        val foodEntity = testFoodItemEntity()
        database.foodItemDao().insertItem(foodEntity)

        repository.setPacked(foodEntity.id, isPacked = true)

        database.foodItemDao().getForTrip(foodEntity.tripId).test {
            assertEquals(true, awaitItem().first().isPacked)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `removeItem - item exists - removed from food list`() = runTest {
        val foodEntity = testFoodItemEntity()
        database.foodItemDao().insertItem(foodEntity)

        repository.removeItem(foodEntity.id)

        database.foodItemDao().getForTrip(foodEntity.tripId).test {
            assertEquals(emptyList<FoodItemEntity>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}