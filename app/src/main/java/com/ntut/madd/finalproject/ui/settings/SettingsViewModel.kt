package com.ntut.madd.finalproject.ui.settings

import com.ntut.madd.finalproject.MainViewModel
import com.ntut.madd.finalproject.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : MainViewModel() {
    private val _shouldRestartApp = MutableStateFlow(false)
    val shouldRestartApp: StateFlow<Boolean>
        get() = _shouldRestartApp.asStateFlow()

    private val _shouldNavigateToSignIn = MutableStateFlow(false)
    val shouldNavigateToSignIn: StateFlow<Boolean>
        get() = _shouldNavigateToSignIn.asStateFlow()

    private val _isAnonymous = MutableStateFlow(true)
    val isAnonymous: StateFlow<Boolean>
        get() = _isAnonymous.asStateFlow()

    fun loadCurrentUser() {
        launchCatching {
            val currentUser = authRepository.currentUser
            _isAnonymous.value = currentUser != null && currentUser.isAnonymous
        }
    }

    fun signOut() {
        launchCatching {
            authRepository.signOut()
            // 登出後直接導航到登入頁面，而不是重啟應用
            _shouldNavigateToSignIn.value = true
        }
    }

    fun deleteAccount() {
        launchCatching {
            authRepository.deleteAccount()
            // 刪除帳戶後也導航到登入頁面
            _shouldNavigateToSignIn.value = true
        }
    }

    fun onNavigationHandled() {
        _shouldRestartApp.value = false
        _shouldNavigateToSignIn.value = false
    }
}