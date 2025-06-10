package com.ntut.madd.finalproject.demo

import com.ntut.madd.finalproject.data.service.RecommendationService
import com.ntut.madd.finalproject.data.utils.TestDataGenerator

/**
 * 推薦系統演示
 * 展示推薦算法如何根據用戶特性進行推薦
 */
fun main() {
    println("=== 智能推薦系統演示 ===\n")
    
    val recommendationService = RecommendationService()
    val testUsers = TestDataGenerator.generateTestUsers()
    
    // 模擬不同類型的用戶來測試推薦效果
    val demoUsers = listOf(
        // 程式設計師用戶
        testUsers.first().copy(
            id = "demo_programmer",
            displayName = "程式設計師小明",
            profile = testUsers.first().profile?.copy(
                interests = listOf("Programming", "Gaming", "Music"),
                personalityTraits = listOf("Analytical", "Creative"),
                position = "Software Engineer",
                major = "Computer Science"
            )
        ),
        
        // 設計師用戶  
        testUsers[2].copy(
            id = "demo_designer",
            displayName = "設計師小美",
            profile = testUsers[2].profile?.copy(
                interests = listOf("Art", "Photography", "Coffee", "Movies"),
                personalityTraits = listOf("Creative", "Artistic"),
                position = "UI Designer",
                major = "Visual Design"
            )
        ),
        
        // 商務人士
        testUsers[1].copy(
            id = "demo_business",
            displayName = "商務小王",
            profile = testUsers[1].profile?.copy(
                interests = listOf("Business", "Travel", "Wine", "Golf"),
                personalityTraits = listOf("Leadership", "Strategic"),
                position = "Product Manager",
                major = "Business Administration"
            )
        )
    )
    
    // 為每個演示用戶生成推薦
    demoUsers.forEach { currentUser ->
        println("👤 當前用戶: ${currentUser.displayName}")
        println("📍 位置: ${currentUser.profile?.city}, ${currentUser.profile?.district}")
        println("💼 職業: ${currentUser.profile?.position} @ ${currentUser.profile?.company}")
        println("🎓 教育: ${currentUser.profile?.major} - ${currentUser.profile?.school}")
        println("❤️  興趣: ${currentUser.profile?.interests?.joinToString(", ")}")
        println("🧠 個性: ${currentUser.profile?.personalityTraits?.joinToString(", ")}")
        println()
        
        // 獲取推薦
        val recommendations = recommendationService.recommendUsers(currentUser, testUsers)
        
        println("📋 推薦結果 (按相似度排序):")
        recommendations.forEachIndexed { index, user ->
            println("${index + 1}. ${user.displayName}")
            println("   📍 ${user.profile?.city}, ${user.profile?.district}")
            println("   💼 ${user.profile?.position}")
            println("   ❤️  ${user.profile?.interests?.take(3)?.joinToString(", ")}")
            
            // 計算並顯示相似度亮點
            val similarities = calculateSimilarityHighlights(currentUser, user)
            if (similarities.isNotEmpty()) {
                println("   ✨ 相似點: ${similarities.joinToString(", ")}")
            }
            println()
        }
        
        println("=" * 60)
        println()
    }
    
    // 展示排除功能
    println("🚫 互動記錄演示")
    val programmer = demoUsers[0]
    val excludeIds = setOf("test_user_2", "test_user_3") // 排除某些用戶
    
    println("排除用戶 ID: ${excludeIds.joinToString(", ")}")
    val filteredRecommendations = recommendationService.recommendUsers(
        programmer, 
        testUsers, 
        excludeIds
    )
    
    println("過濾後的推薦:")
    filteredRecommendations.forEach { user ->
        println("- ${user.displayName} (ID: ${user.id})")
    }
    
    println("\n演示完成！")
}

/**
 * 計算兩個用戶之間的相似度亮點
 */
private fun calculateSimilarityHighlights(user1: com.ntut.madd.finalproject.data.model.User, user2: com.ntut.madd.finalproject.data.model.User): List<String> {
    val highlights = mutableListOf<String>()
    val profile1 = user1.profile ?: return highlights
    val profile2 = user2.profile ?: return highlights
    
    // 檢查興趣相似度
    val commonInterests = profile1.interests.intersect(profile2.interests.toSet())
    if (commonInterests.isNotEmpty()) {
        highlights.add("共同興趣: ${commonInterests.joinToString(", ")}")
    }
    
    // 檢查地理位置
    if (profile1.city.lowercase() == profile2.city.lowercase()) {
        if (profile1.district.lowercase() == profile2.district.lowercase()) {
            highlights.add("同區域")
        } else {
            highlights.add("同城市")
        }
    }
    
    // 檢查教育背景
    if (profile1.school.lowercase() == profile2.school.lowercase()) {
        highlights.add("校友")
    }
    if (profile1.major.lowercase() == profile2.major.lowercase()) {
        highlights.add("同專業")
    }
    
    // 檢查個性特質
    val commonTraits = profile1.personalityTraits.intersect(profile2.personalityTraits.toSet())
    if (commonTraits.isNotEmpty()) {
        highlights.add("相似個性: ${commonTraits.joinToString(", ")}")
    }
    
    return highlights
}

/**
 * 字串重複運算子
 */
private operator fun String.times(n: Int): String = this.repeat(n)
