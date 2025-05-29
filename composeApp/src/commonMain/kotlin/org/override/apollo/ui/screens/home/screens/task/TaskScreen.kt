package org.override.apollo.ui.screens.home.screens.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import apollo_app.composeapp.generated.resources.Res
import apollo_app.composeapp.generated.resources.dock_to_right_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.override.apollo.ui.components.ScreenAction
import org.override.apollo.ui.navigation.NavigationTools
import org.override.apollo.ui.screens.home.screens.tools.ItemTool

@Composable
fun TaskRoot(
    modifier: Modifier = Modifier,
    onDockToLeft: () -> Unit,
    viewModel: TaskViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TaskScreen(
        state = state,
        modifier = modifier,
        onDockToLeft = onDockToLeft,
        onAction = viewModel::onAction
    )
}

@Composable
private fun TaskScreen(
    state: TaskState,
    modifier: Modifier = Modifier,
    onDockToLeft: () -> Unit,
    onAction: (TaskAction) -> Unit,
) {
    Box(
        modifier = modifier
            .background(colorScheme.surfaceContainerLow)
            .clip(shapes.medium),
        content = {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                IconButton(
                    onClick = {
                        onDockToLeft()
                    },
                    content = {
                        Icon(
                            painter = painterResource(Res.drawable.dock_to_right_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                            contentDescription = "Dock to right",
                            modifier = Modifier.size(24.dp),
                            tint = colorScheme.primary
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(10.dp)
                        .background(
                            color = colorScheme.surfaceContainerLowest,
                            shape = shapes.small
                        )
                )
            }
        }
    )
}