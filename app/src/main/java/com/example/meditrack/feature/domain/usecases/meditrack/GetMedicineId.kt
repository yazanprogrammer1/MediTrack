package com.example.meditrack.feature.domain.usecases.meditrack

import com.example.meditrack.feature.domain.repository.Repository

class GetMedicineId (private val repository: Repository){
    suspend operator fun invoke(name:String, date:String):Int{
        return repository.getMedicinesId(name, date)
    }
}