package com.kaylakautai.data

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.kaylakautai.data.repo.RoomTripRepository
import com.kaylakautai.data.repo.TripRepository
import com.kaylakautai.database.PikauDatabase
import com.kaylakautai.database.model.FoodItemEntity
import com.kaylakautai.database.model.PackingListItemEntity
import com.kaylakautai.database.model.TripCategoryEntity
import com.kaylakautai.database.model.TripNoteEntity
import com.kaylakautai.database.model.asModel
import com.kaylakautai.database.model.testFoodItemEntity
import com.kaylakautai.database.model.testPackingListItemEntity
import com.kaylakautai.database.model.testTripCategoryEntity
import com.kaylakautai.database.model.testTripEntity
import com.kaylakautai.database.model.testTripNoteEntity
import com.kaylakautai.model.Trip
import com.kaylakautai.model.TripCategory
import com.kaylakautai.model.testTrip
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
class RoomTripRepositoryTest {

    private lateinit var database: PikauDatabase
    private lateinit var repository: TripRepository

    private val now: LocalDate = LocalDate.now()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PikauDatabase::class.java)
            .allowMainThreadQueries() // test-only; DO NOT do this in prod
            .build()

        repository = RoomTripRepository(
            tripDao = database.tripDao(),
            categoryDao = database.tripCategoryDao()
        )
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun `getPastTrips - no trips exist - returns empty list`() = runTest {
        repository.getPastTrips(now).test {
            val trips = awaitItem()
            assertEquals(emptyList<Trip>(), trips)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getPastTrips - only upcoming trips exist - returns empty list`() = runTest {
        // Add an upcoming trip only
        database.tripDao().insertTrip(
            testTripEntity(
                startDate = now.plusDays(1)
            )
        )

        // Should return an empty list for past trips
        repository.getPastTrips(now).test {
            val trips = awaitItem()
            assertEquals(emptyList<Trip>(), trips)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getPastTrips - trip starts today - excluded from past`() = runTest {
        val categoryEntity = seedDefaultCategoryEntity()

        database.tripDao().insertTrip(
            testTripEntity(
                categoryIds = listOf(categoryEntity.id),
                startDate = now
            )
        )

        // Should return an empty list for past trips
        repository.getPastTrips(now).test {
            val trips = awaitItem()
            assertEquals(emptyList<Trip>(), trips)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getPastTrips - one past trip - returns single trip`() = runTest {
        val categoryEntity = seedDefaultCategoryEntity()

        // Add a past trip
        val tripEntity = testTripEntity(
            startDate = now.minusDays(1),
            categoryIds = listOf(categoryEntity.id)
        )
        database.tripDao().insertTrip(tripEntity)

        repository.getPastTrips(now).test {
            val trips = awaitItem()
            assertEquals(1, trips.size)
            assertEquals(tripEntity.id, trips.first().id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getPastTrips - multiple past trips - returns list of trips sorted by date`() = runTest {
        val categoryEntity = seedDefaultCategoryEntity()

        val tripEntity1 = testTripEntity(
            categoryIds = listOf(categoryEntity.id),
            startDate = now.minusDays(3)
        )
        val tripEntity2 = testTripEntity(
            id = "trip-2",
            categoryIds = listOf(categoryEntity.id),
            startDate = now.minusDays(2)
        )
        val tripEntity3 = testTripEntity(
            id = "trip-3",
            categoryIds = listOf(categoryEntity.id),
            startDate = now.minusDays(1)
        )
        val tripEntity4 = testTripEntity(
            id = "trip-4",
            categoryIds = listOf(categoryEntity.id),
            startDate = now.plusDays(1)
        )
        listOf(tripEntity1, tripEntity2, tripEntity3, tripEntity4).forEach {
            database.tripDao().insertTrip(it)
        }

        repository.getPastTrips(now).test {
            val trips = awaitItem()
            assertEquals(3, trips.size)
            assertEquals(tripEntity3.id, trips.first().id)
            assertEquals(tripEntity2.id, trips[1].id)
            assertEquals(tripEntity1.id, trips[2].id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getPastTrips - trip contains an archived category - return single trip with no error`() =
        runTest {
            val categoryEntity = testTripCategoryEntity()
            val archivedCategory = testTripCategoryEntity(
                id = "tramping",
                displayText = "Tramping",
                isArchived = true
            )
            val allCategoryEntities = listOf(categoryEntity, archivedCategory)
            database.tripCategoryDao().insertAll(allCategoryEntities)

            // Add a past trip
            val tripEntity = testTripEntity(
                startDate = now.minusDays(1),
                categoryIds = allCategoryEntities.map { it.id }
            )
            database.tripDao().insertTrip(tripEntity)

            repository.getPastTrips(now).test {
                val trips = awaitItem()
                assertEquals(1, trips.size)
                assertEquals(tripEntity.id, trips.first().id)
                assertEquals(
                    allCategoryEntities.map { it.asModel() },
                    trips.first().categories
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `getUpcomingTrips - no trips exist - returns empty list`() = runTest {
        repository.getUpcomingTrips(now).test {
            val trips = awaitItem()
            assertEquals(emptyList<Trip>(), trips)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getUpcomingTrips - only past trips exist - returns empty list`() = runTest {
        // Add an past trip only
        database.tripDao().insertTrip(
            testTripEntity(
                startDate = now.minusDays(1)
            )
        )

        // Should return an empty list for upcoming trips
        repository.getUpcomingTrips(now).test {
            val trips = awaitItem()
            assertEquals(emptyList<Trip>(), trips)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getUpcomingTrips - trip starts today - included in upcoming list`() = runTest {
        val categoryEntity = seedDefaultCategoryEntity()

        val tripEntity = testTripEntity(
            categoryIds = listOf(categoryEntity.id),
            startDate = now
        )
        database.tripDao().insertTrip(tripEntity)

        repository.getUpcomingTrips(now).test {
            val trips = awaitItem()
            assertEquals(1, trips.size)
            assertEquals(tripEntity.id, trips.first().id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getUpcomingTrips - one upcoming trip - returns single trip`() = runTest {
        val categoryEntity = seedDefaultCategoryEntity()

        // Add a upcoming trip
        val tripEntity = testTripEntity(
            startDate = now.plusDays(1),
            categoryIds = listOf(categoryEntity.id)
        )
        database.tripDao().insertTrip(tripEntity)

        repository.getUpcomingTrips(now).test {
            val trips = awaitItem()
            assertEquals(1, trips.size)
            assertEquals(tripEntity.id, trips.first().id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getUpcomingTrips - multiple upcoming trips - returns list of trips sorted by date`() =
        runTest {
            val categoryEntity = seedDefaultCategoryEntity()

            val tripEntity1 = testTripEntity(
                categoryIds = listOf(categoryEntity.id),
                startDate = now.plusDays(3)
            )
            val tripEntity2 = testTripEntity(
                id = "trip-2",
                categoryIds = listOf(categoryEntity.id),
                startDate = now.plusDays(2)
            )
            val tripEntity3 = testTripEntity(
                id = "trip-3",
                categoryIds = listOf(categoryEntity.id),
                startDate = now.plusDays(1)
            )
            val tripEntity4 = testTripEntity(
                id = "trip-4",
                categoryIds = listOf(categoryEntity.id),
                startDate = now.minusDays(1)
            )
            listOf(tripEntity1, tripEntity2, tripEntity3, tripEntity4).forEach {
                database.tripDao().insertTrip(it)
            }

            repository.getUpcomingTrips(now).test {
                val trips = awaitItem()
                assertEquals(3, trips.size)
                assertEquals(tripEntity3.id, trips.first().id)
                assertEquals(tripEntity2.id, trips[1].id)
                assertEquals(tripEntity1.id, trips[2].id)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `getTripById - trip with matching ID exists - returns trip`() = runTest {
        val categoryEntity = seedDefaultCategoryEntity()

        // Add a trip
        val tripEntity = testTripEntity(
            startDate = now.plusDays(1),
            categoryIds = listOf(categoryEntity.id)
        )
        database.tripDao().insertTrip(tripEntity)

        repository.getTripById(tripEntity.id).test {
            val trip = awaitItem()
            assertEquals(tripEntity.id, trip?.id)
            assertEquals(listOf(categoryEntity.asModel()), trip?.categories)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getTripById - trip does not exist - returns null`() = runTest {
        repository.getTripById("false-id").test {
            val trip = awaitItem()
            assertEquals(null, trip?.id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `addTrip - single trip added - successfully stored in database`() = runTest {
        val category = seedDefaultCategoryEntity().asModel()
        val trip = testTrip(categories = listOf(category))

        repository.addTrip(trip)
        database.tripDao().getTripById(trip.id).test {
            val tripEntity = awaitItem()
            assertEquals(trip.id, tripEntity?.id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `addTrip - no categories - stores trip with empty category list`() = runTest {
        val trip = testTrip(categories = emptyList())

        repository.addTrip(trip)

        repository.getTripById(trip.id).test {
            val result = awaitItem()
            assertEquals(emptyList<TripCategory>(), result?.categories)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = SQLiteConstraintException::class)
    fun `addTrip - duplicate id - throws`() = runTest {
        val trip = testTrip()
        repository.addTrip(trip)
        repository.addTrip(trip) // same id again
    }

    @Test
    fun `updateTrip - name changed - persists change without altering id`() = runTest {
        val categoryEntity = seedDefaultCategoryEntity()
        val original = testTrip(categories = listOf(categoryEntity.asModel()))
        repository.addTrip(original)

        val updated = original.copy(name = "Updated Trip Name")
        repository.updateTrip(updated)

        repository.getTripById(original.id).test {
            val trip = awaitItem()
            assertEquals(original.id, trip?.id)
            assertEquals("Updated Trip Name", trip?.name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updateTrip - trip has packing list items - items survive the update`() = runTest {
        val trip = testTrip()
        repository.addTrip(trip)

        val packingItem = testPackingListItemEntity(tripId = trip.id)
        database.packingListItemDao().insertItem(packingItem)

        repository.updateTrip(trip.copy(name = "Renamed"))

        database.packingListItemDao().getForTrip(trip.id).test {
            val items = awaitItem()
            assertEquals(1, items.size)
            assertEquals(packingItem.id, items.first().id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `deleteTrip - trip exists - removed from repository`() = runTest {
        val trip = testTrip()
        repository.addTrip(trip)

        repository.deleteTrip(trip.id)

        repository.getTripById(trip.id).test {
            assertEquals(null, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `deleteTrip - trip has packing list items - items are cascade deleted`() = runTest {
        val trip = testTrip()
        repository.addTrip(trip)

        val packingItem = testPackingListItemEntity(tripId = trip.id)
        database.packingListItemDao().insertItem(packingItem)

        repository.deleteTrip(trip.id)

        database.packingListItemDao().getForTrip(trip.id).test {
            assertEquals(emptyList<PackingListItemEntity>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `deleteTrip - trip has food items - items are cascade deleted`() = runTest {
        val trip = testTrip()
        repository.addTrip(trip)

        val foodItem = testFoodItemEntity(tripId = trip.id)
        database.foodItemDao().insertItem(foodItem)

        repository.deleteTrip(trip.id)

        database.foodItemDao().getForTrip(trip.id).test {
            assertEquals(emptyList<FoodItemEntity>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `deleteTrip - trip has notes - notes are cascade deleted`() = runTest {
        val trip = testTrip()
        repository.addTrip(trip)

        val note = testTripNoteEntity(tripId = trip.id)
        database.tripNoteDao().insertNote(note)

        repository.deleteTrip(trip.id)

        database.tripNoteDao().getForTrip(trip.id).test {
            assertEquals(emptyList<TripNoteEntity>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Helpers
    private suspend fun seedDefaultCategoryEntity(): TripCategoryEntity {
        val categoryEntity = testTripCategoryEntity()
        database.tripCategoryDao().insertAll(listOf(categoryEntity))
        return categoryEntity
    }

}