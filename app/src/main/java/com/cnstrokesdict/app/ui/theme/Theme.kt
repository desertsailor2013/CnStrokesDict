package com.cnstrokesdict.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Seed = Color(0xFF2E7D32)

private val LightColors = lightColorScheme(
    primary = Seed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC8E6C9),
    onPrimaryContainer = Color(0xFF002106),
    secondary = Color(0xFF52634E),
    background = Color(0xFFF7FBF7),
    surface = Color.White,
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF9BD49F),
    onPrimary = Color(0xFF003911),
    background = Color(0xFF101510),
    surface = Color(0xFF1A211A),
)

@Composable
fun CnStrokesTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
