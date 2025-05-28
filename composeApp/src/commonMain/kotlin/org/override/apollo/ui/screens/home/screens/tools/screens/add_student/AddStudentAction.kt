package org.override.apollo.ui.screens.home.screens.tools.screens.add_student

sealed interface AddStudentAction {
    data class OnNameChanged(val name: String) : AddStudentAction
    data class OnTuitionChanged(val tuition: String) : AddStudentAction
    data class OnMailChanged(val mail: String) : AddStudentAction
    data class OnPhoneChanged(val phone: String) : AddStudentAction
    data class OnOptionSelected(val option: String) : AddStudentAction
    data class DropdownMenuTrigger(val onClick: () -> Unit) : AddStudentAction
    object OnSaveStudent : AddStudentAction
    object OnCancel : AddStudentAction
    object OnDismissError : AddStudentAction
}