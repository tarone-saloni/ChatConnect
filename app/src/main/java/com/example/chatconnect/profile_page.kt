package com.example.chatconnect

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val CoverTop = Color(0xFF1E3A3A)
private const val STATUS_LIMIT = 100

@Preview(heightDp = 900)
@Composable
fun ProfilePreview() {
    Box(modifier = Modifier.gridBackground()) {
        ProfileContent(name = "saloni", username = "saloni_23", joined = "September 2026")
    }
}


@Composable
fun ProfileScreen() {
    val user = AuthManager.currentUser()
    val email = user?.email.orEmpty()
    val name = user?.displayName?.takeIf { it.isNotBlank() } ?: email.substringBefore("@").ifBlank { "user" }
    val username = email.substringBefore("@").ifBlank { name.lowercase().replace(" ", "_") }
    val joined = user?.metadata?.creationTimestamp
        ?.let { SimpleDateFormat("MMMM yyyy", Locale.ENGLISH).format(Date(it)) }
        ?: "—"
    ProfileContent(name = name, username = username, joined = joined)
}

@Composable
fun ProfileContent(name: String, username: String, joined: String, friends: Int = 0) {
    var tab by remember { mutableIntStateOf(0) }
    var editName by remember { mutableStateOf(name) }
    var status by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        ProfileCard(name, username, joined, friends)

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawLine(Border, Offset(0f, size.height), Offset(size.width, size.height), 1.dp.toPx())
                }
        ) {
            listOf("EDIT PROFILE", "SETTINGS", "ACCOUNT").forEachIndexed { i, label ->
                val active = i == tab
                Text(
                    text = label,
                    color = if (active) Mint else TextGray,
                    fontFamily = Mono,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 2.sp,
                    modifier = Modifier
                        .drawBehind {
                            if (active) {
                                drawLine(Mint, Offset(0f, size.height), Offset(size.width, size.height), 2.dp.toPx())
                            }
                        }
                        .clickable { tab = i }
                        .padding(end = 18.dp, top = 12.dp, bottom = 14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (tab) {
            0 -> PublicProfileCard(
                name = editName,
                onNameChange = { editName = it },
                status = status,
                onStatusChange = { if (it.length <= STATUS_LIMIT) status = it }
            )
            1 -> PlaceholderCard("SETTINGS", "Coming soon")
            else -> PlaceholderCard("ACCOUNT", "Coming soon")
        }
    }
}

@Composable
private fun ProfileCard(name: String, username: String, joined: String, friends: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardBg)
            .border(1.dp, Border)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "PROFILE // @${username.uppercase()}",
                color = TextGray,
                fontFamily = Mono,
                fontSize = 11.sp,
                letterSpacing = 2.sp,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            Box(modifier = Modifier.size(8.dp).background(Mint))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "ONLINE",
                color = Mint,
                fontFamily = Mono,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 2.sp
            )
        }

        // cover strip ke upar avatar aadha overlap karta hai
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Brush.verticalGradient(listOf(CoverTop, CardBg)))
                    .drawBehind {
                        drawLine(Border, Offset(0f, 0f), Offset(size.width, 0f), 1.dp.toPx())
                        drawLine(Border, Offset(0f, size.height), Offset(size.width, size.height), 1.dp.toPx())
                    }
            )
            Box(
                modifier = Modifier
                    .padding(start = 20.dp, top = 50.dp)
                    .size(108.dp, 116.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(108.dp)
                        .background(CardBg)
                        .border(1.dp, Border)
                        .padding(6.dp)
                        .background(AvatarBg)
                ) {
                    Text(
                        text = name.take(1).uppercase(),
                        color = SkyBlue,
                        fontFamily = Mono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 36.sp
                    )
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 16.dp)
                        .size(40.dp)
                        .background(NavActiveBg)
                        .border(1.dp, Mint.copy(alpha = 0.5f))
                        .clickable { }
                ) {
                    CameraIcon(Mint)
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            Text(
                text = name.uppercase(),
                color = TextWhite,
                fontFamily = Mono,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "@$username", color = TextGray, fontFamily = Mono, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                CalendarIcon(TextGray)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Joined $joined", color = TextGray, fontFamily = Mono, fontSize = 14.sp)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawLine(Border, Offset(0f, 0f), Offset(size.width, 0f), 1.dp.toPx())
                }
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = friends.toString(),
                color = TextWhite,
                fontFamily = Mono,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "FRIENDS",
                color = TextGray,
                fontFamily = Mono,
                fontSize = 11.sp,
                letterSpacing = 3.sp
            )
        }
    }
}

