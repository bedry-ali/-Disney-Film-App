package fr.isen.bedry.disneyfilmapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

@Composable
fun FilmDetailScreen(
    film: FilmItem,
    auth: FirebaseAuth,
    database: DatabaseReference,
    onBackClick: () -> Unit
) {
    val userId = auth.currentUser?.uid

    var watched by remember { mutableStateOf(false) }
    var wantToWatch by remember { mutableStateOf(false) }
    var owned by remember { mutableStateOf(false) }
    var wantToGetRidOf by remember { mutableStateOf(false) }

    LaunchedEffect(film.title) {
        userId?.let { uid ->
            database.child("users")
                .child(uid)
                .child("filmsStatus")
                .child(film.title)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        watched = snapshot.child("watched").getValue(Boolean::class.java) ?: false
                        wantToWatch = snapshot.child("wantToWatch").getValue(Boolean::class.java) ?: false
                        owned = snapshot.child("owned").getValue(Boolean::class.java) ?: false
                        wantToGetRidOf = snapshot.child("wantToGetRidOf").getValue(Boolean::class.java) ?: false
                    }

                    override fun onCancelled(error: DatabaseError) {
                        println("Firebase error: ${error.message}")
                    }
                })
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Button(onClick = { onBackClick() }) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = film.title,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text("Episode: ${film.number}")
        Text("Year: ${film.year}")
        Text("Genre: ${film.genre}")

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = {
            userId?.let { uid ->
                val newValue = !watched
                database.child("users")
                    .child(uid)
                    .child("filmsStatus")
                    .child(film.title)
                    .child("watched")
                    .setValue(newValue)

                watched = newValue
            }
        }) {
            Text(if (watched) "Watched ✅" else "Watched")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            userId?.let { uid ->
                val newValue = !wantToWatch
                database.child("users")
                    .child(uid)
                    .child("filmsStatus")
                    .child(film.title)
                    .child("wantToWatch")
                    .setValue(newValue)

                wantToWatch = newValue
            }
        }) {
            Text(if (wantToWatch) "Want to watch ✅" else "Want to watch")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            userId?.let { uid ->
                val newValue = !owned
                database.child("users")
                    .child(uid)
                    .child("filmsStatus")
                    .child(film.title)
                    .child("owned")
                    .setValue(newValue)

                owned = newValue
            }
        }) {
            Text(if (owned) "Own on DVD / Blu-ray ✅" else "Own on DVD / Blu-ray")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            userId?.let { uid ->
                val newValue = !wantToGetRidOf
                database.child("users")
                    .child(uid)
                    .child("filmsStatus")
                    .child(film.title)
                    .child("wantToGetRidOf")
                    .setValue(newValue)

                wantToGetRidOf = newValue
            }
        }) {
            Text(if (wantToGetRidOf) "Want to get rid of ✅" else "Want to get rid of")
        }
    }
}