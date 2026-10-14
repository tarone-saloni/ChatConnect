package com.example.chatconnect

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val SkyBlue = Color(0xFF7DD3FC)
val Amber = Color(0xFFF5B85A)
val BadgeRed = Color(0xFFE5737A)
val AvatarBg = Color(0xFF253244)
val NavActiveBg = Color(0xFF13232A)

data class RecentChat(val initials: String, val name: String, val action: String, val date: String)

private val recentChats = listOf(
    RecentChat("AV", "AMIT VERMA", "sent you a message", "Sep 26"),
    RecentChat("AV", "AMIT VERMA", "sent you a message", "Sep 26"),
)

@Preview(heightDp = 900)
@Composable
fun HomeUIPagePreview() {
    HomeUIPage()
}

@Composable
fun HomeUIPage() {
    var searching by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }

    BackHandler(enabled = searching) {
        searching = false
        query = ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .gridBackground()
    ) {
        if (searching) {
            SearchTopBar(
                query = query,
                onQueryChange = { query = it },
                onBack = {
                    searching = false
                    query = ""
                }
            )
        } else {
            HomeTopBar(
                notifCount = 4,
                onSearchClick = { searching = true },
                onNotifClick = {}
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            if (searching) {
                SearchResults(query)
            } else {
                HomeContent()
            }
        }

        StatusStrip()
        BottomNav(selected = selectedTab, onSelect = { selectedTab = it })
    }
}

@Composable
fun HomeTopBar(notifCount: Int, onSearchClick: () -> Unit, onNotifClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(Border, Offset(0f, size.height), Offset(size.width, size.height), 1.dp.toPx())
            }
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_192),
            contentDescription = "logo",
            modifier = Modifier.size(36.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "CHATSCONNECT",
                color = TextWhite,
                fontFamily = Mono,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                letterSpacing = 3.sp
            )
            Text(
                text = "SECURE MESSAGING",
                color = TextGray,
                fontFamily = Mono,
                fontSize = 10.sp,
                letterSpacing = 3.sp
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clickable { onSearchClick() }
        ) {
            Image(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "search",
                colorFilter = ColorFilter.tint(Mint),
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clickable { onNotifClick() }
        ) {
            Image(
                painter = painterResource(id = R.drawable.notification),
                contentDescription = "notifications",
                colorFilter = ColorFilter.tint(Mint),
                modifier = Modifier.size(22.dp)
            )
            if (notifCount > 0) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-2).dp, y = 4.dp)
                        .size(16.dp)
                        .background(BadgeRed)
                ) {
                    Text(
                        text = notifCount.toString(),
                        color = PageBg,
                        fontFamily = Mono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SearchTopBar(query: String, onQueryChange: (String) -> Unit, onBack: () -> Unit) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 14.dp)
            .background(CardBg)
            .border(1.dp, Mint)
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clickable { onBack() }
        ) {
            BackArrowIcon(color = Mint)
        }
        Box(modifier = Modifier.weight(1f)) {
            if (query.isEmpty()) {
                Text(
                    text = "Search...",
                    color = TextGray,
                    fontFamily = Mono,
                    fontSize = 15.sp
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                cursorBrush = SolidColor(Mint),
                textStyle = TextStyle(color = TextWhite, fontFamily = Mono, fontSize = 15.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            )
        }
        if (query.isNotEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onQueryChange("") }
            ) {
                Text(text = "✕", color = TextGray, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun SearchResults(query: String) {
    val results = recentChats
        .filter { query.isBlank() || it.name.contains(query.trim(), ignoreCase = true) }
        .distinctBy { it.name }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        SectionLabel(if (query.isBlank()) "CHATS" else "RESULTS")
        Spacer(modifier = Modifier.height(10.dp))
        if (results.isEmpty()) {
            Text(
                text = "No chats found for \"$query\"",
                color = TextGray,
                fontFamily = Mono,
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardBg)
                    .border(1.dp, Border)
            ) {
                results.forEachIndexed { i, chat ->
                    ActivityRow(chat, showDivider = i < results.lastIndex)
                }
            }
        }
    }
}

@Composable
fun HomeContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
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
                text = "MONDAY, OCTOBER 12 / OVERVIEW",
                color = TextGray,
                fontFamily = Mono,
                fontSize = 11.sp,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "GOOD EVENING, SALONI",
                color = TextWhite,
                fontFamily = Mono,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your conversations, assistant and encryption status at a glance.",
                color = TextGray,
                fontFamily = Mono,
                fontSize = 13.sp,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.border(1.dp, Border)) {
            Row {
                StatBox("01", "12", "MESSAGES", Mint, Modifier.weight(1f))
                StatBox("02", "1", "CONVERSATIONS", SkyBlue, Modifier.weight(1f))
            }
            Row {
                StatBox("03", "0", "GROUPS", Amber, Modifier.weight(1f))
                StatBox("04", "1", "ONLINE NOW", Mint, Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        SectionLabel("QUICK ACTIONS")
        Spacer(modifier = Modifier.height(12.dp))

        Column(modifier = Modifier.border(1.dp, Border)) {
            Row {
                QuickAction("NEW MESSAGE", Modifier.weight(1f))
                QuickAction("NEW GROUP", Modifier.weight(1f))
            }
            Row {
                QuickAction("FIND PEOPLE", Modifier.weight(1f))
                QuickAction("ASK GANDU", Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

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
                    .drawBehind {
                        drawLine(Border, Offset(0f, size.height), Offset(size.width, size.height), 1.dp.toPx())
                    }
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                Text(
                    text = "RECENT ACTIVITY",
                    color = Mint,
                    fontFamily = Mono,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 3.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "ALL CHATS →",
                    color = TextGray,
                    fontFamily = Mono,
                    fontSize = 12.sp,
                    letterSpacing = 3.sp,
                    modifier = Modifier.clickable { }
                )
            }
            recentChats.forEachIndexed { i, chat ->
                ActivityRow(chat, showDivider = i < recentChats.lastIndex)
            }
        }
    }
}

@Composable
fun SectionLabel(text: String) {
    Text(
        text = text,
        color = TextGray,
        fontFamily = Mono,
        fontSize = 11.sp,
        letterSpacing = 4.sp
    )
}

@Composable
fun StatBox(index: String, value: String, label: String, labelColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(CardBg)
            .border(0.5.dp, Border)
            .drawBehind {
                // top-right corner bracket
                val len = 12.dp.toPx()
                val inset = 10.dp.toPx()
                val stroke = 1.dp.toPx()
                val x = size.width - inset
                drawLine(TextGray, Offset(x - len, inset), Offset(x, inset), stroke)
                drawLine(TextGray, Offset(x, inset), Offset(x, inset + len), stroke)
            }
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Column {
            Text(text = index, color = TextGray, fontFamily = Mono, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = TextWhite,
                fontFamily = Mono,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                color = labelColor,
                fontFamily = Mono,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 2.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
fun QuickAction(text: String, modifier: Modifier = Modifier) {
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(color = Mint)) { append(">  ") }
            append(text)
        },
        color = TextWhite,
        fontFamily = Mono,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        letterSpacing = 2.sp,
        maxLines = 1,
        modifier = modifier
            .background(CardBg)
            .border(0.5.dp, Border)
            .clickable { }
            .padding(horizontal = 20.dp, vertical = 22.dp)
    )
}

@Composable
fun ActivityRow(chat: RecentChat, showDivider: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .drawBehind {
                if (showDivider) {
                    drawLine(Border, Offset(0f, size.height), Offset(size.width, size.height), 1.dp.toPx())
                }
            }
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .background(AvatarBg)
        ) {
            Text(
                text = chat.initials,
                color = TextWhite,
                fontFamily = Mono,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = TextWhite, fontWeight = FontWeight.Bold)) {
                        append(chat.name)
                    }
                    append(" ${chat.action}")
                },
                color = TextGray,
                fontFamily = Mono,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = chat.date, color = TextGray, fontFamily = Mono, fontSize = 12.sp)
        }
    }
}

