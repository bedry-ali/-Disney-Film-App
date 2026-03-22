package fr.isen.bedry.disneyfilmapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import fr.isen.bedry.disneyfilmapp.ui.theme.DarkCard
import fr.isen.bedry.disneyfilmapp.ui.theme.GrayText
import fr.isen.bedry.disneyfilmapp.ui.theme.WhiteText

@Composable
fun ProfileScreen(
    auth: FirebaseAuth,
    database: DatabaseReference,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onProfileClick: () -> Unit
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
                            val watched = filmSnapshot.child("watched").getValue(Boolean::class.java) ?: false
                            val owned = filmSnapshot.child("owned").getValue(Boolean::class.java) ?: false
                            val wantToWatch = filmSnapshot.child("wantToWatch").getValue(Boolean::class.java) ?: false
                            val wantToGetRidOf = filmSnapshot.child("wantToGetRidOf").getValue(Boolean::class.java) ?: false

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

                    override fun onCancelled(error: DatabaseError) {}
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

    UiScreen(
        title = "Profile",
        showBack = true,
        onBackClick = onBackClick,
        onProfileClick = onProfileClick,
        onLogoutClick = onLogoutClick
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkCard),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Logged in as", color = GrayText)
                Text(userEmail, color = WhiteText, style = MaterialTheme.typography.titleMedium)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            item {
                ProfileSection("Watched movies", watchedMovies) { removeStatus(it, "watched") }
                Spacer(modifier = Modifier.height(16.dp))
                ProfileSection("Owned movies", ownedMovies) { removeStatus(it, "owned") }
                Spacer(modifier = Modifier.height(16.dp))
                ProfileSection("Want to watch", wantToWatchMovies) { removeStatus(it, "wantToWatch") }
                Spacer(modifier = Modifier.height(16.dp))
                ProfileSection("Want to get rid of", wantToGetRidOfMovies) { removeStatus(it, "wantToGetRidOf") }
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
        color = WhiteText,
        style = MaterialTheme.typography.titleLarge
    )

    Spacer(modifier = Modifier.height(8.dp))

    if (movies.isEmpty()) {
        Text("No movies", color = GrayText)
    } else {
        Column {
            movies.forEach { movie ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
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
                        Text(movie, color = WhiteText)

                        TextButton(onClick = { onRemove(movie) }) {
                            Text("Remove")
                        }
                    }
                }
            }
        }
    }
}