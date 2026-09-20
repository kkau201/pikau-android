package com.kaylakautai.database.util

import androidx.room.TypeConverter
import java.time.LocalDateTime

class LocalDateTimeConverter {
    @TypeConverter
    fun toDateTime(value: String): LocalDateTime = LocalDateTime.parse(value)

    @TypeConverter
    fun toDateTimeString(value: LocalDateTime): String = value.toString()
}