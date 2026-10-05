package com.kaylakautai.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.kaylakautai.data.repo.RoomTripRepository
import com.kaylakautai.data.repo.TripRepository
import com.kaylakautai.database.PikauDatabase
import com.kaylakautai.database.model.testTripCategoryEntity
import com.kaylakautai.database.model.testTripEntity
import com.kaylakautai.model.Trip
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.After
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
    fun `getPastTrips - no past trips - returns empty list`() = runTest {
        // No trips (past or upcoming) exist
        repository.getPastTrips(now).test {
            val trips = awaitItem()
            assertEquals(emptyList<Trip>(), trips)
            cancelAndIgnoreRemainingEvents()
        }

        // Add an upcoming trip
        database.tripDao().insertTrip(
            testTripEntity(
                startDate = now.plusDays(1)
            )
        )
        // Should still return an empty list for past trips
        repository.getPastTrips(now).test {
            val trips = awaitItem()
            assertEquals(emptyList<Trip>(), trips)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getPastTrips - one past trip - returns single trip`() = runTest {
        val categoryEntity = testTripCategoryEntity()
        database.tripCategoryDao().insertAll(listOf(categoryEntity))

        // Add a past trip
        val tripEntity = testTripEntity(
            startDate = now.minusDays(1),
            categoryIds = listOf(categoryEntity.id)
        )
        database.tripDao().insertTrip(tripEntity)
        // Add an upcoming trip
        database.tripDao().insertTrip(
            testTripEntity(
                id = "trip-2",
                startDate = now.plusDays(1)
            )
        )

        repository.getPastTrips(now).test {
            val trips = awaitItem()
            assertEquals(1, trips.size)
            assertEquals(tripEntity.id, trips.first().id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getPastTrips - multiple past trips - returns list of trips sorted by date`() = runTest {
        val categoryEntity = testTripCategoryEntity()
        database.tripCategoryDao().insertAll(listOf(categoryEntity))

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


}