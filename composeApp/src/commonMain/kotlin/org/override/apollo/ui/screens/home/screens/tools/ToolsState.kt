package org.override.apollo.ui.screens.home.screens.tools

import androidx.compose.runtime.*
import org.override.apollo.utils.enums.Tools
import kotlin.enums.EnumEntries

@Immutable
data class ToolsState(
    val isLoading: Boolean = false,
    val tools: EnumEntries<Tools> = Tools.entries,
)