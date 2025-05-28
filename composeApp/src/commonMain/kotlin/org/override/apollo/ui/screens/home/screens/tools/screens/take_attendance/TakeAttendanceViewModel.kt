package org.override.apollo.ui.screens.home.screens.tools.screens.take_attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.override.apollo.ui.screens.home.screens.tools.screens.take_attendance.utils.Course
import org.override.apollo.ui.screens.home.screens.tools.screens.take_attendance.utils.Student

class TakeAttendanceViewModel : ViewModel() {

    private val _state = MutableStateFlow(TakeAttendanceState())
    val state = _state
        .onStart { }
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

            // Cargar estudiantes del curso seleccionado
            loadStudentsForCourse(courseId)
        }
    }

    private fun loadStudentsForCourse(courseId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                // Simular carga de estudiantes
                delay(800)

                // Mock data de estudiantes - aquí conectarías con tu repositorio real
                val mockStudents = when (courseId) {
                    "1" -> listOf( // Matemáticas
                        Student(
                            id = "s1",
                            name = "Ana García López",
                            matricula = "2021001",
                            email = "ana.garcia@ejemplo.com",
                            phone = "555-0101"
                        ),
                        Student(
                            id = "s2",
                            name = "Carlos Mendoza Rivera",
                            matricula = "2021002",
                            email = "carlos.mendoza@ejemplo.com",
                            phone = "555-0102"
                        ),
                        Student(
                            id = "s3",
                            name = "María Elena Vargas",
                            matricula = "2021003",
                            email = "maria.vargas@ejemplo.com",
                            phone = "555-0103"
                        ),
                        Student(
                            id = "s4",
                            name = "José Luis Martínez",
                            matricula = "2021004",
                            email = "jose.martinez@ejemplo.com",
                            phone = "555-0104"
                        ),
                        Student(
                            id = "s5",
                            name = "Sofía Hernández Cruz",
                            matricula = "2021005",
                            email = "sofia.hernandez@ejemplo.com",
                            phone = "555-0105"
                        )
                    )
                    "2" -> listOf( // Programación
                        Student(
                            id = "s6",
                            name = "Diego Ramírez Flores",
                            matricula = "2022001",
                            email = "diego.ramirez@ejemplo.com",
                            phone = "555-0201"
                        ),
                        Student(
                            id = "s7",
                            name = "Isabella Torres Morales",
                            matricula = "2022002",
                            email = "isabella.torres@ejemplo.com",
                            phone = "555-0202"
                        ),
                        Student(
                            id = "s8",
                            name = "Alejandro Castillo Ruiz",
                            matricula = "2022003",
                            email = "alejandro.castillo@ejemplo.com",
                            phone = "555-0203"
                        ),
                        Student(
                            id = "s9",
                            name = "Valentina Sánchez Ortega",
                            matricula = "2022004",
                            email = "valentina.sanchez@ejemplo.com",
                            phone = "555-0204"
                        )
                    )
                    "3" -> listOf( // Base de Datos
                        Student(
                            id = "s10",
                            name = "Fernando Gutiérrez Vega",
                            matricula = "2020001",
                            email = "fernando.gutierrez@ejemplo.com",
                            phone = "555-0301"
                        ),
                        Student(
                            id = "s11",
                            name = "Camila Jiménez Peña",
                            matricula = "2020002",
                            email = "camila.jimenez@ejemplo.com",
                            phone = "555-0302"
                        ),
                        Student(
                            id = "s12",
                            name = "Ricardo Moreno Silva",
                            matricula = "2020003",
                            email = "ricardo.moreno@ejemplo.com",
                            phone = "555-0303"
                        ),
                        Student(
                            id = "s13",
                            name = "Gabriela Ramos Castro",
                            matricula = "2020004",
                            email = "gabriela.ramos@ejemplo.com",
                            phone = "555-0304"
                        ),
                        Student(
                            id = "s14",
                            name = "Sebastián Luna Herrera",
                            matricula = "2020005",
                            email = "sebastian.luna@ejemplo.com",
                            phone = "555-0305"
                        ),
                        Student(
                            id = "s15",
                            name = "Andrea Molina Espinoza",
                            matricula = "2020006",
                            email = "andrea.molina@ejemplo.com",
                            phone = "555-0306"
                        )
                    )
                    else -> emptyList()
                }

                // Inicializar registros de asistencia (todos ausentes por defecto)
                val initialAttendanceRecords = mockStudents.associate { student ->
                    student.id to false
                }

                _state.update {
                    it.copy(
                        isLoading = false,
                        students = mockStudents,
                        attendanceRecords = initialAttendanceRecords,
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
                // Simular carga de datos
                delay(1000)

                // Mock data - aquí conectarías con tu repositorio real
                val mockCourses = listOf(
                    Course(
                        id = "1",
                        name = "Matemáticas",
                        career = "Ingeniería en Sistemas",
                        section = "A",
                        grade = "3er año"
                    ),
                    Course(
                        id = "2",
                        name = "Programación",
                        career = "Ingeniería en Sistemas",
                        section = "B",
                        grade = "2do año"
                    ),
                    Course(
                        id = "3",
                        name = "Base de Datos",
                        career = "Ingeniería en Sistemas",
                        section = "A",
                        grade = "4to año"
                    )
                )

                _state.update {
                    it.copy(
                        isLoading = false,
                        courses = mockCourses,
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