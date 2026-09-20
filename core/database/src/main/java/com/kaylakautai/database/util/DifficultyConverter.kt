package com.kaylakautai.database.util

import androidx.room.TypeConverter
import com.kaylakautai.model.Difficulty
import kotlin.enums.enumEntries

class DifficultyConverter {
    @TypeConverter
    fun toDifficulty(value: Int): Difficulty {
        return enumEntries<Difficulty>()[value]
    }

    @TypeConverter
    fun fromDifficulty(value: Difficulty): Int {
        return value.ordinal
    }
}