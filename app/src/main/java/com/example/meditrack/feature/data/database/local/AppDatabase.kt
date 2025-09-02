package com.example.meditrack.feature.data.database.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.meditrack.feature.domain.model.Medicine


@Database(entities = [Medicine::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val medicineDao: MedicineDao

    companion object {
        const val DATABASE_NAME = "app_database"
    }

}
