package com.example.meditrack.feature.domain.usecases.meditrack

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.meditrack.R
import com.example.meditrack.feature.domain.converters.LocalDateConverter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ValidateDate(private val context: Context) {
    operator fun invoke(
        date: Long,
    ): String {

        val calendarDate = LocalDateConverter.millisToCalender(date)

        val dateString = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            .format(calendarDate.time)

        val currentDate = Calendar.getInstance()
        val currentDateString = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            .format(currentDate.time)

        return if (dateString == currentDateString) { //selectedDate.before(Calendar.getInstance())
            ""
        } else if (calendarDate.after(currentDate)) {
            ""
        } else {
            ContextCompat.getContextForLanguage(context).getString(R.string.invalid_date_msg)
        }
    }
}