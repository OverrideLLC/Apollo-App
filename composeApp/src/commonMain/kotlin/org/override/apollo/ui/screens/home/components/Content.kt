package org.override.apollo.ui.screens.home.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.override.apollo.ui.screens.home.HomeAction
import org.override.apollo.ui.screens.home.HomeState
import org.override.apollo.ui.screens.home.screens.ai.AiRoot
import org.override.apollo.ui.screens.home.screens.task.TaskRoot
import org.override.apollo.ui.screens.home.screens.tools.ToolsRoot

@Composable
internal fun Content(
    padding: PaddingValues,
    state: HomeState,
    onAction: (HomeAction) -> Unit,
) {
    val sizeTask by animateFloatAsState(
        targetValue = if (state.dockToLeft) 0.1f else 0.3f,
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = 0
        ),
        label = "sizeTask"
    )

    val sizeWorkspace by animateFloatAsState(
        targetValue = if (state.dockToLeft && state.dockToRight) 0.8f else if (state.dockToLeft || state.dockToRight) 0.6f else 0.4f,
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = 0
        ),
        label = "sizeWorkspace"
    )

    val sizeTools by animateFloatAsState(
        targetValue = if (state.dockToRight) 0.1f else 0.3f,
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = 0
        ),
    )
    Column( // Cambia LazyColumn a Column
        modifier = Modifier
            .fillMaxSize()
            .padding(padding).padding(bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Spacer(modifier = Modifier.padding(16.dp))
            TaskRoot(
                modifier = Modifier
                    .weight(sizeTask)
                    .fillMaxHeight(),
                onDockToLeft = { onAction(HomeAction.DockToLeft) }
            )
            AiRoot(modifier = Modifier.weight(sizeWorkspace).fillMaxHeight())
            ToolsRoot(
                onDockToRight = { onAction(HomeAction.DockToRight) },
                isExpanded = state.dockToRight,
                toolSelect = state.toolSelect,
                toolSelection = { onAction(HomeAction.ToolSelect(it)) },
                modifier = Modifier
                    .weight(sizeTools)
                    .fillMaxHeight(),
            )
            Spacer(modifier = Modifier.padding(16.dp))
        }
    }
}