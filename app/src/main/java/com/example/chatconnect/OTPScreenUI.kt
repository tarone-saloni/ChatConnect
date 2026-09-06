package com.example.chatconnect

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatconnect.ui.theme.NeonBlue
import com.example.chatconnect.ui.theme.NeonCyan

/** Pulls the Activity out of a Context - Firebase phone auth needs an Activity. */
internal fun Context.findActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

@Composable
fun PhoneNumberScreen(
    onOtpSent: (verificationId: String, phoneNumber: String) -> Unit,
    onAutoVerified: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val activity = LocalContext.current.findActivity()

    var phoneNumber by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Background()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Sign in with phone",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Enter your number with country code, we'll send an OTP.",
                color = Color.White,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone number") },
                placeholder = { Text("+91XXXXXXXXXX") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = neonFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = Color.Red, fontSize = 14.sp, textAlign = TextAlign.Center)
            }

            Spacer(modifier = Modifier.height(24.dp))

            NeonButton(
                text = "Send OTP",
                isLoading = isLoading,
                onClick = {
                    val number = phoneNumber.trim()
                    when {
                        number.isBlank() -> errorMessage = "Enter a phone number"
                        !number.startsWith("+") ->
                            errorMessage = "Include the country code, e.g. +91XXXXXXXXXX"
                        activity == null -> errorMessage = "Something went wrong, please reopen the app"
                        else -> {
                            errorMessage = null
                            isLoading = true
                            AuthManager.sendOtp(
                                phoneNumber = number,
                                activity = activity,
                                onCodeSent = { verificationId ->
                                    isLoading = false
                                    onOtpSent(verificationId, number)
                                },
                                onAutoVerified = {
                                    isLoading = false
                                    onAutoVerified()
                                },
                                onError = { error ->
                                    isLoading = false
                                    errorMessage = error
                                }
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Go back",
                color = NeonCyan,
                fontSize = 15.sp,
                modifier = Modifier.clickable { onBack() }
            )
        }
    }
}

@Composable
fun OtpVerifyScreen(
    verificationId: String,
    phoneNumber: String = "",
    onVerified: () -> Unit,
    onBack: () -> Unit = {}
) {
    val activity = LocalContext.current.findActivity()

    var code by remember { mutableStateOf("") }
    var currentVerificationId by remember { mutableStateOf(verificationId) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var infoMessage by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Background()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Enter OTP",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (phoneNumber.isBlank()) "Enter the 6 digit code"
                else "Enter the 6 digit code sent to $phoneNumber",
                color = Color.White,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = code,
                onValueChange = { if (it.length <= 6 && it.all(Char::isDigit)) code = it },
                label = { Text("OTP") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = neonFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = Color.Red, fontSize = 14.sp, textAlign = TextAlign.Center)
            }
            infoMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = NeonCyan, fontSize = 14.sp, textAlign = TextAlign.Center)
            }

            Spacer(modifier = Modifier.height(24.dp))

            NeonButton(
                text = "Verify",
                isLoading = isLoading,
                onClick = {
                    if (code.length != 6) {
                        errorMessage = "Enter the full 6 digit OTP"
                        return@NeonButton
                    }
                    errorMessage = null
                    infoMessage = null
                    isLoading = true
                    AuthManager.verifyOtp(currentVerificationId, code) { success, error ->
                        isLoading = false
                        if (success) onVerified() else errorMessage = error
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Resend OTP",
                color = NeonCyan,
                fontSize = 15.sp,
                modifier = Modifier.clickable(enabled = !isLoading && phoneNumber.isNotBlank()) {
                    if (activity == null) return@clickable
                    errorMessage = null
                    infoMessage = null
                    isLoading = true
                    AuthManager.sendOtp(
                        phoneNumber = phoneNumber,
                        activity = activity,
                        isResend = true,
                        onCodeSent = { newId ->
                            isLoading = false
                            currentVerificationId = newId
                            infoMessage = "New OTP sent"
                        },
                        onAutoVerified = {
                            isLoading = false
                            onVerified()
                        },
                        onError = { error ->
                            isLoading = false
                            errorMessage = error
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Change number",
                color = Color.White,
                fontSize = 15.sp,
                modifier = Modifier.clickable { onBack() }
            )
        }
    }
}

@Composable
private fun neonFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color.White,
    unfocusedBorderColor = Color.White,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    cursorColor = Color.White,
    focusedLabelColor = Color.White,
    unfocusedLabelColor = Color.White
)

@Composable
private fun NeonButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(listOf(NeonBlue, NeonCyan)))
            .clickable(enabled = !isLoading) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.White)
        } else {
            Text(text = text, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}
