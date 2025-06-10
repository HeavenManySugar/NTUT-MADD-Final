package com.ntut.madd.finalproject.data.service

import com.ntut.madd.finalproject.data.model.User
import com.ntut.madd.finalproject.data.model.UserProfile
import com.ntut.madd.finalproject.data.utils.TestDataGenerator
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

/**
 * 推薦系統整合測試
 * 測試完整的推薦流程和相似度計算
 */
class RecommendationSystemIntegrationTest {

    private lateinit var recommendationService: RecommendationService
    private lateinit var testUsers: List<User>

    @Before
    fun setup() {
        recommendationService = RecommendationService()
        testUsers = TestDataGenerator.generateTestUsers()
    }

    @Test
    fun testBasicRecommendation() {
        // 創建一個測試用戶（對程式設計有興趣）
        val currentUser = User(
            id = "current_user",
            ownerId = "current_owner",
            displayName = "Test User",
            email = "test@example.com",
            isAnonymous = false,
            profile = UserProfile(
                city = "Taipei",
                district = "Xinyi",
                position = "Software Engineer",
                company = "Test Corp",
                degree = "Bachelor",
                school = "National Taiwan University",
                major = "Computer Science",
                interests = listOf("Programming", "Music", "Reading"),
                personalityTraits = listOf("Analytical", "Creative"),
                aboutMe = "I love coding and creating software solutions.",
                lookingFor = "Looking for someone who shares technical interests.",
                isProfileComplete = true,
                profileCompletedAt = System.currentTimeMillis()
            )
        )

        // 獲取推薦
        val recommendations = recommendationService.recommendUsers(currentUser, testUsers)

        // 驗證結果
        assertNotNull("推薦結果不應為 null", recommendations)
        assertTrue("應該有推薦結果", recommendations.isNotEmpty())
        
        // 第一個推薦應該是與程式設計最相關的用戶（Alice Chen）
        val topRecommendation = recommendations.first()
        assertEquals("Alice Chen", topRecommendation.displayName)
        
        // 打印推薦統計
        TestDataGenerator.printRecommendationStats(currentUser, testUsers, recommendations)
    }

    @Test
    fun testLocationBasedRecommendation() {
        // 創建台中用戶
        val taichungUser = User(
            id = "taichung_user",
            ownerId = "taichung_owner",
            displayName = "Taichung User",
            email = "taichung@example.com",
            isAnonymous = false,
            profile = UserProfile(
                city = "Taichung",
                district = "Xitun",
                interests = listOf("Data Science", "Hiking"),
                personalityTraits = listOf("Analytical"),
                aboutMe = "I work in data science and love hiking.",
                lookingFor = "Looking for local friends.",
                isProfileComplete = true,
                profileCompletedAt = System.currentTimeMillis()
            )
        )

        val recommendations = recommendationService.recommendUsers(taichungUser, testUsers)
        
        // David Zhang 應該是首選（同城市 + 相似興趣）
        val topRecommendation = recommendations.first()
        assertEquals("David Zhang", topRecommendation.displayName)
    }

    @Test
    fun testExcludeInteractedUsers() {
        val currentUser = testUsers.first()
        val excludeUserIds = setOf("test_user_2", "test_user_3") // 排除 Bob 和 Carol
        
        val recommendations = recommendationService.recommendUsers(
            currentUser, 
            testUsers, 
            excludeUserIds
        )
        
        // 確認被排除的用戶不在推薦結果中
        val recommendedIds = recommendations.map { it.id }
        assertFalse("Bob 應該被排除", recommendedIds.contains("test_user_2"))
        assertFalse("Carol 應該被排除", recommendedIds.contains("test_user_3"))
        assertTrue("David 應該在推薦中", recommendedIds.contains("test_user_4"))
        assertTrue("Emma 應該在推薦中", recommendedIds.contains("test_user_5"))
    }

    @Test
    fun testRandomnessFunction() {
        val users = testUsers
        
        // 測試添加隨機性
        val randomized1 = recommendationService.addRandomness(users, 0.5)
        val randomized2 = recommendationService.addRandomness(users, 0.5)
        
        // 由於隨機性，兩次結果可能不同（但不是絕對的）
        assertNotNull(randomized1)
        assertNotNull(randomized2)
        assertEquals(users.size, randomized1.size)
        assertEquals(users.size, randomized2.size)
        
        // 測試低隨機因子
        val lowRandomness = recommendationService.addRandomness(users, 0.1)
        assertNotNull(lowRandomness)
        assertEquals(users.size, lowRandomness.size)
    }

    @Test
    fun testEmptyProfile() {
        val emptyUser = User(
            id = "empty_user",
            ownerId = "empty_owner",
            displayName = "Empty User",
            email = "empty@example.com",
            profile = null
        )
        
        val recommendations = recommendationService.recommendUsers(emptyUser, testUsers)
        
        // 應該返回洗牌後的列表
        assertNotNull(recommendations)
        assertTrue(recommendations.isNotEmpty())
    }

    @Test
    fun testPerformance() {
        val currentUser = testUsers.first()
        val largeCandidateList = mutableListOf<User>()
        
        // 創建大量候選用戶
        repeat(100) { i ->
            largeCandidateList.add(
                testUsers.random().copy(
                    id = "user_$i",
                    ownerId = "owner_$i"
                )
            )
        }
        
        val startTime = System.currentTimeMillis()
        val recommendations = recommendationService.recommendUsers(currentUser, largeCandidateList)
        val endTime = System.currentTimeMillis()
        
        val executionTime = endTime - startTime
        println("推薦算法執行時間: ${executionTime}ms (100個候選用戶)")
        
        assertNotNull(recommendations)
        assertTrue("執行時間應在合理範圍內 (<1000ms)", executionTime < 1000)
    }
}
