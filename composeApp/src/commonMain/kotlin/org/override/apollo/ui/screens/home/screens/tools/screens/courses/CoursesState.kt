package org.override.apollo.ui.screens.home.screens.tools.screens.courses

import androidx.compose.runtime.*
import org.override.apollo.utils.data.Course

@Immutable
data class CoursesState(
    val isLoading: Boolean = false,
    val courses: List<Course> = emptyList(),
    val error: String? = null,
    val selectedCourse: Course? = null,
    val showDeleteDialog: Boolean = false,
    val showEditDialog: Boolean = false,
    val searchQuery: String = "",
    val isRefreshing: Boolean = false
)