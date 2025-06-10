package com.ntut.madd.finalproject.ui.userprofile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ntut.madd.finalproject.MainViewModel
import com.ntut.madd.finalproject.data.model.User
import com.ntut.madd.finalproject.data.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserProfileDetailUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class UserProfileDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userProfileRepository: UserProfileRepository
) : MainViewModel() {

    private val userProfileDetailRoute = savedStateHandle.toRoute<UserProfileDetailRoute>()
    private val userId: String = userProfileDetailRoute.userId

    private val _uiState = MutableStateFlow(UserProfileDetailUiState())
    val uiState: StateFlow<UserProfileDetailUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    fun refreshProfile() {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            try {
                userProfileRepository.getUserProfileById(userId).fold(
                    onSuccess = { user ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            user = user,
                            errorMessage = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            user = null,
                            errorMessage = exception.message ?: "Failed to load user profile"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    user = null,
                    errorMessage = e.message ?: "Failed to load user profile"
                )
            }
        }
    }
}
