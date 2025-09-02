package com.example.meditrack.feature.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.meditrack.AlarmReceiver
import com.example.meditrack.feature.domain.alarm.AlarmScheduler
import com.example.meditrack.feature.domain.converters.LocalDateConverter
import com.example.meditrack.feature.domain.model.Medicine
import java.util.Calendar

class MedicineAlarmScheduler(
    private val context: Context,
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun scheduler(medicine: Medicine) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("medicine", medicine)
        }

        Log.e("ks", "before setExact ")
        val timeForDate = LocalDateConverter.toCalender(medicine.startDate).apply {
            set(Calendar.HOUR_OF_DAY, medicine.startTime.hour)
            set(Calendar.MINUTE, medicine.startTime.minute)
            set(Calendar.SECOND, 0)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeForDate.timeInMillis,
            PendingIntent.getBroadcast(
                context,
                medicine.id.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(medicine: Medicine) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                medicine.id.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}