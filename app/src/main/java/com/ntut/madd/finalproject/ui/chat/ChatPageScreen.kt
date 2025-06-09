package com.ntut.madd.finalproject.ui.chat

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
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.ntut.madd.finalproject.data.model.ErrorMessage // 你的 ErrorMessage 定義
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ntut.madd.finalproject.ui.component.*
import com.ntut.madd.finalproject.ui.theme.MakeItSoTheme
import kotlinx.serialization.Serializable
import androidx.compose.material3.Surface
import androidx.compose.ui.unit.dp

@Serializable
object ChatPageRoute

@Composable
fun ChatPageScreen(
    openHomeScreen: () -> Unit,
    showErrorSnackbar: (ErrorMessage) -> Unit,
    currentRoute: String = "profile",
    onNavigate: (String) -> Unit = {},
    viewModel: ChatPageViewModel = hiltViewModel()
) {
    val shouldRestartApp by viewModel.shouldRestartApp.collectAsStateWithLifecycle()

    if (shouldRestartApp) {
        openHomeScreen()
    } else {
        ChatPageScreenContent(
            name = "Emma",
            isOnline = true,
            initials = "E",
            onBackClick = { /* 返回 */ },
            onInfoClick = { /* Info 點擊 */ },
            onExitClick = { /* 離開點擊 */ }
        )
    }
}

@Composable
fun ChatPageScreenContent(
    name: String,
    isOnline: Boolean,
    initials: String,
    onBackClick: () -> Unit,
    onInfoClick: () -> Unit,
    onExitClick: () -> Unit
) {

    var inputText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Surface(
                tonalElevation = 100.dp, // 陰影效果
                color = Color.Black
            ) {
                ChatHeader(
                    name = name,
                    isOnline = isOnline,
                    initials = initials,
                    onBackClick = onBackClick,
                    onInfoClick = onInfoClick,
                    onExitClick = onExitClick
                )
            }
        },
        bottomBar = {
            ChatInputBar(
                text = inputText,
                onTextChange = { inputText = it },
                onSendClick = {
                    if (inputText.isNotBlank()) {
                        // 實際傳送邏輯可放這裡
                        println("Send: $inputText")
                        inputText = ""
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF2F2F2))
        ) {
            // Chat page 內容
            val messages = listOf(
                ChatMessage("我以為這是交友軟體", "00:00", isMe = true, isRead = true),
                ChatMessage("不是哦", "00:01", isMe = false)
            )

            LazyColumn (
                modifier = Modifier.padding(top = 8.dp)
            ){
                items(messages) { ChatBubble(it) }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatPageScreenPreview() {
    MakeItSoTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1200.dp) // ✅ 預估高度夠容納整個註冊表單
        ) {
            ChatPageScreenContent(
                name = "Emma",
                isOnline = true,
                initials = "E",
                onBackClick = { /* 返回 */ },
                onInfoClick = { /* Info 點擊 */ },
                onExitClick = { /* 離開點擊 */ }
            )
        }
    }
}