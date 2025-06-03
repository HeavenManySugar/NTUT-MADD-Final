package com.ntut.madd.finalproject.ui.setup2

import com.ntut.madd.finalproject.ui.setup.BaseSetupViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

@HiltViewModel
class Setup2ViewModel @Inject constructor() : BaseSetupViewModel() {
    
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    
    private val _position = MutableStateFlow("")
    val position: StateFlow<String> = _position.asStateFlow()
    
    private val _company = MutableStateFlow("")
    val company: StateFlow<String> = _company.asStateFlow()
    
    override val isFormValid: StateFlow<Boolean> = combine(_position, _company) { position, company ->
        position.isNotBlank() && company.isNotBlank()
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )
    
    fun updatePosition(newPosition: String) {
        _position.value = newPosition
    }
    
    fun updateCompany(newCompany: String) {
        _company.value = newCompany
    }
}
