package com.kaylakautai.data.repo

import com.kaylakautai.database.dao.TripNoteDao
import com.kaylakautai.database.model.TripNoteEntity
import com.kaylakautai.database.model.asModel
import com.kaylakautai.model.TripNote
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.util.UUID

internal class RoomTripNoteRepository @Inject constructor(
    private val tripNoteDao: TripNoteDao
) : TripNoteRepository {

    override fun getNotesForTrip(tripId: String): Flow<List<TripNote>> =
        tripNoteDao.getForTrip(tripId).map { it.map { entity -> entity.asModel() } }

    override suspend fun addNote(tripId: String, content: String) {
        tripNoteDao.insertNote(
            TripNoteEntity(
                id = UUID.randomUUID().toString(),
                tripId = tripId,
                content = content,
                createdAt = LocalDateTime.now()
            )
        )
    }

    override suspend fun deleteNote(id: String) = tripNoteDao.deleteNote(id)
}