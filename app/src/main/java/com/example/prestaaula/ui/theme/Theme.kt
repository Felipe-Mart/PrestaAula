
package com.example.prestaaula.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val AzulOscuro = Color(0xFF1E3A5F)

// Colores del modo oscuro.
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),
    onPrimary = Color(0xFF102A43),
    secondary = Color(0xFFB0BEC5),
    onSecondary = Color(0xFF263238),
    tertiary = Color(0xFF80CBC4),
    background = Color(0xFF121820),
    onBackground = Color(0xFFE8EDF2),
    surface = Color(0xFF1D2733),
    onSurface = Color(0xFFE8EDF2),
    surfaceVariant = Color(0xFF344454),
    onSurfaceVariant = Color(0xFFCBD5DF),
    error = Color(0xFFCF6679)
)

// Colores del modo claro.
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1E3A5F),
    onPrimary = Color.White,
    secondary = Color(0xFF526D82),
    onSecondary = Color.White,
    tertiary = Color(0xFF39796F),
    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFBFE),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE8EDF2),
    onSurfaceVariant = Color(0xFF465563),
    error = Color(0xFFB3261E)
)

@Composable
fun PrestaAulaTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
