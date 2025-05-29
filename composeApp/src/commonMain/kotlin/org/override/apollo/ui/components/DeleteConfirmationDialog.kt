package org.override.apollo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import apollo_app.composeapp.generated.resources.Res
import apollo_app.composeapp.generated.resources.warning_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import org.jetbrains.compose.resources.painterResource
import org.override.apollo.utils.data.Course

@Composable
fun DeleteConfirmationDialog(
    courseName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                painter = painterResource(Res.drawable.warning_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(
                text = "Eliminar Curso",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "¿Estás seguro de que deseas eliminar el curso:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "\"$courseName\"?",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Esta acción no se puede deshacer.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = "Eliminar",
                    color = MaterialTheme.colorScheme.onError
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancelar",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}

@Composable
fun EditCourseDialog(
    course: Course,
    onSave: (String, String, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var courseName by remember { mutableStateOf(course.name) }
    var career by remember { mutableStateOf(course.career) }
    var section by remember { mutableStateOf(course.section) }
    var grade by remember { mutableStateOf(course.degree) }
    var showError by remember { mutableStateOf(false) }

    val isFormValid = courseName!!.isNotBlank() &&
            career!!.isNotBlank() &&
            section!!.isNotBlank() &&
            grade!!.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Editar Curso",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextInputField(
                    label = "Nombre del Curso",
                    value = courseName ?: "",
                    onValueChange = {
                        courseName = it
                        showError = false
                    },
                    placeholder = "Ej: Matemáticas",
                    isRequired = true,
                    isError = courseName?.isEmpty() == true && showError
                )

                TextInputField(
                    label = "Carrera",
                    value = career ?: "",
                    onValueChange = {
                        career = it
                        showError = false
                    },
                    placeholder = "Ej: Ingeniería en Sistemas",
                    isRequired = true,
                    isError = career?.isEmpty() == true && showError
                )

                TextInputField(
                    label = "Sección",
                    value = section ?: "",
                    onValueChange = {
                        section = it
                        showError = false
                    },
                    placeholder = "Ej: A",
                    isRequired = true,
                    isError = section?.isEmpty() == true && showError
                )

                TextInputField(
                    label = "Grado",
                    value = grade ?: "",
                    onValueChange = {
                        grade = it
                        showError = false
                    },
                    placeholder = "Ej: 3er año",
                    isRequired = true,
                    isError = grade?.isEmpty() == true && showError
                )

                if (showError) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = "Por favor, completa todos los campos requeridos",
                            modifier = Modifier.padding(12.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isFormValid) {
                        onSave(courseName ?: "", career ?: "", section ?: "", grade ?: "")
                    } else {
                        showError = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Guardar",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancelar",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}