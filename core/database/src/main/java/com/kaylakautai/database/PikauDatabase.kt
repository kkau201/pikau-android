package com.kaylakautai.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kaylakautai.database.dao.TripDao
import com.kaylakautai.database.model.TripEntity
import com.kaylakautai.database.util.CategoryListConverter
import com.kaylakautai.database.util.DifficultyConverter
import com.kaylakautai.database.util.LocalDateTimeConverter

@Database(
    entities = [
        TripEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    LocalDateTimeConverter::class,
    DifficultyConverter::class,
    CategoryListConverter::class
)
internal abstract class PikauDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
}