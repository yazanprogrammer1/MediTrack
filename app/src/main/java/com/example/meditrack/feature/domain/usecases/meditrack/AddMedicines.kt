package com.example.meditrack.feature.domain.usecases.meditrack

import com.example.meditrack.feature.domain.model.Medicine
import com.example.meditrack.feature.domain.repository.Repository

class AddMedicines(private val repository: Repository) {
    suspend operator fun invoke(medicines: List<Medicine>) {
        repository.insertMedicines(medicines)
    }
}