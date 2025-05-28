package org.override.apollo.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Un componente de menú desplegable reutilizable para Compose Multiplatform.
 *
 * @param T El tipo de los elementos en la lista del menú.
 * @param items La lista de elementos a mostrar en el menú desplegable.
 * @param selectedItem El elemento actualmente seleccionado (opcional, para mostrar una selección).
 * @param onItemSelected Callback que se invoca cuando un elemento es seleccionado.
 * @param triggerContent El Composable que actuará como disparador para mostrar/ocultar el menú.
 * Recibe un lambda `onClick` que debe ser llamado para alternar el estado del menú.
 * @param modifier Modificador para personalizar la apariencia o el comportamiento del Box que envuelve el trigger y el menú.
 * @param dropdownModifier Modificador para personalizar la apariencia o el comportamiento del DropdownMenu.
 * @param itemContent El Composable para renderizar cada elemento en el menú desplegable.
 * Recibe el elemento `T` y un lambda `onClick` que debe ser llamado cuando el item es presionado.
 */
/**
 * Un componente de menú desplegable reutilizable para Compose Multiplatform con Material 3.
 *
 * @param T El tipo de los elementos en la lista del menú.
 * @param items La lista de elementos a mostrar en el menú desplegable.
 * @param selectedItem El elemento actualmente seleccionado (opcional, para mostrar una selección).
 * @param onItemSelected Callback que se invoca cuando un elemento es seleccionado.
 * @param triggerContent El Composable que actuará como disparador para mostrar/ocultar el menú.
 * Recibe un lambda `onClick` que debe ser llamado para alternar el estado del menú.
 * @param modifier Modificador para personalizar la apariencia o el comportamiento del Box que envuelve el trigger y el menú.
 * @param dropdownModifier Modificador para personalizar la apariencia o el comportamiento del DropdownMenu.
 * @param itemContent El Composable para renderizar cada elemento en el menú desplegable.
 * Recibe el elemento `T` y un lambda `onClick` que debe ser llamado cuando el item es presionado.
 */
@Composable
fun <T> DropdownMenuComponent(
    items: List<T>,
    selectedItem: T? = null, // Opcional, para mostrar el item seleccionado en el trigger, por ejemplo
    onItemSelected: (T) -> Unit,
    triggerContent: @Composable (onClick: () -> Unit) -> Unit,
    modifier: Modifier = Modifier,
    dropdownModifier: Modifier = Modifier,
    itemContent: @Composable (item: T, onClick: () -> Unit) -> Unit = { item, onClick ->
        // Implementación por defecto para DropdownMenuItem con Text en Material 3
        DropdownMenuItem(
            text = { Text(text = item.toString()) }, // En M3, 'text' es un @Composable () -> Unit
            onClick = onClick
        )
    }
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.wrapContentSize(Alignment.TopStart)) {
        // Contenido que dispara el menú desplegable
        triggerContent {
            expanded = !expanded
        }

        // Menú desplegable de Material 3
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = dropdownModifier
        ) {
            items.forEach { item ->
                // Contenido de cada elemento del menú
                itemContent(item) {
                    onItemSelected(item)
                    expanded = false
                }
            }
        }
    }
}