package org.override.apollo.ui.screens.home.screens.tools.screens.courses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.override.apollo.ui.components.CourseCard
import org.override.apollo.ui.components.DeleteConfirmationDialog
import org.override.apollo.ui.components.EditCourseDialog
import org.override.apollo.ui.components.EmptyState
import org.override.apollo.ui.components.ErrorMessage
import org.override.apollo.ui.components.LoadingIndicator
import org.override.apollo.ui.components.SearchBar

@Composable
fun CoursesRoot(
    viewModel: CoursesViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CoursesScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun CoursesScreen(
    state: CoursesState,
    onAction: (CoursesAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header
        Text(
            text = "Gestión de Cursos",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Search Bar
        SearchBar(
            query = state.searchQuery,
            onQueryChange = { onAction(CoursesAction.SearchCourses(it)) },
            placeholder = "Buscar cursos...",
            modifier = Modifier.fillMaxWidth()
        )

        // Error State
        state.error?.let { error ->
            ErrorMessage(
                message = error,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when {
                state.isLoading -> {
                    LoadingIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.courses.isEmpty() && !state.isLoading -> {
                    EmptyState(
                        message = if (state.searchQuery.isNotEmpty())
                            "No se encontraron cursos que coincidan con '${state.searchQuery}'"
                        else "No hay cursos registrados",
                        description = if (state.searchQuery.isEmpty())
                            "Agrega tu primer curso para comenzar"
                        else "Intenta con otros términos de búsqueda",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(
                            items = state.courses,
                            key = { it.id ?: "" }
                        ) { course ->
                            CourseCard(
                                course = course,
                                onEditClick = {
                                    onAction(CoursesAction.SelectCourse(course))
                                    onAction(CoursesAction.ShowEditDialog)
                                },
                                onDeleteClick = {
                                    onAction(CoursesAction.SelectCourse(course))
                                    onAction(CoursesAction.ShowDeleteDialog)
                                }
                            )
                        }
                    }
                }
            }

            // Pull to refresh indicator
            if (state.isRefreshing) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                )
            }
        }
    }

    // Dialogs
    if (state.showDeleteDialog && state.selectedCourse != null) {
        DeleteConfirmationDialog(
            courseName = state.selectedCourse.name ?: "",
            onConfirm = { onAction(CoursesAction.DeleteCourse) },
            onDismiss = { onAction(CoursesAction.HideDeleteDialog) }
        )
    }

    if (state.showEditDialog && state.selectedCourse != null) {
        EditCourseDialog(
            course = state.selectedCourse,
            onSave = { name, career, section, grade ->
                onAction(CoursesAction.UpdateCourse(name, career, section, grade))
            },
            onDismiss = { onAction(CoursesAction.HideEditDialog) }
        )
    }
}