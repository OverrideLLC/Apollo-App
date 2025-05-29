package org.override.apollo.ui.screens.home.screens.tools.screens.take_attendance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import apollo_app.composeapp.generated.resources.Res
import apollo_app.composeapp.generated.resources.save_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.override.apollo.ui.screens.home.screens.tools.screens.take_attendance.components.AttendanceMethodSelector
import org.override.apollo.ui.screens.home.screens.tools.screens.take_attendance.components.CourseSelector
import org.override.apollo.ui.screens.home.screens.tools.screens.take_attendance.components.StudentAttendanceList
import org.override.apollo.ui.screens.home.screens.tools.screens.take_attendance.utils.AttendanceMethod

@Composable
fun TakeAttendanceRoot(
    viewModel: TakeAttendanceViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TakeAttendanceScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun TakeAttendanceScreen(
    state: TakeAttendanceState,
    onAction: (TakeAttendanceAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Loading State
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = colorScheme.primary
                )
            }
            return
        }

        // Error State
        state.error?.let { error ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = error,
                    color = colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // Course Selection
        CourseSelector(
            courses = state.courses,
            selectedCourse = state.selectedCourse,
            onCourseSelected = { course ->
                onAction(TakeAttendanceAction.SelectCourse(course.id?:""))
            }
        )

        // Attendance Method Selection (only show if course is selected)
        if (state.selectedCourse != null) {
            AttendanceMethodSelector(
                selectedMethod = state.selectedMethod,
                onMethodSelected = { method ->
                    onAction(TakeAttendanceAction.SelectAttendanceMethod(method))
                }
            )

            // Attendance Content based on selected method
            when (state.selectedMethod) {
                AttendanceMethod.LIST -> {
                    if (state.students.isNotEmpty()) {
                        StudentAttendanceList(
                            students = state.students,
                            attendanceRecords = state.attendanceRecords,
                            onToggleAttendance = { studentId ->
                                onAction(TakeAttendanceAction.ToggleStudentAttendance(studentId))
                            },
                            onMarkAllPresent = { present ->
                                onAction(TakeAttendanceAction.MarkAllPresent(present))
                            }
                        )
                    } else {
                        // Empty state for students
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = colorScheme.primaryContainer.copy(alpha = 0.1f)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No hay estudiantes registrados",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Agrega estudiantes al curso para poder tomar asistencia",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }

                AttendanceMethod.QR_CODE -> {
                    // Placeholder for QR Code method
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Gray.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Método QR",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Esta funcionalidad estará disponible próximamente",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                }

                AttendanceMethod.SCAN -> {
                    // Placeholder for Scan method
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Gray.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Método de Escaneo",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Esta funcionalidad estará disponible próximamente",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            // Save Button (only show if there are students and attendance method is LIST)
            if (state.students.isNotEmpty() && state.selectedMethod == AttendanceMethod.LIST) {
                Button(
                    onClick = { onAction(TakeAttendanceAction.SaveAttendance) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.secondaryContainer
                    ),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.save_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Guardar Asistencia",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = colorScheme.onSecondaryContainer
                    )
                }
            }
        } else {
            // Empty state when no course is selected
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Selecciona un curso",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Para comenzar a tomar asistencia, selecciona un curso de la lista",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        // Success message when attendance is saved
        if (state.isAttendanceSaved) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = colorScheme.primaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "¡Asistencia guardada exitosamente!",
                    color = colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}