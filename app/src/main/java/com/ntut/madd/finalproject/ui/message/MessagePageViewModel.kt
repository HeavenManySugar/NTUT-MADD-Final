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
        loadConversations()
    }

    private fun loadConversations() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            chatRepository.getUserConversationsWithUserInfo().fold(
                onSuccess = { conversationsWithUsers ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        conversationsWithUsers = conversationsWithUsers
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message
                    )
                }
            )
        }
    }

    fun refreshConversations() {
        loadConversations()
    }
}