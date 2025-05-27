package org.override.apollo.ui.screens.home.screens.tools

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class ToolsViewModel : ViewModel() {

    private val _state = MutableStateFlow(ToolsState())
    val state = _state
        .onStart { }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ToolsState()
        )

    fun onAction(action: ToolsAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}