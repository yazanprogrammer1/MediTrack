package com.example.meditrack.feature.domain.usecases.meditrack

import com.example.meditrack.feature.domain.alarm.AlarmScheduler
import com.example.meditrack.feature.domain.model.Medicine

class ScheduleReminder(private val alarmScheduler: AlarmScheduler) {

    fun schedule(medicine: Medicine) {
        alarmScheduler.scheduler(medicine)
    }

    fun cancel(medicine: Medicine) {
        alarmScheduler.cancel(medicine)
    }
}