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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.ntut.madd.finalproject.data.model.ErrorMessage
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ntut.madd.finalproject.ui.component.*
import com.ntut.madd.finalproject.ui.theme.MakeItSoTheme
import kotlinx.serialization.Serializable
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.CircularProgressIndicator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Serializable
data class ChatPageRoute(val chatId: String = "chat1")

@Composable
fun ChatPageScreen(
    openHomeScreen: () -> Unit,
    showErrorSnackbar: (ErrorMessage) -> Unit,
    chatId: String = "chat1",
    onBackClick: () -> Unit = {},
    openUserProfile: (String) -> Unit = {},
    viewModel: ChatPageViewModel = hiltViewModel()
) {
    val shouldRestartApp by viewModel.shouldRestartApp.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // Load conversation when screen opens
    LaunchedEffect(chatId) {
        viewModel.loadConversation(chatId)
    }
    
    // Show error messages
    uiState.errorMessage?.let { message ->
        LaunchedEffect(message) {
            showErrorSnackbar(ErrorMessage.StringError(message))
        }
    }

    if (shouldRestartApp) {
        openHomeScreen()
    } else {
        ChatPageScreenContent(
            uiState = uiState,
            onBackClick = onBackClick,
            onSendMessage = viewModel::sendMessage,
            onRefresh = viewModel::refreshMessages,
            openUserProfile = openUserProfile
        )
    }
}

@Composable
fun ChatPageScreenContent(
    uiState: ChatUiState,
    onBackClick: () -> Unit,
    onSendMessage: (String) -> Unit,
    onRefresh: () -> Unit,
    openUserProfile: (String) -> Unit = {}
) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    
    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(0) // Scroll to first item (newest due to reverseLayout)
        }
    }

    Scaffold(
        topBar = {
            Surface(
                tonalElevation = 100.dp,
                color = Color.Black
            ) {
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(132.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                } else {
                    ChatHeader(
                        name = uiState.otherUser?.displayName ?: "Unknown User",
                        isOnline = false, // TODO: Implement online status
                        initials = uiState.otherUser?.displayName?.take(1)?.uppercase() ?: "?",
                        onBackClick = onBackClick,
                        onInfoClick = { 
                            uiState.otherUser?.let { user -> 
                                openUserProfile(user.id)
                            }
                        },
                        onExitClick = onBackClick // Leave conversation by going back
                    )
                }
            }
        },
        bottomBar = {
            ChatInputBar(
                text = inputText,
                onTextChange = { inputText = it },
                onSendClick = {
                    if (inputText.isNotBlank() && !uiState.isSendingMessage) {
                        onSendMessage(inputText)
                        inputText = ""
                    }
                },
                isEnabled = !uiState.isSendingMessage
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF2F2F2))
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.messages.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "💬",
                                fontSize = 48.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Start the conversation!",
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Say hello to ${uiState.otherUser?.displayName ?: "your match"}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.padding(top = 8.dp),
                        reverseLayout = true // Show newest messages at bottom
                    ) {
                        items(uiState.messages.reversed()) { message ->
                            RealChatBubble(
                                message = message,
                                currentUserId = uiState.conversation?.participants?.find { 
                                    it != uiState.otherUser?.id 
                                } ?: ""
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RealChatBubble(
    message: com.ntut.madd.finalproject.data.model.Message,
    currentUserId: String
) {
    val isMe = message.senderId == currentUserId
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeString = timeFormat.format(Date(message.timestamp))
    
    // Convert to ChatMessage for existing ChatBubble component
    val chatMessage = ChatMessage(
        content = message.content,
        timestamp = timeString,
        isMe = isMe,
        isRead = message.isRead
    )
    
    ChatBubble(chatMessage)
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
                uiState = ChatUiState(
                    isLoading = false,
                    messages = emptyList(),
                    otherUser = null
                ),
                onBackClick = { /* Go to message page */ },
                onSendMessage = { /* Send message */ },
                onRefresh = { /* Refresh */ },
                openUserProfile = { /* Open user profile */ }
            )
        }
    }
}