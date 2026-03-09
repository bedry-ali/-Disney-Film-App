package fr.isen.bedry.disneyfilmapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilmsScreen(
    title: String,
    films: List<FilmItem>,
    onBackClick: () -> Unit,
    onFilmClick: (FilmItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Button(onClick = { onBackClick() }) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(films) { film ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable { onFilmClick(film) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = film.title)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Year: ${film.year}")
                        Text(text = "Genre: ${film.genre}")
                        Text(text = "Episode: ${film.number}")
                    }
                }
            }
        }
    }
}