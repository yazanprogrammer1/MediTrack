package com.example.meditrack.feature.domain.model

import android.content.Context
import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.meditrack.R
import com.example.meditrack.feature.domain.converters.DaysConverter
import com.example.meditrack.feature.domain.converters.LocalDateConverter
import com.example.meditrack.feature.domain.converters.LocalTimeConverter
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale

@Entity
@TypeConverters(
    LocalTimeConverter::class,
    LocalDateConverter::class,
    DaysConverter::class
)
@Parcelize
data class Medicine @RequiresApi(Build.VERSION_CODES.O) constructor(
    val name: String = "medicine name",
    val description: String = "medicine description",
    val strength: String = "medicine strength",
    val whenToTake: String = "Morning",    // Morning / Afternoon / Night
    val startTime: LocalTime = LocalTime.now(),
    val type: String = "Pill",          // Pill / Syrup / Injection
    val amount: String = "1",        // 1 / 2 / 3 ...
    val frequency: String = "Once daily",
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now(),
    val days: List<String> = emptyList(),     // ["sun", "mon", "wed"]
    val priority: Int = Priority.NORMAL.value,
    val isEnteredTime: Boolean = true,
    val isCompleted: Boolean = false,
    val isMissed: Boolean = false,
    val isRestored: Boolean = false,
    val isTimerFinished: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
) : Parcelable {


    @RequiresApi(Build.VERSION_CODES.O)
    fun getFormattedTime(): String {
        val st = SimpleDateFormat("hh:mm a", Locale.getDefault())
            .format(Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, startTime.hour)
                set(Calendar.MINUTE, startTime.minute)
            }.time)

        return "$st"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFormattedStartTime(): String {
        return SimpleDateFormat("hh:mm a", Locale.getDefault())
            .format(Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, startTime.hour)
                set(Calendar.MINUTE, startTime.minute)
            }.time)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getFormattedStartDate(): String {
        return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            .format(LocalDateConverter.toCalender(startDate).time)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFormattedEndDate(): String {
        return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            .format(LocalDateConverter.toCalender(endDate).time)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getStartEndDateDifferenceInDays(): Long {
        return ChronoUnit.DAYS.between(startDate, endDate)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getFormattedDuration(context: Context): String {
        val taskDurationDays = getStartEndDateDifferenceInDays()

        return if (taskDurationDays == 1L) {
            "1 ${context.getString(R.string.day)}"
        } else {
            "$taskDurationDays ${context.getString(R.string.days)}"
        }
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        val DEFAULT_TASK = Medicine(
            "Programming self-taught",
            "Work hard to get what you want, today is dream but tomorrow gonna be real",
            "strength",
            "Morning",
            LocalTime.now(),
            "Pill",
            "1",
            "Once daily",
            LocalDate.now(),
            LocalDate.now(),
            listOf("Sat", "Tus", "Mon"),
            Priority.NORMAL.value,
            true,
            false
        )

        @RequiresApi(Build.VERSION_CODES.O)
        val DEFAULT_TASKS = listOf(
            DEFAULT_TASK,
            Medicine(
                name = "Paracetamol",
                isEnteredTime = true,
                description = "Pain reliever and fever reducer",
                strength = "500mg",
                whenToTake = "Morning",
                startTime = LocalTime.now(),
                type = "Pill",
                amount = "1",
                frequency = "Twice daily",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                days = listOf("mon", "wed", "fri"),
                priority = 2,
                isCompleted = false,
                isMissed = false,
                isRestored = false,
                isTimerFinished = false,
                id = 1
            ),
            Medicine(
                name = "Vitamin C",
                isEnteredTime = false,
                description = "Boosts immunity",
                strength = "1000mg",
                whenToTake = "Afternoon",
                startTime = LocalTime.now(),
                type = "Pill",
                amount = "2",
                frequency = "Once daily",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                days = listOf("tue", "thu", "sat"),
                priority = 1,
                isCompleted = false,
                isMissed = false,
                isRestored = false,
                isTimerFinished = false,
                id = 2
            ),
            Medicine(
                name = "Ibuprofen",
                isEnteredTime = true,
                description = "Reduces inflammation",
                strength = "200mg",
                whenToTake = "Evening",
                startTime = LocalTime.now(),
                type = "Pill",
                amount = "1",
                frequency = "Thrice daily",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                days = listOf("mon", "tue", "wed", "thu", "fri"),
                priority = 3,
                isCompleted = false,
                isMissed = false,
                isRestored = false,
                isTimerFinished = false,
                id = 3
            )
        )

    }

}