@Composable
fun StatusStrip() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(PageBg)
            .drawBehind {
                drawLine(Border, Offset(0f, 0f), Offset(size.width, 0f), 1.dp.toPx())
            }
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Box(modifier = Modifier.size(8.dp).background(Mint))
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "LINK.ONLINE", color = TextGray, fontFamily = Mono, fontSize = 11.sp, letterSpacing = 2.sp)
        Spacer(modifier = Modifier.width(10.dp))
        Canvas(modifier = Modifier.size(width = 26.dp, height = 12.dp)) {
            val heights = listOf(0.6f, 0.8f, 0.5f, 1f, 0.4f)
            val barW = size.width / 7f
            heights.forEachIndexed { i, h ->
                drawRect(
                    color = if (i < 4) Mint else Border,
                    topLeft = Offset(i * barW * 1.4f, size.height * (1 - h)),
                    size = Size(barW, size.height * h)
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "V2.0", color = TextGray, fontFamily = Mono, fontSize = 11.sp, letterSpacing = 2.sp)
    }
}

@Composable
fun BottomNav(selected: Int, onSelect: (Int) -> Unit) {
    val tabs = listOf("HOME", "MESSAGES", "ASSISTANT", "PROFILE")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PageBg)
            .drawBehind {
                drawLine(Border, Offset(0f, 0f), Offset(size.width, 0f), 1.dp.toPx())
            }
    ) {
        tabs.forEachIndexed { i, label ->
            val active = i == selected
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .background(if (active) NavActiveBg else PageBg)
                    .drawBehind {
                        if (active) drawLine(Mint, Offset(0f, 0f), Offset(size.width, 0f), 2.dp.toPx())
                    }
                    .clickable { onSelect(i) }
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = "%02d".format(i + 1),
                    color = if (active) Mint else TextGray,
                    fontFamily = Mono,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = label,
                    color = if (active) Mint else TextGray,
                    fontFamily = Mono,
                    fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun BackArrowIcon(color: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val stroke = 2.dp.toPx()
        val w = size.width
        val h = size.height
        drawLine(color, Offset(w * 0.12f, h / 2), Offset(w * 0.9f, h / 2), stroke, cap = StrokeCap.Round)
        drawLine(color, Offset(w * 0.12f, h / 2), Offset(w * 0.45f, h * 0.18f), stroke, cap = StrokeCap.Round)
        drawLine(color, Offset(w * 0.12f, h / 2), Offset(w * 0.45f, h * 0.82f), stroke, cap = StrokeCap.Round)
    }
}
