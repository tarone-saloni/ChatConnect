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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun HomeUIPage() {
    var selectedTab by remember { mutableStateOf(0) }
    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundforSignUp()
       TopBarUI()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 90.dp, bottom = 80.dp) // padding equal to the space the topbar/bottombar take up
        ) {
            when (selectedTab) {
                0 -> ChatUI()
                1 -> GroupUI()
                2 -> CallsUI()
            }
        }
        BottomBarUI(
                selectedTab = selectedTab,
                onTabSelected = { newTab -> selectedTab = newTab },
                    modifier = Modifier.align(Alignment.BottomCenter)
            )
    }
}

@Composable
fun TopBarUI() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.user),
            contentDescription = "Profile Logo",
            modifier = Modifier.size(60.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.searchdark),
                contentDescription = "Search Logo",
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))
            Image(
                painter = painterResource(id = R.drawable.notificationdark),
                contentDescription = "Notification Logo",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun BottomBarUI(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF0F121F).copy(alpha = 0.95f))
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onTabSelected(0) }  ) {
            Image(
                painter = painterResource(id = R.drawable.chatdarkmode),
                contentDescription = "Chat Logo",
                modifier = Modifier
                    .height(28.dp)
                    .width(28.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Chats",
                color = Color.White,
                fontSize = 12.sp,
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onTabSelected(1) }  ) {
            Image(
                painter = painterResource(id = R.drawable.group_dark),
                contentDescription = "Group Logo",
                modifier = Modifier
                    .height(28.dp)
                    .width(28.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Groups",
                color = Color.White,
                fontSize = 12.sp,
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { onTabSelected(2) }  ) {
            Image(
                painter = painterResource(id = R.drawable.calldarkmode),
                contentDescription = "call Logo",
                modifier = Modifier
                    .height(28.dp)
                    .width(28.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Calls",
                color = Color.White,
                fontSize = 12.sp,
            )
        }
    }
}