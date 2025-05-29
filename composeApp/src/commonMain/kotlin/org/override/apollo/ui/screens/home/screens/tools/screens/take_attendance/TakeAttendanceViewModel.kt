package org.override.apollo.ui.screens.home.screens.tools.screens.take_attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.shreyaspatil.ai.client.generativeai.common.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.override.apollo.domain.repositories.CourseRepository
import org.override.apollo.domain.repositories.StudentRepository

class TakeAttendanceViewModel(
    private val courseRepository: CourseRepository,
    private val studentRepository: StudentRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TakeAttendanceState())
    val state = _state
        .onStart { loadCourses() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = TakeAttendanceState()
        )

    fun onAction(action: TakeAttendanceAction) {
        when (action) {
            is TakeAttendanceAction.LoadCourses -> {
                loadCourses()
            }

            is TakeAttendanceAction.SelectCourse -> {
                selectCourse(action.courseId)
            }

            is TakeAttendanceAction.SelectAttendanceMethod -> {
                _state.update {
                    it.copy(selectedMethod = action.method)
                }
            }

            is TakeAttendanceAction.ToggleStudentAttendance -> {
                toggleStudentAttendance(action.studentId)
            }

            is TakeAttendanceAction.MarkAllPresent -> {
                markAllPresent(action.present)
            }

            is TakeAttendanceAction.SaveAttendance -> {
                saveAttendance()
            }
        }
    }

    private fun saveAttendance() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, isAttendanceSaved = false) }

            try {
                // Simular el guardado de la asistencia
                delay(1500)

                // Aquí conectarías con tu repositorio real para guardar la asistencia
                // repository.saveAttendance(selectedCourse.id, attendanceRecords)

                _state.update {
                    it.copy(
                        isLoading = false,
                        isAttendanceSaved = true,
                        error = null
                    )
                }

                // Ocultar el mensaje de éxito después de 3 segundos
                delay(3000)
                _state.update { it.copy(isAttendanceSaved = false) }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al guardar la asistencia: ${e.message}",
                        isAttendanceSaved = false
                    )
                }
            }
        }
    }

    private fun markAllPresent(present: Boolean) {
        val currentStudents = _state.value.students
        if (currentStudents.isEmpty()) return

        val newAttendanceRecords = currentStudents.associate { student ->
            student.id to present
        }

        _state.update {
            it.copy(
                attendanceRecords = newAttendanceRecords,
                error = null
            )
        }
    }

    private fun toggleStudentAttendance(studentId: String) {
        val currentRecords = _state.value.attendanceRecords
        val currentStatus = currentRecords[studentId] ?: false

        _state.update {
            it.copy(
                attendanceRecords = currentRecords + (studentId to !currentStatus),
                error = null
            )
        }
    }

    private fun selectCourse(courseId: String) {
        val selectedCourse = _state.value.courses.find { it.id == courseId }

        if (selectedCourse != null) {
            _state.update {
                it.copy(
                    selectedCourse = selectedCourse,
                    isAttendanceSaved = false,
                    error = null
                )
            }
            if (_state.value.selectedCourse?.studentIds != null)
                loadStudentsForCourse(courseId)
        }
    }

    private fun loadStudentsForCourse(courseId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val students = studentRepository.getAllStudents(courseId)

                _state.update {
                    it.copy(
                        isLoading = false,
                        students = students,
                        attendanceRecords = students.associate { student ->
                            student.id to false
                        },
                        error = null
                    )
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        students = emptyList(),
                        attendanceRecords = emptyMap(),
                        error = "Error al cargar los estudiantes: ${e.message}"
                    )
                }
            }
        }
    }

    private fun loadCourses() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val courses = courseRepository.getCourses()
                Log.w("TakeAttendanceViewModel", "Loaded courses: $courses")
                _state.update {
                    it.copy(
                        isLoading = false,
                        courses = courses,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar los cursos: ${e.message}"
                    )
                }
            }
        }
    }
}