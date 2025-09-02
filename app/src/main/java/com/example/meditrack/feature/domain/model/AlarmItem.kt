package com.example.meditrack.feature.domain.model

import java.time.LocalTime

data class AlarmItem(
    val time:LocalTime,
    val name:String,
    val id:Int,
)