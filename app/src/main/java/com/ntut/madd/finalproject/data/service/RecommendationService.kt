package com.ntut.madd.finalproject.data.service

import com.ntut.madd.finalproject.data.model.User
import com.ntut.madd.finalproject.data.model.UserProfile
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sqrt

/**
 * 推薦系統服務
 * 基於用戶興趣、個性特質、地理位置、教育背景等進行智能推薦
 */
@Singleton
class RecommendationService @Inject constructor() {

    /**
     * 為當前用戶推薦最相符的用戶列表
     * @param currentUser 當前用戶
     * @param candidates 候選用戶列表
     * @param excludeUserIds 要排除的用戶ID集合（已互動過的用戶）
     * @return 按相似度排序的推薦用戶列表
     */
    fun recommendUsers(
        currentUser: User, 
        candidates: List<User>, 
        excludeUserIds: Set<String> = emptySet()
    ): List<User> {
        val currentProfile = currentUser.profile ?: return candidates.shuffled()
        
        println("RecommendationService: Starting recommendation for user ${currentUser.id}")
        println("RecommendationService: Candidates count: ${candidates.size}")
        println("RecommendationService: Excluded users count: ${excludeUserIds.size}")
        
        // 過濾掉已經互動過的用戶和當前用戶自己
        val filteredCandidates = candidates.filter { candidate ->
            candidate.id != currentUser.id && 
            candidate.id !in excludeUserIds &&
            candidate.profile != null
        }
        
        println("RecommendationService: Filtered candidates count: ${filteredCandidates.size}")
        
        if (filteredCandidates.isEmpty()) {
            println("RecommendationService: No valid candidates found")
            return emptyList()
        }
        
        // 計算每個候選用戶與當前用戶的相似度
        val scoredUsers = filteredCandidates.mapNotNull { candidate ->
            val candidateProfile = candidate.profile!!
            val similarity = calculateSimilarity(currentProfile, candidateProfile)
            println("RecommendationService: User ${candidate.id} similarity score: $similarity")
            Pair(candidate, similarity)
        }
        
        // 按相似度排序，相似度高的在前面
        val sortedUsers = scoredUsers
            .sortedByDescending { it.second }
            .map { it.first }
            
        println("RecommendationService: Recommendation completed, returning ${sortedUsers.size} users")
        return sortedUsers
    }

    /**
     * 計算兩個用戶的相似度分數
     * @param profile1 用戶1的個人資料
     * @param profile2 用戶2的個人資料
     * @return 相似度分數 (0.0 - 1.0)
     */
    private fun calculateSimilarity(profile1: UserProfile, profile2: UserProfile): Double {
        var totalScore = 0.0
        var totalWeight = 0.0

        // 1. 興趣相似度 (權重: 30%)
        val interestSimilarity = calculateInterestSimilarity(profile1.interests, profile2.interests)
        totalScore += interestSimilarity * 0.3
        totalWeight += 0.3

        // 2. 個性特質相似度 (權重: 25%)
        val personalitySimilarity = calculatePersonalitySimilarity(profile1.personalityTraits, profile2.personalityTraits)
        totalScore += personalitySimilarity * 0.25
        totalWeight += 0.25

        // 3. 地理位置相似度 (權重: 20%)
        val locationSimilarity = calculateLocationSimilarity(profile1, profile2)
        totalScore += locationSimilarity * 0.2
        totalWeight += 0.2

        // 4. 教育背景相似度 (權重: 15%)
        val educationSimilarity = calculateEducationSimilarity(profile1, profile2)
        totalScore += educationSimilarity * 0.15
        totalWeight += 0.15

        // 5. 職業相似度 (權重: 10%)
        val careerSimilarity = calculateCareerSimilarity(profile1, profile2)
        totalScore += careerSimilarity * 0.1
        totalWeight += 0.1

        return if (totalWeight > 0) totalScore / totalWeight else 0.0
    }

    /**
     * 計算興趣相似度
     */
    private fun calculateInterestSimilarity(interests1: List<String>, interests2: List<String>): Double {
        if (interests1.isEmpty() || interests2.isEmpty()) return 0.0

        // 正規化興趣（轉小寫、去空格）
        val normalizedInterests1 = interests1.map { it.trim().lowercase() }
        val normalizedInterests2 = interests2.map { it.trim().lowercase() }

        // 計算共同興趣數量
        val commonInterests = normalizedInterests1.intersect(normalizedInterests2.toSet()).size
        val totalUniqueInterests = (normalizedInterests1 + normalizedInterests2).toSet().size

        // 使用 Jaccard 相似度
        return if (totalUniqueInterests > 0) {
            commonInterests.toDouble() / totalUniqueInterests.toDouble()
        } else 0.0
    }

