package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign

val appleGreen = Color(0xFF8DB600) // Apple Green Hex Code

fun authenticateUser(email: String, password: String): UserRole {
    // Replace this mock logic with real authentication logic
    return when (email) {
        "matambovincent7@gmail.com" -> UserRole.ADMIN
        "provider@example.com" -> UserRole.HEALTHCARE_PROVIDER
        else -> UserRole.PATIENT
    }
}

@Composable
fun LoginScreen(onLoginSuccess: (UserRole) -> Unit, onNavigateToSignUp: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Predictive Healthy Analytics System",
            style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Image(
            painter = painterResource(id = R.drawable.a),
            contentDescription = "Login Image",
            modifier = Modifier.size(200.dp)
        )

        Text(
            text = "Welcome Back",
            style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Login to your account")

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email Address") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val userRole = authenticateUser(email, password)
                onLoginSuccess(userRole)
            },
            colors = ButtonDefaults.buttonColors(containerColor = appleGreen)
        ) {
            Text(text = "Login", color = Color.White)
        }
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Forgot Password",
            color = appleGreen,
            modifier = Modifier.clickable { }
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Or Sign Up",
            color = appleGreen,
            modifier = Modifier.clickable {
                onNavigateToSignUp()
            }
        )
    }
}
