package com.example.chatconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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


                var verificationId by remember { mutableStateOf("") }
                var phoneNumber by remember { mutableStateOf("") }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "welcome",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("welcome") {
                            WelcomeScreen(
                                onGetStarted = { navController.navigate("login") },
                                onLogIn = { navController.navigate("login") }
                            )
                        }
                        composable("login") {
                            CompleteScreen(
                                onSignUpClick = { navController.navigate("signup") },
                                onLoginSuccess = { 
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onOtpSent = { id, number ->
                                    verificationId = id
                                    phoneNumber = number
                                    navController.navigate("otp")
                                }
                            )
                        }
                        composable("otp") {
                            OtpVerifyScreen(
                                verificationId = verificationId,
                                phoneNumber = phoneNumber,
                                onVerified = {
                                    navController.navigate("home") {
                                        popUpTo("welcome") { inclusive = true }
                                    }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("signup") {
                            CreateAccountScreen(
                                onBackToLogin = { navController.popBackStack() },
                                onAuthSuccess = { navController.navigate("verify") }
                            )
                        }
                        composable("verify") {
                            VerifyEmailScreen(
                                onVerified = {
                                    navController.navigate("home") {
                                        popUpTo("welcome") { inclusive = true }
                                    }
                                }
                            )
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


