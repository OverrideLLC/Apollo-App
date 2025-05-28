package org.override.apollo.ui.screens.home.screens.tools.screens.add_course

sealed interface AddCourseAction {
    data class OnCourseNameChanged(val name: String) : AddCourseAction
    data class OnCareerChanged(val career: String) : AddCourseAction
    data class OnSectionChanged(val section: String) : AddCourseAction
    data class OnGradeChanged(val grade: String) : AddCourseAction
    object OnSaveCourse : AddCourseAction
    object OnCancel : AddCourseAction
    object OnDismissError : AddCourseAction
}