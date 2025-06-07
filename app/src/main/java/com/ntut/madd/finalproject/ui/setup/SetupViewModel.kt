package com.ntut.madd.finalproject.ui.setup

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
class SetupViewModel @Inject constructor() : BaseSetupViewModel() {

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _selectedCity = MutableStateFlow("")
    val selectedCity: StateFlow<String> = _selectedCity.asStateFlow()

    private val _district = MutableStateFlow("")
    val district: StateFlow<String> = _district.asStateFlow()

    override val isFormValid: StateFlow<Boolean> = combine(_selectedCity, _district) { city, district ->
        city.isNotEmpty() && district.trim().isNotEmpty()
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    fun updateCity(city: String) {
        _selectedCity.value = city
    }

    fun updateDistrict(district: String) {
        _district.value = district
    }
}