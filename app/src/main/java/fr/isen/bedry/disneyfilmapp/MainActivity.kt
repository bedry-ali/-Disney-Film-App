package fr.isen.bedry.disneyfilmapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        setContent {
            DisneyFilmApp(auth, database)
        }
    }
}

@Composable
fun DisneyFilmApp(auth: FirebaseAuth, database: DatabaseReference) {

    var isLoggedIn by remember { mutableStateOf(true) }
    var showProfile by remember { mutableStateOf(false) }

    var categories by remember { mutableStateOf(listOf<CategoryItem>()) }
    var franchises by remember { mutableStateOf(listOf<FranchiseItem>()) }
    var sagas by remember { mutableStateOf(listOf<SagaItem>()) }
    var films by remember { mutableStateOf(listOf<FilmItem>()) }

    var selectedCategory by remember { mutableStateOf<CategoryItem?>(null) }
    var selectedFranchise by remember { mutableStateOf<FranchiseItem?>(null) }
    var selectedSaga by remember { mutableStateOf<SagaItem?>(null) }
    var selectedFilm by remember { mutableStateOf<FilmItem?>(null) }

    LaunchedEffect(Unit) {
        database.child("categories")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tempList = mutableListOf<CategoryItem>()

                    for (category in snapshot.children) {
                        val name = category.child("categorie").getValue(String::class.java)
                        if (name != null) {
                            tempList.add(CategoryItem(category.key ?: "", name))
                        }
                    }

                    categories = tempList
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Firebase error: ${error.message}")
                }
            })
    }

    fun loadFranchises(categoryId: String) {
        database.child("categories")
            .child(categoryId)
            .child("franchises")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tempList = mutableListOf<FranchiseItem>()

                    for (franchise in snapshot.children) {
                        val name = franchise.child("nom").getValue(String::class.java)
                        if (name != null) {
                            tempList.add(FranchiseItem(franchise.key ?: "", name))
                        }
                    }

                    franchises = tempList
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Firebase error: ${error.message}")
                }
            })
    }

    fun loadSagasOrFilms(categoryId: String, franchiseId: String) {
        database.child("categories")
            .child(categoryId)
            .child("franchises")
            .child(franchiseId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val sousSagasSnapshot = snapshot.child("sous_sagas")
                    val filmsSnapshot = snapshot.child("films")

                    if (sousSagasSnapshot.exists()) {
                        val tempSagas = mutableListOf<SagaItem>()

                        for (saga in sousSagasSnapshot.children) {
                            val name = saga.child("nom").getValue(String::class.java)
                            val sagaName = name ?: "Saga ${saga.key}"
                            tempSagas.add(SagaItem(saga.key ?: "", sagaName))
                        }

                        sagas = tempSagas
                        films = emptyList()
                    } else if (filmsSnapshot.exists()) {
                        val tempFilms = mutableListOf<FilmItem>()

                        for (film in filmsSnapshot.children) {
                            val titre = film.child("titre").getValue(String::class.java) ?: ""
                            val annee = film.child("annee").value?.toString() ?: ""
                            val genre = film.child("genre").getValue(String::class.java) ?: ""
                            val numero = film.child("numero").value?.toString() ?: ""

                            if (titre.isNotEmpty()) {
                                tempFilms.add(
                                    FilmItem(
                                        title = titre,
                                        year = annee,
                                        genre = genre,
                                        number = numero
                                    )
                                )
                            }
                        }

                        films = tempFilms
                        sagas = emptyList()
                    } else {
                        sagas = emptyList()
                        films = emptyList()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Firebase error: ${error.message}")
                }
            })
    }

    fun loadFilms(categoryId: String, franchiseId: String, sagaId: String) {
        database.child("categories")
            .child(categoryId)
            .child("franchises")
            .child(franchiseId)
            .child("sous_sagas")
            .child(sagaId)
            .child("films")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tempList = mutableListOf<FilmItem>()

                    for (film in snapshot.children) {
                        val titre = film.child("titre").getValue(String::class.java) ?: ""
                        val annee = film.child("annee").value?.toString() ?: ""
                        val genre = film.child("genre").getValue(String::class.java) ?: ""
                        val numero = film.child("numero").value?.toString() ?: ""

                        if (titre.isNotEmpty()) {
                            tempList.add(
                                FilmItem(
                                    title = titre,
                                    year = annee,
                                    genre = genre,
                                    number = numero
                                )
                            )
                        }
                    }

                    films = tempList
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Firebase error: ${error.message}")
                }
            })
    }

    when {
        !isLoggedIn -> {
            LoginScreen(
                auth = auth,
                onLoginSuccess = { isLoggedIn = true }
            )
        }

        showProfile -> {
            ProfileScreen(
                auth = auth,
                database = database,
                onBackClick = {
                    showProfile = false
                },
                onLogoutClick = {
                    auth.signOut()
                    showProfile = false
                    selectedCategory = null
                    selectedFranchise = null
                    selectedSaga = null
                    selectedFilm = null
                    isLoggedIn = false
                }
            )
        }

        selectedCategory == null -> {
            CategoriesScreen(
                categories = categories,
                onCategoryClick = { category ->
                    selectedCategory = category
                    selectedFranchise = null
                    selectedSaga = null
                    selectedFilm = null
                    sagas = emptyList()
                    films = emptyList()
                    loadFranchises(category.id)
                },
                onProfileClick = {
                    showProfile = true
                }
            )
        }

        selectedFranchise == null -> {
            FranchisesScreen(
                categoryName = selectedCategory!!.name,
                franchises = franchises,
                onBackClick = {
                    selectedCategory = null
                },
                onFranchiseClick = { franchise ->
                    selectedFranchise = franchise
                    selectedSaga = null
                    selectedFilm = null
                    loadSagasOrFilms(selectedCategory!!.id, franchise.id)
                }
            )
        }

        selectedSaga == null && sagas.isNotEmpty() -> {
            SagasScreen(
                franchiseName = selectedFranchise!!.name,
                sagas = sagas,
                onBackClick = {
                    selectedFranchise = null
                    sagas = emptyList()
                },
                onSagaClick = { saga ->
                    selectedSaga = saga
                    selectedFilm = null
                    loadFilms(selectedCategory!!.id, selectedFranchise!!.id, saga.id)
                }
            )
        }

        selectedFilm == null -> {
            FilmsScreen(
                title = selectedSaga?.name ?: selectedFranchise?.name ?: "Films",
                films = films,
                onBackClick = {
                    if (selectedSaga != null) {
                        selectedSaga = null
                    } else {
                        selectedFranchise = null
                    }
                },
                onFilmClick = { film ->
                    selectedFilm = film
                }
            )
        }

        else -> {
            FilmDetailScreen(
                film = selectedFilm!!,
                auth = auth,
                database = database,
                onBackClick = {
                    selectedFilm = null
                }
            )
        }
    }
}