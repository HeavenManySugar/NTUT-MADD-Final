package com.ntut.madd.finalproject.ui.chat

import androidx.lifecycle.viewModelScope
import com.ntut.madd.finalproject.MainViewModel
import com.ntut.madd.finalproject.data.model.Conversation
import com.ntut.madd.finalproject.data.model.Message
import com.ntut.madd.finalproject.data.model.User
import com.ntut.madd.finalproject.data.repository.ChatRepository
import com.ntut.madd.finalproject.data.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val isLoading: Boolean = true,
    val conversation: Conversation? = null,
    val messages: List<Message> = emptyList(),
    val otherUser: User? = null,
    val errorMessage: String? = null,
    val isSendingMessage: Boolean = false
)

@HiltViewModel
class ChatPageViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val userProfileRepository: UserProfileRepository
) : MainViewModel() {
    
    private val _shouldRestartApp = MutableStateFlow(false)
    val shouldRestartApp: StateFlow<Boolean> = _shouldRestartApp.asStateFlow()
    
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    
    private var conversationId: String = ""

    fun loadConversation(chatId: String) {
        if (chatId == conversationId) return // Already loaded
        
        conversationId = chatId
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            // Load conversation details
            chatRepository.getConversation(chatId).fold(
                onSuccess = { conversation ->
                    if (conversation != null) {
                        _uiState.value = _uiState.value.copy(conversation = conversation)
                        loadOtherUser(conversation)
                        startListeningToMessages(chatId)
                        markMessagesAsRead(chatId)
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Conversation not found"
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Failed to load conversation"
                    )
                }
            )
        }
    }
    
    private suspend fun loadOtherUser(conversation: Conversation) {
        // Find the other user (not the current user)
        userProfileRepository.getCurrentUserId()?.let { currentUserId ->
            val otherUserId = conversation.participants.find { it != currentUserId }
            if (otherUserId != null) {
                userProfileRepository.getUserProfileById(otherUserId).fold(
                    onSuccess = { user ->
                        _uiState.value = _uiState.value.copy(otherUser = user)
                    },
                    onFailure = { exception ->
                        println("ChatPageViewModel: Failed to load other user: ${exception.message}")
                    }
                )
            }
        }
    }
    
    private fun startListeningToMessages(chatId: String) {
        chatRepository.getMessagesFlow(chatId, limit = 100)
            .onEach { messages ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    messages = messages.sortedBy { it.timestamp }
                )
            }
            .launchIn(viewModelScope)
    }
    
    private suspend fun loadMessages(chatId: String) {
        chatRepository.getMessages(chatId, limit = 100).fold(
            onSuccess = { messages ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    messages = messages.sortedBy { it.timestamp }
                )
            },
            onFailure = { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = exception.message ?: "Failed to load messages"
                )
            }
        )
    }
    
    fun sendMessage(content: String) {
        if (content.isBlank() || conversationId.isEmpty()) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSendingMessage = true)
            
            chatRepository.sendMessage(conversationId, content).fold(
                onSuccess = { message ->
                    // Message will be automatically added via real-time listener
                    _uiState.value = _uiState.value.copy(
                        isSendingMessage = false
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isSendingMessage = false,
                        errorMessage = exception.message ?: "Failed to send message"
                    )
                }
            )
        }
    }
    
    private suspend fun markMessagesAsRead(chatId: String) {
        chatRepository.markMessagesAsRead(chatId).fold(
            onSuccess = { 
                println("ChatPageViewModel: Messages marked as read")
            },
            onFailure = { exception ->
                println("ChatPageViewModel: Failed to mark messages as read: ${exception.message}")
            }
        )
    }
    
    fun refreshMessages() {
        if (conversationId.isNotEmpty()) {
            viewModelScope.launch {
                loadMessages(conversationId)
            }
        }
    }
}