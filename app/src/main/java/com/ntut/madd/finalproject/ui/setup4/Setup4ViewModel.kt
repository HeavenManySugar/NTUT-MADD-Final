package com.ntut.madd.finalproject.ui.setup4

import androidx.lifecycle.viewModelScope
import com.ntut.madd.finalproject.ui.setup.BaseSetupViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Setup4ViewModel @Inject constructor() : BaseSetupViewModel() {
    
    private val _selectedInterests = MutableStateFlow<List<String>>(emptyList())
    val selectedInterests: StateFlow<List<String>> = _selectedInterests.asStateFlow()
    
    override val isFormValid: StateFlow<Boolean> = selectedInterests
        .map { interests -> interests.size >= 3 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
    
    fun toggleInterest(interest: String) {
        viewModelScope.launch {
            val currentInterests = _selectedInterests.value.toMutableList()
            if (currentInterests.contains(interest)) {
                currentInterests.remove(interest)
            } else {
                currentInterests.add(interest)
            }
            _selectedInterests.value = currentInterests
        }
    }
    
    fun updateInterests(interests: List<String>) {
        viewModelScope.launch {
            _selectedInterests.value = interests
        }
    }
}
