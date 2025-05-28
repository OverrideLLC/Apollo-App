package org.override.apollo.ui.screens.home.screens.tools.screens.add_student

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import apollo_app.composeapp.generated.resources.Res
import apollo_app.composeapp.generated.resources.arrow_drop_down_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.override.apollo.ui.components.DropdownMenuComponent
import org.override.apollo.ui.components.PrimaryButton
import org.override.apollo.ui.components.TextInputField

@Composable
fun AddStudentRoot(
    viewModel: AddStudentViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AddStudentScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun AddStudentScreen(
    state: AddStudentState,
    onAction: (AddStudentAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            Form(
                state = state,
                onAction = onAction
            )
            PrimaryButton(
                text = "Guardar",
                onClick = { onAction(AddStudentAction.OnSaveStudent) },
                enabled = state.isFormValid,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

@Composable
private fun Form(
    state: AddStudentState,
    onAction: (AddStudentAction) -> Unit
) {
    TextInputField(
        label = "Nombre",
        value = state.name,
        isRequired = true,
        onValueChange = { onAction(AddStudentAction.OnNameChanged(it)) },
        placeholder = "Ej: Juan Pérez"
    )
    TextInputField(
        label = "Matrícula",
        value = state.tuition,
        isRequired = true,
        onValueChange = { onAction(AddStudentAction.OnTuitionChanged(it)) },
        placeholder = "Ej: 20230001"
    )
    TextInputField(
        label = "Correo Electrónico",
        value = state.mail,
        isRequired = false,
        onValueChange = { onAction(AddStudentAction.OnMailChanged(it)) },
        placeholder = "Ej: williamy@domain.com"
    )
    TextInputField(
        label = "Teléfono",
        value = state.phone,
        isRequired = false,
        onValueChange = { onAction(AddStudentAction.OnPhoneChanged(it)) },
        placeholder = "Ej: 555-123-4567"
    )
    DropdownMenuComponent(
        items = state.options,
        selectedItem = state.selectedOption,
        onItemSelected = { onAction(AddStudentAction.OnOptionSelected(it)) },
        triggerContent = { onClick ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = "Curso",
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorScheme.onSurface,
                    modifier = Modifier.padding(8.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .clickable { onClick() }
                        .border(
                            width = 1.dp,
                            color = colorScheme.outline,
                            shape = MaterialTheme.shapes.small
                        ),
                    content = {
                        Text(
                            text = if (state.selectedOption.isEmpty()) "Selecciona una opción" else state.selectedOption,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorScheme.onSurface,
                            modifier = Modifier.padding(8.dp)
                        )
                        IconButton(onClick = onClick) {
                            Icon(
                                painter = painterResource(Res.drawable.arrow_drop_down_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                                contentDescription = "Más opciones",
                                tint = colorScheme.onSurface,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                )
            }
        }
    )
}