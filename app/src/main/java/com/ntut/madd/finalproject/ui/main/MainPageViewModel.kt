package com.ntut.madd.finalproject.ui.main

import com.ntut.madd.finalproject.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainPageViewModel @Inject constructor() : MainViewModel() {
    
    private val _currentRoute = MutableStateFlow("discover") // Start with discover as default
    val currentRoute: StateFlow<String> = _currentRoute.asStateFlow()
    
    private val _shouldRestartApp = MutableStateFlow(false)
    val shouldRestartApp: StateFlow<Boolean> = _shouldRestartApp.asStateFlow()

    fun navigateTo(route: String) {
        _currentRoute.value = route
    }
    
    fun restartApp() {
        _shouldRestartApp.value = true
    }
}
