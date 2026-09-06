package com.example.chatconnect

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatconnect.ui.theme.NeonBlue
import com.example.chatconnect.ui.theme.NeonCyan

@Composable
fun VerifyEmailScreen(
    onVerified: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundforSignUp()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Verify your email",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "We've sent a link to your email. " +
                        "Open the email, click the link, then come back here and " +
                        "tap the button below.",
                color = Color.White,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (message != null) {
                Text(text = message!!, color = Color.Red, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(12.dp))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(listOf(NeonBlue, NeonCyan)))
                    .clickable(enabled = !isLoading) {
                        message = null
                        isLoading = true
                        AuthManager.checkEmailVerified { success, error ->
                            isLoading = false
                            if (success) onVerified() else message = error
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text("I've verified", color = Color.White, fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Resend link",
                color = NeonCyan,
                fontSize = 15.sp,
                modifier = Modifier.clickable {
                    AuthManager.resendVerification { success, error ->
                        message = if (success) "Link sent" else error
                    }
                }
            )
        }
    }
}