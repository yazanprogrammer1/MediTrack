package com.example.meditrack.feature.domain.usecases.meditrack

import com.example.meditrack.feature.domain.repository.Repository

class DeleteMedicines(private val repository: Repository) {
    suspend operator fun invoke(tasksIds:List<Int>){
        repository.deleteMedicines(tasksIds)
    }
}