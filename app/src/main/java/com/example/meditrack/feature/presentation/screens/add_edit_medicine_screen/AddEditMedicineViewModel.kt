package com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meditrack.R
import com.example.meditrack.feature.domain.alarm.AlarmScheduler
import com.example.meditrack.feature.domain.converters.LocalDateConverter
import com.example.meditrack.feature.domain.converters.LocalTimeConverter
import com.example.meditrack.feature.domain.model.Medicine
import com.example.meditrack.feature.domain.model.Priority
import com.example.meditrack.feature.domain.usecases.meditrack.MedicineUseCases
import com.example.meditrack.feature.domain.utils.InvalidTaskException
import com.example.meditrack.feature.presentation.screens.add_edit_medicine_screen.component.PriorityMenuState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class AddEditMedicineViewModel @Inject constructor(
    private val useCases: MedicineUseCases,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
) : ViewModel() {


    private var currentMedicineId: Int = 0

    @Inject
    lateinit var alarmScheduler: AlarmScheduler


    private val _medicineName = mutableStateOf(
        MedicineTextFieldState(
            hint = ContextCompat.getContextForLanguage(context).getString(R.string.medicine_name)
        )
    )
    val medicineName: State<MedicineTextFieldState> = _medicineName

    private val _medicineDescription = mutableStateOf(
        MedicineTextFieldState(
            hint = ContextCompat.getContextForLanguage(context)
                .getString(R.string.medicineNotes)
        )
    )
    val medicineDescription: State<MedicineTextFieldState> = _medicineDescription

    private val _medicineStrength = mutableStateOf(
        MedicineTextFieldState(
            hint = ContextCompat.getContextForLanguage(context)
                .getString(R.string.medicine_strength)
        )
    )
    val medicineStrength: State<MedicineTextFieldState> = _medicineStrength

    private val _medicineWhenToTake = mutableStateOf(
        MedicineTextFieldState(
            hint = ContextCompat.getContextForLanguage(context)
                .getString(R.string.medicine_WhenToTake)
        )
    )
    val medicineWhenToTake: State<MedicineTextFieldState> = _medicineWhenToTake

    private val _medicineType = mutableStateOf(
        MedicineTextFieldState(
            hint = ContextCompat.getContextForLanguage(context).getString(R.string.medicine_Type)
        )
    )
    val medicineType: State<MedicineTextFieldState> = _medicineType

    private val _medicineAmount = mutableStateOf(
        MedicineTextFieldState(
            hint = ContextCompat.getContextForLanguage(context).getString(R.string.medicine_Amount)
        )
    )
    val medicineAmount: State<MedicineTextFieldState> = _medicineAmount

    private val _medicineFrequency = mutableStateOf(
        MedicineTextFieldState(
            hint = ContextCompat.getContextForLanguage(context)
                .getString(R.string.medicine_frequency)
        )
    )
    val medicineFrequency: State<MedicineTextFieldState> = _medicineFrequency

    private val _medicineDays = mutableStateListOf<String>()
    val medicineDays: MutableList<String> = _medicineDays

    private val _medicineStartDate = mutableStateOf(
        MedicineTextState(
            text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                .format(Calendar.getInstance().time)
        )
    )
    val medicineStartDate: State<MedicineTextState> = _medicineStartDate

    private val _medicineEndDate = mutableStateOf(
        MedicineTextState(
            text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                .format(Calendar.getInstance().time)
        )
    )
    val medicineEndDate: State<MedicineTextState> = _medicineEndDate

    private val _medicineStartTime = mutableStateOf(
        MedicineTextState(
            text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
            //LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"))
        )
    )
    val medicineStartTime: State<MedicineTextState> = _medicineStartTime

    private val _medicinePriority = mutableStateOf(PriorityMenuState())
    val medicinePriority: State<PriorityMenuState> = _medicinePriority

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow

    private val _restButtonEnabled =
        derivedStateOf { medicineName.value.text.isNotBlank() || medicineDescription.value.text.isNotBlank() }
    val restButtonEnabled: State<Boolean> = _restButtonEnabled

    @RequiresApi(Build.VERSION_CODES.O)
    private val _pickerInitialStartDate = mutableLongStateOf(
        Instant.now().toEpochMilli()
    )

    @RequiresApi(Build.VERSION_CODES.O)
    val pickerInitialStartDate: State<Long> = _pickerInitialStartDate

    @RequiresApi(Build.VERSION_CODES.O)
    private val _pickerInitialEndDate = mutableLongStateOf(
        Instant.now().toEpochMilli()
    )
    val pickerInitialEndDate: State<Long> = _pickerInitialEndDate

    private val _calenderStartTime = mutableStateOf(
        Calendar.getInstance()
    )
    val calenderStartTime: State<Calendar> = _calenderStartTime

    init {
        savedStateHandle.get<Int>("medicineId")?.let { medicineId ->
            if (medicineId != -1) {
                viewModelScope.launch {
                    useCases.getMedicine(medicineId)?.also { medicine ->
                        currentMedicineId = medicine.id

                        Log.e("Yz", medicine.toString())

                        _medicineName.value = medicineName.value.copy(
                            text = medicine.name,
                            isHintVisible = false,
                            enabled = !medicine.isEnteredTime
                        )

                        _medicineDescription.value = medicineDescription.value.copy(
                            text = medicine.description,
                            isHintVisible = false,
                            enabled = !medicine.isEnteredTime
                        )

                        _medicineStrength.value = medicineStrength.value.copy(
                            text = medicine.strength,
                            isHintVisible = false,
                            enabled = !medicine.isEnteredTime
                        )

                        _medicineWhenToTake.value = medicineWhenToTake.value.copy(
                            text = medicine.whenToTake,
                            isHintVisible = false,
                            enabled = !medicine.isEnteredTime
                        )

                        _medicineType.value = medicineType.value.copy(
                            text = medicine.type,
                            isHintVisible = false,
                            enabled = !medicine.isEnteredTime
                        )

                        _medicineAmount.value = medicineAmount.value.copy(
                            text = medicine.amount,
                            isHintVisible = false,
                            enabled = !medicine.isEnteredTime
                        )

                        _medicineFrequency.value = medicineFrequency.value.copy(
                            text = medicine.frequency,
                            isHintVisible = false,
                            enabled = !medicine.isEnteredTime
                        )

                        _medicineDays.clear()
                        _medicineDays.addAll(medicine.days)

                        _medicineStartDate.value = MedicineTextState(
                            text = medicine.getFormattedStartDate(),
                            clickable = !medicine.isEnteredTime,
                            enabled = !medicine.isEnteredTime
                        )

                        _medicineEndDate.value = MedicineTextState(
                            text = medicine.getFormattedEndDate(),
                            clickable = !medicine.isEnteredTime,
                            enabled = !medicine.isEnteredTime
                        )

                        _medicineStartTime.value = MedicineTextState(
                            text = medicine.getFormattedStartTime(),
                            clickable = !medicine.isEnteredTime,
                            enabled = !medicine.isEnteredTime
                        )

                        _medicinePriority.value = PriorityMenuState(
                            priority = when (medicine.priority) {
                                Priority.NORMAL.value -> Priority.NORMAL
                                Priority.HIGH.value -> Priority.HIGH
                                Priority.LOW.value -> Priority.LOW
                                else -> {
                                    Priority.NORMAL
                                }
                            },
                            enabled = !medicine.isEnteredTime
                        )

                        _calenderStartTime.value = LocalTimeConverter.toCalender(medicine.startTime)

                        _pickerInitialStartDate.longValue =
                            LocalDateConverter.toCalender(medicine.startDate).timeInMillis

                        _pickerInitialEndDate.longValue =
                            LocalDateConverter.toCalender(medicine.startDate).timeInMillis

                    }
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: AddEditMedicineEvent) {
        when (event) {
            is AddEditMedicineEvent.EnteredTitle -> {
                _medicineName.value = medicineName.value.copy(
                    text = event.title
                )
            }

            is AddEditMedicineEvent.ChangeTitleFocus -> {
                _medicineName.value = medicineName.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            medicineName.value.text.isBlank(),
                    isFocused = event.focusState.isFocused
                )
            }

            is AddEditMedicineEvent.EnteredDescription -> {
                _medicineDescription.value = medicineDescription.value.copy(
                    text = event.description
                )
            }

            is AddEditMedicineEvent.ChangeDescriptionFocus -> {
                _medicineDescription.value = medicineDescription.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            medicineDescription.value.text.isBlank(),
                    isFocused = event.focusState.isFocused
                )
            }

            is AddEditMedicineEvent.EnteredStrength -> {
                _medicineStrength.value = medicineStrength.value.copy(
                    text = event.strength
                )
            }

            is AddEditMedicineEvent.ChangeStrengthFocus -> {
                _medicineStrength.value = medicineStrength.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            medicineStrength.value.text.isBlank(),
                    isFocused = event.focusState.isFocused
                )
            }

            is AddEditMedicineEvent.EnteredWhenToTake -> {
                _medicineWhenToTake.value = medicineWhenToTake.value.copy(
                    text = event.whenToTake
                )
            }

            is AddEditMedicineEvent.ChangeWhenToTakeFocus -> {
                _medicineWhenToTake.value = medicineWhenToTake.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            medicineWhenToTake.value.text.isBlank(),
                    isFocused = event.focusState.isFocused
                )
            }

            is AddEditMedicineEvent.EnteredType -> {
                _medicineType.value = medicineType.value.copy(
                    text = event.type
                )
            }

            is AddEditMedicineEvent.ChangeTypeFocus -> {
                _medicineType.value = medicineType.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            medicineType.value.text.isBlank(),
                    isFocused = event.focusState.isFocused
                )
            }

            is AddEditMedicineEvent.EnteredAmount -> {
                _medicineAmount.value = medicineAmount.value.copy(
                    text = event.amount
                )
            }

            is AddEditMedicineEvent.ChangeAmountFocus -> {
                _medicineAmount.value = medicineAmount.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            medicineAmount.value.text.isBlank(),
                    isFocused = event.focusState.isFocused
                )
            }

            is AddEditMedicineEvent.EnteredFrequency -> {
                _medicineFrequency.value = medicineFrequency.value.copy(
                    text = event.frequency
                )
            }

            is AddEditMedicineEvent.ChangeFrequencyFocus -> {
                _medicineFrequency.value = medicineFrequency.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            medicineFrequency.value.text.isBlank(),
                    isFocused = event.focusState.isFocused
                )
            }

            is AddEditMedicineEvent.EnteredDays -> {
                _medicineDays.clear()
                _medicineDays.addAll(event.days)
            }


            is AddEditMedicineEvent.EnteredStartDate -> {
                val validateResult = useCases.validateDate(event.dateLong)
                if (validateResult.isNotBlank()) {
                    viewModelScope.launch {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                validateResult
                            )
                        )
                    }
                } else {
                    _medicineStartDate.value = medicineStartDate.value.copy(
                        text = event.date
                    )
                    _pickerInitialStartDate.longValue = event.dateLong
                }
            }

            is AddEditMedicineEvent.EnteredEndDate -> {
                val validateResult = useCases.validateDate(event.dateLong)
                if (validateResult.isNotBlank()) {
                    viewModelScope.launch {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                validateResult
                            )
                        )
                    }
                } else {
                    _medicineEndDate.value = medicineEndDate.value.copy(
                        text = event.date
                    )
                    _pickerInitialEndDate.longValue = event.dateLong
                }
            }

            is AddEditMedicineEvent.EnteredStartTime -> {
                val validateResult = useCases.validateTime(
                    time = event.time,
                    calender = event.calenderTime,
                    selectedDate = medicineStartDate.value.text,
                    taskStartTime = calenderStartTime.value,
                    timeType = 1
                )

                if (!validateResult.isValid && validateResult.msg.isNotBlank()) {
                    viewModelScope.launch {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(validateResult.msg)
                        )
                    }
                } else {
                    _medicineStartTime.value = medicineStartTime.value.copy(
                        text = event.time
                    )

                    _calenderStartTime.value = event.calenderTime
                }
            }

            is AddEditMedicineEvent.ChoosePriority -> {
                _medicinePriority.value = medicinePriority.value.copy(
                    priority = event.priority
                )
            }

            is AddEditMedicineEvent.BackButton -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.BackButton)
                }
            }

            is AddEditMedicineEvent.ResetField -> {
                _medicineName.value = medicineName.value.copy(
                    text = "",
                    isHintVisible = medicineName.value.text.isBlank() || !medicineName.value.isFocused
                )
                _medicineDescription.value = medicineDescription.value.copy(
                    text = "",
                    isHintVisible = medicineDescription.value.text.isBlank() || !medicineDescription.value.isFocused
                )
                _medicineStartDate.value = medicineStartDate.value.copy(
                    text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                        .format(Calendar.getInstance().time)
                )
                _medicineEndDate.value = medicineEndDate.value.copy(
                    text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                        .format(Calendar.getInstance().time)
                )
                _medicineStartTime.value = medicineStartTime.value.copy(
                    text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
                )
                _medicinePriority.value = medicinePriority.value.copy(
                    priority = Priority.NORMAL
                )
            }

            is AddEditMedicineEvent.SaveTask -> {
                viewModelScope.launch {
                    try {
                        val medicine = Medicine(
                            name = medicineName.value.text,
                            description = medicineDescription.value.text,
                            startDate = LocalDateConverter.fromCalender(
                                LocalDateConverter.millisToCalender(
                                    _pickerInitialStartDate.value
                                )
                            ),
                            endDate = LocalDateConverter.fromCalender(
                                LocalDateConverter.millisToCalender(
                                    _pickerInitialEndDate.value
                                )
                            ),
                            strength = medicineStrength.value.text,
                            whenToTake = medicineWhenToTake.value.text,
                            type = medicineType.value.text,
                            amount = medicineAmount.value.text,
                            frequency = medicineFrequency.value.text,
                            days = medicineDays,
                            startTime = LocalTimeConverter.fromCalender(calenderStartTime.value),
                            priority = medicinePriority.value.priority.value,
                            isEnteredTime = false,
                            id = currentMedicineId
                        )

                        useCases.addMedicine(
                            medicine
                        )
                        _eventFlow.emit(UiEvent.SaveButton)

                        if (currentMedicineId != 0) {
                            Log.e("Yz", "the current task id  :$currentMedicineId")
                            useCases.scheduleReminder.schedule(medicine)
                        } else {
                            val id =
                                useCases.getMedicineId(
                                    medicine.startTime.toString(),
                                    medicine.startDate.toString()
                                )
                            Log.e("Yz", "the id in else  :$id")

                            useCases.scheduleReminder.schedule(medicine.copy(id = id))

                        }


                    } catch (e: InvalidTaskException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message
                            )
                        )
                    }
                }
            }

        }
    }


    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object SaveButton : UiEvent()
        object BackButton : UiEvent()
    }
}