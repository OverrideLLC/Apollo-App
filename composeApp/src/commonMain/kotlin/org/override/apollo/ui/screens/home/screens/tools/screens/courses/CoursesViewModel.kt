package org.override.apollo.ui.screens.home.screens.tools.screens.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.override.apollo.domain.repositories.CourseRepository
import org.override.apollo.utils.data.Course

class CoursesViewModel(
    private val courseRepository: CourseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CoursesState())
    private val _searchQuery = MutableStateFlow("")

    val state = combine(
        _state,
        _searchQuery
    ) { state, query ->
        val filteredCourses = if (query.isBlank()) {
            state.courses
        } else {
            state.courses.filter { course ->
                course.name?.contains(query, ignoreCase = true) == true ||
                        course.career?.contains(query, ignoreCase = true) == true ||
                        course.section?.contains(query, ignoreCase = true) == true ||
                        course.degree?.contains(query, ignoreCase = true) == true
            }
        }
        state.copy(
            courses = filteredCourses,
            searchQuery = query
        )
    }.onStart {
        loadCourses()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = CoursesState()
    )

    fun onAction(action: CoursesAction) {
        when (action) {
            is CoursesAction.LoadCourses -> loadCourses()
            is CoursesAction.RefreshCourses -> refreshCourses()
            is CoursesAction.SearchCourses -> _searchQuery.value = action.query
            is CoursesAction.SelectCourse -> _state.value = _state.value.copy(selectedCourse = action.course)
            is CoursesAction.ShowDeleteDialog -> _state.value = _state.value.copy(showDeleteDialog = true)
            is CoursesAction.HideDeleteDialog -> _state.value = _state.value.copy(showDeleteDialog = false)
            is CoursesAction.ShowEditDialog -> _state.value = _state.value.copy(showEditDialog = true)
            is CoursesAction.HideEditDialog -> _state.value = _state.value.copy(showEditDialog = false)
            is CoursesAction.DeleteCourse -> deleteCourse()
            is CoursesAction.UpdateCourse -> updateCourse(action.name, action.career, action.section, action.grade)
        }
    }

    private fun loadCourses() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                val courses = courseRepository.getCourses()
                _state.value = _state.value.copy(
                    isLoading = false,
                    courses = courses,
                    error = null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Error al cargar los cursos: ${e.message}"
                )
            }
        }
    }

    private fun refreshCourses() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isRefreshing = true)
            loadCourses()
            _state.value = _state.value.copy(isRefreshing = false)
        }
    }

    private fun deleteCourse() {
        val selectedCourse = _state.value.selectedCourse ?: return

        viewModelScope.launch {
            try {
                // Simular eliminación - aquí iría la llamada a la API/DB
                delay(500)

                val updatedCourses = _state.value.courses.filter { it.id != selectedCourse.id }
                _state.value = _state.value.copy(
                    courses = updatedCourses,
                    selectedCourse = null,
                    showDeleteDialog = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Error al eliminar el curso: ${e.message}",
                    showDeleteDialog = false
                )
            }
        }
    }

    private fun updateCourse(name: String, career: String, section: String, grade: String) {
        val selectedCourse = _state.value.selectedCourse ?: return

        viewModelScope.launch {
            try {

                val updatedCourse = selectedCourse.copy(
                    name = name,
                    career = career,
                    section = section,
                    degree = grade
                )

                val updatedCourses = _state.value.courses.map { course ->
                    if (course.id == selectedCourse.id) updatedCourse else course
                }

                courseRepository.updateCourse(updatedCourse)

                _state.value = _state.value.copy(
                    courses = updatedCourses,
                    selectedCourse = null,
                    showEditDialog = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Error al actualizar el curso: ${e.message}",
                    showEditDialog = false
                )
            }
        }
    }
}