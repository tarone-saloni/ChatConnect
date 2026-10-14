package com.example.chatconnect

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val PurpleAvatar = Color(0xFF7C4DDB)

data class Contact(
    val initials: String,
    val name: String,
    val username: String,
    val avatarBg: Color,
    val initialsColor: Color = TextWhite
)

private val contacts = listOf(
    Contact("V", "Vansh", "@1sh", Color(0xFF1F2630), Amber),
    Contact("VB", "Vibhanshu", "@vibhu_23", Color(0xFF3F4752)),
    Contact("GA", "GANESH", "@2023bcs003", Color(0xFF0B0D10)),
    Contact("PN", "Pratik Narwade", "@pratiknar_1", Color(0xFF4B5563)),
    Contact("SK", "Sailesh kasbekar", "@sailesh", PurpleAvatar),
    Contact("VI", "Vibhanshu", "@vibhu", PurpleAvatar),
    Contact("AB", "ABOT", "@abot", PurpleAvatar),
)

@Preview(heightDp = 900)
@Composable
fun MessagesPreview() {
    Box(modifier = Modifier.gridBackground()) { MessagesContent() }
}

@Composable
fun MessagesContent() {
    var query by remember { mutableStateOf("") }
    var tab by remember { mutableIntStateOf(0) }

    val people = contacts.filter {
        query.isBlank() ||
            it.name.contains(query.trim(), ignoreCase = true) ||
            it.username.contains(query.trim(), ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .drawBehind {
                        drawLine(
                            color = Border,
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f))
                        )
                    }
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = "CHANNELS",
                    color = TextGray,
                    fontFamily = Mono,
                    fontSize = 11.sp,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "MESSAGES",
                    color = TextWhite,
                    fontFamily = Mono,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    letterSpacing = 1.sp
                )
            }
            SquareButton(onClick = {}) { ComposeIcon(TextWhite) }
            Spacer(modifier = Modifier.width(10.dp))
            SquareButton(onClick = {}) { AddPersonIcon(TextWhite) }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBg)
                .border(1.dp, Border)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "search",
                colorFilter = ColorFilter.tint(TextGray),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Box(modifier = Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Text(
                        text = "Search conversations",
                        color = TextGray,
                        fontFamily = Mono,
                        fontSize = 14.sp
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = { query = it },
                    singleLine = true,
                    cursorBrush = SolidColor(Mint),
                    textStyle = TextStyle(color = TextWhite, fontFamily = Mono, fontSize = 14.sp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Border)
        ) {
            listOf("ALL", "PEOPLE", "GROUPS").forEachIndexed { i, label ->
                val active = i == tab
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .background(if (active) NavActiveBg else CardBg)
                        .drawBehind {
                            if (active) {
                                drawLine(Mint, Offset(0f, size.height), Offset(size.width, size.height), 2.dp.toPx())
                            }
                        }
                        .clickable { tab = i }
                        .padding(vertical = 14.dp)
                ) {
                    Text(
                        text = label,
                        color = if (active) Mint else TextGray,
                        fontFamily = Mono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 2.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBg)
                .border(1.dp, Border)
        ) {
            if (tab == 2) {
                EmptyNote("No groups yet")
            } else {
                Text(
                    text = "// PEOPLE YOU CAN MESSAGE",
                    color = TextGray,
                    fontFamily = Mono,
                    fontSize = 11.sp,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 18.dp, bottom = 6.dp)
                )
                if (people.isEmpty()) {
                    EmptyNote("No one found for \"$query\"")
                }
                people.forEach { ContactRow(it) }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun EmptyNote(text: String) {
    Text(
        text = text,
        color = TextGray,
        fontFamily = Mono,
        fontSize = 13.sp,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
    )
}

@Composable
fun ContactRow(contact: Contact) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .background(contact.avatarBg)
        ) {
            Text(
                text = contact.initials,
                color = contact.initialsColor,
                fontFamily = Mono,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = contact.name,
                color = TextWhite,
                fontFamily = Mono,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = contact.username, color = TextGray, fontFamily = Mono, fontSize = 12.sp)
        }
    }
}

@Composable
fun SquareButton(onClick: () -> Unit, content: @Composable () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(48.dp)
            .background(CardBg)
            .border(1.dp, Border)
            .clickable { onClick() }
    ) {
        content()
    }
}

@Composable
fun ComposeIcon(color: Color) {
    Canvas(modifier = Modifier.size(18.dp)) {
        val stroke = 1.6.dp.toPx()
        val w = size.width
        val h = size.height
        drawLine(color, Offset(w * 0.5f, h * 0.1f), Offset(w * 0.1f, h * 0.1f), stroke, cap = StrokeCap.Round)
        drawLine(color, Offset(w * 0.1f, h * 0.1f), Offset(w * 0.1f, h * 0.9f), stroke, cap = StrokeCap.Round)
        drawLine(color, Offset(w * 0.1f, h * 0.9f), Offset(w * 0.9f, h * 0.9f), stroke, cap = StrokeCap.Round)
        drawLine(color, Offset(w * 0.9f, h * 0.9f), Offset(w * 0.9f, h * 0.5f), stroke, cap = StrokeCap.Round)
        drawLine(color, Offset(w * 0.4f, h * 0.6f), Offset(w * 0.85f, h * 0.15f), stroke * 1.4f, cap = StrokeCap.Round)
    }
}

@Composable
fun AddPersonIcon(color: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val stroke = 1.6.dp.toPx()
        val w = size.width
        val h = size.height
        drawCircle(color, radius = w * 0.17f, center = Offset(w * 0.38f, h * 0.3f), style = Stroke(stroke))
        drawArc(
            color = color,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(w * 0.08f, h * 0.58f),
            size = Size(w * 0.6f, h * 0.6f),
            style = Stroke(stroke)
        )

        drawLine(color, Offset(w * 0.8f, h * 0.32f), Offset(w * 0.8f, h * 0.6f), stroke, cap = StrokeCap.Round)
        drawLine(color, Offset(w * 0.66f, h * 0.46f), Offset(w * 0.94f, h * 0.46f), stroke, cap = StrokeCap.Round)
    }
}
