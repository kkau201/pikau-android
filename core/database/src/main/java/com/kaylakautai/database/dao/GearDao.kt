package com.kaylakautai.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kaylakautai.database.model.GearEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GearDao {

    @Insert
    suspend fun insertGear(gear: GearEntity)

    @Update
    suspend fun updateGear(gear: GearEntity)

    @Query("DELETE FROM gear WHERE id = :gearId")
    suspend fun deleteGear(gearId: String)

    @Query("SELECT * FROM gear ORDER BY name ASC")
    fun getAllGear(): Flow<List<GearEntity>>

    @Query("SELECT * FROM gear WHERE id = :id")
    fun getGearById(id: String): Flow<GearEntity?>

}