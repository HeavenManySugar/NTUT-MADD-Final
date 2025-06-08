package com.ntut.madd.finalproject.ui.setup5

import androidx.lifecycle.viewModelScope
import com.ntut.madd.finalproject.ui.setup.BaseSetupViewModel
import com.ntut.madd.finalproject.data.repository.SetupDataManager
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
class Setup5ViewModel @Inject constructor(
    setupDataManager: SetupDataManager
) : BaseSetupViewModel(setupDataManager) {

    private val _selectedTraits = MutableStateFlow<List<String>>(emptyList())
    val selectedTraits: StateFlow<List<String>> = _selectedTraits.asStateFlow()

    override val isFormValid: StateFlow<Boolean> = selectedTraits
        .map { traits -> traits.size >= 3 } // 至少要選擇3個特質
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    override fun saveCurrentStepData() {
        setupDataManager.updatePersonalityTraits(_selectedTraits.value)
    }

    fun toggleTrait(trait: String) {
        viewModelScope.launch {
            val currentTraits = _selectedTraits.value.toMutableList()
            if (currentTraits.contains(trait)) {
                currentTraits.remove(trait)
            } else if (currentTraits.size < 5) { // 最多選擇5個
                currentTraits.add(trait)
            }
            _selectedTraits.value = currentTraits
        }
    }
}