    /**
     * 計算個性特質相似度
     */
    private fun calculatePersonalitySimilarity(traits1: List<String>, traits2: List<String>): Double {
        if (traits1.isEmpty() || traits2.isEmpty()) return 0.0

        // 正規化特質
        val normalizedTraits1 = traits1.map { it.trim().lowercase() }
        val normalizedTraits2 = traits2.map { it.trim().lowercase() }

        // 計算共同特質數量
        val commonTraits = normalizedTraits1.intersect(normalizedTraits2.toSet()).size
        val totalUniqueTraits = (normalizedTraits1 + normalizedTraits2).toSet().size

        return if (totalUniqueTraits > 0) {
            commonTraits.toDouble() / totalUniqueTraits.toDouble()
        } else 0.0
    }

    /**
     * 計算地理位置相似度
     */
    private fun calculateLocationSimilarity(profile1: UserProfile, profile2: UserProfile): Double {
        // 如果城市相同，給高分
        if (profile1.city.isNotEmpty() && profile2.city.isNotEmpty()) {
            if (profile1.city.trim().lowercase() == profile2.city.trim().lowercase()) {
                // 如果區域也相同，給滿分
                return if (profile1.district.isNotEmpty() && profile2.district.isNotEmpty() &&
                    profile1.district.trim().lowercase() == profile2.district.trim().lowercase()) {
                    1.0
                } else {
                    0.8 // 同城市不同區域
                }
            }
        }
        return 0.0
    }

    /**
     * 計算教育背景相似度
     */
    private fun calculateEducationSimilarity(profile1: UserProfile, profile2: UserProfile): Double {
        var similarity = 0.0
        var factors = 0

        // 學歷相似度
        if (profile1.degree.isNotEmpty() && profile2.degree.isNotEmpty()) {
            similarity += if (profile1.degree.trim().lowercase() == profile2.degree.trim().lowercase()) 1.0 else 0.0
            factors++
        }

        // 學校相似度
        if (profile1.school.isNotEmpty() && profile2.school.isNotEmpty()) {
            similarity += if (profile1.school.trim().lowercase() == profile2.school.trim().lowercase()) 1.0 else 0.0
            factors++
        }

        // 專業相似度
        if (profile1.major.isNotEmpty() && profile2.major.isNotEmpty()) {
            similarity += if (profile1.major.trim().lowercase() == profile2.major.trim().lowercase()) 1.0 else 0.0
            factors++
        }

        return if (factors > 0) similarity / factors else 0.0
    }

    /**
     * 計算職業相似度
     */
    private fun calculateCareerSimilarity(profile1: UserProfile, profile2: UserProfile): Double {
        var similarity = 0.0
        var factors = 0

        // 職位相似度
        if (profile1.position.isNotEmpty() && profile2.position.isNotEmpty()) {
            similarity += if (profile1.position.trim().lowercase() == profile2.position.trim().lowercase()) 1.0 else 0.0
            factors++
        }

        // 公司相似度
        if (profile1.company.isNotEmpty() && profile2.company.isNotEmpty()) {
            similarity += if (profile1.company.trim().lowercase() == profile2.company.trim().lowercase()) 1.0 else 0.0
            factors++
        }

        return if (factors > 0) similarity / factors else 0.0
    }

    /**
     * 為推薦結果加入一些隨機性，避免結果過於固定
     * @param users 推薦用戶列表
     * @param randomFactor 隨機因子 (0.0-1.0)，越高越隨機
     * @return 混合後的用戶列表
     */
    fun addRandomness(users: List<User>, randomFactor: Double = 0.2): List<User> {
        val (topUsers, restUsers) = users.chunked((users.size * (1 - randomFactor)).toInt().coerceAtLeast(1))
            .let { chunks ->
                if (chunks.isNotEmpty()) {
                    chunks[0] to chunks.drop(1).flatten()
                } else {
                    emptyList<User>() to users
                }
            }

        return topUsers + restUsers.shuffled()
    }
}
