package com.kaylakautai.database.util

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateTimeConverter {

    @TypeConverter
    fun toDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString)
    }

    @TypeConverter
    fun toDateString(date: LocalDate): String {
        return date.toString()
    }
}
