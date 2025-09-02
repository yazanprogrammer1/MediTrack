package com.example.meditrack.feature.data.repository

import com.example.meditrack.feature.data.database.local.MedicineDao
import com.example.meditrack.feature.domain.model.Medicine
import com.example.meditrack.feature.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class RepositoryImpl(val medicineDao: MedicineDao) : Repository {
    override fun getAllMedicines(): Flow<List<Medicine>> {
        return medicineDao.getAllMedicines()
    }

    override suspend fun getMedicineById(id: Int): Medicine? {
        return medicineDao.getTaskById(id)
    }

    override suspend fun insertMedicine(task: Medicine) {
        medicineDao.insertMedicine(task)
    }

    override suspend fun insertMedicines(tasks: List<Medicine>) {
        medicineDao.insertMedicines(tasks)
    }

    override suspend fun deleteMedicine(task: Medicine) {
        medicineDao.deleteMedicine(task)
    }

    override suspend fun deleteMedicines(tasksIds: List<Int>) {
        medicineDao.deleteMedicines(tasksIds)
    }

    override suspend fun hasSameStartTime(date: String, time: String, id: Int?): Medicine? {
        return medicineDao.hasSameStartTime(date, time, id)
    }

    override suspend fun hasHighPriority(date: String, id: Int): Medicine? {
        return medicineDao.hasHighPriority(date, id)
    }

    override suspend fun getMedicinesId(startTime: String, date: String): Int {
        return medicineDao.getMedicineId(startTime, date)
    }

    override suspend fun markMedicineCompleted(medicineId: Int) {
        return medicineDao.markMedicineCompleted(medicineId)
    }

    override suspend fun markMedicineMISSED(medicineId: Int) {
        return medicineDao.markMedicineMISSED(medicineId)
    }


}