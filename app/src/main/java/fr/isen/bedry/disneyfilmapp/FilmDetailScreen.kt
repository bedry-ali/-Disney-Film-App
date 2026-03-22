package fr.isen.bedry.disneyfilmapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun FilmDetailScreen(
    film: FilmItem,
    auth: FirebaseAuth,
    database: DatabaseReference,
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val userId = auth.currentUser?.uid

    var watched by remember { mutableStateOf(false) }
    var wantToWatch by remember { mutableStateOf(false) }
    var owned by remember { mutableStateOf(false) }
    var wantToGetRidOf by remember { mutableStateOf(false) }

    var usersWhoOwnAndWantToGetRid by remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(film.title, userId) {
        if (userId == null) {
            println("ERROR: userId is null")
            return@LaunchedEffect
        }

        database.child("users")
            .child(userId)
            .child("filmsStatus")
            .child(film.title)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    watched = snapshot.child("watched").getValue(Boolean::class.java) ?: false
                    wantToWatch = snapshot.child("wantToWatch").getValue(Boolean::class.java) ?: false
                    owned = snapshot.child("owned").getValue(Boolean::class.java) ?: false
                    wantToGetRidOf = snapshot.child("wantToGetRidOf").getValue(Boolean::class.java) ?: false

                    println("READ STATUS -> ${film.title}")
                    println("watched=$watched wantToWatch=$wantToWatch owned=$owned wantToGetRidOf=$wantToGetRidOf")
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Firebase read error: ${error.message}")
                }
            })

        database.child("users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tempUsers = mutableListOf<String>()

                    for (userSnapshot in snapshot.children) {
                        val otherUserId = userSnapshot.key ?: continue
                        val email = userSnapshot.child("email").getValue(String::class.java) ?: "Unknown user"

                        val filmStatusSnapshot = userSnapshot
                            .child("filmsStatus")
                            .child(film.title)

                        val otherOwned = filmStatusSnapshot
                            .child("owned")
                            .getValue(Boolean::class.java) ?: false

                        val otherWantToGetRidOf = filmStatusSnapshot
                            .child("wantToGetRidOf")
                            .getValue(Boolean::class.java) ?: false

                        if (otherUserId != userId && otherOwned && otherWantToGetRidOf) {
                            tempUsers.add(email)
                        }
                    }

                    usersWhoOwnAndWantToGetRid = tempUsers
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Firebase users list error: ${error.message}")
                }
            })
    }

    UiScreen(
        title = film.title,
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
                Text("Episode: ${film.number}", color = GrayText)
                Text("Year: ${film.year}", color = GrayText)
                Text("Genre: ${film.genre}", color = GrayText)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        StatusButton(
            text = if (watched) "Watched ✅" else "Watched",
            onClick = {
                if (userId == null) {
                    println("ERROR: userId is null on watched click")
                    return@StatusButton
                }

                val newValue = !watched
                database.child("users")
                    .child(userId)
                    .child("filmsStatus")
                    .child(film.title)
                    .child("watched")
                    .setValue(newValue)
                    .addOnSuccessListener {
                        watched = newValue
                        println("WRITE watched=$newValue for ${film.title}")
                    }
                    .addOnFailureListener { e ->
                        println("WRITE watched failed: ${e.message}")
                    }
            }
        )

        StatusButton(
            text = if (wantToWatch) "Want to watch ✅" else "Want to watch",
            onClick = {
                if (userId == null) {
                    println("ERROR: userId is null on wantToWatch click")
                    return@StatusButton
                }

                val newValue = !wantToWatch
                database.child("users")
                    .child(userId)
                    .child("filmsStatus")
                    .child(film.title)
                    .child("wantToWatch")
                    .setValue(newValue)
                    .addOnSuccessListener {
                        wantToWatch = newValue
                        println("WRITE wantToWatch=$newValue for ${film.title}")
                    }
                    .addOnFailureListener { e ->
                        println("WRITE wantToWatch failed: ${e.message}")
                    }
            }
        )

        StatusButton(
            text = if (owned) "Own on DVD / Blu-ray ✅" else "Own on DVD / Blu-ray",
            onClick = {
                if (userId == null) {
                    println("ERROR: userId is null on owned click")
                    return@StatusButton
                }

                val newValue = !owned
                database.child("users")
                    .child(userId)
                    .child("filmsStatus")
                    .child(film.title)
                    .child("owned")
                    .setValue(newValue)
                    .addOnSuccessListener {
                        owned = newValue
                        println("WRITE owned=$newValue for ${film.title}")
                    }
                    .addOnFailureListener { e ->
                        println("WRITE owned failed: ${e.message}")
                    }
            }
        )

        StatusButton(
            text = if (wantToGetRidOf) "Want to get rid of ✅" else "Want to get rid of",
            onClick = {
                if (userId == null) {
                    println("ERROR: userId is null on wantToGetRidOf click")
                    return@StatusButton
                }

                val newValue = !wantToGetRidOf
                database.child("users")
                    .child(userId)
                    .child("filmsStatus")
                    .child(film.title)
                    .child("wantToGetRidOf")
                    .setValue(newValue)
                    .addOnSuccessListener {
                        wantToGetRidOf = newValue
                        println("WRITE wantToGetRidOf=$newValue for ${film.title}")
                    }
                    .addOnFailureListener { e ->
                        println("WRITE wantToGetRidOf failed: ${e.message}")
                    }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Users who own this movie and want to get rid of it",
            color = WhiteText,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (usersWhoOwnAndWantToGetRid.isEmpty()) {
            Text("No users found", color = GrayText)
        } else {
            LazyColumn {
                items(usersWhoOwnAndWantToGetRid) { email ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkCard),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = email,
                            color = WhiteText,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        Text(text)
    }
}