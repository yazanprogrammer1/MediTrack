package com.example.meditrack.feature.domain.repository


import com.example.meditrack.feature.domain.model.Medicine
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getAllMedicines(): Flow<List<Medicine>>


    suspend fun getMedicineById(id: Int): Medicine?


    suspend fun insertMedicine(medicine: Medicine)


    suspend fun insertMedicines(medicines: List<Medicine>)


    suspend fun deleteMedicine(medicine: Medicine)


    suspend fun deleteMedicines(tasksIds: List<Int>)


    suspend fun hasSameStartTime(date: String, time: String, id: Int?): Medicine?

    suspend fun hasHighPriority(date: String, id: Int): Medicine?

    suspend fun getMedicinesId(startTime: String, date: String): Int

    suspend fun markMedicineCompleted(medicineId: Int)

    suspend fun markMedicineMISSED(medicineId: Int)

}