package org.override.apollo.ui.screens.home.screens.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class TaskViewModel : ViewModel() {

    private val _state = MutableStateFlow(TaskState())
    val state = _state
        .onStart { }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = TaskState()
        )

    fun onAction(action: TaskAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}