package org.override.apollo.ui.screens.home.screens.tools.screens.add_course

import androidx.compose.runtime.*

@Immutable
data class AddCourseState(
    val isLoading: Boolean = false,
    val courseName: String = "",
    val career: String = "",
    val section: String = "",
    val grade: String = "",
    val errorMessage: String? = null,
    val isFormValid: Boolean = false
)