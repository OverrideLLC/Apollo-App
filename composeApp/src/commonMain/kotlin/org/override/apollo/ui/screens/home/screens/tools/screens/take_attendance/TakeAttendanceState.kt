package org.override.apollo.ui.screens.home.screens.tools.screens.take_attendance

import androidx.compose.runtime.*
import org.override.apollo.ui.screens.home.screens.tools.screens.take_attendance.utils.AttendanceMethod
import org.override.apollo.ui.screens.home.screens.tools.screens.take_attendance.utils.Course
import org.override.apollo.ui.screens.home.screens.tools.screens.take_attendance.utils.Student

@Immutable
data class TakeAttendanceState(
    val isLoading: Boolean = false,
    val courses: List<Course> = emptyList(),
    val selectedCourse: Course? = null,
    val students: List<Student> = emptyList(),
    val attendanceRecords: Map<String, Boolean> = emptyMap(), // studentId -> isPresent
    val selectedMethod: AttendanceMethod = AttendanceMethod.LIST,
    val isAttendanceSaved: Boolean = false,
    val error: String? = null
)