@Composable
private fun PublicProfileCard(
    name: String,
    onNameChange: (String) -> Unit,
    status: String,
    onStatusChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardBg)
            .border(1.dp, Border)
    ) {
        CardHeader("PUBLIC PROFILE", "VISIBLE TO EVERYONE")

        Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 20.dp)) {
            FieldLabel("NAME")
            BasicTextField(
                value = name,
                onValueChange = onNameChange,
                singleLine = true,
                cursorBrush = SolidColor(Mint),
                textStyle = TextStyle(color = TextWhite, fontFamily = Mono, fontWeight = FontWeight.Bold, fontSize = 15.sp),
                decorationBox = { inner ->
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(FieldBg)
                            .border(1.dp, Border)
                            .padding(horizontal = 18.dp)
                    ) { inner() }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
            FieldLabel("STATUS")
            BasicTextField(
                value = status,
                onValueChange = onStatusChange,
                cursorBrush = SolidColor(Mint),
                textStyle = TextStyle(color = TextWhite, fontFamily = Mono, fontSize = 14.sp, lineHeight = 20.sp),
                decorationBox = { inner ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 56.dp)
                            .background(FieldBg)
                            .border(1.dp, Border)
                            .padding(horizontal = 18.dp, vertical = 8.dp)
                    ) {
                        SmileIcon(TextGray)
                        Spacer(modifier = Modifier.width(12.dp))
                        Box(modifier = Modifier.weight(1f)) {
                            if (status.isEmpty()) {
                                Text(
                                    text = "What's happening? e.g. On holiday until Monday",
                                    color = TextGray.copy(alpha = 0.7f),
                                    fontFamily = Mono,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp
                                )
                            }
                            inner()
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "${status.length} / $STATUS_LIMIT", color = TextGray, fontFamily = Mono, fontSize = 12.sp)
        }
    }
}

@Composable
private fun PlaceholderCard(title: String, note: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardBg)
            .border(1.dp, Border)
    ) {
        CardHeader(title, "")
        Text(
            text = note,
            color = TextGray,
            fontFamily = Mono,
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 20.dp)
        )
    }
}

@Composable
private fun CardHeader(title: String, trailing: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(Border, Offset(0f, size.height), Offset(size.width, size.height), 1.dp.toPx())
            }
            .padding(horizontal = 18.dp, vertical = 14.dp)
    ) {
        Text(
            text = title,
            color = Mint,
            fontFamily = Mono,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            letterSpacing = 2.sp,
            modifier = Modifier.weight(1f)
        )
        Text(text = trailing, color = TextGray, fontFamily = Mono, fontSize = 11.sp, letterSpacing = 2.sp)
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        color = TextGray,
        fontFamily = Mono,
        fontSize = 11.sp,
        letterSpacing = 3.sp,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

@Composable
fun CameraIcon(color: Color) {
    Canvas(modifier = Modifier.size(18.dp, 16.dp)) {
        val stroke = 1.5.dp.toPx()
        val w = size.width
        val h = size.height
        drawRect(color, topLeft = Offset(0f, h * 0.2f), size = Size(w, h * 0.8f), style = Stroke(stroke))
        drawRect(color, topLeft = Offset(w * 0.32f, 0f), size = Size(w * 0.36f, h * 0.2f), style = Stroke(stroke))
        drawCircle(color, radius = h * 0.2f, center = Offset(w / 2, h * 0.6f), style = Stroke(stroke))
    }
}

@Composable
fun CalendarIcon(color: Color) {
    Canvas(modifier = Modifier.size(18.dp)) {
        val stroke = 1.5.dp.toPx()
        val w = size.width
        val h = size.height
        drawRect(color, topLeft = Offset(0f, h * 0.15f), size = Size(w, h * 0.85f), style = Stroke(stroke))
        drawLine(color, Offset(0f, h * 0.4f), Offset(w, h * 0.4f), stroke)
        drawLine(color, Offset(w * 0.28f, 0f), Offset(w * 0.28f, h * 0.28f), stroke)
        drawLine(color, Offset(w * 0.72f, 0f), Offset(w * 0.72f, h * 0.28f), stroke)
    }
}

@Composable
fun SmileIcon(color: Color) {
    Canvas(modifier = Modifier.size(18.dp)) {
        val stroke = 1.5.dp.toPx()
        val w = size.width
        drawCircle(color, radius = w / 2 - stroke, style = Stroke(stroke))
        drawCircle(color, radius = stroke * 0.7f, center = Offset(w * 0.36f, w * 0.4f))
        drawCircle(color, radius = stroke * 0.7f, center = Offset(w * 0.64f, w * 0.4f))
        drawArc(
            color = color,
            startAngle = 20f,
            sweepAngle = 140f,
            useCenter = false,
            topLeft = Offset(w * 0.3f, w * 0.35f),
            size = Size(w * 0.4f, w * 0.35f),
            style = Stroke(stroke)
        )
    }
}
