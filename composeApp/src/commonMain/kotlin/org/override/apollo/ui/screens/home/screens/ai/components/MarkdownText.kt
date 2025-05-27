package org.override.apollo.ui.screens.home.screens.ai.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import apollo_app.composeapp.generated.resources.Res
import apollo_app.composeapp.generated.resources.content_copy_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import apollo_app.composeapp.generated.resources.terminal_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.flavours.gfm.GFMTokenTypes
import org.intellij.markdown.parser.MarkdownParser
import org.jetbrains.compose.resources.painterResource

// Funciones de utilidad (sin cambios)
fun copyToClipboard(text: String) {
    println("Copied to clipboard: $text")
    // Implementa la copia al portapapeles real aquí
}

fun openUrlInBrowser(url: String) {
    println("Opening URL: $url")
    // Implementa la apertura de URL real aquí
}

fun extractHostName(url: String): String {
    return try {
        url.substringAfter("://").substringBefore("/")
    } catch (e: Exception) {
        url // Devuelve la URL original si el parseo falla
    }
}

@Composable
internal fun MarkdownText(
    markdown: String,
    color: Color,
    onRunCode: (String) -> Unit
) {
    val flavour = remember { GFMFlavourDescriptor() }
    val parser = remember(flavour) { MarkdownParser(flavour) }
    val rootNode = remember(markdown, parser) { parser.buildMarkdownTreeFromString(markdown) }

    Column {
        rootNode.children.forEachIndexed { index, child ->
            RenderNode(
                node = child,
                textColor = color,
                markdownText = markdown,
                onRunCode = onRunCode,
                indentLevel = 0 // Los nodos raíz comienzan con indentLevel 0
            )
            if (index < rootNode.children.size - 1) {
                Spacer(modifier = Modifier.height(8.dp)) // Espacio entre bloques de nivel superior
            }
        }
    }
}

