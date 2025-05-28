package org.override.apollo.ui.screens.home.screens.tools.screens.take_attendance.utils

import androidx.compose.runtime.Immutable

@Immutable
data class Course(
    val id: String,
    val name: String,
    val career: String,
    val section: String,
    val grade: String
)