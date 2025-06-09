package com.ntut.madd.finalproject.ui.splash

import com.ntut.madd.finalproject.MainViewModel
import com.ntut.madd.finalproject.data.repository.AuthRepository
import com.ntut.madd.finalproject.data.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay
import javax.inject.Inject

enum class NavigationTarget {
    None,
    Home,
    SignIn,
    Setup
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userProfileRepository: UserProfileRepository
) : MainViewModel() {
    
    private val _navigationTarget = MutableStateFlow(NavigationTarget.None)
    val navigationTarget: StateFlow<NavigationTarget> = _navigationTarget.asStateFlow()

    fun checkAuthStatus() {
        launchCatching {
            // 顯示啟動畫面至少1.5秒
            delay(1500)
            
            val currentUser = authRepository.currentUser
            
            if (currentUser == null) {
                // 沒有登入，導航到登入頁面
                _navigationTarget.value = NavigationTarget.SignIn
            } else {
                // 已經登入，檢查是否有用戶資料
                val profileResult = userProfileRepository.getUserProfile()
                if (profileResult.isSuccess && profileResult.getOrNull() != null) {
                    // 有用戶資料，導航到主頁面
                    _navigationTarget.value = NavigationTarget.Home
                } else {
                    // 沒有用戶資料，導航到設定頁面
                    _navigationTarget.value = NavigationTarget.Setup
                }
            }
        }
    }
    
    fun onNavigationHandled() {
        _navigationTarget.value = NavigationTarget.None
    }
}
