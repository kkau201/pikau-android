package com.kaylakautai.database.util

import androidx.room.TypeConverter
import com.kaylakautai.model.Difficulty
import kotlin.enums.enumEntries

class DifficultyConverter {
    @TypeConverter
    fun toDifficulty(value: String): Difficulty = Difficulty.valueOf(value)

    @TypeConverter
    fun fromDifficulty(value: Difficulty): String = value.name
}