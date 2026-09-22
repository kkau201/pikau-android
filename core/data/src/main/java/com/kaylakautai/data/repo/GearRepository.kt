package com.kaylakautai.data.repo

import com.kaylakautai.model.Gear
import kotlinx.coroutines.flow.Flow

interface GearRepository {

    fun getAllGear(): Flow<List<Gear>>

    fun getGearById(id: String): Flow<Gear?>

    suspend fun addGear(gear: Gear)

    suspend fun updateGear(gear: Gear)

    suspend fun deleteGear(id: String)
}