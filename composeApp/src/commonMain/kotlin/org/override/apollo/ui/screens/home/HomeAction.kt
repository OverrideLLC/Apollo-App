package org.override.apollo.ui.screens.home

import org.override.apollo.utils.enums.Tools

sealed interface HomeAction {
    object DockToLeft : HomeAction
    object DockToRight : HomeAction
    data class ToolSelect(val tool: Tools?) : HomeAction
}