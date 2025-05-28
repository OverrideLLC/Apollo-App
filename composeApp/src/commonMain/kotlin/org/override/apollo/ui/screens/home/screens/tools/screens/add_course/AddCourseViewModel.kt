package org.override.apollo.ui.screens.home.screens.tools.screens.add_course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.override.apollo.domain.repositories.CourseRepository
import org.override.apollo.utils.data.Course
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddCourseViewModel(
    private val repository: CourseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddCourseState())

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
            initialValue = AddCourseState()
        )

    fun onAction(action: AddCourseAction) {
        when (action) {
            is AddCourseAction.OnCourseNameChanged -> {
                _state.value = _state.value.copy(
                    courseName = action.name,
                    errorMessage = null
                )
            }

            AddCourseAction.OnSaveCourse -> {
                saveCourse()
            }

            AddCourseAction.OnCancel -> {
                // Lógica para cerrar el diálogo
                // Esto debería ser manejado por el componente padre
            }

            AddCourseAction.OnDismissError -> {
                _state.value = _state.value.copy(errorMessage = null)
            }

            is AddCourseAction.OnCareerChanged -> {
                _state.value = _state.value.copy(
                    career = action.career,
                    errorMessage = null
                )
            }
            is AddCourseAction.OnGradeChanged -> {
                _state.value = _state.value.copy(
                    grade = action.grade,
                    errorMessage = null
                )
            }
            is AddCourseAction.OnSectionChanged -> {
                _state.value = _state.value.copy(
                    section = action.section,
                    errorMessage = null
                )
            }
        }
    }

    private fun validateForm(state: AddCourseState): Boolean {
        return state.courseName.isNotBlank() &&
                state.career.isNotBlank() &&
                state.section.isNotBlank() &&
                state.grade.isNotBlank()
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun saveCourse() {
        val currentState = _state.value

        if (!validateForm(currentState)) {
            _state.value = currentState.copy(
                errorMessage = "Por favor, completa todos los campos obligatorios"
            )
            return
        }

        viewModelScope.launch {
            _state.value = currentState.copy(isLoading = true, errorMessage = null)

            try {
                repository.createCourse(
                    Course(
                        id = Uuid.random().toString(),
                        name = currentState.courseName,
                        career = currentState.career,
                        section = currentState.section,
                        degree = currentState.grade,
                        teacherId = "TEACHER_007",
                        studentIds = emptyList(),
                        attendanceIds = emptyMap(),
                        assignmentsIds = emptyList()
                    )
                )
                resetState()
            } catch (e: Exception) {
                _state.value = currentState.copy(
                    isLoading = false,
                    errorMessage = "Error al crear el curso: ${e.message}"
                )
            }
        }
    }

    fun resetState() {
        _state.value = AddCourseState()
    }
}