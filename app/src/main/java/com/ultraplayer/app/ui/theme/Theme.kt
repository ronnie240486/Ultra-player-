package com.ultraplayer.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val UltraPlayerColorScheme = darkColorScheme(
    primary = AccentCyan,
    secondary = AccentMagenta,
    background = Black,
    surface = DarkSurface,
    onPrimary = Black,
    onBackground = White,
    onSurface = White,
    error = WarningOrange,
)

@Composable
fun UltraPlayerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = UltraPlayerColorScheme,
        content = content
    )
}
