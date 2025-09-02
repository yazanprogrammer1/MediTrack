package com.example.meditrack.feature.data.database.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.meditrack.feature.domain.model.Medicine
import kotlinx.coroutines.flow.Flow


@Dao
interface MedicineDao {

    @Query("SELECT * FROM medicine")
    fun getAllMedicines(): Flow<List<Medicine>>

    @Query("SELECT * FROM medicine WHERE id = :id")
    suspend fun getTaskById(id: Int): Medicine?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicine(medicine: Medicine)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicines(medicines: List<Medicine>)

    @Delete
    suspend fun deleteMedicine(medicine: Medicine)

    @Query("DELETE FROM medicine WHERE id in (:medicinesIds)")
    suspend fun deleteMedicines(medicinesIds: List<Int>)

    @Query("SELECT * FROM medicine WHERE startTime = :time AND startDate = :date And id NOT in (:id) LIMIT 1")
    suspend fun hasSameStartTime(date: String, time: String, id: Int?): Medicine?

    @Query("SELECT * FROM medicine WHERE startDate = :date AND priority = 1 AND id != :id LIMIT 1")
    suspend fun hasHighPriority(date: String, id: Int): Medicine?

    @Query("SELECT id FROM medicine WHERE startDate = :date AND startTime = :startTime")
    suspend fun getMedicineId(startTime: String, date: String): Int

    //...>
    @Query("UPDATE medicine SET isCompleted = 1 WHERE id = :medicineId")
    suspend fun markMedicineCompleted(medicineId: Int)

    @Query("UPDATE medicine SET isMissed = 1 WHERE id = :medicineId")
    suspend fun markMedicineMISSED(medicineId: Int)
}