package com.elm.projectmanagment.data.local

import androidx.room.TypeConverter
import androidx.room.TypeConverters

class Converters {
    @TypeConverter
    fun fromList(value: List<String>?): String? {
        return value?.joinToString(separator = ",")?:""
    }

    @TypeConverter
    fun toList(value: String?): List<String> {
        return value?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
    }

    @TypeConverter
    fun toDate(value: Long?): java.util.Date? {
        return value?.let { java.util.Date(it) }
    }

    @TypeConverter
    fun fromDate(date: java.util.Date?): Long? {
        return date?.time
    }
}