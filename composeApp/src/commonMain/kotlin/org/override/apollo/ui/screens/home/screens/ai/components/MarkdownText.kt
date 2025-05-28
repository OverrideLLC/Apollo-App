package org.override.apollo.ui.screens.home.screens.ai.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import apollo_app.composeapp.generated.resources.Res
import apollo_app.composeapp.generated.resources.content_copy_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import apollo_app.composeapp.generated.resources.terminal_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.gfm.GFMElementTypes
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser
import org.jetbrains.compose.resources.painterResource

// Funciones de utilidad
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
        url.substringAfter("://").substringBefore("/").substringBefore("?").substringBefore("#")
    } catch (e: Exception) {
        url
    }
}

// Función para limpiar texto y preservar espacios
fun String.cleanMarkdownText(): String {
    return this.replace(Regex("\\s+"), " ").trim()
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
                indentLevel = 0
            )
            if (index < rootNode.children.size - 1) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
internal fun RenderNode(
    node: ASTNode,
    textColor: Color,
    markdownText: String,
    indentLevel: Int,
    onRunCode: (String) -> Unit
) {
    val startPadding = (16 * indentLevel).dp

    when (node.type) {
        // Headers
        MarkdownElementTypes.ATX_1 -> RenderHeader(node, textColor, markdownText, 1, startPadding, onRunCode)
        MarkdownElementTypes.ATX_2 -> RenderHeader(node, textColor, markdownText, 2, startPadding, onRunCode)
        MarkdownElementTypes.ATX_3 -> RenderHeader(node, textColor, markdownText, 3, startPadding, onRunCode)
        MarkdownElementTypes.ATX_4 -> RenderHeader(node, textColor, markdownText, 4, startPadding, onRunCode)
        MarkdownElementTypes.ATX_5 -> RenderHeader(node, textColor, markdownText, 5, startPadding, onRunCode)
        MarkdownElementTypes.ATX_6 -> RenderHeader(node, textColor, markdownText, 6, startPadding, onRunCode)

        // Setext headers
        MarkdownElementTypes.SETEXT_1 -> RenderHeader(node, textColor, markdownText, 1, startPadding, onRunCode)
        MarkdownElementTypes.SETEXT_2 -> RenderHeader(node, textColor, markdownText, 2, startPadding, onRunCode)

        // Paragraph
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
            Text(
                text = annotatedString,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp,
                modifier = Modifier.padding(start = startPadding)
            )
        }

        // Lists
        MarkdownElementTypes.UNORDERED_LIST,
        MarkdownElementTypes.ORDERED_LIST -> {
            Column(modifier = Modifier.padding(start = startPadding)) {
                var itemNumber = 1
                node.children.filter { it.type == MarkdownElementTypes.LIST_ITEM }
                    .forEachIndexed { index, listItemNode ->
                        val marker = if (node.type == MarkdownElementTypes.ORDERED_LIST) {
                            "${itemNumber++}."
                        } else {
                            when (indentLevel % 3) {
                                0 -> "•"
                                1 -> "◦"
                                else -> "▪"
                            }
                        }
                        RenderListItem(
                            itemNode = listItemNode,
                            marker = marker,
                            textColor = textColor,
                            markdownText = markdownText,
                            baseIndentForNestedBlocks = indentLevel + 1,
                            onRunCode = onRunCode
                        )
                        if (index < node.children.filter { it.type == MarkdownElementTypes.LIST_ITEM }.size - 1) {
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
            }
        }

        // Code blocks
        MarkdownElementTypes.CODE_FENCE -> {
            val infoString = node.children.find { it.type == MarkdownTokenTypes.FENCE_LANG }
                ?.getTextInNode(markdownText)?.toString()?.trim() ?: ""

            val codeContent = node.children
                .filter { it.type == MarkdownTokenTypes.CODE_FENCE_CONTENT }
                .joinToString("") { it.getTextInNode(markdownText).toString() }
                .trim()

            RenderCodeBlock(
                code = codeContent,
                language = infoString,
                modifier = Modifier.padding(start = startPadding),
                onRunCode = onRunCode
            )
        }

        MarkdownElementTypes.CODE_BLOCK -> {
            val codeContent = node.children
                .filter { it.type == MarkdownTokenTypes.CODE_LINE }
                .joinToString("\n") { it.getTextInNode(markdownText).toString() }
                .trim()

            RenderCodeBlock(
                code = codeContent,
                language = "",
                modifier = Modifier.padding(start = startPadding),
                onRunCode = onRunCode
            )
        }

        // Block quotes
        MarkdownElementTypes.BLOCK_QUOTE -> {
            Row(modifier = Modifier.padding(start = startPadding)) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(IntrinsicSize.Min)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    node.children.forEach { child ->
                        RenderNode(
                            node = child,
                            textColor = textColor.copy(alpha = 0.8f),
                            markdownText = markdownText,
                            indentLevel = 0,
                            onRunCode = onRunCode
                        )
                    }
                }
            }
        }

        // Horizontal rule
//        MarkdownElementTypes.HORIZONTAL_RULE -> {
//            Divider(
//                modifier = Modifier
//                    .padding(start = startPadding)
//                    .padding(vertical = 16.dp),
//                thickness = 1.dp,
//                color = MaterialTheme.colorScheme.outline
//            )
//        }

        // Tables (GFM)
        GFMElementTypes.TABLE -> {
            RenderTable(
                node = node,
                textColor = textColor,
                markdownText = markdownText,
                modifier = Modifier.padding(start = startPadding),
                onRunCode = onRunCode
            )
        }

        // Strikethrough (GFM)
        GFMElementTypes.STRIKETHROUGH -> {
            val annotatedString = buildAnnotatedString {
                pushStyle(SpanStyle(textDecoration = TextDecoration.LineThrough, color = textColor))
                node.children.forEach { child ->
                    RenderInlineNode(child, this, textColor, markdownText, onRunCode)
                }
                pop()
            }
            Text(text = annotatedString, modifier = Modifier.padding(start = startPadding))
        }

        // Task lists (GFM)
        MarkdownElementTypes.LIST_ITEM -> {
            // Este caso se maneja en las listas padre, pero podría ser un task list item
            if (isTaskListItem(node, markdownText)) {
                val isChecked = getTaskListItemStatus(node, markdownText)
                val checkbox = if (isChecked) "☑" else "☐"

                Row(modifier = Modifier.padding(start = startPadding)) {
                    Text(
                        text = checkbox,
                        color = textColor,
                        modifier = Modifier.width(24.dp)
                    )
                    Column {
                        node.children.forEach { child ->
                            if (child.type != MarkdownTokenTypes.TEXT ||
                                !child.getTextInNode(markdownText).toString().matches(Regex("\\s*\\[[x ]\\].*"))) {
                                RenderNode(
                                    node = child,
                                    textColor = textColor,
                                    markdownText = markdownText,
                                    indentLevel = 0,
                                    onRunCode = onRunCode
                                )
                            }
                        }
                    }
                }
            }
        }

        else -> {
            // Fallback para nodos no manejados
            node.children.forEach { child ->
                RenderNode(
                    node = child,
                    textColor = textColor,
                    markdownText = markdownText,
                    indentLevel = indentLevel,
                    onRunCode = onRunCode
                )
            }
        }
    }
}

