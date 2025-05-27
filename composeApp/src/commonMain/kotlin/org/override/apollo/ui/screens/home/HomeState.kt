package org.override.apollo.ui.screens.home

import androidx.compose.runtime.*
import org.override.apollo.utils.enums.Tools
import kotlin.enums.EnumEntries

@Immutable
data class HomeState(
    val isLoading: Boolean = false,
    val dockToLeft: Boolean = false,
    val dockToRight: Boolean = false,
    val toolSelect: Tools? = null,
    val tools: EnumEntries<Tools> = Tools.entries
)