package com.ntut.madd.finalproject.data.service

import com.ntut.madd.finalproject.data.model.User
import com.ntut.madd.finalproject.data.model.UserProfile
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

/**
 * 推薦系統單元測試
 */
class RecommendationServiceTest {

    private lateinit var recommendationService: RecommendationService

    @Before
    fun setUp() {
        recommendationService = RecommendationService()
    }

    @Test
    fun `test interest similarity calculation`() {
        val currentUser = createTestUser(
            id = "user1",
            interests = listOf("Music", "Sports", "Reading")
        )
        
        val candidate1 = createTestUser(
            id = "user2", 
            interests = listOf("Music", "Sports", "Gaming") // 2 common interests
        )
        
        val candidate2 = createTestUser(
            id = "user3",
            interests = listOf("Cooking", "Travel", "Art") // 0 common interests
        )
        
        val candidates = listOf(candidate1, candidate2)
        val recommendations = recommendationService.recommendUsers(currentUser, candidates)
        
        // 有共同興趣的用戶應該排在前面
        assertEquals(candidate1.id, recommendations.first().id)
        assertEquals(candidate2.id, recommendations.last().id)
    }

    @Test
    fun `test location similarity prioritization`() {
        val currentUser = createTestUser(
            id = "user1",
            city = "Taipei",
            district = "Xinyi"
        )
        
        val sameLocationUser = createTestUser(
            id = "user2",
            city = "Taipei", 
            district = "Xinyi"
        )
        
        val sameCityUser = createTestUser(
            id = "user3",
            city = "Taipei",
            district = "Daan"
        )
        
        val differentCityUser = createTestUser(
            id = "user4",
            city = "Kaohsiung",
            district = "Zuoying"
        )
        
        val candidates = listOf(differentCityUser, sameCityUser, sameLocationUser)
        val recommendations = recommendationService.recommendUsers(currentUser, candidates)
        
        // 相同地點的用戶應該排在最前面
        assertEquals(sameLocationUser.id, recommendations.first().id)
    }

    @Test
    fun `test exclusion of interacted users`() {
        val currentUser = createTestUser(id = "user1")
        
        val user2 = createTestUser(id = "user2")
        val user3 = createTestUser(id = "user3")
        val user4 = createTestUser(id = "user4")
        
        val candidates = listOf(user2, user3, user4)
        val excludeIds = setOf("user2", "user3") // 已經互動過的用戶
        
        val recommendations = recommendationService.recommendUsers(
            currentUser, 
            candidates, 
            excludeIds
        )
        
        // 只應該返回未互動過的用戶
        assertEquals(1, recommendations.size)
        assertEquals("user4", recommendations.first().id)
    }

    @Test
    fun `test personality traits matching`() {
        val currentUser = createTestUser(
            id = "user1",
            personalityTraits = listOf("Outgoing", "Creative", "Analytical")
        )
        
        val similarPersonality = createTestUser(
            id = "user2",
            personalityTraits = listOf("Outgoing", "Creative", "Friendly") // 2 common traits
        )
        
        val differentPersonality = createTestUser(
            id = "user3",
            personalityTraits = listOf("Introverted", "Practical", "Calm") // 0 common traits
        )
        
        val candidates = listOf(differentPersonality, similarPersonality)
        val recommendations = recommendationService.recommendUsers(currentUser, candidates)
        
        // 相似個性的用戶應該排在前面
        assertEquals(similarPersonality.id, recommendations.first().id)
    }

    @Test
    fun `test education background matching`() {
        val currentUser = createTestUser(
            id = "user1",
            degree = "Bachelor",
            school = "NTUT",
            major = "Computer Science"
        )
        
        val sameEducation = createTestUser(
            id = "user2",
            degree = "Bachelor",
            school = "NTUT", 
            major = "Computer Science"
        )
        
        val partialMatch = createTestUser(
            id = "user3",
            degree = "Bachelor",
            school = "NTU",
            major = "Computer Science"
        )
        
        val noMatch = createTestUser(
            id = "user4",
            degree = "Master",
            school = "NCTU",
            major = "Business"
        )
        
        val candidates = listOf(noMatch, partialMatch, sameEducation)
        val recommendations = recommendationService.recommendUsers(currentUser, candidates)
        
        // 教育背景完全匹配的用戶應該排在最前面
        assertEquals(sameEducation.id, recommendations.first().id)
    }

    @Test
    fun `test randomness feature`() {
        val users = (1..10).map { createTestUser(id = "user$it") }
        
        val result1 = recommendationService.addRandomness(users, 0.5)
        val result2 = recommendationService.addRandomness(users, 0.5)
        
        // 由於加入了隨機性，兩次結果不應該完全相同
        assertNotEquals(result1.map { it.id }, result2.map { it.id })
    }

    private fun createTestUser(
        id: String,
        interests: List<String> = emptyList(),
        personalityTraits: List<String> = emptyList(),
        city: String = "",
        district: String = "",
        degree: String = "",
        school: String = "",
        major: String = "",
        position: String = "",
        company: String = ""
    ): User {
        val profile = UserProfile(
            interests = interests,
            personalityTraits = personalityTraits,
            city = city,
            district = district,
            degree = degree,
            school = school,
            major = major,
            position = position,
            company = company,
            isProfileComplete = true
        )
        
        return User(
            id = id,
            ownerId = id,
            displayName = "Test User $id",
            email = "test$id@example.com",
            isAnonymous = false,
            profile = profile
        )
    }
}
