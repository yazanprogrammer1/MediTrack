package com.example.meditrack.feature.domain.converters

import androidx.room.TypeConverter

object DaysConverter {
    @TypeConverter
    fun fromDaysList(days: List<String>): String {
        return days.joinToString(",")  // sun,mon,tue
    }

    @TypeConverter
    fun toDaysList(data: String): List<String> {
        return if (data.isEmpty()) emptyList() else data.split(",")
    }
}