package org.override.apollo.ui.screens.home.screens.tools.screens.add_student

import androidx.compose.runtime.*

@Immutable
data class AddStudentState(
    val isLoading: Boolean = false,
    val name: String = "",
    val tuition: String = "",
    val mail: String = "",
    val phone: String = "",
    val selectedOption: String = "",
    val options: List<String> = emptyList(),
    val errorMessage: String? = null,
    val isFormValid: Boolean = false
) {
    constructor() : this(
        isLoading = false,
        name = "",
        tuition = "",
        mail = "",
        phone = "",
        selectedOption = "",
        options = listOf("Opción 1", "Opción 2", "Opción 3", "Opción 4", "Opción 5", "Opción 6"),
        errorMessage = null,
        isFormValid = false
    )
}