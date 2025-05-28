package org.override.apollo.ui.screens.home.screens.tools.screens.add_course

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.override.apollo.ui.components.PrimaryButton
import org.override.apollo.ui.components.SecondaryButton
import org.override.apollo.ui.components.TextInputField

@Composable
fun AddCourseRoot(
    viewModel: AddCourseViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AddCourseScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun AddCourseScreen(
    state: AddCourseState,
    onAction: (AddCourseAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Course Name
            TextInputField(
                label = "Nombre de Curso",
                value = state.courseName,
                onValueChange = { onAction(AddCourseAction.OnCourseNameChanged(it)) },
                placeholder = "Ej: Matemáticas",
                isRequired = true,
                isError = state.courseName.isEmpty() && state.errorMessage != null
            )

            // Career
            TextInputField(
                label = "Carrera",
                value = state.career,
                onValueChange = { onAction(AddCourseAction.OnCareerChanged(it)) },
                placeholder = "Ej: Ingeniería en Sistemas",
                isRequired = true,
                isError = state.career.isEmpty() && state.errorMessage != null
            )

            // Section
            TextInputField(
                label = "Sección",
                value = state.section,
                onValueChange = { onAction(AddCourseAction.OnSectionChanged(it)) },
                placeholder = "Ej: A",
                isRequired = true,
                isError = state.section.isEmpty() && state.errorMessage != null
            )

            // Grade
            TextInputField(
                label = "Grado",
                value = state.grade,
                onValueChange = { onAction(AddCourseAction.OnGradeChanged(it)) },
                placeholder = "Ej: 3er año",
                isRequired = true,
                isError = state.grade.isEmpty() && state.errorMessage != null
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Error Message
        if (state.errorMessage != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = state.errorMessage,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PrimaryButton(
                text = "Crear Curso",
                onClick = { onAction(AddCourseAction.OnSaveCourse) },
                modifier = Modifier.weight(1f),
                enabled = state.isFormValid && !state.isLoading,
                isLoading = state.isLoading
            )
        }
    }
}