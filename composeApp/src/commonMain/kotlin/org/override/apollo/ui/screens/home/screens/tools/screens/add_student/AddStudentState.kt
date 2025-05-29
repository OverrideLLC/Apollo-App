package org.override.apollo.ui.screens.home.screens.tools.screens.add_student

import androidx.compose.runtime.*
import org.override.apollo.utils.data.Course

@Immutable
data class AddStudentState(
    val isLoading: Boolean = false,
    val name: String = "",
    val tuition: String = "",
    val mail: String = "",
    val phone: String = "",
    val selectedOption: Course? = null,
    val options: List<Course> = emptyList(),
    val errorMessage: String? = null,
    val isFormValid: Boolean = false
)