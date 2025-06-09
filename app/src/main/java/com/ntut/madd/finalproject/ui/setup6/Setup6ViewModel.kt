package com.ntut.madd.finalproject.ui.setup6

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntut.madd.finalproject.MainViewModel
import com.ntut.madd.finalproject.data.repository.SetupDataManager
import com.ntut.madd.finalproject.data.repository.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class Setup6ViewModel @Inject constructor(
    private val setupDataManager: SetupDataManager,
    private val userProfileRepository: UserProfileRepository
) : MainViewModel() {

    private val _aboutMe = MutableStateFlow("")
    val aboutMe: StateFlow<String> = _aboutMe.asStateFlow()

    private val _lookingFor = MutableStateFlow("")
    val lookingFor: StateFlow<String> = _lookingFor.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    private val _setupCompleted = MutableStateFlow(false)
    val setupCompleted: StateFlow<Boolean> = _setupCompleted.asStateFlow()

    // Reactive form validation state
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
        _aboutMe.value = text
    }

    fun updateLookingFor(text: String) {
        _lookingFor.value = text
    }

    suspend fun saveProfileAndComplete(): Result<Unit> {
        return try {
            _isSubmitting.value = true
            
            // Update the final step data
            setupDataManager.updateAbout(_aboutMe.value, _lookingFor.value)
            
            // Generate complete user profile
            val userProfile = setupDataManager.generateUserProfile()
            
            // Save to Firebase
            val result = userProfileRepository.saveUserProfile(userProfile)
            
            if (result.isSuccess) {
                _setupCompleted.value = true
                // Clear setup data after successful save
                setupDataManager.clearAllData()
            }
            
            result
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            _isSubmitting.value = false
        }
    }

    fun getAboutMeCharacterCount(): String {
        return "${_aboutMe.value.length}/500"
    }

    fun getLookingForCharacterCount(): String {
        return "${_lookingFor.value.length}/300"
    }
}