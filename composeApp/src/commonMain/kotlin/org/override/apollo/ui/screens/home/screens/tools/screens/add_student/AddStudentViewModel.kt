package org.override.apollo.ui.screens.home.screens.tools.screens.add_student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.override.apollo.domain.repositories.CourseRepository
import org.override.apollo.domain.repositories.StudentRepository
import org.override.apollo.utils.data.Student
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddStudentViewModel(
    private val courseRepository: CourseRepository,
    private val studentRepository: StudentRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddStudentState())
    val state = combine(
        _state,
        _state
    ) { currentState, _ ->
        currentState.copy(
            isFormValid = validateForm(currentState)
        )
    }
        .onStart { loadData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AddStudentState()
        )

    @OptIn(ExperimentalUuidApi::class)
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
                saveStudent(
                    student = Student(
                        id = Uuid.random().toString(),
                        displayName = _state.value.name,
                        tuition = _state.value.tuition,
                        email = _state.value.mail,
                        phone = _state.value.phone,
                        courseIds = listOf(_state.value.selectedOption?.id?: "")
                    ),
                    courseId = _state.value.selectedOption?.id?: ""
                )
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
        return state.name.isNotEmpty() && state.tuition.isNotEmpty() && state.selectedOption?.id != null
    }

    private fun loadData() {
        try {
            viewModelScope.launch {
                val courses = courseRepository.getCourses()
                _state.value = _state.value.copy(
                    options = courses.map { it }
                )
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun saveStudent(student: Student, courseId: String) {
        try {
            viewModelScope.launch {
                studentRepository.addStudent(
                    student = student,
                    courseId = courseId
                )
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}