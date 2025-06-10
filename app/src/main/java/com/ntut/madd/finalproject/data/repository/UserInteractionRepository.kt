package com.ntut.madd.finalproject.data.repository

import com.ntut.madd.finalproject.data.datasource.UserInteractionRemoteDataSource
import com.ntut.madd.finalproject.data.model.InteractionType
import com.ntut.madd.finalproject.data.model.UserInteraction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInteractionRepository @Inject constructor(
    private val userInteractionRemoteDataSource: UserInteractionRemoteDataSource,
    private val authRepository: AuthRepository
) {

    /**
     * 記錄用戶喜歡某個用戶
     */
    suspend fun recordApproval(targetUserId: String): Result<Unit> {
        return try {
            val currentUser = authRepository.currentUser
            if (currentUser == null) {
                Result.failure(Exception("User not authenticated"))
            } else {
                userInteractionRemoteDataSource.recordInteraction(
                    userId = currentUser.uid,
                    targetUserId = targetUserId,
                    action = InteractionType.APPROVE
                )
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 記錄用戶拒絕某個用戶
     */
    suspend fun recordRejection(targetUserId: String): Result<Unit> {
        return try {
            val currentUser = authRepository.currentUser
            if (currentUser == null) {
                Result.failure(Exception("User not authenticated"))
            } else {
                userInteractionRemoteDataSource.recordInteraction(
                    userId = currentUser.uid,
                    targetUserId = targetUserId,
                    action = InteractionType.REJECT
                )
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 獲取用戶的互動歷史
     */
    suspend fun getUserInteractions(limit: Int = 100): Result<List<UserInteraction>> {
        return try {
            val currentUser = authRepository.currentUser
            if (currentUser == null) {
                Result.failure(Exception("User not authenticated"))
            } else {
                val interactions = userInteractionRemoteDataSource.getUserInteractions(currentUser.uid, limit)
                Result.success(interactions)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 獲取用戶拒絕的用戶ID集合
     */
    suspend fun getRejectedUserIds(): Result<Set<String>> {
        return try {
            val currentUser = authRepository.currentUser
            if (currentUser == null) {
                Result.failure(Exception("User not authenticated"))
            } else {
                val rejectedIds = userInteractionRemoteDataSource.getRejectedUserIds(currentUser.uid)
                Result.success(rejectedIds)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 獲取用戶喜歡的用戶ID集合
     */
    suspend fun getApprovedUserIds(): Result<Set<String>> {
        return try {
            val currentUser = authRepository.currentUser
            if (currentUser == null) {
                Result.failure(Exception("User not authenticated"))
            } else {
                val approvedIds = userInteractionRemoteDataSource.getApprovedUserIds(currentUser.uid)
                Result.success(approvedIds)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
