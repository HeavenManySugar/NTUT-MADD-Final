package com.ntut.madd.finalproject.ui.message

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.ntut.madd.finalproject.data.model.ErrorMessage
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ntut.madd.finalproject.ui.messages.FilterChips
import com.ntut.madd.finalproject.ui.messages.MessageListWithSeparators
import com.ntut.madd.finalproject.ui.messages.MessagePreview
import com.ntut.madd.finalproject.ui.messages.SearchInputField
import com.ntut.madd.finalproject.ui.messages.TopFadeOverlay
import com.ntut.madd.finalproject.ui.component.*
import com.ntut.madd.finalproject.ui.theme.MakeItSoTheme
import kotlinx.serialization.Serializable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

@Serializable
object MessagePageRoute

@Composable
fun MessagePageScreen(
    openHomeScreen: () -> Unit,
    showErrorSnackbar: (ErrorMessage) -> Unit,
    currentRoute: String = "profile",
    onNavigate: (String) -> Unit = {},
    openChatScreen: (String) -> Unit = {},
    viewModel: MessagePageViewModel = hiltViewModel()
) {
    val shouldRestartApp by viewModel.shouldRestartApp.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (shouldRestartApp) {
        openHomeScreen()
    } else {
        MessagePageScreenContent(
            uiState = uiState,
            currentRoute = currentRoute,
            onNavigate = onNavigate,
            openChatScreen = openChatScreen,
            showErrorSnackbar = showErrorSnackbar,
            onRefresh = viewModel::refreshConversations
        )
    }
}

@Composable
fun MessagePageScreenContent(
    uiState: MessageUiState,
    currentRoute: String = "messages",
    onNavigate: (String) -> Unit = {},
    openChatScreen: (String) -> Unit = {},
    showErrorSnackbar: (ErrorMessage) -> Unit = {},
    onRefresh: () -> Unit = {}
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
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // Page Information
            GradientBackgroundBox() {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(48.dp)) // Balance for refresh button
                    
                    Column(
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
                    
                    IconButton(
                        onClick = onRefresh,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            /** Searching Bar **/
            Column(
                modifier = Modifier.fillMaxWidth().padding(12.dp)
            ) {
                SearchInputField(query = query, onQueryChange = { query = it })
                Spacer(modifier = Modifier.height(12.dp))
                FilterChips(selected = selectedFilter, onSelect = { selectedFilter = it })
            }
            TopFadeOverlay()
            
            // Show error if any
            uiState.errorMessage?.let { message ->
                showErrorSnackbar(ErrorMessage.StringError(message))
            }
            
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading conversations...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            } else if (uiState.errorMessage != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            text = "⚠️",
                            fontSize = 48.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Failed to load conversations",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.errorMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        androidx.compose.material3.Button(onClick = onRefresh) {
                            Text("Retry")
                        }
                    }
                }
            } else if (uiState.conversationsWithUsers.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No conversations yet",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Start matching to chat with people!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                MessageListWithSeparators(
                    messages = uiState.conversationsWithUsers.map { conversationWithUser ->
                        val conversation = conversationWithUser.conversation
                        val otherUser = conversationWithUser.otherUser
                        MessagePreview(
                            chatId = conversation.id,
                            initials = otherUser?.displayName?.take(1)?.uppercase() ?: "?",
                            userName = otherUser?.displayName ?: "Unknown User",
                            lastMessage = conversation.lastMessage.ifEmpty { "Say hello!" },
                            timeAgo = formatTimestamp(conversation.lastMessageTimestamp),
                            isOnline = false // Can be enhanced later
                        )
                    },
                    onChatClick = { chatId -> openChatScreen(chatId) }
                )
            }
        }
    }
}

/**
 * Format timestamp to relative time string
 */
private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60 * 1000 -> "Just now"
        diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)} min ago"
        diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)} hr ago"
        diff < 7 * 24 * 60 * 60 * 1000 -> "${diff / (24 * 60 * 60 * 1000)} days ago"
        else -> "Long ago"
    }
}

@Preview(showBackground = true)
@Composable
fun MessagePageScreenPreview() {
    MakeItSoTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1200.dp)
        ) {
            MessagePageScreenContent(
                uiState = MessageUiState(
                    isLoading = false,
                    conversationsWithUsers = emptyList(),
                    errorMessage = null
                ),
                currentRoute = "messages",
                onNavigate = {},
                openChatScreen = {},
                showErrorSnackbar = {},
                onRefresh = {}
            )
        }
    }
}
