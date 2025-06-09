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
data class ChatPageRoute(val chatId: String = "chat1")

@Composable
fun ChatPageScreen(
    openHomeScreen: () -> Unit,
    showErrorSnackbar: (ErrorMessage) -> Unit,
    chatId: String = "chat1",
    onBackClick: () -> Unit = {},
    viewModel: ChatPageViewModel = hiltViewModel()
) {
    val shouldRestartApp by viewModel.shouldRestartApp.collectAsStateWithLifecycle()
    if (shouldRestartApp) {
        openHomeScreen()
    } else {
        // 根據 chatId 獲取聊天信息
        val (name, initials, isOnline) = when (chatId) {
            "chat1" -> Triple("Emily", "E", true)
            "chat2" -> Triple("Alex", "A", false)
            "chat3" -> Triple("Maggie", "M", true)
            "chat4" -> Triple("Liam", "L", false)
            else -> Triple("Unknown", "U", false)
        }
        
        ChatPageScreenContent(
            name = name,
            isOnline = isOnline,
            initials = initials,
            onBackClick = onBackClick,
            onInfoClick = { /* Info click */ },
            onExitClick = { /* Leave conversation */ }
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
                tonalElevation = 100.dp,
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
            // Chat
            val messages = listOf(
                ChatMessage("I think this is an matching APP", "00:00", isMe = true, isRead = true),
                ChatMessage("No", "00:01", isMe = false),
                ChatMessage("What is this?", "00:00", isMe = true, isRead = true),
                ChatMessage("My MADD final project.", "00:01", isMe = false),
                ChatMessage("Oh! I realized it!", "00:00", isMe = true, isRead = true),
                ChatMessage("Please gave me 100,thanks!", "00:01", isMe = false)
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
                .height(1200.dp)
        ) {
            ChatPageScreenContent(
                name = "Emma",
                isOnline = true,
                initials = "E",
                onBackClick = { /* Go to message page */ },
                onInfoClick = { /* Info click */ },
                onExitClick = { /* Leave conversation */ }
            )
        }
    }
}