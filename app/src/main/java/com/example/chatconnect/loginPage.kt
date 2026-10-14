package com.example.chatconnect

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


val PageBg = Color(0xFF0A0E14)
val GridLine = Color(0xFF111821)
val CardBg = Color(0xFF0F151D)
val FieldBg = Color(0xFF0B1016)
val Border = Color(0xFF1E2731)
val Mint = Color(0xFF5EEAD4)
val TextWhite = Color(0xFFE6EDF3)
val TextGray = Color(0xFF8B949E)
val SignInBg = Color(0xFF13232A)
val Mono = FontFamily.Monospace

@Preview(heightDp = 900)
@Composable
fun Forlogo() {
    AuthPage { signIn(onCreateAccountClick = {}) }
}
fun Modifier.gridBackground(): Modifier = this
    .background(PageBg)
    .drawBehind {
        val step = 28.dp.toPx()
        var x = 0f
        while (x < size.width) {
            drawLine(GridLine, Offset(x, 0f), Offset(x, size.height), 1f)
            x += step
        }
        var y = 0f
        while (y < size.height) {
            drawLine(GridLine, Offset(0f, y), Offset(size.width, y), 1f)
            y += step
        }
    }

@Composable
fun AuthPage(card: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .gridBackground()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 60.dp)
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

        Column(
            modifier = Modifier
                .drawBehind {
                    drawLine(
                        color = Border,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f))
                    )
                }
                .padding(start = 20.dp)
        ) {
            Text(
                text = "SECURE WORKSPACE",
                color = TextGray,
                fontFamily = Mono,
                fontSize = 12.sp,
                letterSpacing = 3.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = buildAnnotatedString {
                    append("START WITH\nTHE ")
                    withStyle(SpanStyle(color = Mint)) {
                        append("CHANNEL.")
                    }
                },
                color = TextWhite,
                fontFamily = Mono,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp,
                lineHeight = 40.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Messages, calls and your own assistant in one place — " +
                        "with direct messages encrypted before they leave your device.",
                color = TextGray,
                fontFamily = Mono,
                fontSize = 13.sp,
                lineHeight = 21.sp
            )
        }

        Spacer(modifier = Modifier.height(27.dp))

        card()
    }
}

@Composable
fun signIn(onCreateAccountClick: () -> Unit, onSignInSuccess: () -> Unit = {}) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

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
                text = "WELCOME BACK",
                color = TextWhite,
                fontFamily = Mono,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Sign in to continue to ChatsConnect.",
                color = TextGray,
                fontFamily = Mono,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            FieldTitle("EMAIL")
            DesignTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "you@example.com",
                keyboardType = KeyboardType.Email,
                leadingIcon = { MailIcon() }
            )

            Spacer(modifier = Modifier.height(18.dp))

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

            Spacer(modifier = Modifier.height(18.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(SignInBg)
                    .border(1.dp, Border)
                    .clickable {
                        if (email.isBlank() || password.isBlank()) {
                            Toast.makeText(context, "Email aur password dalo", Toast.LENGTH_SHORT).show()
                            return@clickable
                        }
                        AuthManager.signInWithEmail(email.trim(), password) { ok, error ->
                            if (ok) onSignInSuccess()
                            else Toast.makeText(context, error ?: "Sign in failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            ) {
                Text(
                    text = "SIGN IN",
                    color = Mint.copy(alpha = 0.6f),
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
                    text = "CONTINUE WITH GITHUB",
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
                text = "New here? ",
                color = TextGray,
                fontFamily = Mono,
                fontSize = 13.sp
            )
            Text(
                text = "Create an account",
                color = Mint,
                fontFamily = Mono,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onCreateAccountClick() }
            )
        }
    }
}

@Composable
fun FieldTitle(text: String) {
    Text(
        text = text,
        color = TextWhite,
        fontFamily = Mono,
        fontSize = 12.sp,
        letterSpacing = 3.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun DesignTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        textStyle = TextStyle(color = TextWhite, fontFamily = Mono, fontSize = 15.sp),
        cursorBrush = SolidColor(Mint),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(FieldBg)
                    .border(1.dp, Border)
                    .padding(horizontal = 16.dp)
            ) {
                if (leadingIcon != null) {
                    leadingIcon()
                    Spacer(modifier = Modifier.width(14.dp))
                }
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = TextGray.copy(alpha = 0.7f),
                            fontFamily = Mono,
                            fontSize = 15.sp
                        )
                    }
                    innerTextField()
                }
                if (trailingIcon != null) {
                    Spacer(modifier = Modifier.width(12.dp))
                    trailingIcon()
                }
            }
        }
    )
}

@Composable
fun DashedLine(modifier: Modifier) {
    Box(
        modifier = modifier
            .height(1.dp)
            .drawBehind {
                drawLine(
                    color = Border,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
                )
            }
    )
}

@Composable
fun MailIcon() {
    Canvas(modifier = Modifier.size(20.dp, 16.dp)) {
        val stroke = 1.5.dp.toPx()
        drawRect(color = TextGray, style = Stroke(stroke))
        drawLine(TextGray, Offset(0f, 0f), Offset(size.width / 2, size.height * 0.55f), stroke, StrokeCap.Round)
        drawLine(TextGray, Offset(size.width, 0f), Offset(size.width / 2, size.height * 0.55f), stroke, StrokeCap.Round)
    }
}

@Composable
fun EyeIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(24.dp)) {
        val stroke = 1.5.dp.toPx()
        val eyeHeight = size.height * 0.55f
        drawOval(
            color = TextGray,
            topLeft = Offset(0f, (size.height - eyeHeight) / 2),
            size = Size(size.width, eyeHeight),
            style = Stroke(stroke)
        )
        drawCircle(color = TextGray, radius = size.minDimension * 0.14f, style = Stroke(stroke))
    }
}
