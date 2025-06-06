package org.override.apollo.ui.screens.home.screens.ai.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import org.override.apollo.ui.screens.home.screens.ai.AiAction
import org.override.apollo.ui.screens.home.screens.ai.AiViewModel

@Composable
internal fun MessageBubble(
    message: AiViewModel.Message,
    onAction: (AiAction) -> Unit,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (message.isUser) colorScheme.background else colorScheme.background,
        animationSpec = tween(durationMillis = 300)
    )

    val textColor by animateColorAsState(
        targetValue = if (message.isUser) colorScheme.tertiary else colorScheme.tertiary,
        animationSpec = tween(durationMillis = 300)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(if (message.isUser) 0.8f else 0.9f)
                .shadow(1.dp, RoundedCornerShape(16.dp))
                .clip(
                    RoundedCornerShape(
                        topStart = if (message.isUser) 16.dp else 0.dp,
                        topEnd = if (message.isUser) 0.dp else 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )
                )
                .background(backgroundColor)
                .padding(12.dp)
        ) {
            MarkdownText(
                markdown = message.text,
                color = textColor,
                onRunCode = { onAction(AiAction.RunCode(message.text)) }
            )
        }
    }
}