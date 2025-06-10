package com.ntut.madd.finalproject.data.repository

import com.ntut.madd.finalproject.data.datasource.ChatRemoteDataSource
import com.ntut.madd.finalproject.data.model.Conversation
import com.ntut.madd.finalproject.data.model.Match
import com.ntut.madd.finalproject.data.model.Message
import com.ntut.madd.finalproject.data.model.User
import javax.inject.Inject
import javax.inject.Singleton

data class ConversationWithUser(
    val conversation: Conversation,
    val otherUser: User?
)

@Singleton
class ChatRepository @Inject constructor(
    private val chatRemoteDataSource: ChatRemoteDataSource,
    private val authRepository: AuthRepository,
    private val userProfileRepository: UserProfileRepository
) {

    /**
     * Create a match and conversation between two users
     */
    suspend fun createMatch(user1Id: String, user2Id: String): Result<Match> {
        return try {
            // Check if match already exists
            val existingMatch = chatRemoteDataSource.getMatch(user1Id, user2Id)
            if (existingMatch != null) {
                Result.success(existingMatch)
            } else {
                val match = chatRemoteDataSource.createMatch(user1Id, user2Id)
                Result.success(match)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Check if two users are matched
     */
    suspend fun areUsersMatched(user1Id: String, user2Id: String): Result<Boolean> {
        return try {
            val isMatched = chatRemoteDataSource.areUsersMatched(user1Id, user2Id)
            Result.success(isMatched)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get all matches for current user
     */
    suspend fun getUserMatches(): Result<List<Match>> {
        return try {
            val currentUser = authRepository.currentUser
            if (currentUser == null) {
                Result.failure(Exception("User not authenticated"))
            } else {
                val matches = chatRemoteDataSource.getUserMatches(currentUser.uid)
                Result.success(matches)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get conversations for current user
     */
    suspend fun getUserConversations(): Result<List<Conversation>> {
        return try {
            val currentUser = authRepository.currentUser
            if (currentUser == null) {
                Result.failure(Exception("User not authenticated"))
            } else {
                val conversations = chatRemoteDataSource.getUserConversations(currentUser.uid)
                Result.success(conversations)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get conversation by ID
     */
    suspend fun getConversation(conversationId: String): Result<Conversation?> {
        return try {
            val conversation = chatRemoteDataSource.getConversation(conversationId)
            Result.success(conversation)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Send a message
     */
    suspend fun sendMessage(conversationId: String, content: String): Result<Message> {
        return try {
            val currentUser = authRepository.currentUser
            if (currentUser == null) {
                Result.failure(Exception("User not authenticated"))
            } else {
                val message = chatRemoteDataSource.sendMessage(conversationId, currentUser.uid, content)
                Result.success(message)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get messages for a conversation
     */
    suspend fun getMessages(conversationId: String, limit: Int = 100): Result<List<Message>> {
        return try {
            val messages = chatRemoteDataSource.getMessages(conversationId, limit)
            Result.success(messages)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Mark messages as read
     */
    suspend fun markMessagesAsRead(conversationId: String): Result<Unit> {
        return try {
            val currentUser = authRepository.currentUser
            if (currentUser == null) {
                Result.failure(Exception("User not authenticated"))
            } else {
                chatRemoteDataSource.markMessagesAsRead(conversationId, currentUser.uid)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get conversation ID for a match between current user and another user
     */
    suspend fun getConversationIdForUser(otherUserId: String): Result<String?> {
        return try {
            val currentUser = authRepository.currentUser
            if (currentUser == null) {
                Result.failure(Exception("User not authenticated"))
            } else {
                val match = chatRemoteDataSource.getMatch(currentUser.uid, otherUserId)
                Result.success(match?.conversationId)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get conversations for current user with other user information
     */
    suspend fun getUserConversationsWithUserInfo(): Result<List<ConversationWithUser>> {
        return try {
            val currentUser = authRepository.currentUser
            if (currentUser == null) {
                Result.failure(Exception("User not authenticated"))
            } else {
                val conversations = chatRemoteDataSource.getUserConversations(currentUser.uid)
                val conversationsWithUsers = conversations.map { conversation ->
                    // Find the other user ID (not current user)
                    val otherUserId = conversation.participants.find { it != currentUser.uid }
                    val otherUser = if (otherUserId != null) {
                        userProfileRepository.getUserProfileById(otherUserId).getOrNull()
                    } else null

                    ConversationWithUser(
                        conversation = conversation,
                        otherUser = otherUser
                    )
                }
                Result.success(conversationsWithUsers)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
