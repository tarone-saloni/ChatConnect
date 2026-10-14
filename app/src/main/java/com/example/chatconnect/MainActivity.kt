package com.example.chatconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatconnect.ui.theme.ChatConnectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatConnectTheme {
                val navController = rememberNavController()
                val startDestination = if (AuthManager.currentUser() != null) "home" else "login"

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") {
                            AuthPage {
                                signIn(
                                    onCreateAccountClick = { navController.navigate("signup") },
                                    onSignInSuccess = {
                                        navController.navigate("home") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                )
                            }
                        }
                        composable("signup") {
                            signupScreen(onSignInClick = { navController.popBackStack() })
                        }
                        composable("home") {
                            HomeUIPage()
                        }
                    }
                }
            }
        }
    }
}
