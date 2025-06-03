package com.ntut.madd.finalproject.ui.setup

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseSetupViewModel : ViewModel() {
    
    private val _navigateToNext = MutableStateFlow(false)
    val navigateToNext: StateFlow<Boolean> = _navigateToNext.asStateFlow()
    
    abstract val isFormValid: StateFlow<Boolean>
    
    fun onNextClicked() {
        _navigateToNext.value = true
    }
    
    fun onNavigateHandled() {
        _navigateToNext.value = false
    }
}
