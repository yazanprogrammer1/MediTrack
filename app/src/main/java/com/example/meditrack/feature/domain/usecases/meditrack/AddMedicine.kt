package com.example.meditrack.feature.domain.usecases.meditrack

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.meditrack.R
import com.example.meditrack.feature.domain.converters.LocalDateConverter
import com.example.meditrack.feature.domain.converters.LocalTimeConverter
import com.example.meditrack.feature.domain.model.Medicine
import com.example.meditrack.feature.domain.repository.Repository
import com.example.meditrack.feature.domain.utils.InvalidTaskException
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar


class AddMedicine(private val repository: Repository, private val context: Context) {

    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(InvalidTaskException::class)
    suspend operator fun invoke(medicine: Medicine) {

        val dateString = medicine.startDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        val currentDateString = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        val date = LocalDateConverter.toCalender(medicine.startDate)
        val currentDate = Calendar.getInstance()
        val startTime = LocalTimeConverter.toCalender(medicine.startTime)
        val currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"))

        if (!medicine.isRestored) {// the check here cuz if we want to restore deleted task we don't need to
            //validate it anymore cuz this validation when add new task or edit existing one
            if (medicine.name.isBlank()) {
                throw InvalidTaskException(
                    ContextCompat.getContextForLanguage(context).getString(R.string.empty_title_msg)
                )
            }

            if (medicine.description.isBlank()) {
                throw InvalidTaskException(
                    ContextCompat.getContextForLanguage(context)
                        .getString(R.string.empty_decription_msg)
                )
            }

            if (dateString != currentDateString && date.before(currentDate)) {
                throw InvalidTaskException(
                    ContextCompat.getContextForLanguage(context)
                        .getString(R.string.current_time_msg)
                )
            }

            if (medicine.startTime.format(DateTimeFormatter.ofPattern("hh:mm a")) == currentTime && dateString == currentDateString) {
                throw InvalidTaskException(
                    ContextCompat.getContextForLanguage(context)
                        .getString(R.string.current_time_start_time_msg)
                )
            }

            if (startTime.before(Calendar.getInstance()) && dateString == currentDateString) {
                throw InvalidTaskException(
                    ContextCompat.getContextForLanguage(context)
                        .getString(R.string.start_time_for_today_out_msg)
                )
            }

            if (repository.hasSameStartTime(
                    medicine.startDate.toString(),
                    medicine.startTime.toString(),
                    medicine.id
                ) != null
            ) {
                throw InvalidTaskException(
                    ContextCompat.getContextForLanguage(context)
                        .getString(R.string.task_same_start_time_for_date_msg)
                )
            }


            if (medicine.priority == 1) {
                if (repository.hasHighPriority(
                        medicine.startDate.toString(),
                        medicine.id
                    ) != null
                ) {
                    throw InvalidTaskException(
                        ContextCompat.getContextForLanguage(context)
                            .getString(R.string.high_priority_for_same_date_msg)
                    )
                }
            }
        }
        repository.insertMedicine(medicine)
    }
}