package org.override.apollo.ui.screens.home.screens.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import apollo_app.composeapp.generated.resources.Res
import apollo_app.composeapp.generated.resources.dock_to_right_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.override.apollo.ui.components.ScreenAction
import org.override.apollo.ui.navigation.NavigationTools
import org.override.apollo.utils.enums.Tools

@Composable
fun ToolsRoot(
    modifier: Modifier = Modifier,
    onDockToRight: () -> Unit,
    isExpanded: Boolean = false,
    toolSelect: Tools? = null,
    toolSelection: (Tools?) -> Unit = {},
    viewModel: ToolsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ToolsScreen(
        state = state,
        modifier = modifier,
        onDockToRight = onDockToRight,
        isExpanded = isExpanded,
        toolSelection = toolSelection,
        toolSelect = toolSelect,
        onAction = viewModel::onAction
    )
}

@Composable
private fun ToolsScreen(
    state: ToolsState,
    modifier: Modifier = Modifier,
    onDockToRight: () -> Unit,
    isExpanded: Boolean = false,
    toolSelect: Tools? = null,
    toolSelection: (Tools?) -> Unit = {},
    onAction: (ToolsAction) -> Unit,
) {
    Box(
        modifier = modifier
            .background(colorScheme.surface.copy(alpha = 0.7f))
            .clip(shape = shapes.medium),
        content = {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                IconButton(
                    onClick = {
                        onDockToRight()
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
                            color = colorScheme.onTertiary,
                            shape = shapes.small
                        )
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    content = {
                        items(state.tools) { tool ->
                            ItemTool(
                                isExpanded = isExpanded,
                                tool = tool,
                                click = { toolSelection(tool) },
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                )

                toolSelect?.let { tool ->
                    ScreenAction(
                        icon = tool.icon,
                        name = tool.nameString,
                        size = tool.size,
                        close = { toolSelection(null) },
                        content = { NavigationTools(tool.route) }
                    )
                } ?: run { }
            }
        }
    )
}

@Composable
fun ItemTool(
    isExpanded: Boolean,
    click: () -> Unit,
    tool: Tools
) {
    if (!isExpanded) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable { click() }
                .background(
                    color = colorScheme.onTertiary,
                    shape = shapes.small
                ),
            contentAlignment = Alignment.Center,
            content = {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(tool.icon),
                        contentDescription = tool.name,
                        tint = colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        text = tool.nameString,
                        color = colorScheme.primary,
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 20.sp
                    )
                }
            }
        )
    } else {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(
                    color = colorScheme.background,
                    shape = shapes.small
                )
                .clickable { click() },
            contentAlignment = Alignment.Center,
            content = {
                Icon(
                    painter = painterResource(tool.icon),
                    contentDescription = tool.name,
                    tint = colorScheme.primary,
                    modifier = Modifier.size(30.dp)
                )
            }
        )
    }
}