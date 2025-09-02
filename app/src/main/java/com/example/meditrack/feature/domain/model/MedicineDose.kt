package com.example.meditrack.feature.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity
data class MedicineDose(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val medicineDateId: Int, // Foreign key (يربطها بتاريخ محدد)
    val time: LocalTime,
    val isTaken: Boolean = false,
)

