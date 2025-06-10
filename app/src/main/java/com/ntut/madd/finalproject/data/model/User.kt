package com.ntut.madd.finalproject.data.model

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId val id: String = "",
    val ownerId: String = "",
    val email: String = "",
    val displayName: String = "",
    val isAnonymous: Boolean = true,
    val profile: UserProfile? = null
)

data class UserProfile(
    // Step 1: Location
    val city: String = "",
    val district: String = "",
    
    // Step 2: Career
    val position: String = "",
    val company: String = "",
    
    // Step 3: Education
    val degree: String = "",
    val school: String = "",
    val major: String = "",
    
    // Step 4: Interests (up to 5)
    val interests: List<String> = emptyList(),
    
    // Step 5: Personality traits (3-5)
    val personalityTraits: List<String> = emptyList(),
    
    // Step 6: About
    val aboutMe: String = "",
    val lookingFor: String = "",
    
    // Metadata
    val profileCompletedAt: Long = 0L, // timestamp when profile was completed
    val isProfileComplete: Boolean = false
)