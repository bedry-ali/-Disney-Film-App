package fr.isen.bedry.disneyfilmapp.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PurpleAccent,
    secondary = PurpleAccent2,
    background = BlackBackground,
    surface = DarkCard,
    onPrimary = WhiteText,
    onSecondary = WhiteText,
    onBackground = WhiteText,
    onSurface = WhiteText
)

@Composable
fun DisneyFilmAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography(),
        content = content
    )
}