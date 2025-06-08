package com.ntut.madd.finalproject.ui.setup

import androidx.lifecycle.ViewModel
import com.ntut.madd.finalproject.data.repository.SetupDataManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseSetupViewModel(
    protected val setupDataManager: SetupDataManager
) : ViewModel() {
    
    private val _navigateToNext = MutableStateFlow(false)
    val navigateToNext: StateFlow<Boolean> = _navigateToNext.asStateFlow()
    
    abstract val isFormValid: StateFlow<Boolean>
    
    fun onNextClicked() {
        if (isFormValid.value) {
            saveCurrentStepData()
            _navigateToNext.value = true
        }
    }
    
    fun onNavigateHandled() {
        _navigateToNext.value = false
    }
    
    // Each ViewModel should implement this to save their specific step data
    protected abstract fun saveCurrentStepData()
}
