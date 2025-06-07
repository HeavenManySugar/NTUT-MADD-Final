package com.ntut.madd.finalproject.ui.setup3

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
class Setup3ViewModel @Inject constructor(
    setupDataManager: SetupDataManager
) : BaseSetupViewModel(setupDataManager) {

    private val _selectedDegree = MutableStateFlow("")
    val selectedDegree: StateFlow<String> = _selectedDegree.asStateFlow()

    private val _school = MutableStateFlow("")
    val school: StateFlow<String> = _school.asStateFlow()

    private val _major = MutableStateFlow("")
    val major: StateFlow<String> = _major.asStateFlow()

    override val isFormValid: StateFlow<Boolean> = combine(_selectedDegree, _school, _major) { degree, school, major ->
        degree.isNotEmpty() && school.trim().isNotEmpty() && school.trim().length >= 2 && major.trim().isNotEmpty() && major.trim().length >= 2
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    override fun saveCurrentStepData() {
        setupDataManager.updateEducation(_selectedDegree.value, _school.value, _major.value)
    }

    fun updateDegree(degree: String) {
        _selectedDegree.value = degree
    }

    fun updateSchool(school: String) {
        _school.value = school
    }

    fun updateMajor(major: String) {
        _major.value = major
    }
}