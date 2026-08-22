package com.example.chatconnect

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun ChatUI() {
    Box() {
        BackgroundforSignUp()
        Text(text = "There is Chat",
            color = Color.White)
    }
}