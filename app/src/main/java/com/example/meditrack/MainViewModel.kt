package com.example.meditrack

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meditrack.feature.domain.usecases.app_entry.AppEntryUseCases
import com.example.meditrack.feature.presentation.navgraph.Routs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appEntryUseCases: AppEntryUseCases,
) : ViewModel() {


    var splashCondition by mutableStateOf(true)
        private set

    var startDestination by mutableStateOf(Routs.AppStartNavigation.rout)
        private set

    init {
        appEntryUseCases.readAppEntry().onEach { shouldStartFromMainScreen ->
            if (shouldStartFromMainScreen) {
                startDestination = Routs.MedicineNavigation.rout
            } else {
                startDestination = Routs.AppStartNavigation.rout
            }
            delay(300)
            splashCondition = false
        }.launchIn(viewModelScope)
    }

}