package com.ntut.madd.finalproject.data.model

import com.google.firebase.firestore.DocumentId

/**
 * Chat conversation data model
 * Represents a conversation between two matched users
 */
data class Conversation(
    @DocumentId val id: String = "",
    val participants: List<String> = emptyList(), // List of user IDs (should always be 2 for matches)
    val createdAt: Long = System.currentTimeMillis(),
    val lastMessageTimestamp: Long = System.currentTimeMillis(),
    val lastMessage: String = "",
    val lastMessageSenderId: String = "",
    val isActive: Boolean = true
)

/**
 * Individual message in a conversation
 */
data class Message(
    @DocumentId val id: String = "",
    val conversationId: String = "",
    val senderId: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val messageType: MessageType = MessageType.TEXT
)

enum class MessageType {
    TEXT,
    IMAGE,
    SYSTEM // For system messages like "You matched!"
}

/**
 * Match data model
 * Represents a mutual like between two users
 */
data class Match(
    @DocumentId val id: String = "",
    val user1Id: String = "",
    val user2Id: String = "",
    val matchedAt: Long = System.currentTimeMillis(),
    val conversationId: String = "", // Reference to the chat conversation
    val isActive: Boolean = true
)
