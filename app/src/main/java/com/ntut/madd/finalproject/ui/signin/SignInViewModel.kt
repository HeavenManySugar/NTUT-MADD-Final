package com.ntut.madd.finalproject.ui.signin

import com.ntut.madd.finalproject.MainViewModel
import com.ntut.madd.finalproject.data.model.ErrorMessage
import com.ntut.madd.finalproject.data.repository.AuthRepository
import com.ntut.madd.finalproject.data.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userProfileRepository: UserProfileRepository
) : MainViewModel() {
    private val _shouldRestartApp = MutableStateFlow(false)
    val shouldRestartApp: StateFlow<Boolean>
        get() = _shouldRestartApp.asStateFlow()
        
    private val _shouldNavigateToSetup = MutableStateFlow(false)
    val shouldNavigateToSetup: StateFlow<Boolean>
        get() = _shouldNavigateToSetup.asStateFlow()

    fun signIn(
        email: String,
        password: String,
        showErrorSnackbar: (ErrorMessage) -> Unit
    ) {
        launchCatching(showErrorSnackbar) {
            authRepository.signIn(email, password)
            checkUserProfileAndNavigate()
        }
    }
    
    fun anonymousSignIn(showErrorSnackbar: (ErrorMessage) -> Unit) {
        launchCatching(showErrorSnackbar) {
            authRepository.createGuestAccount()
            checkUserProfileAndNavigate()
        }
    }
    
    fun googleSignIn(showErrorSnackbar: (ErrorMessage) -> Unit) {
        // TODO: 實現Google登入
        // 暫時使用匿名登入代替
        launchCatching(showErrorSnackbar) {
            authRepository.createGuestAccount()
            checkUserProfileAndNavigate()
        }
    }
    
    private suspend fun checkUserProfileAndNavigate() {
        val profileResult = userProfileRepository.getUserProfile()
        if (profileResult.isSuccess && profileResult.getOrNull() != null) {
            // 用戶已有profile，導航到主頁
            _shouldRestartApp.value = true
        } else {
            // 用戶沒有profile，導航到setup
            _shouldNavigateToSetup.value = true
        }
    }
    
    fun onNavigationHandled() {
        _shouldRestartApp.value = false
        _shouldNavigateToSetup.value = false
    }
}