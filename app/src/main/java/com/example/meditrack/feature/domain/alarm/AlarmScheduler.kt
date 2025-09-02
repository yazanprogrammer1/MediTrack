package com.example.meditrack.feature.domain.alarm

import com.example.meditrack.feature.domain.model.Medicine


interface AlarmScheduler {
    fun scheduler(medicine: Medicine)
    fun cancel(medicine: Medicine)
}