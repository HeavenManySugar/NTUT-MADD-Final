package com.ntut.madd.finalproject.ui.matches

import com.ntut.madd.finalproject.MainViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class MatchesPageViewModel @Inject constructor(
    // 如果 Profile 要用 repository 可注入
) : MainViewModel() {
    private val _shouldRestartApp = MutableStateFlow(false)
    val shouldRestartApp: StateFlow<Boolean>
        get() = _shouldRestartApp.asStateFlow()
    private val _currentRoute = MutableStateFlow("profile")
    val currentRoute: StateFlow<String> = _currentRoute.asStateFlow()

    fun navigateTo(route: String) {
        _currentRoute.value = route
    }

    // 你可以額外加一些 Profile 狀態
    private val _username = MutableStateFlow("匿名使用者")
    val username: StateFlow<String> = _username.asStateFlow()

    fun setUsername(name: String) {
        _username.value = name
    }
}