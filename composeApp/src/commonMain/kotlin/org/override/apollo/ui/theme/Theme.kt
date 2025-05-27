package org.override.apollo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Esquema de colores para tema claro
private val LightColorScheme = lightColorScheme(
    primary = lightPrimary,
    onPrimary = lightOnPrimary,
    primaryContainer = lightPrimaryContainer,
    onPrimaryContainer = lightOnPrimaryContainer,
    inversePrimary = lightInversePrimary,

    secondary = lightSecondary,
    onSecondary = lightOnSecondary,
    secondaryContainer = lightSecondaryContainer,
    onSecondaryContainer = lightOnSecondaryContainer,

    tertiary = lightTertiary,
    onTertiary = lightOnTertiary,
    tertiaryContainer = lightTertiaryContainer,
    onTertiaryContainer = lightOnTertiaryContainer,

    background = lightBackground,
    onBackground = lightOnBackground,

    surface = lightSurface,
    onSurface = lightOnSurface,
    surfaceVariant = lightSurfaceVariant,
    onSurfaceVariant = lightOnSurfaceVariant,
    surfaceTint = lightSurfaceTint,

    inverseSurface = lightInverseSurface,
    inverseOnSurface = lightInverseOnSurface,

    error = lightError,
    onError = lightOnError,
    errorContainer = lightErrorContainer,
    onErrorContainer = lightOnErrorContainer,

    outline = lightOutline,
    outlineVariant = lightOutlineVariant,
    scrim = lightScrim,

    surfaceBright = lightSurfaceBright,
    surfaceContainer = lightSurfaceContainer,
    surfaceContainerHigh = lightSurfaceContainerHigh,
    surfaceContainerHighest = lightSurfaceContainerHighest,
    surfaceContainerLow = lightSurfaceContainerLow,
    surfaceContainerLowest = lightSurfaceContainerLowest,
    surfaceDim = lightSurfaceDim
)

// Esquema de colores para tema oscuro
private val DarkColorScheme = darkColorScheme(
    primary = darkPrimary,
    onPrimary = darkOnPrimary,
    primaryContainer = darkPrimaryContainer,
    onPrimaryContainer = darkOnPrimaryContainer,
    inversePrimary = darkInversePrimary,

    secondary = darkSecondary,
    onSecondary = darkOnSecondary,
    secondaryContainer = darkSecondaryContainer,
    onSecondaryContainer = darkOnSecondaryContainer,

    tertiary = darkTertiary,
    onTertiary = darkOnTertiary,
    tertiaryContainer = darkTertiaryContainer,
    onTertiaryContainer = darkOnTertiaryContainer,

    background = darkBackground,
    onBackground = darkOnBackground,

    surface = darkSurface,
    onSurface = darkOnSurface,
    surfaceVariant = darkSurfaceVariant,
    onSurfaceVariant = darkOnSurfaceVariant,
    surfaceTint = darkSurfaceTint,

    inverseSurface = darkInverseSurface,
    inverseOnSurface = darkInverseOnSurface,

    error = darkError,
    onError = darkOnError,
    errorContainer = darkErrorContainer,
    onErrorContainer = darkOnErrorContainer,

    outline = darkOutline,
    outlineVariant = darkOutlineVariant,
    scrim = darkScrim,

    surfaceBright = darkSurfaceBright,
    surfaceContainer = darkSurfaceContainer,
    surfaceContainerHigh = darkSurfaceContainerHigh,
    surfaceContainerHighest = darkSurfaceContainerHighest,
    surfaceContainerLow = darkSurfaceContainerLow,
    surfaceContainerLowest = darkSurfaceContainerLowest,
    surfaceDim = darkSurfaceDim
)

// Colores extendidos que no están en MaterialTheme por defecto
data class ExtendedColors(
    val success: Color,
    val onSuccess: Color,
    val warning: Color,
    val onWarning: Color,
    val info: Color,
    val onInfo: Color,
    val divider: Color,
    val shadow: Color,
    val textSecondary: Color,
    val textDisabled: Color
)

val LightExtendedColors = ExtendedColors(
    success = Success,
    onSuccess = lightOnPrimary,
    warning = Warning,
    onWarning = lightOnPrimary,
    info = Info,
    onInfo = lightOnPrimary,
    divider = Divider,
    shadow = Shadow,
    textSecondary = TextSecondary,
    textDisabled = TextDisabled
)

val DarkExtendedColors = ExtendedColors(
    success = SuccessDark,
    onSuccess = darkOnPrimary,
    warning = WarningDark,
    onWarning = darkOnPrimary,
    info = InfoDark,
    onInfo = darkOnPrimary,
    divider = DividerDark,
    shadow = ShadowDark,
    textSecondary = TextSecondaryDark,
    textDisabled = TextDisabledDark
)

// CompositionLocal para colores extendidos
val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

@Composable
fun ApolloTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

// Extension para acceder fácilmente a los colores extendidos
val MaterialTheme.extendedColors: ExtendedColors
    @Composable
    get() = LocalExtendedColors.current

// Utilidades para usar los colores extendidos
object ApolloColors {
    val success: Color @Composable get() = MaterialTheme.extendedColors.success
    val onSuccess: Color @Composable get() = MaterialTheme.extendedColors.onSuccess
    val warning: Color @Composable get() = MaterialTheme.extendedColors.warning
    val onWarning: Color @Composable get() = MaterialTheme.extendedColors.onWarning
    val info: Color @Composable get() = MaterialTheme.extendedColors.info
    val onInfo: Color @Composable get() = MaterialTheme.extendedColors.onInfo
    val divider: Color @Composable get() = MaterialTheme.extendedColors.divider
    val shadow: Color @Composable get() = MaterialTheme.extendedColors.shadow
    val textSecondary: Color @Composable get() = MaterialTheme.extendedColors.textSecondary
    val textDisabled: Color @Composable get() = MaterialTheme.extendedColors.textDisabled
}