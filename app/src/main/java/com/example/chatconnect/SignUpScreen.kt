package com.example.chatconnect

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(heightDp = 900)
@Composable
fun signupScreen(onSignInClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .gridBackground()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 30.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBg)
                .border(1.dp, Border)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_192),
                contentDescription = "app logo",
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "CHATSCONNECT",
                color = TextWhite,
                fontFamily = Mono,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                letterSpacing = 3.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        signUp(onSignInClick = onSignInClick)
    }
}

@Composable
fun signUp(onSignInClick: () -> Unit) {
    val context = LocalContext.current

    var step by remember { mutableIntStateOf(0) }

    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var code by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                val len = 16.dp.toPx()
                val stroke = 2.dp.toPx()
                val gap = 6.dp.toPx()
                drawLine(Mint, Offset(-gap, -gap), Offset(-gap + len, -gap), stroke)
                drawLine(Mint, Offset(-gap, -gap), Offset(-gap, -gap + len), stroke)
                val r = size.width + gap
                val b = size.height + gap
                drawLine(Mint, Offset(r, b), Offset(r - len, b), stroke)
                drawLine(Mint, Offset(r, b), Offset(r, b - len), stroke)
            }
            .background(CardBg)
            .border(1.dp, Border)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Text(
                text = "AUTHENTICATION",
                color = TextGray,
                fontFamily = Mono,
                fontSize = 11.sp,
                letterSpacing = 3.sp,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .background(Mint)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "SECURE",
                color = Mint,
                fontFamily = Mono,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 2.sp
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Border)
        )

        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "CREATE YOUR ACCOUNT",
                color = TextWhite,
                fontFamily = Mono,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "It takes less than a minute.",
                color = TextGray,
                fontFamily = Mono,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            StepTabs(
                current = step,
                onStepClick = { if (it < step) step = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            when (step) {
                0 -> {
                    FieldTitle("NAME")
                    DesignTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = "Your name",
                        keyboardType = KeyboardType.Text,
                        leadingIcon = { PersonIcon() }
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    FieldTitle("USERNAME")
                    DesignTextField(
                        value = username,
                        onValueChange = { new ->
                            username = new.filter { it.isLetterOrDigit() || it == '_' }.take(20)
                        },
                        placeholder = "yourname",
                        keyboardType = KeyboardType.Text,
                        leadingIcon = { AtIcon() }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HintText("3-20 letters, numbers or underscores")

                    Spacer(modifier = Modifier.height(18.dp))

                    FieldTitle("EMAIL")
                    DesignTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "you@example.com",
                        keyboardType = KeyboardType.Email,
                        leadingIcon = { MailIcon() }
                    )
                }

                1 -> {
                    FieldTitle("PASSWORD")
                    DesignTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "",
                        keyboardType = KeyboardType.Password,
                        visualTransformation = if (showPassword) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            EyeIcon(modifier = Modifier.clickable { showPassword = !showPassword })
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HintText("At least 8 characters")

                    Spacer(modifier = Modifier.height(18.dp))

                    FieldTitle("CONFIRM PASSWORD")
                    DesignTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = "",
                        keyboardType = KeyboardType.Password,
                        visualTransformation = if (showPassword) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        }
                    )
                }

                else -> {
                    FieldTitle("VERIFICATION CODE")
                    DesignTextField(
                        value = code,
                        onValueChange = { new -> code = new.filter { it.isDigit() }.take(6) },
                        placeholder = "6-digit code",
                        keyboardType = KeyboardType.Number
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HintText("We sent a code to ${email.ifEmpty { "your email" }}")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(SignInBg)
                    .border(1.dp, Mint.copy(alpha = 0.5f))
                    .clickable {
                        if (step < 2) {
                            step++
                        } else if (email.isBlank() || password.isBlank()) {
                            Toast.makeText(context, "Email aur password dalo", Toast.LENGTH_SHORT).show()
                        } else if (password != confirmPassword) {
                            Toast.makeText(context, "Passwords match nahi kar rahe", Toast.LENGTH_SHORT).show()
                        } else {
                            AuthManager.signUpWithEmail(email.trim(), password) { ok, error ->
                                if (ok) {
                                    Toast.makeText(context, "Account ban gaya! Email verify karke sign in karo", Toast.LENGTH_LONG).show()
                                    onSignInClick()
                                } else {
                                    Toast.makeText(context, error ?: "Sign up failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
            ) {
                Text(
                    text = if (step < 2) "CONTINUE" else "CREATE ACCOUNT",
                    color = Mint,
                    fontFamily = Mono,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    letterSpacing = 5.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                DashedLine(Modifier.weight(1f))
                Text(
                    text = "OR",
                    color = TextGray,
                    fontFamily = Mono,
                    fontSize = 11.sp,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                DashedLine(Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .border(1.dp, Border)
                    .clickable {
                        // Button click hone par ye code chalega
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.github_5968866),
                    contentDescription = "github logo",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "SIGN UP WITH GITHUB",
                    color = TextWhite,
                    fontFamily = Mono,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    letterSpacing = 2.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Border)
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp)
        ) {
            Text(
                text = "Already have an account? ",
                color = TextGray,
                fontFamily = Mono,
                fontSize = 13.sp
            )
            Text(
                text = "Sign in",
                color = Mint,
                fontFamily = Mono,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onSignInClick() }
            )
        }
    }
}

// 01 DETAILS | 02 PASSWORD | 03 VERIFY
@Composable
private fun StepTabs(current: Int, onStepClick: (Int) -> Unit) {
    val labels = listOf("01 DETAILS", "02 PASSWORD", "03 VERIFY")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .border(1.dp, Border)
    ) {
        labels.forEachIndexed { index, label ->
            val selected = index == current
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(if (selected) SignInBg else FieldBg)
                    .drawBehind {
                        if (selected) {
                            val h = 2.dp.toPx()
                            drawRect(Mint, Offset(0f, size.height - h), Size(size.width, h))
                        }
                    }
                    .clickable { onStepClick(index) }
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = label,
                    color = if (selected) Mint else TextGray,
                    fontFamily = Mono,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    letterSpacing = 1.sp,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun HintText(text: String) {
    Text(
        text = text,
        color = TextGray,
        fontFamily = Mono,
        fontSize = 12.sp
    )
}

@Composable
private fun PersonIcon() {
    Canvas(modifier = Modifier.size(20.dp)) {
        val stroke = 1.5.dp.toPx()
        drawCircle(
            color = TextGray,
            radius = size.width * 0.2f,
            center = Offset(size.width / 2, size.height * 0.3f),
            style = Stroke(stroke)
        )
        drawArc(
            color = TextGray,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(size.width * 0.15f, size.height * 0.62f),
            size = Size(size.width * 0.7f, size.height * 0.6f),
            style = Stroke(stroke)
        )
    }
}

@Composable
private fun AtIcon() {
    Text(
        text = "@",
        color = TextGray,
        fontFamily = Mono,
        fontSize = 18.sp
    )
}

@Preview
@Composable
fun Password_Screen(){
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    Box(){
        Column() {
            FieldTitle("PASSWORD")
            DesignTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "",
                keyboardType = KeyboardType.Password,
                visualTransformation = if (showPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    EyeIcon(modifier = Modifier.clickable { showPassword = !showPassword })
                }
            )

            HintText("At least 8 characters")
        }
    }

}