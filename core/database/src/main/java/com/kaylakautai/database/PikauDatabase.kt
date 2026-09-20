package com.kaylakautai.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kaylakautai.database.dao.FoodItemDao
import com.kaylakautai.database.dao.GearCategoryDao
import com.kaylakautai.database.dao.GearDao
import com.kaylakautai.database.dao.PackingListItemDao
import com.kaylakautai.database.dao.TripCategoryDao
import com.kaylakautai.database.dao.TripDao
import com.kaylakautai.database.dao.TripNoteDao
import com.kaylakautai.database.model.FoodItemEntity
import com.kaylakautai.database.model.GearCategoryEntity
import com.kaylakautai.database.model.GearEntity
import com.kaylakautai.database.model.PackingListItemEntity
import com.kaylakautai.database.model.TripCategoryEntity
import com.kaylakautai.database.model.TripEntity
import com.kaylakautai.database.model.TripNoteEntity
import com.kaylakautai.database.util.DifficultyConverter
import com.kaylakautai.database.util.LocalDateConverter
import com.kaylakautai.database.util.LocalDateTimeConverter
import com.kaylakautai.database.util.StringListConverter

@Database(
    entities = [
        GearCategoryEntity::class,
        GearEntity::class,
        FoodItemEntity::class,
        PackingListItemEntity::class,
        TripEntity::class,
        TripCategoryEntity::class,
        TripNoteEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    DifficultyConverter::class,
    LocalDateConverter::class,
    LocalDateTimeConverter::class,
    StringListConverter::class
)
internal abstract class PikauDatabase : RoomDatabase() {
    abstract fun foodItemDao(): FoodItemDao
    abstract fun gearDao(): GearDao
    abstract fun gearCategoryDao(): GearCategoryDao
    abstract fun packingListItemDao(): PackingListItemDao
    abstract fun tripDao(): TripDao
    abstract fun tripCategoryDao(): TripCategoryDao
    abstract fun tripNoteDao(): TripNoteDao
}