@Composable
private fun RenderHeader(
    node: ASTNode,
    textColor: Color,
    markdownText: String,
    level: Int,
    startPadding: androidx.compose.ui.unit.Dp,
    onRunCode: (String) -> Unit
) {
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

    val textStyle = when (level) {
        1 -> MaterialTheme.typography.headlineLarge
        2 -> MaterialTheme.typography.headlineMedium
        3 -> MaterialTheme.typography.headlineSmall
        4 -> MaterialTheme.typography.titleLarge
        5 -> MaterialTheme.typography.titleMedium
        else -> MaterialTheme.typography.titleSmall
    }

    Text(
        text = annotatedString,
        style = textStyle,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = startPadding, bottom = 8.dp)
    )

    // Añadir línea debajo de H1 y H2
    if (level <= 2) {
        Divider(
            modifier = Modifier
                .padding(start = startPadding)
                .padding(bottom = 8.dp),
            thickness = if (level == 1) 2.dp else 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun RenderCodeBlock(
    code: String,
    language: String,
    modifier: Modifier = Modifier,
    onRunCode: (String) -> Unit
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.medium
            )
            .fillMaxWidth()
    ) {
        if (language.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = language,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { copyToClipboard(code) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.content_copy_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                            contentDescription = "Copiar código",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    if (isExecutableLanguage(language)) {
                        IconButton(
                            onClick = { onRunCode(code) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.terminal_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                                contentDescription = "Ejecutar código",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        } else {
            // Solo botón de copiar si no hay lenguaje especificado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { copyToClipboard(code) },
                    modifier = Modifier.size(32.dp).padding(4.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.content_copy_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24),
                        contentDescription = "Copiar código",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Text(
            text = code,
            fontFamily = FontFamily.Monospace,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
            lineHeight = 20.sp,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun RenderTable(
    node: ASTNode,
    textColor: Color,
    markdownText: String,
    modifier: Modifier = Modifier,
    onRunCode: (String) -> Unit
) {
    val rows = mutableListOf<List<String>>()
    var alignments = listOf<String>()

    // Procesar filas de la tabla
    node.children.forEach { child ->
        when (child.type) {
            GFMElementTypes.HEADER -> {
                val headerCells = mutableListOf<String>()
                child.children.filter { it.type == GFMElementTypes.ROW }
                    .forEach { cell ->
                        val cellText = buildString {
                            cell.children.forEach { cellChild ->
                                append(cellChild.getTextInNode(markdownText).toString())
                            }
                        }.trim()
                        headerCells.add(cellText)
                    }
                rows.add(headerCells)
            }
            GFMElementTypes.ROW -> {
                val rowCells = mutableListOf<String>()
                child.children.filter { it.type == GFMElementTypes.ROW }
                    .forEach { cell ->
                        val cellText = buildString {
                            cell.children.forEach { cellChild ->
                                append(cellChild.getTextInNode(markdownText).toString())
                            }
                        }.trim()
                        rowCells.add(cellText)
                    }
                rows.add(rowCells)
            }
        }
    }

    if (rows.isNotEmpty()) {
        LazyRow(
            modifier = modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    MaterialTheme.shapes.medium
                )
        ) {
            items(rows.first().indices.toList()) { columnIndex ->
                Column(
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .padding(1.dp)
                ) {
                    rows.forEachIndexed { rowIndex, row ->
                        val cellText = row.getOrElse(columnIndex) { "" }
                        val isHeader = rowIndex == 0

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (isHeader) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                    else Color.Transparent
                                )
                                .padding(8.dp)
                        ) {
                            Text(
                                text = cellText,
                                style = if (isHeader) MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                else MaterialTheme.typography.bodyMedium,
                                color = textColor,
                                textAlign = TextAlign.Start
                            )
                        }

                        if (rowIndex < rows.size - 1) {
                            Divider(
                                thickness = 0.5.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                            )
                        }
                    }
                }

                if (columnIndex < rows.first().size - 1) {
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(IntrinsicSize.Min)
                            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    )
                }
            }
        }
    }
}

@Composable
internal fun RenderListItem(
    itemNode: ASTNode,
    marker: String,
    textColor: Color,
    markdownText: String,
    baseIndentForNestedBlocks: Int,
    onRunCode: (String) -> Unit
) {
    // Verificar si es un task list item
    if (isTaskListItem(itemNode, markdownText)) {
        val isChecked = getTaskListItemStatus(itemNode, markdownText)
        val checkbox = if (isChecked) "☑" else "☐"

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = checkbox,
                color = textColor,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .width(24.dp)
                    .align(Alignment.Top)
            )
            Column {
                // Renderizar contenido sin el checkbox
                val filteredChildren = itemNode.children.filter { child ->
                    if (child.type == MarkdownTokenTypes.TEXT) {
                        val text = child.getTextInNode(markdownText).toString()
                        !text.matches(Regex("^\\s*\\[[x ]\\].*"))
                    } else true
                }

                filteredChildren.forEach { contentChild ->
                    val childNodeIndentLevel = when (contentChild.type) {
                        MarkdownElementTypes.UNORDERED_LIST,
                        MarkdownElementTypes.ORDERED_LIST,
                        MarkdownElementTypes.BLOCK_QUOTE -> baseIndentForNestedBlocks
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
    } else {
        // Lista normal
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = marker,
                color = textColor,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .width(24.dp)
                    .align(Alignment.Top)
            )
            Column {
                itemNode.children.forEach { contentChild ->
                    val childNodeIndentLevel = when (contentChild.type) {
                        MarkdownElementTypes.UNORDERED_LIST,
                        MarkdownElementTypes.ORDERED_LIST,
                        MarkdownElementTypes.BLOCK_QUOTE -> baseIndentForNestedBlocks
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
}

@Composable
internal fun RenderInlineNode(
    node: ASTNode,
    builder: AnnotatedString.Builder,
    textColor: Color,
    markdownText: String,
    onRunCode: (String) -> Unit
) {
    when (node.type) {
        MarkdownTokenTypes.TEXT -> {
            builder.pushStyle(SpanStyle(color = textColor))
            val text = node.getTextInNode(markdownText).toString()
            builder.append(text)
            builder.pop()
        }

        MarkdownElementTypes.STRONG -> {
            builder.pushStyle(SpanStyle(fontWeight = FontWeight.Bold, color = textColor))
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

        // Strikethrough (GFM)
        GFMElementTypes.STRIKETHROUGH -> {
            builder.pushStyle(SpanStyle(textDecoration = TextDecoration.LineThrough, color = textColor))
            node.children.forEach { child ->
                RenderInlineNode(child, builder, textColor, markdownText, onRunCode)
            }
            builder.pop()
        }

        MarkdownTokenTypes.EOL -> {
            builder.append(" ") // Preservar espacios entre palabras
        }

        MarkdownTokenTypes.HARD_LINE_BREAK -> {
            builder.append("\n")
        }

        MarkdownElementTypes.CODE_SPAN -> {
            builder.pushStyle(
                SpanStyle(
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.primary,
                    background = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                )
            )

            val codeSpanText = node.children
                .filterNot { it.type == MarkdownTokenTypes.BACKTICK }
                .joinToString("") { it.getTextInNode(markdownText).toString() }

            builder.append(" $codeSpanText ")
            builder.pop()
        }

        MarkdownElementTypes.INLINE_LINK -> {
            val linkDestinationNode = node.children.find { it.type == MarkdownElementTypes.LINK_DESTINATION }
            val fullUrl = linkDestinationNode?.getTextInNode(markdownText)?.toString() ?: ""
            val linkTextNode = node.children.find { it.type == MarkdownElementTypes.LINK_TEXT }

            builder.pushStringAnnotation(tag = "URL", annotation = fullUrl)
            builder.pushStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                )
            )

            if (linkTextNode != null && linkTextNode.children.isNotEmpty()) {
                linkTextNode.children.forEach { child ->
                    RenderInlineNode(child, builder, MaterialTheme.colorScheme.primary, markdownText, onRunCode)
                }
            } else {
                builder.append(extractHostName(fullUrl).ifEmpty { fullUrl })
            }

            builder.pop()
            builder.pop()
        }

        // Autolinks
        MarkdownElementTypes.AUTOLINK -> {
            val url = node.getTextInNode(markdownText).toString()
            builder.pushStringAnnotation(tag = "URL", annotation = url)
            builder.pushStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                )
            )
            builder.append(url)
            builder.pop()
            builder.pop()
        }

        // Images
        MarkdownElementTypes.IMAGE -> {
            val imageDestination = node.children.find { it.type == MarkdownElementTypes.LINK_DESTINATION }
                ?.getTextInNode(markdownText)?.toString() ?: ""
            val altText = node.children.find { it.type == MarkdownElementTypes.LINK_TEXT }
                ?.children?.joinToString("") { it.getTextInNode(markdownText).toString() } ?: "Image"

            // Por ahora, mostrar como texto
            builder.pushStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.secondary,
                    fontStyle = FontStyle.Italic
                )
            )
            builder.append("[$altText]($imageDestination)")
            builder.pop()
        }

        // HTML tags - ignorar por seguridad
        MarkdownTokenTypes.HTML_TAG -> {
            // Ignorar tags HTML por seguridad, no renderizar contenido
        }

        // Line breaks y whitespace
        MarkdownTokenTypes.WHITE_SPACE -> {
            builder.append(" ")
        }

        else -> {
            // Fallback: renderizar hijos
            node.children.forEach { child ->
                RenderInlineNode(child, builder, textColor, markdownText, onRunCode)
            }
        }
    }
}

// Funciones auxiliares
private fun isTaskListItem(node: ASTNode, markdownText: String): Boolean {
    return node.children.any { child ->
        if (child.type == MarkdownTokenTypes.TEXT) {
            val text = child.getTextInNode(markdownText).toString()
            text.matches(Regex("^\\s*\\[[x ]\\].*"))
        } else false
    }
}

private fun getTaskListItemStatus(node: ASTNode, markdownText: String): Boolean {
    node.children.forEach { child ->
        if (child.type == MarkdownTokenTypes.TEXT) {
            val text = child.getTextInNode(markdownText).toString()
            val match = Regex("^\\s*\\[([x ])\\]").find(text)
            if (match != null) {
                return match.groupValues[1].lowercase() == "x"
            }
        }
    }
    return false
}

private fun isExecutableLanguage(language: String): Boolean {
    val executableLanguages = setOf(
        "python", "py", "javascript", "js", "typescript", "ts",
        "java", "kotlin", "kt", "scala", "go", "rust", "ruby", "rb",
        "php", "c", "cpp", "c++", "csharp", "cs", "swift", "r",
        "bash", "sh", "shell", "powershell", "ps1", "sql"
    )
    return language.lowercase() in executableLanguages
}

private fun decodeHtmlEntity(entity: String): String {
    // Función simplificada para casos específicos si se necesita en el futuro
    return when (entity) {
        "&amp;" -> "&"
        "&lt;" -> "<"
        "&gt;" -> ">"
        "&quot;" -> "\""
        "&apos;" -> "'"
        "&nbsp;" -> " "
        else -> entity
    }
}