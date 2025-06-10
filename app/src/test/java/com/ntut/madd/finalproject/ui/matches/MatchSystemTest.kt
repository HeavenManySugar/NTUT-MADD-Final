package com.ntut.madd.finalproject.ui.matches

import com.ntut.madd.finalproject.data.model.User
import com.ntut.madd.finalproject.data.model.UserProfile
import com.ntut.madd.finalproject.data.model.UserInteraction
import com.ntut.madd.finalproject.data.model.InteractionType
import org.junit.Test
import org.junit.Assert.*

/**
 * Match System Test
 * 測試Match系統的基本功能
 */
class MatchSystemTest {

    @Test
    fun `test MatchesUiState creation`() {
        // Test creating a basic MatchesUiState
        val uiState = MatchesUiState(
            isLoading = false,
            usersWhoLikedMe = emptyList(),
            mutualMatches = emptyList(),
            errorMessage = null
        )
        
        assertFalse("Loading should be false", uiState.isLoading)
        assertTrue("Users who liked me should be empty", uiState.usersWhoLikedMe.isEmpty())
        assertTrue("Mutual matches should be empty", uiState.mutualMatches.isEmpty())
        assertNull("Error message should be null", uiState.errorMessage)
    }

    @Test
    fun `test MatchesUiState with data`() {
        // Create sample users
        val user1 = createTestUser("user1", "Alice Chen")
        val user2 = createTestUser("user2", "Bob Wang")
        
        val uiState = MatchesUiState(
            isLoading = false,
            usersWhoLikedMe = listOf(user1, user2),
            mutualMatches = listOf(user1),
            errorMessage = null
        )
        
        assertEquals("Should have 2 users who liked me", 2, uiState.usersWhoLikedMe.size)
        assertEquals("Should have 1 mutual match", 1, uiState.mutualMatches.size)
        assertEquals("First user should be Alice", "Alice Chen", uiState.usersWhoLikedMe[0].displayName)
    }

    @Test
    fun `test UserInteraction model for likes`() {
        val interaction = UserInteraction(
            id = "interaction1",
            userId = "user1",
            targetUserId = "user2",
            action = InteractionType.APPROVE,
            timestamp = System.currentTimeMillis(),
            sessionId = "session1"
        )
        
        assertEquals("User ID should match", "user1", interaction.userId)
        assertEquals("Target user ID should match", "user2", interaction.targetUserId)
        assertEquals("Action should be APPROVE", InteractionType.APPROVE, interaction.action)
    }

    @Test
    fun `test mutual match detection logic`() {
        // Create test users
        val user1 = createTestUser("user1", "Alice")
        val user2 = createTestUser("user2", "Bob")
        val user3 = createTestUser("user3", "Carol")
        
        // Simulate users who liked current user
        val usersWhoLikedMe = listOf(user1, user2, user3)
        
        // Simulate users that current user approved
        val currentUserApprovedIds = setOf("user1", "user3") // Alice and Carol
        
        // Find mutual matches
        val mutualMatches = usersWhoLikedMe.filter { user ->
            currentUserApprovedIds.contains(user.id)
        }
        
        assertEquals("Should have 2 mutual matches", 2, mutualMatches.size)
        assertTrue("Should contain Alice", mutualMatches.any { it.displayName == "Alice" })
        assertTrue("Should contain Carol", mutualMatches.any { it.displayName == "Carol" })
        assertFalse("Should not contain Bob", mutualMatches.any { it.displayName == "Bob" })
    }

    private fun createTestUser(id: String, displayName: String): User {
        return User(
            id = id,
            ownerId = id,
            displayName = displayName,
            email = "$displayName@test.com",
            isAnonymous = false,
            profile = UserProfile(
                city = "Taipei",
                district = "Xinyi",
                position = "Software Engineer",
                company = "Test Corp",
                degree = "Bachelor",
                school = "National Taiwan University",
                major = "Computer Science",
                interests = listOf("Programming", "Music"),
                personalityTraits = listOf("Creative", "Analytical"),
                aboutMe = "Test user profile",
                lookingFor = "Looking for someone interesting",
                isProfileComplete = true,
                profileCompletedAt = System.currentTimeMillis()
            )
        )
    }
}
