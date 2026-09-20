package com.kaylakautai.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaylakautai.database.model.PackingListItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PackingListItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: PackingListItemEntity)

    @Delete
    suspend fun deleteItem(item: PackingListItemEntity)

    @Query("UPDATE packing_list_items SET is_packed = :isPacked WHERE id = :id")
    suspend fun setPacked(id: String, isPacked: Boolean)

    /**
     * Returns a trip's list of packing list items (may be empty).
     * Packing list items are unordered - only the packed/unpacked state matters.
     */
    @Query("SELECT * FROM packing_list_items WHERE trip_id = :tripId")
    fun getForTrip(tripId: String): Flow<List<PackingListItemEntity>>
}