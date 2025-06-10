package com.ntut.madd.finalproject.ui.discover

import androidx.lifecycle.viewModelScope
import com.ntut.madd.finalproject.MainViewModel
import com.ntut.madd.finalproject.data.model.User
import com.ntut.madd.finalproject.data.repository.UserProfileRepository
import com.ntut.madd.finalproject.data.repository.UserInteractionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DiscoverUiState(
    val isLoading: Boolean = false,
    val currentUser: User? = null,
    val availableProfiles: List<User> = emptyList(),
    val currentProfileIndex: Int = 0,
    val errorMessage: String? = null
)

@HiltViewModel
class DiscoverPageViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val userInteractionRepository: UserInteractionRepository
) : MainViewModel() {
    
    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()
    
    private val _shouldRestartApp = MutableStateFlow(false)
    val shouldRestartApp: StateFlow<Boolean>
        get() = _shouldRestartApp.asStateFlow()
        
    private val _currentRoute = MutableStateFlow("discover")
    val currentRoute: StateFlow<String> = _currentRoute.asStateFlow()

    init {
        loadDiscoverableProfiles()
    }

    fun navigateTo(route: String) {
        _currentRoute.value = route
    }

    private fun loadDiscoverableProfiles() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            println("DiscoverPageViewModel: Starting to load recommended users...")
            
            // 使用智能推薦系統獲取推薦用戶
            userProfileRepository.getRecommendedUsers(limit = 20).fold(
                onSuccess = { users ->
                    println("DiscoverPageViewModel: Recommended users returned ${users.size} users")
                    if (users.isNotEmpty()) {
                        _uiState.value = _uiState.value.copy(
                            availableProfiles = users,
                            currentProfileIndex = 0,
                            isLoading = false
                        )
                        println("DiscoverPageViewModel: Successfully loaded ${users.size} recommended users")
                        // 打印前幾個用戶的資訊用於調試
                        users.take(3).forEachIndexed { index, user ->
                            println("DiscoverPageViewModel: User $index: ${user.displayName} (ID: ${user.id})")
                        }
                    } else {
                        println("DiscoverPageViewModel: No recommended users found, falling back to basic recommendations")
                        // 如果沒有推薦用戶，退回到基本推薦
                        loadBasicRecommendations()
                    }
                },
                onFailure = { exception ->
                    println("DiscoverPageViewModel: Failed to load recommended users: ${exception.message}")
                    // 如果推薦系統失敗，退回到基本推薦
                    loadBasicRecommendations()
                }
            )
        }
    }

    private fun loadBasicRecommendations() {
        viewModelScope.launch {
            println("DiscoverPageViewModel: Loading basic recommendations...")
            userProfileRepository.getDiscoverableUsers(limit = 20).fold(
                onSuccess = { users ->
                    println("DiscoverPageViewModel: Basic recommendations returned ${users.size} users")
                    if (users.isNotEmpty()) {
                        _uiState.value = _uiState.value.copy(
                            availableProfiles = users,
                            currentProfileIndex = 0,
                            isLoading = false
                        )
                        println("DiscoverPageViewModel: Successfully loaded basic recommendations")
                    } else {
                        // 如果完全沒有其他用戶，顯示錯誤訊息而不是當前用戶
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            availableProfiles = emptyList(),
                            errorMessage = "No other users available for discovery. Please check back later!"
                        )
                        println("DiscoverPageViewModel: No discoverable users found")
                    }
                },
                onFailure = { exception ->
                    println("DiscoverPageViewModel: Failed to load basic recommendations: ${exception.message}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to load recommendations: ${exception.message}"
                    )
                }
            )
        }
    }

    private fun loadMoreRecommendations() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            userProfileRepository.getRecommendedUsers(limit = 10).fold(
                onSuccess = { newUsers ->
                    if (newUsers.isNotEmpty()) {
                        _uiState.value = _uiState.value.copy(
                            availableProfiles = newUsers,
                            currentProfileIndex = 0,
                            isLoading = false,
                            errorMessage = null
                        )
                        println("DiscoverPageViewModel: Successfully loaded ${newUsers.size} more recommendations")
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = null,
                            availableProfiles = emptyList()
                        )
                        println("DiscoverPageViewModel: No more recommendations available")
                    }
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Unable to load more recommendations"
                    )
                    println("DiscoverPageViewModel: Failed to load more recommendations: ${exception.message}")
                }
            )
        }
    }

    fun retryLoading() {
        loadDiscoverableProfiles()
    }

    fun refreshRecommendations() {
        loadDiscoverableProfiles()
    }

    fun getCurrentProfile(): User? {
        val profiles = _uiState.value.availableProfiles
        val index = _uiState.value.currentProfileIndex
        return if (profiles.isNotEmpty() && index < profiles.size) {
            profiles[index]
        } else null
    }

    fun onRejectProfile() {
        val currentProfile = getCurrentProfile()
        if (currentProfile != null) {
            // 記錄拒絕互動
            viewModelScope.launch {
                userInteractionRepository.recordRejection(currentProfile.id).fold(
                    onSuccess = {
                        println("DiscoverPageViewModel: Successfully recorded rejection for user ${currentProfile.id}")
                    },
                    onFailure = { exception ->
                        println("DiscoverPageViewModel: Failed to record rejection: ${exception.message}")
                    }
                )
            }
        }
        moveToNextProfile()
    }

    fun onApproveProfile() {
        val currentProfile = getCurrentProfile()
        if (currentProfile != null) {
            // 記錄喜歡互動
            viewModelScope.launch {
                userInteractionRepository.recordApproval(currentProfile.id).fold(
                    onSuccess = {
                        println("DiscoverPageViewModel: Successfully recorded approval for user ${currentProfile.id}")
                    },
                    onFailure = { exception ->
                        println("DiscoverPageViewModel: Failed to record approval: ${exception.message}")
                    }
                )
            }
        }
        moveToNextProfile()
    }

    private fun moveToNextProfile() {
        val currentIndex = _uiState.value.currentProfileIndex
        val profilesCount = _uiState.value.availableProfiles.size
        
        if (currentIndex < profilesCount - 1) {
            _uiState.value = _uiState.value.copy(
                currentProfileIndex = currentIndex + 1
            )
        } else {
            // 沒有更多個人資料時，嘗試載入更多推薦
            println("DiscoverPageViewModel: Reached end of profiles, trying to load more recommendations...")
            loadMoreRecommendations()
        }
    }
}