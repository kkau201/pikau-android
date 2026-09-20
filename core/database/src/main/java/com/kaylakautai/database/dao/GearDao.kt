package com.kaylakautai.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kaylakautai.database.model.GearEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GearDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGear(gear: GearEntity)

    @Delete
    suspend fun deleteGear(gear: GearEntity)

    @Query("SELECT * FROM gear ORDER BY name ASC")
    fun getAllGear(): Flow<List<GearEntity>>

}