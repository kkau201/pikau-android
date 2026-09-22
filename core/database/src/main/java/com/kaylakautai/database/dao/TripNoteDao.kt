package com.kaylakautai.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaylakautai.database.model.TripNoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripNoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: TripNoteEntity)

    @Query("DELETE FROM trips WHERE id = :noteId")
    suspend fun deleteNote(noteId: String)

    @Query("SELECT * FROM trip_notes WHERE trip_id = :tripId ORDER BY created_at DESC")
    fun getForTrip(tripId: String): Flow<List<TripNoteEntity>>
}