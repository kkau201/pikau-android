package com.kaylakautai.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.kaylakautai.data.repo.RoomTripNoteRepository
import com.kaylakautai.data.repo.TripNoteRepository
import com.kaylakautai.database.PikauDatabase
import com.kaylakautai.database.model.TripNoteEntity
import com.kaylakautai.database.model.testTripEntity
import com.kaylakautai.model.TripNote
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RoomTripNoteRepositoryTest {

    private lateinit var database: PikauDatabase
    private lateinit var repository: TripNoteRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PikauDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        repository = RoomTripNoteRepository(tripNoteDao = database.tripNoteDao())

        runBlocking {
            database.tripDao().insertTrip(testTripEntity(id = "trip-1"))
        }
    }

    @After
    fun teardown() = database.close()

    @Test
    fun `getNotesForTrip - no notes - returns empty list`() = runTest {
        database.tripNoteDao().getForTrip("trip-1").test {
            assertEquals(emptyList<TripNote>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `addNote - note added - appears in trip notes`() = runTest {
        repository.addNote(tripId = "trip-1", content = "Check the weather forecast")

        database.tripNoteDao().getForTrip("trip-1").test {
            val notes = awaitItem()
            assertEquals(1, notes.size)
            assertEquals("Check the weather forecast", notes.first().content)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getNotesForTrip - multiple notes - returns newest first`() = runTest {
        repository.addNote(tripId = "trip-1", content = "First note")
        repository.addNote(tripId = "trip-1", content = "Second note")

        database.tripNoteDao().getForTrip("trip-1").test {
            val notes = awaitItem()
            assertEquals(2, notes.size)
            assertEquals("Second note", notes.first().content)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `deleteNote - note exists - removed from trip notes`() = runTest {
        repository.addNote(tripId = "trip-1", content = "Check the weather forecast")
        val noteId = database.tripNoteDao().getForTrip("trip-1").first().first().id

        repository.deleteNote(noteId)

        database.tripNoteDao().getForTrip("trip-1").test {
            assertEquals(emptyList<TripNoteEntity>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}