package com.example.meditrack.feature.domain.usecases.meditrack

import com.example.meditrack.feature.domain.model.Medicine
import com.example.meditrack.feature.domain.repository.Repository


class GetMedicine(private val repository: Repository) {

    suspend operator fun invoke(id:Int): Medicine?{
       return repository.getMedicineById(id)
    }
}