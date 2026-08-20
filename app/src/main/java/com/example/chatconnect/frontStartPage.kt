
package com.example.chatconnect

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import com.example.chatconnect.ui.theme.BorderColor
import com.example.chatconnect.ui.theme.CardBg
import com.example.chatconnect.ui.theme.DarkBg
import com.example.chatconnect.ui.theme.DarkBg2
import com.example.chatconnect.ui.theme.GlowBlue
import com.example.chatconnect.ui.theme.NeonBlue
import com.example.chatconnect.ui.theme.NeonCyan
import com.example.chatconnect.ui.theme.NeonPurple
import com.example.chatconnect.ui.theme.NeonPurple2
import com.example.chatconnect.ui.theme.TealGreen
import com.example.chatconnect.ui.theme.TextPrimary
import com.example.chatconnect.ui.theme.TextSecondary
import kotlinx.coroutines.delay




@Preview
@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit = {},
    onLogIn: () -> Unit = {}
) {

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { delay(100); visible = true }

    val alphaAnim by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(800, easing = EaseOut),
        label = "alpha"
    )
    val slideAnim by animateFloatAsState(
        targetValue = if (visible) 0f else 60f,
        animationSpec = tween(800, easing = EaseOutCubic),
        label = "slide"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "float")
    val float1 by infiniteTransition.animateFloat(
        initialValue = -8f, targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(2800, easing = EaseInOut), RepeatMode.Reverse),
        label = "f1"
    )
    val float2 by infiniteTransition.animateFloat(
        initialValue = 6f, targetValue = -10f,
        animationSpec = infiniteRepeatable(tween(3200, easing = EaseInOut), RepeatMode.Reverse),
        label = "f2"
    )
    val float3 by infiniteTransition.animateFloat(
        initialValue = -6f, targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(2500, easing = EaseInOut), RepeatMode.Reverse),
        label = "f3"
    )
    val float4 by infiniteTransition.animateFloat(
        initialValue = 10f, targetValue = -6f,
        animationSpec = infiniteRepeatable(tween(3600, easing = EaseInOut), RepeatMode.Reverse),
        label = "f4"
    )

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

        FloatingIconBadge(
            modifier = Modifier
                .offset(24.dp, (100 + float1).dp)
                .size(76.dp),
            gradient = Brush.linearGradient(
                listOf(Color(0xFF9333EA), Color(0xFF6D28D9))
            ),
            icon = "💬",
            shadowColor = NeonPurple
        )

        FloatingIconBadge(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset((-24).dp, (110 + float2).dp)
                .size(70.dp),
            gradient = Brush.linearGradient(
                listOf(Color(0xFF0F4C5C), Color(0xFF0D3B4A))
            ),
            icon = "📞",
            shadowColor = NeonCyan
        )

        FloatingIconBadge(
            modifier = Modifier
                .offset(16.dp, (390 + float3).dp)
                .size(68.dp),
            gradient = Brush.linearGradient(
                listOf(Color(0xFF3B4BC8), Color(0xFF2D3A9E))
            ),
            icon = "👥",
            shadowColor = NeonBlue
        )

        FloatingIconBadge(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset((-20).dp, (400 + float4).dp)
                .size(64.dp),
            gradient = Brush.linearGradient(
                listOf(Color(0xFF4C1D8A), Color(0xFF3B0D6E))
            ),
            icon = "🔒",
            shadowColor = NeonPurple2
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp)
                .alpha(alphaAnim)
                .offset(y = slideAnim.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(220.dp))


            NexTalkLogo()

            Spacer(modifier = Modifier.height(0.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = TextPrimary, fontWeight = FontWeight.Black)) {
                        append("Nex")
                    }
                    withStyle(SpanStyle(
                        brush = Brush.linearGradient(listOf(NeonBlue, NeonCyan)),
                        fontWeight = FontWeight.Black
                    )) {
                        append("Talk")
                    }
                },
                fontSize = 40.sp,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Connect. Chat. Anytime.",
                color = TextSecondary,
                fontSize = 16.sp,
                letterSpacing = 0.2.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            FeaturePillsRow()

            Spacer(modifier = Modifier.height(36.dp))

            GetStartedButton(onClick = onGetStarted)

            Spacer(modifier = Modifier.height(20.dp))

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

@Composable
fun NexTalkLogo() {
    Image(
        painter = painterResource(id = R.drawable.applogo),
        contentDescription = "Logo",
        modifier = Modifier
            .size(160.dp)
            .clip(RoundedCornerShape(24.dp)),
        contentScale = ContentScale.Fit //
    )
}

@Composable
fun FloatingIconBadge(
    modifier: Modifier = Modifier,
    gradient: Brush,
    icon: String,
    shadowColor: Color
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {

        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(16.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(shadowColor.copy(alpha = 0.5f))
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(22.dp))
                .background(gradient)
                .background(Color.White.copy(alpha = 0.06f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = icon, fontSize = 28.sp)
        }
    }
}

@Composable
fun GlowBlob(modifier: Modifier = Modifier, color: Color) {
    Box(
        modifier = modifier
            .blur(60.dp)
            .clip(CircleShape)
            .background(color)
    )
}

@Composable
fun FeaturePillsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FeaturePill(icon = "💬", label = "Real-time\nMessaging", iconBg = NeonPurple)
        FeatureDivider()
        FeaturePill(icon = "👥", label = "Group\nChats", iconBg = TealGreen)
        FeatureDivider()
        FeaturePill(icon = "🔒", label = "Secure &\nPrivate", iconBg = NeonBlue)
    }
}

@Composable
fun FeatureDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(60.dp)
            .background(BorderColor)
    )
}

@Composable
fun FeaturePill(icon: String, label: String, iconBg: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(CardBg)
                .background(
                    Brush.radialGradient(
                        listOf(iconBg.copy(alpha = 0.25f), Color.Transparent)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = icon, fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 17.sp
        )
    }
}

@Composable
fun GetStartedButton(onClick: () -> Unit) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(listOf(GlowBlue, NeonPurple))
            )
            .clickable {
                pressed = true
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Get Started",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.3.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "→",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    LaunchedEffect(pressed) {
        if (pressed) { delay(150); pressed = false }
    }
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen()
}