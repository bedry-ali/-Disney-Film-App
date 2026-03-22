package fr.isen.bedry.disneyfilmapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.isen.bedry.disneyfilmapp.ui.theme.DarkCard
import fr.isen.bedry.disneyfilmapp.ui.theme.GrayText
import fr.isen.bedry.disneyfilmapp.ui.theme.WhiteText

@Composable
fun FilmsScreen(
    title: String,
    films: List<FilmItem>,
    onBackClick: () -> Unit,
    onFilmClick: (FilmItem) -> Unit,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    UiScreen(
        title = title,
        showBack = true,
        onBackClick = onBackClick,
        onProfileClick = onProfileClick,
        onLogoutClick = onLogoutClick
    ) {
        LazyColumn {
            items(films) { film ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable { onFilmClick(film) }
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = film.title,
                            color = WhiteText,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text("Year: ${film.year}", color = GrayText)
                        Text("Genre: ${film.genre}", color = GrayText)
                        Text("Episode: ${film.number}", color = GrayText)
                    }
                }
            }
        }
    }
}