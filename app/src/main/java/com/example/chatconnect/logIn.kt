package com.example.chatconnect

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatconnect.AuthManager.signInWithEmail
import com.example.chatconnect.ui.theme.DarkBg
import com.example.chatconnect.ui.theme.DarkBg2
import com.example.chatconnect.ui.theme.NeonBlue
import com.example.chatconnect.ui.theme.NeonCyan
import com.example.chatconnect.ui.theme.NeonPurple
import com.example.chatconnect.ui.theme.TextPrimary
import kotlinx.coroutines.launch


@Composable
fun CompleteScreen(
    onSignUpClick: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    onOtpSent: (verificationId: String, phoneNumber: String) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val scope = rememberCoroutineScope()
    var emailOrPhone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    // As soon as the user types digits or a +, treat this as a phone login.
    val isPhoneInput = isPhoneNumberInput(emailOrPhone)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Background()
        
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Logo()
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Text(
                text = "Welcome Back",
                color = Color.White,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            OutlinedTextField(
                value = emailOrPhone,
                onValueChange = { emailOrPhone = it },
                label = { Text("Email or Phone") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (isPhoneInput) KeyboardType.Phone else KeyboardType.Email
                ),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedLabelColor = Color.White
                ),
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            if (isPhoneInput) {
                Text(
                    text = "Enter your number with country code, we'll send an OTP.",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            } else {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White
                    ),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Forgot password?",
                    color = NeonCyan,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .align(Alignment.End)
                        .clickable { /* Handle click */ }
                )
            }
            
            Spacer(modifier = Modifier.height(30.dp))
            
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(listOf(NeonBlue, NeonCyan)))
                    .clickable(enabled = !isLoading) {
                        val entered = emailOrPhone.trim()
                        if (isPhoneInput) {
                            when {
                                !entered.startsWith("+") ->
                                    errorMessage = "Include the country code, e.g. +91XXXXXXXXXX"
                                activity == null -> errorMessage = "Something went wrong, please reopen the app"
                                else -> {
                                    errorMessage = null
                                    isLoading = true
                                    AuthManager.sendOtp(
                                        phoneNumber = entered,
                                        activity = activity,
                                        onCodeSent = { verificationId ->
                                            isLoading = false
                                            onOtpSent(verificationId, entered)
                                        },
                                        onAutoVerified = {
                                            isLoading = false
                                            onLoginSuccess()
                                        },
                                        onError = { error ->
                                            isLoading = false
                                            errorMessage = error
                                        }
                                    )
                                }
                            }
                            return@clickable
                        }

                        if (entered.isBlank() || password.isBlank()) {
                            errorMessage = "Please fill all fields"
                            return@clickable
                        }
                        errorMessage = null
                        isLoading = true
                        signInWithEmail(entered, password.trim()) { success, error ->
                            isLoading = false
                            if (success) {
                                onLoginSuccess()
                            } else {
                                errorMessage = error
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    androidx.compose.material3.CircularProgressIndicator(color = Color.White)
                } else {
                    Text(
                        text = if (isPhoneInput) "Send OTP" else "Log In",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(30.dp))
            
            Midline()
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                SocialButton(
                    text = "Google",
                    iconRes = R.drawable.google,
                    enabled = !isLoading,
                    modifier = Modifier.weight(1f)
                ) {
                    errorMessage = null
                    isLoading = true
                    scope.launch {
                        GoogleAuthHelper.signIn(
                            activityContext = context,
                            onSuccess = { idToken ->
                                AuthManager.signInWithGoogleIdToken(idToken) { success, error ->
                                    isLoading = false
                                    if (success) onLoginSuccess() else errorMessage = error
                                }
                            },
                            onError = { error ->
                                isLoading = false
                                errorMessage = error
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Don't have an account?",
                    color = Color.White,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Sign Up",
                    color = NeonCyan,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onSignUpClick()
                    }
                )
            }
        }
    }
}

/** True when the input looks like a phone number - starts with "+", or is only digits/spaces/dashes. */
private fun isPhoneNumberInput(value: String): Boolean {
    val trimmed = value.trim()
    if (trimmed.isBlank()) return false
    if (trimmed.startsWith("+")) return true
    return trimmed.any(Char::isDigit) && trimmed.all { it.isDigit() || it == ' ' || it == '-' }
}


@Composable
fun SocialButton(
    text: String,
    iconRes: Int?,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .clickable(enabled = enabled) { onClick() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (iconRes != null) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(text = text, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun Background(){

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DarkBg, DarkBg2, Color(0xFF0A0E2A))
                )
            )
    ) {


        GlowBlob(
            modifier = Modifier
                .offset((-40).dp, 120.dp)
                .size(260.dp),
            color = NeonPurple.copy(alpha = 0.18f)
        )
        GlowBlob(
            modifier = Modifier
                .offset(160.dp, 60.dp)
                .size(200.dp),
            color = NeonBlue.copy(alpha = 0.15f)
        )
        GlowBlob(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset((-40).dp, (-100).dp)
                .size(320.dp),
            color = NeonCyan.copy(alpha = 0.10f)
        )
        GlowBlob(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(40.dp, (-200).dp)
                .size(200.dp),
            color = NeonPurple.copy(alpha = 0.13f)
        )
}}

@Composable
fun Midline(){
   Row(
       verticalAlignment = Alignment.CenterVertically,
       horizontalArrangement = Arrangement.Center,
       modifier = Modifier.fillMaxWidth()
   ) {
       Box(
           modifier = Modifier
               .width(70.dp)
               .height(1.dp)
               .background(Color.White)
       )
       Text(
           text = "or continue with",
           fontSize = 16.sp,
           color = Color.White,
           modifier = Modifier.padding(horizontal = 16.dp)
       )
       Box(
           modifier = Modifier
               .width(70.dp)
               .height(1.dp)
               .background(Color.White)
       )
   }
}

@Preview
@Composable
fun CompleteScreenPreview() {
    CompleteScreen()
}
