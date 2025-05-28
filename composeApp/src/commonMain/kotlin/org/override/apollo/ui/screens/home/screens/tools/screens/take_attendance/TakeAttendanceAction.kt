package org.override.apollo.ui.screens.home.screens.tools.screens.take_attendance

import org.override.apollo.ui.screens.home.screens.tools.screens.take_attendance.utils.AttendanceMethod

sealed interface TakeAttendanceAction {
    data class SelectCourse(val courseId: String) : TakeAttendanceAction
    data class SelectAttendanceMethod(val method: AttendanceMethod) : TakeAttendanceAction
    data class ToggleStudentAttendance(val studentId: String) : TakeAttendanceAction
    data class MarkAllPresent(val present: Boolean) : TakeAttendanceAction
    object SaveAttendance : TakeAttendanceAction
    object LoadCourses : TakeAttendanceAction
}

