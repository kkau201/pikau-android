package com.kaylakautai.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaylakautai.database.model.FoodItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: FoodItemEntity)

    @Delete
    suspend fun deleteItem(item: FoodItemEntity)

    @Query("UPDATE food_items SET is_packed = :isPacked WHERE id = :id")
    suspend fun setPacked(id: String, isPacked: Boolean)

    /**
     * Returns a trip's list of food items to pack (may be empty).
     * Food items are unordered - only the packed/unpacked state matters.
     */
    @Query("SELECT * FROM food_items WHERE trip_id = :tripId")
    fun getForTrip(tripId: String): Flow<List<FoodItemEntity>>
}