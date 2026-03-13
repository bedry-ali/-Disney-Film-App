package fr.isen.bedry.disneyfilmapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

@Composable
fun ProfileScreen(
    auth: FirebaseAuth,
    database: DatabaseReference,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val userId = auth.currentUser?.uid
    val userEmail = auth.currentUser?.email ?: "Unknown user"

    var watchedMovies by remember { mutableStateOf(listOf<String>()) }
    var ownedMovies by remember { mutableStateOf(listOf<String>()) }
    var wantToWatchMovies by remember { mutableStateOf(listOf<String>()) }
    var wantToGetRidOfMovies by remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(Unit) {
        userId?.let { uid ->
            database.child("users")
                .child(uid)
                .child("filmsStatus")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val watchedList = mutableListOf<String>()
                        val ownedList = mutableListOf<String>()
                        val wantToWatchList = mutableListOf<String>()
                        val wantToGetRidOfList = mutableListOf<String>()

                        for (filmSnapshot in snapshot.children) {
                            val filmTitle = filmSnapshot.key ?: continue

                            val watched = filmSnapshot.child("watched")
                                .getValue(Boolean::class.java) ?: false

                            val owned = filmSnapshot.child("owned")
                                .getValue(Boolean::class.java) ?: false

                            val wantToWatch = filmSnapshot.child("wantToWatch")
                                .getValue(Boolean::class.java) ?: false

                            val wantToGetRidOf = filmSnapshot.child("wantToGetRidOf")
                                .getValue(Boolean::class.java) ?: false

                            if (watched) watchedList.add(filmTitle)
                            if (owned) ownedList.add(filmTitle)
                            if (wantToWatch) wantToWatchList.add(filmTitle)
                            if (wantToGetRidOf) wantToGetRidOfList.add(filmTitle)
                        }

                        watchedMovies = watchedList
                        ownedMovies = ownedList
                        wantToWatchMovies = wantToWatchList
                        wantToGetRidOfMovies = wantToGetRidOfList
                    }

                    override fun onCancelled(error: DatabaseError) {
                        println("Firebase error: ${error.message}")
                    }
                })
        }
    }

    fun removeStatus(filmTitle: String, statusKey: String) {
        userId?.let { uid ->
            database.child("users")
                .child(uid)
                .child("filmsStatus")
                .child(filmTitle)
                .child(statusKey)
                .setValue(false)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { onBackClick() }) {
                Text("Back")
            }

            Button(onClick = { onLogoutClick() }) {
                Text("Logout")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Email: $userEmail")

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                ProfileSection(
                    title = "Watched movies",
                    movies = watchedMovies,
                    onRemove = { filmTitle ->
                        removeStatus(filmTitle, "watched")
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                ProfileSection(
                    title = "Owned movies",
                    movies = ownedMovies,
                    onRemove = { filmTitle ->
                        removeStatus(filmTitle, "owned")
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                ProfileSection(
                    title = "Want to watch",
                    movies = wantToWatchMovies,
                    onRemove = { filmTitle ->
                        removeStatus(filmTitle, "wantToWatch")
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                ProfileSection(
                    title = "Want to get rid of",
                    movies = wantToGetRidOfMovies,
                    onRemove = { filmTitle ->
                        removeStatus(filmTitle, "wantToGetRidOf")
                    }
                )
            }
        }
    }
}

@Composable
fun ProfileSection(
    title: String,
    movies: List<String>,
    onRemove: (String) -> Unit
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge
    )

    Spacer(modifier = Modifier.height(8.dp))

    if (movies.isEmpty()) {
        Text(text = "No movies")
    } else {
        Column {
            movies.forEach { movie ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = movie)

                        Button(onClick = { onRemove(movie) }) {
                            Text("Remove")
                        }
                    }
                }
            }
        }
    }
}