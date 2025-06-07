package com.ntut.madd.finalproject.ui.setup2

import androidx.lifecycle.viewModelScope
import com.ntut.madd.finalproject.ui.setup.BaseSetupViewModel
import com.ntut.madd.finalproject.data.repository.SetupDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class Setup2ViewModel @Inject constructor(
    setupDataManager: SetupDataManager
) : BaseSetupViewModel(setupDataManager) {

    private val _position = MutableStateFlow("")
    val position: StateFlow<String> = _position.asStateFlow()

    private val _company = MutableStateFlow("")
    val company: StateFlow<String> = _company.asStateFlow()

    override val isFormValid: StateFlow<Boolean> = combine(_position, _company) { position, company ->
        position.isNotBlank() && position.length >= 2 && company.isNotBlank() && company.length >= 2
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    override fun saveCurrentStepData() {
        setupDataManager.updateCareer(_position.value, _company.value)
    }

    fun updatePosition(newPosition: String) {
        _position.value = newPosition
    }

    fun updateCompany(newCompany: String) {
        _company.value = newCompany
    }
}