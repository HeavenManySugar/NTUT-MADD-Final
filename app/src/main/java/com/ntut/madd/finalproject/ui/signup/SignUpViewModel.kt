package com.ntut.madd.finalproject.ui.signup

import com.ntut.madd.finalproject.MainViewModel
import com.ntut.madd.finalproject.R
import com.ntut.madd.finalproject.data.model.ErrorMessage
import com.ntut.madd.finalproject.data.repository.AuthRepository
import com.ntut.madd.finalproject.data.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userProfileRepository: UserProfileRepository
) : MainViewModel() {
    private val _shouldRestartApp = MutableStateFlow(false)
    val shouldRestartApp: StateFlow<Boolean>
        get() = _shouldRestartApp.asStateFlow()
        
    private val _shouldNavigateToSetup = MutableStateFlow(false)
    val shouldNavigateToSetup: StateFlow<Boolean>
        get() = _shouldNavigateToSetup.asStateFlow()

    fun signUp(
        email: String,
        password: String,
        repeatPassword: String,
        showErrorSnackbar: (ErrorMessage) -> Unit
    ) {
        if (!email.isValidEmail()) {
            showErrorSnackbar(ErrorMessage.IdError(R.string.invalid_email))
            return
        }

        if (!password.isValidPassword()) {
            showErrorSnackbar(ErrorMessage.IdError(R.string.invalid_password))
            return
        }

        if (password != repeatPassword) {
            showErrorSnackbar(ErrorMessage.IdError(R.string.passwords_do_not_match))
            return
        }

        launchCatching(showErrorSnackbar) {
            authRepository.signUp(email, password)
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