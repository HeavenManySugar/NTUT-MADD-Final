package com.ntut.madd.finalproject.data.repository

import com.ntut.madd.finalproject.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetupDataManager @Inject constructor() {
    
    // Step 1: Location
    private val _city = MutableStateFlow("")
    val city: StateFlow<String> = _city.asStateFlow()
    
    private val _district = MutableStateFlow("")
    val district: StateFlow<String> = _district.asStateFlow()
    
    // Step 2: Career
    private val _position = MutableStateFlow("")
    val position: StateFlow<String> = _position.asStateFlow()
    
    private val _company = MutableStateFlow("")
    val company: StateFlow<String> = _company.asStateFlow()
    
    // Step 3: Education
    private val _degree = MutableStateFlow("")
    val degree: StateFlow<String> = _degree.asStateFlow()
    
    private val _school = MutableStateFlow("")
    val school: StateFlow<String> = _school.asStateFlow()
    
    private val _major = MutableStateFlow("")
    val major: StateFlow<String> = _major.asStateFlow()
    
    // Step 4: Interests
    private val _interests = MutableStateFlow<List<String>>(emptyList())
    val interests: StateFlow<List<String>> = _interests.asStateFlow()
    
    // Step 5: Personality traits
    private val _personalityTraits = MutableStateFlow<List<String>>(emptyList())
    val personalityTraits: StateFlow<List<String>> = _personalityTraits.asStateFlow()
    
    // Step 6: About
    private val _aboutMe = MutableStateFlow("")
    val aboutMe: StateFlow<String> = _aboutMe.asStateFlow()
    
    private val _lookingFor = MutableStateFlow("")
    val lookingFor: StateFlow<String> = _lookingFor.asStateFlow()
    
    // Update methods for Step 1
    fun updateLocation(city: String, district: String) {
        _city.value = city
        _district.value = district
    }
    
    // Update methods for Step 2
    fun updateCareer(position: String, company: String) {
        _position.value = position
        _company.value = company
    }
    
    // Update methods for Step 3
    fun updateEducation(degree: String, school: String, major: String) {
        _degree.value = degree
        _school.value = school
        _major.value = major
    }
    
    // Update methods for Step 4
    fun updateInterests(interests: List<String>) {
        _interests.value = interests
    }
    
    // Update methods for Step 5
    fun updatePersonalityTraits(traits: List<String>) {
        _personalityTraits.value = traits
    }
    
    // Update methods for Step 6
    fun updateAbout(aboutMe: String, lookingFor: String) {
        _aboutMe.value = aboutMe
        _lookingFor.value = lookingFor
    }
    
    // Generate UserProfile from collected data
    fun generateUserProfile(): UserProfile {
        return UserProfile(
            city = _city.value,
            district = _district.value,
            position = _position.value,
            company = _company.value,
            degree = _degree.value,
            school = _school.value,
            major = _major.value,
            interests = _interests.value,
            personalityTraits = _personalityTraits.value,
            aboutMe = _aboutMe.value,
            lookingFor = _lookingFor.value
        )
    }
    
    // Clear all data (for testing or reset purposes)
    fun clearAllData() {
        _city.value = ""
        _district.value = ""
        _position.value = ""
        _company.value = ""
        _degree.value = ""
        _school.value = ""
        _major.value = ""
        _interests.value = emptyList()
        _personalityTraits.value = emptyList()
        _aboutMe.value = ""
        _lookingFor.value = ""
    }
}
