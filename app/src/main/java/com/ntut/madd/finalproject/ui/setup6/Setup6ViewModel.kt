package com.ntut.madd.finalproject.ui.setup6

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class Setup6ViewModel @Inject constructor() : ViewModel() {

    private val _aboutMe = MutableStateFlow("")
    val aboutMe: StateFlow<String> = _aboutMe.asStateFlow()

    private val _lookingFor = MutableStateFlow("")
    val lookingFor: StateFlow<String> = _lookingFor.asStateFlow()

    // 響應式的表單驗證狀態
    val isFormValid: StateFlow<Boolean> = combine(
        _aboutMe,
        _lookingFor
    ) { aboutMe, lookingFor ->
        aboutMe.isNotBlank() || lookingFor.isNotBlank()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    fun updateAboutMe(text: String) {
        if (text.length <= 500) {
            _aboutMe.value = text
        }
    }

    fun updateLookingFor(text: String) {
        if (text.length <= 300) {
            _lookingFor.value = text
        }
    }

    // 保留此方法以防其他地方需要使用
    fun isValid(): Boolean {
        return _aboutMe.value.isNotBlank() || _lookingFor.value.isNotBlank()
    }

    fun getAboutMeCharacterCount(): String {
        return "${_aboutMe.value.length}/500"
    }

    fun getLookingForCharacterCount(): String {
        return "${_lookingFor.value.length}/300"
    }
}