@Composable
internal fun RenderNode(
    node: ASTNode,
    textColor: Color,
    markdownText: String,
    indentLevel: Int, // Nivel de indentación actual para este nodo
    onRunCode: (String) -> Unit
) {
    // Calcula el padding inicial basado en el nivel de indentación.
    // Cada nivel de indentación añade 16.dp de padding.
    val startPadding = (16 * indentLevel).dp

    when (node.type) {
        MarkdownElementTypes.PARAGRAPH -> {
            val annotatedString = buildAnnotatedString {
                node.children.forEach { child ->
                    RenderInlineNode(
                        node = child,
                        builder = this,
                        textColor = textColor,
                        markdownText = markdownText,
                        onRunCode = onRunCode
                    )
                }
            }
            // El párrafo aplica el padding calculado.
            // Si indentLevel es 0 (ej. párrafo en raíz, o párrafo dentro de un item de lista), no habrá padding adicional aquí.
            Text(text = annotatedString, modifier = Modifier.padding(start = startPadding))
        }

        MarkdownElementTypes.UNORDERED_LIST,
        MarkdownElementTypes.ORDERED_LIST -> {
            // La columna que contiene los ítems de la lista aplica el padding.
            Column(modifier = Modifier.padding(start = startPadding)) {
                var itemNumber = 1 // Para listas ordenadas
                node.children.filter { it.type == MarkdownElementTypes.LIST_ITEM }
                    .forEachIndexed { index, listItemNode ->
                        val marker = if (node.type == MarkdownElementTypes.ORDERED_LIST) "${itemNumber++}." else "•"
                        RenderListItem(
                            itemNode = listItemNode,
                            marker = marker,
                            textColor = textColor,
                            markdownText = markdownText,
                            // El indentLevel para los bloques *dentro* de este item será `indentLevel + 1`
                            // Esto se pasa a RenderListItem para que sepa cómo indentar listas anidadas.
                            baseIndentForNestedBlocks = indentLevel + 1,
                            onRunCode = onRunCode
                        )
                        if (index < node.children.filter { it.type == MarkdownElementTypes.LIST_ITEM }.size - 1) {
                            Spacer(modifier = Modifier.height(4.dp)) // Espacio entre ítems de lista
                        }
                    }
            }
        }

        MarkdownElementTypes.CODE_FENCE -> {
            // Corrección: Usar MarkdownTokenTypes.FENCE_LANG para el string de información (lenguaje)
            // ya que GFMTokenTypes.INFO no está disponible según la imagen proporcionada.
            val infoString = node.children.find { it.type == MarkdownTokenTypes.FENCE_LANG }
                ?.getTextInNode(markdownText)?.toString()?.trim() ?: ""

            val codeContent = node.children
                .filter { it.type == MarkdownTokenTypes.CODE_FENCE_CONTENT }
                .joinToString("") { it.getTextInNode(markdownText).toString() }

            // El bloque de código (la columna con fondo) aplica el padding.
            Column(
                modifier = Modifier
                    .padding(start = startPadding)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), // Usar colores del tema
                        shape = MaterialTheme.shapes.small
                    )
                    .fillMaxWidth()
            ) {
                if (infoString.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant, // Usar colores del tema
                                shape = MaterialTheme.shapes.small // Podría ser solo top corners
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = infoString,
                            style = MaterialTheme.typography.bodySmall, // Un poco más pequeño para info
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                        )
                        Row(
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { copyToClipboard(codeContent) },
                                modifier = Modifier.size(32.dp).padding(4.dp),
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.content_copy_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24), // Reemplazar con un ícono de resources
                                    contentDescription = "Copiar código",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (infoString.equals("python", ignoreCase = true)) { // Ejemplo
                                IconButton(
                                    onClick = { onRunCode(codeContent) },
                                    modifier = Modifier.size(32.dp).padding(4.dp),
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.terminal_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24), // Reemplazar
                                        contentDescription = "Ejecutar Código",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
                Text(
                    text = codeContent, // No usar trimIndent() aquí para preservar la indentación del código original
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor, // O un color específico para código
                    modifier = Modifier.padding(8.dp).fillMaxWidth()
                )
            }
        }
        // TODO: Implementar otros tipos de bloques como BLOCK_QUOTE, ATX_HEADERs, HORIZONTAL_RULE
        // MarkdownElementTypes.BLOCK_QUOTE
        // MarkdownElementTypes.ATX_1, ATX_2, ..., ATX_6
        // MarkdownElementTypes.HORIZONTAL_RULE (o THEMATIC_BREAK)

        else -> {
            // Fallback para nodos no manejados explícitamente: renderizar sus hijos con el mismo nivel de indentación.
            // Esto puede o no ser correcto dependiendo del tipo de nodo.
            node.children.forEach { child ->
                RenderNode(
                    node = child,
                    textColor = textColor,
                    markdownText = markdownText,
                    indentLevel = indentLevel, // Propaga el indentLevel actual
                    onRunCode = onRunCode
                )
            }
        }
    }
}

@Composable
internal fun RenderListItem(
    itemNode: ASTNode, // El nodo LIST_ITEM
    marker: String,
    textColor: Color,
    markdownText: String,
    baseIndentForNestedBlocks: Int, // Nivel de indentación base para bloques anidados (ej. una lista dentro de este item)
    // Este valor es (indentLevel de la lista padre) + 1.
    onRunCode: (String) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // El marcador del item de lista. Su padding lateral implícito es manejado por el Row/Column padre.
        Text(
            text = marker,
            color = textColor, // O MaterialTheme.colorScheme.onSurface
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.width(24.dp) // Espacio para el marcador (ej. "• ", "1. ")
                .align(Alignment.Top) // Alinear marcador con la primera línea de texto si el contenido es multilínea
        )
        // Columna para el contenido del item de lista.
        Column {
            itemNode.children.forEach { contentChild ->
                // Determinar el indentLevel para el RenderNode del contentChild.
                val childNodeIndentLevel = when (contentChild.type) {
                    // Para listas anidadas o citas en bloque, usar el baseIndentForNestedBlocks.
                    // Esto indentará la lista anidada un nivel más que la lista padre.
                    MarkdownElementTypes.UNORDERED_LIST,
                    MarkdownElementTypes.ORDERED_LIST,
                    MarkdownElementTypes.BLOCK_QUOTE -> baseIndentForNestedBlocks

                    // Para párrafos, bloques de código, etc., directamente dentro del item de lista,
                    // su RenderNode no debe añadir padding adicional. Su alineación ya está
                    // controlada por esta estructura de Column dentro del Row del item.
                    else -> 0
                }

                RenderNode(
                    node = contentChild,
                    textColor = textColor,
                    markdownText = markdownText,
                    indentLevel = childNodeIndentLevel,
                    onRunCode = onRunCode
                )
            }
        }
    }
}

