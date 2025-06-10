package com.ntut.madd.finalproject.data.repository

import com.ntut.madd.finalproject.data.datasource.UserProfileRemoteDataSource
import com.ntut.madd.finalproject.data.model.User
import com.ntut.madd.finalproject.data.model.UserProfile
import com.ntut.madd.finalproject.data.service.RecommendationService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileRepository @Inject constructor(
    private val userProfileRemoteDataSource: UserProfileRemoteDataSource,
    private val authRepository: AuthRepository,
    private val recommendationService: RecommendationService,
    private val userInteractionRepository: UserInteractionRepository
) {

    suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            val currentUser = authRepository.currentUser
            println("UserProfileRepository: Current user = $currentUser")
            println("UserProfileRepository: User ID = ${currentUser?.uid}")
            println("UserProfileRepository: Is Anonymous = ${currentUser?.isAnonymous}")
            
            if (currentUser == null) {
                println("UserProfileRepository: ERROR - User not authenticated")
                Result.failure(Exception("User not authenticated"))
            } else {
                println("UserProfileRepository: Attempting to save profile for user ${currentUser.uid}")
                userProfileRemoteDataSource.saveUserProfile(
                    userId = currentUser.uid,
                    profile = profile,
                    email = currentUser.email ?: "",
                    displayName = currentUser.displayName ?: "",
                    isAnonymous = currentUser.isAnonymous
                )
                
                println("UserProfileRepository: Profile save successful")
                Result.success(Unit)
            }
        } catch (e: Exception) {
            println("UserProfileRepository: ERROR - ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(): Result<User?> {
        return try {
            val currentUser = authRepository.currentUser
            if (currentUser == null) {
                Result.failure(Exception("User not authenticated"))
            } else {
                val user = userProfileRemoteDataSource.getUserProfile(currentUser.uid)
                Result.success(user)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            val currentUser = authRepository.currentUser
            if (currentUser == null) {
                Result.failure(Exception("User not authenticated"))
            } else {
                userProfileRemoteDataSource.updateUserProfile(currentUser.uid, profile)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserProfileAndDisplayName(profile: UserProfile, displayName: String): Result<Unit> {
        return try {
            val currentUser = authRepository.currentUser
            if (currentUser == null) {
                Result.failure(Exception("User not authenticated"))
            } else {
                userProfileRemoteDataSource.updateUserProfileAndDisplayName(currentUser.uid, profile, displayName)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDiscoverableUsers(limit: Int = 10): Result<List<User>> {
        return try {
            val currentUser = authRepository.currentUser
            if (currentUser == null) {
                Result.failure(Exception("User not authenticated"))
            } else {
                val users = userProfileRemoteDataSource.getDiscoverableUsers(currentUser.uid, limit)
                Result.success(users)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllUsers(limit: Int = 50): Result<List<User>> {
        return try {
            val users = userProfileRemoteDataSource.getAllUsers(limit)
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRecommendedUsers(limit: Int = 10): Result<List<User>> {
        return try {
            val currentUser = authRepository.currentUser
            if (currentUser == null) {
                println("UserProfileRepository: User not authenticated")
                Result.failure(Exception("User not authenticated"))
            } else {
                println("UserProfileRepository: Getting recommendations for user ${currentUser.uid}")
                
                // 獲取當前用戶資料
                val currentUserProfile = userProfileRemoteDataSource.getUserProfile(currentUser.uid)
                println("UserProfileRepository: Current user profile exists: ${currentUserProfile != null}")
                println("UserProfileRepository: Current user profile complete: ${currentUserProfile?.profile?.isProfileComplete}")
                
                if (currentUserProfile?.profile == null) {
                    println("UserProfileRepository: Current user has no complete profile, falling back to basic recommendations")
                    // 如果當前用戶沒有完整資料，退回到基本的推薦
                    return getDiscoverableUsers(limit)
                }
                
                // 獲取已經互動過的用戶ID（包括喜歡和拒絕的）
                val interactedUserIds = try {
                    val rejectedIds = userInteractionRepository.getRejectedUserIds().getOrElse { emptySet() }
                    val approvedIds = userInteractionRepository.getApprovedUserIds().getOrElse { emptySet() }
                    val combined = rejectedIds + approvedIds
                    println("UserProfileRepository: Excluding ${combined.size} previously interacted users")
                    combined
                } catch (e: Exception) {
                    println("UserProfileRepository: Failed to get interaction history: ${e.message}")
                    emptySet()
                }
                
                // 獲取所有可推薦的用戶（擴大搜索範圍以獲得更好的推薦）
                val allCandidates = userProfileRemoteDataSource.getDiscoverableUsers(currentUser.uid, limit * 3)
                println("UserProfileRepository: Found ${allCandidates.size} candidate users")
                
                if (allCandidates.isEmpty()) {
                    println("UserProfileRepository: No candidate users found")
                    Result.success(emptyList())
                } else {
                    // 使用推薦算法排序，同時排除已互動過的用戶
                    val recommendedUsers = recommendationService.recommendUsers(
                        currentUserProfile, 
                        allCandidates, 
                        interactedUserIds
                    )
                    
                    println("UserProfileRepository: Recommendation service returned ${recommendedUsers.size} users")
                    
                    // 加入一些隨機性，避免結果過於固定
                    val finalUsers = recommendationService.addRandomness(recommendedUsers, 0.15)
                    
                    // 限制返回數量
                    val result = finalUsers.take(limit)
                    println("UserProfileRepository: Returning ${result.size} recommended users")
                    
                    Result.success(result)
                }
            }
        } catch (e: Exception) {
            println("UserProfileRepository: Error in getRecommendedUsers: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * 獲取喜歡當前用戶的用戶資料列表
     */
    suspend fun getUsersWhoLikedMe(): Result<List<User>> {
        return try {
            val currentUser = authRepository.currentUser
            if (currentUser == null) {
                println("UserProfileRepository: User not authenticated")
                Result.failure(Exception("User not authenticated"))
            } else {
                println("UserProfileRepository: Getting users who liked current user ${currentUser.uid}")
                
                // 獲取喜歡當前用戶的用戶ID列表
                val usersWhoLikedMeIds = userInteractionRepository.getUsersWhoLikedMe().getOrElse { 
                    println("UserProfileRepository: Failed to get users who liked me")
                    return Result.success(emptyList())
                }
                
                println("UserProfileRepository: Found ${usersWhoLikedMeIds.size} users who liked current user")
                
                if (usersWhoLikedMeIds.isEmpty()) {
                    return Result.success(emptyList())
                }
                
                // 獲取這些用戶的完整資料
                val userProfiles = mutableListOf<User>()
                for (userId in usersWhoLikedMeIds) {
                    try {
                        val user = userProfileRemoteDataSource.getUserProfile(userId)
                        if (user != null && user.profile != null) {
                            userProfiles.add(user)
                        }
                    } catch (e: Exception) {
                        println("UserProfileRepository: Failed to get profile for user $userId: ${e.message}")
                    }
                }
                
                println("UserProfileRepository: Successfully loaded ${userProfiles.size} profiles of users who liked current user")
                Result.success(userProfiles)
            }
        } catch (e: Exception) {
            println("UserProfileRepository: Error in getUsersWhoLikedMe: ${e.message}")
            Result.failure(e)
        }
    }
}
