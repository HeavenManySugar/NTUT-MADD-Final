package com.ntut.madd.finalproject.ui.setup

import com.ntut.madd.finalproject.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor() : MainViewModel() {
    private val _selectedCity = MutableStateFlow("")
    val selectedCity: StateFlow<String>
        get() = _selectedCity.asStateFlow()
    
    private val _district = MutableStateFlow("")
    val district: StateFlow<String>
        get() = _district.asStateFlow()
    
    private val _isFormValid = MutableStateFlow(false)
    val isFormValid: StateFlow<Boolean>
        get() = _isFormValid.asStateFlow()
    
    private val _navigateToNext = MutableStateFlow(false)
    val navigateToNext: StateFlow<Boolean>
        get() = _navigateToNext.asStateFlow()
    
    fun updateCity(city: String) {
        _selectedCity.value = city
        validateForm()
    }
    
    fun updateDistrict(district: String) {
        _district.value = district
        validateForm()
    }
    
    private fun validateForm() {
        _isFormValid.value = _selectedCity.value.isNotEmpty() && 
                            _district.value.trim().isNotEmpty()
    }
    
    fun onNextClicked() {
        if (_isFormValid.value) {
            _navigateToNext.value = true
        }
    }
    
    fun onNavigateHandled() {
        _navigateToNext.value = false
    }
}
