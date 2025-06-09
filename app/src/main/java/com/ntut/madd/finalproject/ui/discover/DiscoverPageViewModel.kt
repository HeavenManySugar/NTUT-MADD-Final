package com.ntut.madd.finalproject.ui.discover

import androidx.lifecycle.viewModelScope
import com.ntut.madd.finalproject.MainViewModel
import com.ntut.madd.finalproject.data.model.User
import com.ntut.madd.finalproject.data.repository.UserProfileRepository
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
    private val userProfileRepository: UserProfileRepository
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
            
            // First try to get discoverable users (excluding current user)
            userProfileRepository.getDiscoverableUsers(limit = 20).fold(
                onSuccess = { users ->
                    if (users.isNotEmpty()) {
                        _uiState.value = _uiState.value.copy(
                            availableProfiles = users,
                            currentProfileIndex = 0,
                            isLoading = false
                        )
                    } else {
                        // If no discoverable users, fall back to showing current user's profile for testing
                        loadCurrentUserProfile()
                    }
                },
                onFailure = { exception ->
                    // If discoverable users fetch fails, fall back to current user profile
                    println("DiscoverPageViewModel: Failed to load discoverable users: ${exception.message}")
                    loadCurrentUserProfile()
                }
            )
        }
    }

    private fun loadCurrentUserProfile() {
        viewModelScope.launch {
            userProfileRepository.getUserProfile().fold(
                onSuccess = { user ->
                    if (user != null) {
                        _uiState.value = _uiState.value.copy(
                            currentUser = user,
                            availableProfiles = listOf(user),
                            currentProfileIndex = 0,
                            isLoading = false
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "No profile found. Please complete your profile first."
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Failed to load profile"
                    )
                }
            )
        }
    }

    fun getCurrentProfile(): User? {
        val profiles = _uiState.value.availableProfiles
        val index = _uiState.value.currentProfileIndex
        return if (profiles.isNotEmpty() && index < profiles.size) {
            profiles[index]
        } else null
    }

    fun onRejectProfile() {
        // TODO: Implement rejection logic (save to database, analytics, etc.)
        moveToNextProfile()
    }

    fun onApproveProfile() {
        // TODO: Implement approval logic (save to database, check for matches, etc.)
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
            // No more profiles, could load more or show "no more profiles" message
            _uiState.value = _uiState.value.copy(
                errorMessage = "No more profiles available"
            )
        }
    }

    fun retryLoading() {
        loadDiscoverableProfiles()
    }
}