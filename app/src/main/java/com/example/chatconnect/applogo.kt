package com.example.chatconnect

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatconnect.ui.theme.NeonBlue
import com.example.chatconnect.ui.theme.NeonCyan
import com.example.chatconnect.ui.theme.TextPrimary

@Composable
fun Logo(){
    Column(
        modifier = Modifier
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.applogo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(180.dp)
                .clip(RoundedCornerShape(24.dp)),
            contentScale = ContentScale.Fit
        )
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
}
}