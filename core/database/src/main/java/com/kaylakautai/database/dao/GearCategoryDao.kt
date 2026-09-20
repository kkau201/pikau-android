package com.kaylakautai.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaylakautai.database.model.GearCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GearCategoryDao {
    @Query("SELECT * FROM gear_categories WHERE is_archived = 0")
    fun getActive(): Flow<List<GearCategoryEntity>>

    @Query("SELECT * FROM gear_categories")
    fun getAll(): Flow<List<GearCategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(categories: List<GearCategoryEntity>)

    @Query("UPDATE gear_categories SET is_archived = 1 WHERE id = :id")
    suspend fun archive(id: String)
}