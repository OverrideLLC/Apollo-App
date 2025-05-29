package org.override.apollo.ui.screens.home.screens.tools.screens.courses

import org.override.apollo.utils.data.Course

sealed interface CoursesAction {
    data object LoadCourses : CoursesAction
    data object RefreshCourses : CoursesAction
    data class SearchCourses(val query: String) : CoursesAction
    data class SelectCourse(val course: Course) : CoursesAction
    data object ShowDeleteDialog : CoursesAction
    data object HideDeleteDialog : CoursesAction
    data object ShowEditDialog : CoursesAction
    data object HideEditDialog : CoursesAction
    data object DeleteCourse : CoursesAction
    data class UpdateCourse(
        val name: String,
        val career: String,
        val section: String,
        val grade: String
    ) : CoursesAction
}