package com.ntut.madd.finalproject.message


import androidx.compose.foundation.background

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Divider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

import com.ntut.madd.finalproject.data.model.ErrorMessage // 你的 ErrorMessage 定義

import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ntut.madd.finalproject.messages.FilterChips
import com.ntut.madd.finalproject.messages.MessageListWithSeparators
import com.ntut.madd.finalproject.messages.MessagePreview
import com.ntut.madd.finalproject.messages.MessagePreviewCard
import com.ntut.madd.finalproject.messages.SearchInputField
import com.ntut.madd.finalproject.messages.TopFadeOverlay
import com.ntut.madd.finalproject.ui.component.*
import com.ntut.madd.finalproject.ui.theme.MakeItSoTheme
import kotlinx.serialization.Serializable

@Serializable
object MessagePageRoute

@Composable
fun MessagePageScreen(
    openHomeScreen: () -> Unit,
    showErrorSnackbar: (ErrorMessage) -> Unit,
    currentRoute: String = "profile",
    onNavigate: (String) -> Unit = {},
    viewModel: MessagePageViewModel = hiltViewModel()
) {
    val shouldRestartApp by viewModel.shouldRestartApp.collectAsStateWithLifecycle()

    if (shouldRestartApp) {
        openHomeScreen()
    } else {
        MessagePageScreenContent(
            currentRoute = currentRoute,
            onNavigate = onNavigate
        )
    }
}

@Composable
fun MessagePageScreenContent(
    currentRoute: String = "messages",
    onNavigate: (String) -> Unit = {}
) {

    var query by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 這裡放畫面主內容
            // 頭部
            GradientBackgroundBox() {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Messages",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Your Conversations",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            /** 搜尋欄 **/
            Column(
                modifier = Modifier.fillMaxWidth().padding(12.dp)
            ) {
                SearchInputField(query = query, onQueryChange = { query = it })
                Spacer(modifier = Modifier.height(12.dp))
                FilterChips(selected = selectedFilter, onSelect = { selectedFilter = it })
            }
            TopFadeOverlay()
            MessageListWithSeparators(
                messages = listOf(
                    MessagePreview("chat1", "E", "Emily", "Hey! How was your day? 😊", "2 min ago", true),
                    MessagePreview("chat2", "A", "Alex", "Let’s catch up tomorrow!", "10 min ago", false),
                    MessagePreview("chat3", "M", "Maggie", "I'll send the files later", "1 hr ago", true),
                    MessagePreview("chat4", "L", "Liam", "Got it, thanks!", "2 hr ago", false)
                ),
                onChatClick = {} // 👈 這樣代表按下去什麼都不做
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessagePageScreenPreview() {
    MakeItSoTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1200.dp) // ✅ 預估高度夠容納整個註冊表單
        ) {
            MessagePageScreenContent(
                currentRoute = "messages",
                onNavigate = {}
            )
        }
    }
}
