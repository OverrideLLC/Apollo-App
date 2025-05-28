package org.override.apollo.ui.screens.home.screens.tools.screens.add_student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class AddStudentViewModel : ViewModel() {

    private val _state = MutableStateFlow(AddStudentState())
    val state = combine(
        _state,
        _state
    ) { currentState, _ ->
        currentState.copy(
            isFormValid = validateForm(currentState)
        )
    }
        .onStart { }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AddStudentState()
        )

    fun onAction(action: AddStudentAction) {
        when (action) {
            is AddStudentAction.OnNameChanged -> {
                _state.value = _state.value.copy(name = action.name)
            }

            is AddStudentAction.OnTuitionChanged -> {
                _state.value = _state.value.copy(tuition = action.tuition)
            }

            is AddStudentAction.OnMailChanged -> {
                _state.value = _state.value.copy(mail = action.mail)
            }

            is AddStudentAction.OnPhoneChanged -> {
                _state.value = _state.value.copy(phone = action.phone)
            }

            is AddStudentAction.OnOptionSelected -> {
                _state.value = _state.value.copy(selectedOption = action.option)
            }

            is AddStudentAction.DropdownMenuTrigger -> {
                action.onClick()
            }

            is AddStudentAction.OnSaveStudent -> {
                // Lógica para guardar el estudiante
            }

            is AddStudentAction.OnCancel -> {
                // Lógica para cancelar la acción
            }

            is AddStudentAction.OnDismissError -> {
                // Lógica para ocultar el error
            }
        }
    }

    private fun validateForm(state: AddStudentState): Boolean {
        return state.name.isNotEmpty() && state.tuition.isNotEmpty() && state.selectedOption.isNotEmpty()
    }

}