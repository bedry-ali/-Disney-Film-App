package fr.isen.bedry.disneyfilmapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun RegisterScreen(
    auth: FirebaseAuth,
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create your account",
            color = Color.White,
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = firstName,
            onValueChange = {
                firstName = it
                errorMessage = ""
            },
            label = { Text("First name", color = Color.LightGray) },
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            colors = textFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = {
                lastName = it
                errorMessage = ""
            },
            label = { Text("Last name", color = Color.LightGray) },
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            colors = textFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                errorMessage = ""
            },
            label = { Text("Email", color = Color.LightGray) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            colors = textFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = ""
            },
            label = { Text("Password", color = Color.LightGray) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            colors = textFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Button(
            onClick = {
                val cleanFirstName = firstName.trim()
                val cleanLastName = lastName.trim()
                val cleanEmail = email.trim()
                val cleanPassword = password.trim()

                if (
                    cleanFirstName.isEmpty() ||
                    cleanLastName.isEmpty() ||
                    cleanEmail.isEmpty() ||
                    cleanPassword.isEmpty()
                ) {
                    errorMessage = "Please fill in all fields"
                    return@Button
                }

                auth.createUserWithEmailAndPassword(cleanEmail, cleanPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid

                            if (userId != null) {
                                val userMap = mapOf(
                                    "firstName" to cleanFirstName,
                                    "lastName" to cleanLastName,
                                    "email" to cleanEmail
                                )

                                FirebaseDatabase.getInstance()
                                    .reference
                                    .child("users")
                                    .child(userId)
                                    .setValue(userMap)
                                    .addOnSuccessListener {
                                        onRegisterSuccess()
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
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { onBackToLogin() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Back to Login")
        }
    }
}

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFF7C4DFF),
    unfocusedBorderColor = Color.Gray,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedLabelColor = Color(0xFF7C4DFF),
    unfocusedLabelColor = Color.LightGray,
    cursorColor = Color.White
)