package com.example.meditrack.feature.domain.converters

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

object LocalDateConverter {

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    @JvmStatic
    fun fromString(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    @JvmStatic
    fun toString(value: LocalDate?): String? {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return value?.format(formatter)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun fromCalender(calender: Calendar): LocalDate {
        return LocalDate.of(
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH) + 1,// +1 here cuz when we convert calender to LocaleDate
            // the Calender.MONTH give u the month before current month and the opposite
            // if u want to convert from LocaleDate to Calender u should -1 as in
            // getFormattedDate in medicine data class
            calender.get(Calendar.DAY_OF_MONTH)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toCalender(date: LocalDate): Calendar {
        return Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, date.dayOfMonth)
            set(Calendar.MONTH, date.monthValue - 1)
            set(Calendar.YEAR, date.year)
        }
    }

    fun millisToCalender(timeMillis: Long): Calendar {
        return Calendar.getInstance().apply {
            timeInMillis = timeMillis
        }
    }
}



