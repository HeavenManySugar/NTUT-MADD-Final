package com.ntut.madd.finalproject.ui.home

import com.ntut.madd.finalproject.MainViewModel
import com.ntut.madd.finalproject.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : MainViewModel() {
    private val _isLoadingUser = MutableStateFlow(true)
    val isLoadingUser: StateFlow<Boolean>
        get() = _isLoadingUser.asStateFlow()
    
    init {
        loadCurrentUser()
    }
    
    private fun loadCurrentUser() {
        launchCatching {
            // 檢查用戶是否已認證
            val currentUser = authRepository.currentUser
            if (currentUser == null) {
                // 如果沒有認證的用戶，創建匿名帳戶
                authRepository.createGuestAccount()
            }
            _isLoadingUser.value = false
        }
    }
}