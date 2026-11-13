package com.kaylakautai.data.testing

import com.kaylakautai.data.repo.GearRepository
import com.kaylakautai.model.Gear
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeGearRepository : GearRepository {
    private val gear = MutableStateFlow<List<Gear>>(emptyList())

    /** Thrown from [getAllGear] on the next collection. Clear it to let a retry succeed. */
    var failure: Throwable? = null

    fun emit(gear: List<Gear>) {
        this.gear.value = gear
    }

    override fun getAllGear(): Flow<List<Gear>> = gear.map { items ->
        failure?.let { throw it }
        items
    }

    override fun getGearById(id: String): Flow<Gear?> = gear.map { items ->
        failure?.let { throw it }
        items.find { it.id == id }
    }

    override suspend fun addGear(gear: Gear) {
        this.gear.update { it + gear }
    }

    override suspend fun updateGear(gear: Gear) {
        this.gear.update { current ->
            current.map { if (it.id == gear.id) gear else it }
        }
    }

    override suspend fun deleteGear(id: String) {
        gear.update { current -> current.filterNot { it.id == id } }
    }
}
