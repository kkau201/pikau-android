package com.kaylakautai.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaylakautai.database.model.PackingListItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PackingListItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(item: PackingListItemEntity)

    @Query("DELETE FROM packing_list_items WHERE trip_id = :tripId AND gear_id = :gearId")
    suspend fun deleteByGear(tripId: String, gearId: String)

    @Query("UPDATE packing_list_items SET is_packed = :isPacked WHERE trip_id = :tripId AND gear_id = :gearId")
    suspend fun setPackedByGear(tripId: String, gearId: String, isPacked: Boolean)

    /**
     * Returns a trip's list of packing list items (may be empty).
     * Packing list items are unordered - only the packed/unpacked state matters.
     */
    @Query("SELECT * FROM packing_list_items WHERE trip_id = :tripId")
    fun getForTrip(tripId: String): Flow<List<PackingListItemEntity>>
}