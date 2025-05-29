package org.override.apollo.ui.screens.home.screens.tools.screens.take_attendance.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import apollo_app.composeapp.generated.resources.Res
import apollo_app.composeapp.generated.resources.arrow_drop_down_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import apollo_app.composeapp.generated.resources.arrow_drop_up_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import apollo_app.composeapp.generated.resources.cancel_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import apollo_app.composeapp.generated.resources.check_circle_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import apollo_app.composeapp.generated.resources.list_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import apollo_app.composeapp.generated.resources.qr_code_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import apollo_app.composeapp.generated.resources.qr_code_scanner_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import apollo_app.composeapp.generated.resources.radio_button_unchecked_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.override.apollo.ui.screens.home.screens.tools.screens.take_attendance.utils.AttendanceMethod
import org.override.apollo.utils.data.Course
import org.override.apollo.utils.data.Student

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseSelector(
    courses: List<Course>,
    selectedCourse: Course?,
    onCourseSelected: (Course) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = "Seleccionar Curso",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedCourse?.let { "${it.name} - ${it.section}" }
                    ?: "Selecciona un curso",
                onValueChange = { },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        painter = painterResource(
                            if (expanded) Res.drawable.arrow_drop_up_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24 else Res.drawable.arrow_drop_down_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
                        ),
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorScheme.onSurfaceVariant,
                    unfocusedBorderColor = colorScheme.surfaceContainerHigh
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                courses.forEach { course ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(
                                    text = course.name ?: "",
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "${course.career} - ${course.section} - ${course.degree}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colorScheme.onSurface
                                )
                            }
                        },
                        onClick = {
                            onCourseSelected(course)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AttendanceMethodSelector(
    selectedMethod: AttendanceMethod,
    onMethodSelected: (AttendanceMethod) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Método de Asistencia",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            AttendanceMethodCard(
                method = AttendanceMethod.LIST,
                selectedMethod = selectedMethod,
                onMethodSelected = onMethodSelected,
                icon = Res.drawable.list_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24,
                title = "Lista",
                description = "Marcar manualmente",
                modifier = Modifier.weight(1f)
            )

            AttendanceMethodCard(
                method = AttendanceMethod.QR_CODE,
                selectedMethod = selectedMethod,
                onMethodSelected = onMethodSelected,
                icon = Res.drawable.qr_code_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24,
                title = "QR",
                description = "Código QR",
                modifier = Modifier.weight(1f),
                enabled = false // Por ahora deshabilitado
            )

            AttendanceMethodCard(
                method = AttendanceMethod.SCAN,
                selectedMethod = selectedMethod,
                onMethodSelected = onMethodSelected,
                icon = Res.drawable.qr_code_scanner_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24,
                title = "Escanear",
                description = "Escáner avanzado",
                modifier = Modifier.weight(1f),
                enabled = false // Por ahora deshabilitado
            )
        }
    }
}

@Composable
private fun AttendanceMethodCard(
    method: AttendanceMethod,
    selectedMethod: AttendanceMethod,
    onMethodSelected: (AttendanceMethod) -> Unit,
    icon: DrawableResource,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val isSelected = selectedMethod == method
    val backgroundColor = when {
        !enabled -> colorScheme.surfaceContainerLow.copy(alpha = 0.1f)
        isSelected -> colorScheme.primaryContainer.copy(alpha = 0.1f)
        else -> colorScheme.surfaceContainerLow.copy(alpha = 0f)
    }
    val borderColor = when {
        !enabled -> colorScheme.surfaceContainerLow.copy(alpha = 0.3f)
        isSelected -> colorScheme.onPrimaryContainer
        else -> colorScheme.surfaceContainerLow.copy(alpha = 0.3f)
    }

    Card(
        modifier = modifier
            .clickable(enabled = enabled) {
                if (enabled) onMethodSelected(method)
            }
            .border(
                width = if (isSelected && enabled) 1.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        border = null
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = when {
                    !enabled -> colorScheme.onSurface
                    isSelected -> colorScheme.onPrimaryContainer
                    else -> colorScheme.onSurface
                },
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                color = if (enabled) colorScheme.onPrimaryContainer else colorScheme.onSurface
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.onPrimaryContainer,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun StudentAttendanceList(
    students: List<Student>,
    attendanceRecords: Map<String, Boolean>,
    onToggleAttendance: (String) -> Unit,
    onMarkAllPresent: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val presentCount = attendanceRecords.values.count { it }
    val totalStudents = students.size

    Column(modifier = modifier) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = colorScheme.primaryContainer.copy(alpha = 0.1f)
            ),
            border = null,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Lista de Asistencia",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Presentes: $presentCount / $totalStudents",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onMarkAllPresent(true) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = colorScheme.primaryContainer
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.foundation.BorderStroke(
                                1.dp, colorScheme.onPrimaryContainer
                            ).brush
                        )
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.check_circle_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Todos Presentes",
                            color = colorScheme.onPrimaryContainer
                        )
                    }

                    OutlinedButton(
                        onClick = { onMarkAllPresent(false) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = colorScheme.onPrimaryContainer
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.foundation.BorderStroke(
                                1.dp, colorScheme.onPrimaryContainer
                            ).brush
                        )
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.cancel_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Todos Ausentes",
                            color = colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de estudiantes - CAMBIADO: Column en lugar de LazyColumn
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            students.forEach { student ->
                StudentAttendanceItem(
                    student = student,
                    isPresent = attendanceRecords[student.id] ?: false,
                    onToggleAttendance = { onToggleAttendance(student.id) }
                )
            }
        }
    }
}

@Composable
fun StudentAttendanceItem(
    student: Student,
    isPresent: Boolean,
    onToggleAttendance: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColorCard =
        if (isPresent) colorScheme.primaryContainer else colorScheme.errorContainer
    val contentColorCard =
        if (isPresent) colorScheme.onPrimaryContainer else colorScheme.onErrorContainer

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onToggleAttendance() },
        colors = CardDefaults.cardColors(containerColor = containerColorCard),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        border = null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Información del estudiante
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = student.displayName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = contentColorCard
                )

                Text(
                    text = "Matrícula: ${student.tuition}",
                    style = MaterialTheme.typography.bodySmall,
                    color = contentColorCard
                )

                if (student.email.isNotEmpty()) {
                    Text(
                        text = student.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = contentColorCard
                    )
                }
            }

            // Estado de asistencia
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = if (isPresent) "Presente" else "Ausente",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = contentColorCard
                )

                Icon(
                    painter = painterResource(if (isPresent) Res.drawable.check_circle_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24 else Res.drawable.radio_button_unchecked_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                    contentDescription = null,
                    tint = contentColorCard,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}