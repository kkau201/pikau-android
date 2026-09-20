package com.kaylakautai.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaylakautai.database.model.TripCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripCategoryDao {
    @Query("SELECT * FROM trip_categories WHERE is_archived = 0")
    fun getActive(): Flow<List<TripCategoryEntity>>

    @Query("SELECT * FROM trip_categories")
    fun getAll(): Flow<List<TripCategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(categories: List<TripCategoryEntity>)

    @Query("UPDATE trip_categories SET is_archived = 1 WHERE id = :id")
    suspend fun archive(id: String)
}