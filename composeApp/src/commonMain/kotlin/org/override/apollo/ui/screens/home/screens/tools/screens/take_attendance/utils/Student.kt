package org.override.apollo.ui.screens.home.screens.tools.screens.take_attendance.utils

import androidx.compose.runtime.Immutable

@Immutable
data class Student(
    val id: String,
    val name: String,
    val matricula: String,
    val email: String,
    val phone: String
)