package com.kaylakautai.data.repo

import com.kaylakautai.model.TripNote
import kotlinx.coroutines.flow.Flow

interface TripNoteRepository {

    fun getNotesForTrip(tripId: String): Flow<List<TripNote>>

    suspend fun addNote(tripId: String, content: String)

    suspend fun deleteNote(id: String)

}