@Composable
internal fun RenderInlineNode(
    node: ASTNode,
    builder: AnnotatedString.Builder,
    textColor: Color,
    markdownText: String,
    onRunCode: (String) -> Unit // Aunque no todos los nodos inline lo usan, se pasa por consistencia
) {
    when (node.type) {
        MarkdownTokenTypes.TEXT -> {
            builder.pushStyle(SpanStyle(color = textColor)) // fontWeight normal por defecto
            builder.append(node.getTextInNode(markdownText).toString())
            builder.pop()
        }

        MarkdownElementTypes.STRONG -> {
            builder.pushStyle(SpanStyle(fontWeight = FontWeight.Bold, color = textColor)) // Usar textColor
            node.children.forEach { child ->
                RenderInlineNode(child, builder, textColor, markdownText, onRunCode)
            }
            builder.pop()
        }

        MarkdownElementTypes.EMPH -> {
            builder.pushStyle(SpanStyle(fontStyle = FontStyle.Italic, color = textColor))
            node.children.forEach { child ->
                RenderInlineNode(child, builder, textColor, markdownText, onRunCode)
            }
            builder.pop()
        }

        MarkdownTokenTypes.EOL -> { // Salto de línea suave (soft break)
            builder.append(" ") // GFM trata EOL como espacio en muchos contextos dentro de un párrafo
        }

        MarkdownTokenTypes.HARD_LINE_BREAK -> {
            builder.append("\n")
        }

        MarkdownElementTypes.CODE_SPAN -> {
            builder.pushStyle(
                SpanStyle(
                    fontFamily = FontFamily.Monospace,
                    color = textColor, // O un color específico para código inline
                    background = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                )
            )
            // El contenido de CODE_SPAN es usualmente un nodo TEXT hijo.
            val codeSpanText = node.children.find { it.type == MarkdownTokenTypes.TEXT }
                ?.getTextInNode(markdownText)?.toString()
                ?: node.children.filterNot { it.type == MarkdownTokenTypes.BACKTICK } // Fallback si no hay TEXT
                    .joinToString("") { it.getTextInNode(markdownText).toString() }

            builder.append(codeSpanText)
            builder.pop()
        }

        MarkdownElementTypes.INLINE_LINK -> {
            val linkDestinationNode = node.children.find { it.type == MarkdownElementTypes.LINK_DESTINATION }
            val fullUrl = linkDestinationNode?.getTextInNode(markdownText)?.toString() ?: ""

            val linkTextNode = node.children.find { it.type == MarkdownElementTypes.LINK_TEXT }

            builder.pushStringAnnotation(tag = "URL", annotation = fullUrl) // Anotación para click
            builder.pushStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.primary, // Color para enlaces
                    textDecoration = TextDecoration.Underline
                )
            )

            if (linkTextNode != null && linkTextNode.children.isNotEmpty()) {
                // Renderizar el texto del enlace (puede contener otros elementos inline)
                linkTextNode.children.forEach { child ->
                    RenderInlineNode(child, builder, MaterialTheme.colorScheme.primary, markdownText, onRunCode)
                }
            } else {
                // Fallback si no hay texto de enlace explícito (menos común para INLINE_LINK)
                // o si se prefiere mostrar la URL o su hostname.
                builder.append(extractHostName(fullUrl).ifEmpty { fullUrl })
            }

            builder.pop() // Pop style
            builder.pop() // Pop annotation
            // La gestión del click para la anotación "URL" debe hacerse en el Text composable que usa este AnnotatedString
            // (generalmente en el RenderNode de PARAGRAPH).
        }

        // TODO: Implementar otros tipos inline como IMAGE (MarkdownElementTypes.IMAGE)
        // MarkdownElementTypes.IMAGE

        else -> {
            // Fallback para nodos inline no manejados: renderizar sus hijos.
            node.children.forEach { child ->
                RenderInlineNode(child, builder, textColor, markdownText, onRunCode)
            }
        }
    }
}
