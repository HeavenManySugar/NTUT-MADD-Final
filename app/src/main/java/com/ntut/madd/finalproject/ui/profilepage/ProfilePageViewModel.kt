package com.ntut.madd.finalproject.ui.profilepage;

import androidx.lifecycle.viewModelScope
import com.ntut.madd.finalproject.MainViewModel
import com.ntut.madd.finalproject.data.model.User
import com.ntut.madd.finalproject.data.model.UserProfile
import com.ntut.madd.finalproject.data.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null,
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
    val editableProfile: UserProfile? = null
)

@HiltViewModel
class ProfilePageViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository
) : MainViewModel() {
    private val _shouldRestartApp = MutableStateFlow(false)
    val shouldRestartApp: StateFlow<Boolean>
        get() = _shouldRestartApp.asStateFlow()
    private val _currentRoute = MutableStateFlow("profile")
    val currentRoute: StateFlow<String> = _currentRoute.asStateFlow()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    fun navigateTo(route: String) {
        _currentRoute.value = route
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            userProfileRepository.getUserProfile().fold(
                onSuccess = { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        user = user,
                        errorMessage = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Failed to load profile"
                    )
                }
            )
        }
    }

    fun refreshProfile() {
        loadUserProfile()
    }
    
    fun startEditing() {
        val currentUser = _uiState.value.user
        if (currentUser?.profile != null) {
            _uiState.value = _uiState.value.copy(
                isEditing = true,
                editableProfile = currentUser.profile.copy()
            )
        }
    }
    
    fun cancelEditing() {
        _uiState.value = _uiState.value.copy(
            isEditing = false,
            editableProfile = null
        )
    }
    
    fun updateDisplayName(displayName: String) {
        val currentUser = _uiState.value.user ?: return
        _uiState.value = _uiState.value.copy(
            user = currentUser.copy(displayName = displayName)
        )
    }
    
    fun updateCity(city: String) {
        val currentProfile = _uiState.value.editableProfile ?: return
        _uiState.value = _uiState.value.copy(
            editableProfile = currentProfile.copy(city = city)
        )
    }
    
    fun updateDistrict(district: String) {
        val currentProfile = _uiState.value.editableProfile ?: return
        _uiState.value = _uiState.value.copy(
            editableProfile = currentProfile.copy(district = district)
        )
    }
    
    fun updatePosition(position: String) {
        val currentProfile = _uiState.value.editableProfile ?: return
        _uiState.value = _uiState.value.copy(
            editableProfile = currentProfile.copy(position = position)
        )
    }
    
    fun updateCompany(company: String) {
        val currentProfile = _uiState.value.editableProfile ?: return
        _uiState.value = _uiState.value.copy(
            editableProfile = currentProfile.copy(company = company)
        )
    }
    
    fun updateDegree(degree: String) {
        val currentProfile = _uiState.value.editableProfile ?: return
        _uiState.value = _uiState.value.copy(
            editableProfile = currentProfile.copy(degree = degree)
        )
    }
    
    fun updateSchool(school: String) {
        val currentProfile = _uiState.value.editableProfile ?: return
        _uiState.value = _uiState.value.copy(
            editableProfile = currentProfile.copy(school = school)
        )
    }
    
    fun updateMajor(major: String) {
        val currentProfile = _uiState.value.editableProfile ?: return
        _uiState.value = _uiState.value.copy(
            editableProfile = currentProfile.copy(major = major)
        )
    }
    
    fun updateAboutMe(aboutMe: String) {
        val currentProfile = _uiState.value.editableProfile ?: return
        _uiState.value = _uiState.value.copy(
            editableProfile = currentProfile.copy(aboutMe = aboutMe)
        )
    }
    
    fun updateLookingFor(lookingFor: String) {
        val currentProfile = _uiState.value.editableProfile ?: return
        _uiState.value = _uiState.value.copy(
            editableProfile = currentProfile.copy(lookingFor = lookingFor)
        )
    }
    
    fun updateInterests(interests: List<String>) {
        val currentProfile = _uiState.value.editableProfile ?: return
        _uiState.value = _uiState.value.copy(
            editableProfile = currentProfile.copy(interests = interests)
        )
    }
    
    fun updatePersonalityTraits(traits: List<String>) {
        val currentProfile = _uiState.value.editableProfile ?: return
        _uiState.value = _uiState.value.copy(
            editableProfile = currentProfile.copy(personalityTraits = traits)
        )
    }
    
    fun saveProfile() {
        val editableProfile = _uiState.value.editableProfile ?: return
        val currentUser = _uiState.value.user ?: return
        
        // Validate profile before saving
        val validationErrors = validateProfile(editableProfile)
        if (validationErrors.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = validationErrors.joinToString("\n")
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            
            userProfileRepository.updateUserProfileAndDisplayName(editableProfile, currentUser.displayName).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        user = currentUser.copy(profile = editableProfile),
                        isEditing = false,
                        editableProfile = null,
                        isSaving = false
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        errorMessage = exception.message ?: "Failed to save profile"
                    )
                }
            )
        }
    }
    
    private fun validateProfile(profile: UserProfile): List<String> {
        val errors = mutableListOf<String>()
        
        // Validate text fields with minimum character requirements
        if (profile.aboutMe.isNotEmpty() && profile.aboutMe.length < 50) {
            errors.add("About Me must be at least 50 characters (current: ${profile.aboutMe.length})")
        }
        
        if (profile.lookingFor.isNotEmpty() && profile.lookingFor.length < 20) {
            errors.add("Looking For must be at least 20 characters (current: ${profile.lookingFor.length})")
        }
        
        // Validate required field lengths (at least 2 characters for non-empty fields)
        if (profile.city.isNotEmpty() && profile.city.length < 2) {
            errors.add("City must be at least 2 characters")
        }
        
        if (profile.district.isNotEmpty() && profile.district.length < 2) {
            errors.add("District must be at least 2 characters")
        }
        
        if (profile.position.isNotEmpty() && profile.position.length < 2) {
            errors.add("Position must be at least 2 characters")
        }
        
        if (profile.company.isNotEmpty() && profile.company.length < 2) {
            errors.add("Company must be at least 2 characters")
        }
        
        if (profile.school.isNotEmpty() && profile.school.length < 2) {
            errors.add("School must be at least 2 characters")
        }
        
        if (profile.major.isNotEmpty() && profile.major.length < 2) {
            errors.add("Major must be at least 2 characters")
        }
        
        // Validate interests selection (3-5 required)
        if (profile.interests.isNotEmpty() && profile.interests.size < 3) {
            errors.add("Please select at least 3 interests (current: ${profile.interests.size})")
        }
        
        // Validate personality traits selection (3-5 required)
        if (profile.personalityTraits.isNotEmpty() && profile.personalityTraits.size < 3) {
            errors.add("Please select at least 3 personality traits (current: ${profile.personalityTraits.size})")
        }
        
        return errors
    }
}