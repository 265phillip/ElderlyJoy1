package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf("login") }
    var userRole by remember { mutableStateOf<UserRole?>(null) }

    when (currentScreen) {
        "login" -> LoginScreen(
            onLoginSuccess = {
                userRole = it
                currentScreen = "home"
            },
            onNavigateToSignUp = { currentScreen = "signup" }
        )
        "signup" -> SignUpScreen(
            onSignUpSuccess = {
                userRole = UserRole.PATIENT
                currentScreen = "home"
            },
            onNavigateToLogin = { currentScreen = "login" }
        )
        "home" -> when (userRole) {
            UserRole.ADMIN -> AdminHomePage()
            UserRole.HEALTHCARE_PROVIDER -> ProviderHomePage()
            UserRole.PATIENT -> PatientHomePage(
                onChatClick = { currentScreen = "chat" },
                onHealthCheckClick = { currentScreen = "health_check" },
                onNotificationsClick = { currentScreen = "notifications" },
                onSettingsClick = { currentScreen = "settings" }
            )
            null -> LoginScreen(onLoginSuccess = {}, onNavigateToSignUp = {})
        }
        "chat" -> ChatScreen(onBackClick = { currentScreen = "home" })
        "health_check" -> HealthCheckScreen(onBackClick = { currentScreen = "home" })
        "notifications" -> NotificationsScreen(onBackClick = { currentScreen = "home" })
        "settings" -> SettingsScreen(onBackClick = { currentScreen = "home" })
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    MyApplicationTheme {
        AppNavigation()
    }
}
