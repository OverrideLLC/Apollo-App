package org.override.apollo.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class HomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state
        .onStart { }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HomeState()
        )

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.DockToLeft -> {
                _state.value = _state.value.copy(dockToLeft = !state.value.dockToLeft)
            }
            is HomeAction.DockToRight -> {
                _state.value = _state.value.copy(dockToRight = !state.value.dockToRight)
            }
            is HomeAction.ToolSelect -> {
                _state.value = _state.value.copy(toolSelect = action.tool)
            }
        }
    }

}