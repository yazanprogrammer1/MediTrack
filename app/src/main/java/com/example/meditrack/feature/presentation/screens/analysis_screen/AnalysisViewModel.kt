package com.example.meditrack.feature.presentation.screens.analysis_screen

import android.os.Build
import android.util.Log.e
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meditrack.feature.domain.model.Medicine
import com.example.meditrack.feature.domain.usecases.meditrack.MedicineUseCases
import com.example.meditrack.feature.domain.utils.MedicineOrder
import com.example.meditrack.feature.domain.utils.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val useCases: MedicineUseCases,
) : ViewModel() {

    var job: Job? = null

    init {
        getTodayMedicines()
    }

    private val _todayMedicines = mutableStateOf(emptyList<Medicine>())
    val todayMedicines: State<List<Medicine>> = _todayMedicines

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTodayMedicines() {
        job?.cancel()
        job = useCases.getMedicines(MedicineOrder.Today(OrderType.Ascending)).onEach {
            try {
                _todayMedicines.value = it
            } catch (e: NullPointerException) {
                e("Yz", "${e.message} at a Analysis viewModel")
            } catch (e: Exception) {
                e("Yz", "${e.message} at a Analysis viewModel")
            }

        }.launchIn(viewModelScope)
    }
}


