package com.example.meditrack.feature.domain.usecases.meditrack


import android.content.Context
import androidx.core.content.ContextCompat
import com.example.meditrack.R
import com.example.meditrack.feature.domain.model.ValidateTimeResult
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ValidateTime(private val context: Context) {
    operator fun invoke(
        selectedDate: String,
        time: String,
        calender: Calendar,
        timeType: Int,
        taskStartTime: Calendar,
    ): ValidateTimeResult {


        val currentDateTime = Calendar.getInstance()
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            .format(currentDateTime.time)
        val currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault())
            .format(currentDateTime.time)


        return if (selectedDate == currentDate && time == currentTime) {
            ValidateTimeResult(
                false,
                context.getString(R.string.current_time_msg)
            )
        } else if (selectedDate == currentDate && calender.before(currentDateTime)) {
            ValidateTimeResult(
                false,
                context.getString(R.string.time_for_today_out_msg)
            )
        }
        /*else if (selectedDate == currentDate && taskStartTime.after(taskEndTime)){
            onEvent(AddEditTaskEvent.OnSelectTimeDate(
                "Start time can't be after end time"
            ))
        } else if (selectedDate == currentDate && taskEndTime.before(taskStartTime)){
            onEvent(AddEditTaskEvent.OnSelectTimeDate(
                "End time can't be before start time"
            ))*/
        else {
            when (timeType) {

                1 -> {

                    if (calender.before(taskStartTime)) {

                        ValidateTimeResult(
                            false,
                            ContextCompat.getContextForLanguage(context)
                                .getString(R.string.end_time_before_start_time_msg)
                        )

                    } else if (time == SimpleDateFormat("hh:mm a", Locale.getDefault()).format(
                            taskStartTime.time
                        )
                    ) {

                        ValidateTimeResult(
                            false,
                            ContextCompat.getContextForLanguage(context)
                                .getString(R.string.end_time_as_start_time_msg)
                        )

                    } else {

                        ValidateTimeResult(
                            true,
                            ""
                        )
                    }

                }

                else -> {
                    ValidateTimeResult(false, "")
                }
            }
        }

    }
}
