package fr.isen.bedry.disneyfilmapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun LoginScreen(
    auth: FirebaseAuth,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Disney Film App Login")

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                errorMessage = ""
            },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = ""
            },
            label = { Text("Password") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Button(onClick = {
            val cleanEmail = email.trim()
            val cleanPassword = password.trim()

            if (cleanEmail.isEmpty() || cleanPassword.isEmpty()) {
                errorMessage = "Please fill in all fields"
                return@Button
            }

            auth.signInWithEmailAndPassword(cleanEmail, cleanPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        val currentEmail = auth.currentUser?.email ?: cleanEmail

                        if (userId != null) {
                            FirebaseDatabase.getInstance()
                                .reference
                                .child("users")
                                .child(userId)
                                .child("email")
                                .setValue(currentEmail)
                                .addOnSuccessListener {
                                    onLoginSuccess()
                                }
                                .addOnFailureListener { e ->
                                    errorMessage = e.message ?: "Database save failed"
                                }
                        } else {
                            errorMessage = "User ID not found"
                        }
                    } else {
                        errorMessage = task.exception?.message ?: "Login failed"
                    }
                }
        }) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            val cleanEmail = email.trim()
            val cleanPassword = password.trim()

            if (cleanEmail.isEmpty() || cleanPassword.isEmpty()) {
                errorMessage = "Please fill in all fields"
                return@Button
            }

            auth.createUserWithEmailAndPassword(cleanEmail, cleanPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid

                        if (userId != null) {
                            FirebaseDatabase.getInstance()
                                .reference
                                .child("users")
                                .child(userId)
                                .child("email")
                                .setValue(cleanEmail)
                                .addOnSuccessListener {
                                    onLoginSuccess()
                                }
                                .addOnFailureListener { e ->
                                    errorMessage = e.message ?: "Database save failed"
                                }
                        } else {
                            errorMessage = "User ID not found"
                        }
                    } else {
                        errorMessage = task.exception?.message ?: "Register failed"
                    }
                }
        }) {
            Text("Register")
        }
    }
}