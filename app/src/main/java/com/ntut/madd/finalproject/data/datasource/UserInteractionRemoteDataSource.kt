package com.ntut.madd.finalproject.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.ntut.madd.finalproject.data.model.InteractionType
import com.ntut.madd.finalproject.data.model.UserInteraction
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserInteractionRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val interactionsCollection = firestore.collection(USER_INTERACTIONS_COLLECTION)

    /**
     * 記錄用戶互動行為
     */
    suspend fun recordInteraction(
        userId: String,
        targetUserId: String,
        action: InteractionType,
        sessionId: String = generateSessionId()
    ) {
        try {
            val interaction = UserInteraction(
                userId = userId,
                targetUserId = targetUserId,
                action = action,
                timestamp = System.currentTimeMillis(),
                sessionId = sessionId
            )
            
            interactionsCollection.add(interaction).await()
            println("UserInteractionRemoteDataSource: Recorded interaction: $userId $action $targetUserId")
        } catch (e: Exception) {
            println("UserInteractionRemoteDataSource: Failed to record interaction: ${e.message}")
            // 不拋出例外，避免影響主要功能
        }
    }

    /**
     * 獲取用戶的互動歷史
     */
    suspend fun getUserInteractions(userId: String, limit: Int = 100): List<UserInteraction> {
        return try {
            val query = interactionsCollection
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(limit.toLong())
            
            val documents = query.get().await()
            documents.mapNotNull { document ->
                document.toObject(UserInteraction::class.java)
            }
        } catch (e: Exception) {
            println("UserInteractionRemoteDataSource: Failed to get user interactions: ${e.message}")
            emptyList()
        }
    }

    /**
     * 獲取用戶拒絕的用戶ID列表
     */
    suspend fun getRejectedUserIds(userId: String): Set<String> {
        return try {
            val query = interactionsCollection
                .whereEqualTo("userId", userId)
                .whereEqualTo("action", InteractionType.REJECT.name)
            
            val documents = query.get().await()
            documents.mapNotNull { document ->
                document.toObject(UserInteraction::class.java)?.targetUserId
            }.toSet()
        } catch (e: Exception) {
            println("UserInteractionRemoteDataSource: Failed to get rejected users: ${e.message}")
            emptySet()
        }
    }

    /**
     * 獲取用戶喜歡的用戶ID列表
     */
    suspend fun getApprovedUserIds(userId: String): Set<String> {
        return try {
            val query = interactionsCollection
                .whereEqualTo("userId", userId)
                .whereEqualTo("action", InteractionType.APPROVE.name)
            
            val documents = query.get().await()
            documents.mapNotNull { document ->
                document.toObject(UserInteraction::class.java)?.targetUserId
            }.toSet()
        } catch (e: Exception) {
            println("UserInteractionRemoteDataSource: Failed to get approved users: ${e.message}")
            emptySet()
        }
    }

    private fun generateSessionId(): String {
        return "session_${System.currentTimeMillis()}_${(1000..9999).random()}"
    }

    companion object {
        private const val USER_INTERACTIONS_COLLECTION = "user_interactions"
    }
}
