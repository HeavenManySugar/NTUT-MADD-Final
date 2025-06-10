package com.ntut.madd.finalproject.ui.message

import androidx.lifecycle.viewModelScope
import com.ntut.madd.finalproject.MainViewModel
import com.ntut.madd.finalproject.data.model.Conversation
import com.ntut.madd.finalproject.data.repository.ChatRepository
import com.ntut.madd.finalproject.data.repository.ConversationWithUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MessageUiState(
    val isLoading: Boolean = false,
    val conversationsWithUsers: List<ConversationWithUser> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class MessagePageViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : MainViewModel() {
    
    private val _shouldRestartApp = MutableStateFlow(false)
    val shouldRestartApp: StateFlow<Boolean> = _shouldRestartApp.asStateFlow()
    
    private val _uiState = MutableStateFlow(MessageUiState())
    val uiState: StateFlow<MessageUiState> = _uiState.asStateFlow()

    init {
        println("MessagePageViewModel: Initializing...")
        loadConversations()
    }

    private fun loadConversations() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            println("MessagePageViewModel: Starting to load conversations...")
            
            try {
                chatRepository.getUserConversationsWithUserInfo().fold(
                    onSuccess = { conversationsWithUsers ->
                        println("MessagePageViewModel: Successfully loaded ${conversationsWithUsers.size} conversations")
                        conversationsWithUsers.forEach { conversation ->
                            println("MessagePageViewModel: Conversation ${conversation.conversation.id} with user ${conversation.otherUser?.displayName}")
                        }
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            conversationsWithUsers = conversationsWithUsers
                        )
                    },
                    onFailure = { exception ->
                        println("MessagePageViewModel: Failed to load conversations: ${exception.message}")
                        exception.printStackTrace()
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Unknown error occurred"
                        )
                    }
                )
            } catch (e: Exception) {
                println("MessagePageViewModel: Exception caught in loadConversations: ${e.message}")
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load conversations: ${e.message}"
                )
            }
        }
    }

    fun refreshConversations() {
        loadConversations()
    }
}