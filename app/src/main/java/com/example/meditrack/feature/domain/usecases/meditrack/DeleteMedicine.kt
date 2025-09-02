package com.example.meditrack.feature.domain.usecases.meditrack

import com.example.meditrack.feature.domain.model.Medicine
import com.example.meditrack.feature.domain.repository.Repository

class DeleteMedicine(private val repository: Repository) {

    suspend operator fun invoke(medicine: Medicine) {
        repository.deleteMedicine(medicine)
    }
}