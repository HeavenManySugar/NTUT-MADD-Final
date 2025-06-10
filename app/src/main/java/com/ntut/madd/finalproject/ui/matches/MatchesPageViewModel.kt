package com.ntut.madd.finalproject.ui.matches

import androidx.lifecycle.viewModelScope
import com.ntut.madd.finalproject.MainViewModel
import com.ntut.madd.finalproject.data.model.User
import com.ntut.madd.finalproject.data.repository.UserProfileRepository
import com.ntut.madd.finalproject.data.repository.UserInteractionRepository
import com.ntut.madd.finalproject.data.repository.ChatRepository
import com.ntut.madd.finalproject.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MatchesUiState(
    val isLoading: Boolean = false,
    val usersWhoLikedMe: List<User> = emptyList(),
    val mutualMatches: List<User> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class MatchesPageViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val userInteractionRepository: UserInteractionRepository,
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository
) : MainViewModel() {
    
    private val _shouldRestartApp = MutableStateFlow(false)
    val shouldRestartApp: StateFlow<Boolean>
        get() = _shouldRestartApp.asStateFlow()
    
    private val _currentRoute = MutableStateFlow("matches")
    val currentRoute: StateFlow<String> = _currentRoute.asStateFlow()

    private val _uiState = MutableStateFlow(MatchesUiState())
    val uiState: StateFlow<MatchesUiState> = _uiState.asStateFlow()

    init {
        loadUsersWhoLikedMe()
    }

    fun navigateTo(route: String) {
        _currentRoute.value = route
    }

    /**
     * 載入喜歡當前用戶的用戶列表
     */
    fun loadUsersWhoLikedMe() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            userProfileRepository.getUsersWhoLikedMe().fold(
                onSuccess = { users ->
                    println("MatchesPageViewModel: Successfully loaded ${users.size} users who liked me")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        usersWhoLikedMe = users,
                        errorMessage = null
                    )
                    
                    // 計算互相喜歡的用戶 (mutual matches)
                    calculateMutualMatches(users)
                },
                onFailure = { exception ->
                    println("MatchesPageViewModel: Failed to load users who liked me: ${exception.message}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Failed to load matches"
                    )
                }
            )
        }
    }

    /**
     * 計算互相喜歡的用戶
     */
    private fun calculateMutualMatches(usersWhoLikedMe: List<User>) {
        viewModelScope.launch {
            // 獲取當前用戶喜歡的用戶ID列表
            userInteractionRepository.getApprovedUserIds().fold(
                onSuccess = { approvedIds ->
                    // 找出互相喜歡的用戶
                    val mutualMatches = usersWhoLikedMe.filter { user ->
                        approvedIds.contains(user.id)
                    }
                    
                    println("MatchesPageViewModel: Found ${mutualMatches.size} mutual matches")
                    _uiState.value = _uiState.value.copy(mutualMatches = mutualMatches)
                },
                onFailure = { exception ->
                    println("MatchesPageViewModel: Failed to get approved users: ${exception.message}")
                }
            )
        }
    }

    /**
     * 刷新匹配數據
     */
    fun refresh() {
        loadUsersWhoLikedMe()
    }

    /**
     * 接受某個用戶的喜歡 (如果當前用戶還沒有對該用戶做出反應)
     */
    fun acceptUser(user: User) {
        viewModelScope.launch {
            userInteractionRepository.recordApproval(user.id).fold(
                onSuccess = {
                    println("MatchesPageViewModel: Successfully accepted user ${user.id}")
                    // 創建匹配和聊天對話
                    createMatchAndChat(user.id)
                    // 重新載入數據以更新互相匹配列表
                    loadUsersWhoLikedMe()
                },
                onFailure = { exception ->
                    println("MatchesPageViewModel: Failed to accept user: ${exception.message}")
                }
            )
        }
    }

    /**
     * 拒絕某個用戶的喜歡
     */
    fun rejectUser(user: User) {
        viewModelScope.launch {
            userInteractionRepository.recordRejection(user.id).fold(
                onSuccess = {
                    println("MatchesPageViewModel: Successfully rejected user ${user.id}")
                    // 從列表中移除該用戶
                    val updatedList = _uiState.value.usersWhoLikedMe.filter { it.id != user.id }
                    _uiState.value = _uiState.value.copy(usersWhoLikedMe = updatedList)
                },
                onFailure = { exception ->
                    println("MatchesPageViewModel: Failed to reject user: ${exception.message}")
                }
            )
        }
    }

    /**
     * 創建匹配和聊天對話
     */
    private fun createMatchAndChat(otherUserId: String) {
        viewModelScope.launch {
            val currentUser = authRepository.currentUser
            if (currentUser != null) {
                chatRepository.createMatch(currentUser.uid, otherUserId).fold(
                    onSuccess = { match ->
                        println("MatchesPageViewModel: Successfully created match with conversation ID: ${match.conversationId}")
                    },
                    onFailure = { exception ->
                        println("MatchesPageViewModel: Failed to create match: ${exception.message}")
                    }
                )
            } else {
                println("MatchesPageViewModel: Failed to create match: User not authenticated")
            }
        }
    }

    /**
     * 獲取與指定用戶的聊天對話ID
     */
    fun getChatId(userId: String, callback: (String?) -> Unit) {
        viewModelScope.launch {
            chatRepository.getConversationIdForUser(userId).fold(
                onSuccess = { conversationId ->
                    callback(conversationId)
                },
                onFailure = { exception ->
                    println("MatchesPageViewModel: Failed to get conversation ID: ${exception.message}")
                    callback(null)
                }
            )
        }
    }

    /**
     * 檢查是否可以與指定用戶聊天 (是否為互相匹配)
     */
    fun canChatWithUser(userId: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val currentUser = authRepository.currentUser
            if (currentUser != null) {
                chatRepository.areUsersMatched(currentUser.uid, userId).fold(
                    onSuccess = { isMatched ->
                        callback(isMatched)
                    },
                    onFailure = { exception ->
                        println("MatchesPageViewModel: Failed to check match status: ${exception.message}")
                        callback(false)
                    }
                )
            } else {
                println("MatchesPageViewModel: Failed to check match status: User not authenticated")
                callback(false)
            }
        }
    }
}