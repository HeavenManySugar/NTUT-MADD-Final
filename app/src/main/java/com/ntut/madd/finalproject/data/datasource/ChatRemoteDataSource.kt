package com.ntut.madd.finalproject.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.ntut.madd.finalproject.data.model.Conversation
import com.ntut.madd.finalproject.data.model.Match
import com.ntut.madd.finalproject.data.model.Message
import com.ntut.madd.finalproject.data.model.MessageType
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val conversationsCollection = firestore.collection(CONVERSATIONS_COLLECTION)
    private val messagesCollection = firestore.collection(MESSAGES_COLLECTION)
    private val matchesCollection = firestore.collection(MATCHES_COLLECTION)

    /**
     * Create a new conversation between two users
     */
    suspend fun createConversation(user1Id: String, user2Id: String): String {
        println("ChatRemoteDataSource: Creating conversation between $user1Id and $user2Id")
        val conversation = Conversation(
            participants = listOf(user1Id, user2Id).sorted(), // Sort for consistency
            createdAt = System.currentTimeMillis(),
            lastMessageTimestamp = System.currentTimeMillis(),
            lastMessage = "You matched! Start chatting now.",
            lastMessageSenderId = "system",
            isActive = true
        )
        
        println("ChatRemoteDataSource: Conversation object: $conversation")
        val documentRef = conversationsCollection.add(conversation).await()
        println("ChatRemoteDataSource: Created conversation with ID: ${documentRef.id}")
        
        // TODO: Add initial system message when permissions are fixed
        // Currently commented out due to Firestore permissions issue
        /*
        val systemMessage = Message(
            conversationId = documentRef.id,
            senderId = "system",
            content = "🎉 You matched! Start chatting now.",
            messageType = MessageType.SYSTEM
        )
        messagesCollection.add(systemMessage).await()
        */
        
        return documentRef.id
    }

    /**
     * Create a match record and conversation
     */
    suspend fun createMatch(user1Id: String, user2Id: String): Match {
        // Create conversation first
        val conversationId = createConversation(user1Id, user2Id)
        
        val match = Match(
            user1Id = minOf(user1Id, user2Id), // Consistent ordering
            user2Id = maxOf(user1Id, user2Id),
            matchedAt = System.currentTimeMillis(),
            conversationId = conversationId
        )
        
        val documentRef = matchesCollection.add(match).await()
        return match.copy(id = documentRef.id)
    }

    /**
     * Check if two users are already matched
     */
    suspend fun areUsersMatched(user1Id: String, user2Id: String): Boolean {
        val sortedIds = listOf(user1Id, user2Id).sorted()
        val query = matchesCollection
            .whereEqualTo("user1Id", sortedIds[0])
            .whereEqualTo("user2Id", sortedIds[1])
            .whereEqualTo("active", true)
        
        val documents = query.get().await()
        return !documents.isEmpty
    }

    /**
     * Get existing match between two users
     */
    suspend fun getMatch(user1Id: String, user2Id: String): Match? {
        val sortedIds = listOf(user1Id, user2Id).sorted()
        val query = matchesCollection
            .whereEqualTo("user1Id", sortedIds[0])
            .whereEqualTo("user2Id", sortedIds[1])
            .whereEqualTo("active", true)
        
        val documents = query.get().await()
        return if (documents.isEmpty) {
            null
        } else {
            documents.first().toObject(Match::class.java)
        }
    }

    /**
     * Get all matches for a user
     */
    suspend fun getUserMatches(userId: String): List<Match> {
        val query1 = matchesCollection
            .whereEqualTo("user1Id", userId)
            .whereEqualTo("active", true)
        
        val query2 = matchesCollection
            .whereEqualTo("user2Id", userId)
            .whereEqualTo("active", true)
        
        val results1 = query1.get().await()
        val results2 = query2.get().await()
        
        val matches = mutableListOf<Match>()
        results1.forEach { doc ->
            matches.add(doc.toObject(Match::class.java))
        }
        results2.forEach { doc ->
            matches.add(doc.toObject(Match::class.java))
        }
        
        return matches.sortedByDescending { it.matchedAt }
    }

    /**
     * Get conversations for a user
     */
    suspend fun getUserConversations(userId: String): List<Conversation> {
        try {
            println("ChatRemoteDataSource: Getting conversations for user $userId")
            
            // First try without active filter to see if conversations exist at all
            val queryWithoutActive = conversationsCollection
                .whereArrayContains("participants", userId)
                .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)
            
            val documentsWithoutActive = queryWithoutActive.get().await()
            println("ChatRemoteDataSource: Found ${documentsWithoutActive.size()} total conversation documents (ignoring active)")
            
            // Now try with active filter
            val query = conversationsCollection
                .whereArrayContains("participants", userId)
                .whereEqualTo("active", true)
                .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)
            
            val documents = query.get().await()
            println("ChatRemoteDataSource: Found ${documents.size()} conversation documents with active=true")
            
            // Log details of all conversations for debugging
            documentsWithoutActive.forEachIndexed { index, doc ->
                val data = doc.data
                println("ChatRemoteDataSource: Document $index: ${doc.id}")
                println("ChatRemoteDataSource: participants: ${data["participants"]}")
                println("ChatRemoteDataSource: active: ${data["active"]}")
                println("ChatRemoteDataSource: lastMessage: ${data["lastMessage"]}")
            }
            
            val conversations = documents.mapNotNull { doc ->
                try {
                    val conversation = doc.toObject(Conversation::class.java)
                    println("ChatRemoteDataSource: Parsed conversation ${conversation.id} with participants ${conversation.participants}")
                    conversation
                } catch (e: Exception) {
                    println("ChatRemoteDataSource: Failed to parse conversation ${doc.id}: ${e.message}")
                    null
                }
            }
            
            println("ChatRemoteDataSource: Successfully parsed ${conversations.size} conversations")
            return conversations
        } catch (e: Exception) {
            println("ChatRemoteDataSource: Exception in getUserConversations: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    /**
     * Get conversation by ID
     */
    suspend fun getConversation(conversationId: String): Conversation? {
        val document = conversationsCollection.document(conversationId).get().await()
        return if (document.exists()) {
            document.toObject(Conversation::class.java)
        } else {
            null
        }
    }

    /**
     * Send a message in a conversation
     */
    suspend fun sendMessage(conversationId: String, senderId: String, content: String): Message {
        try {
            println("ChatRemoteDataSource: Attempting to send message - conversationId: $conversationId, senderId: $senderId")
            
            val message = Message(
                conversationId = conversationId,
                senderId = senderId,
                content = content,
                timestamp = System.currentTimeMillis()
            )
            
            val documentRef = messagesCollection.add(message).await()
            val messageWithId = message.copy(id = documentRef.id)
            
            println("ChatRemoteDataSource: Message sent successfully with ID: ${documentRef.id}")
            
            // Update conversation's last message
            conversationsCollection.document(conversationId).update(
                mapOf(
                    "lastMessage" to content,
                    "lastMessageSenderId" to senderId,
                    "lastMessageTimestamp" to System.currentTimeMillis()
                )
            ).await()
            
            println("ChatRemoteDataSource: Conversation updated successfully")
            return messageWithId
        } catch (e: Exception) {
            println("ChatRemoteDataSource: Failed to send message: ${e.message}")
            throw e
        }
    }

    /**
     * Get messages for a conversation
     */
    suspend fun getMessages(conversationId: String, limit: Int = 100): List<Message> {
        val query = messagesCollection
            .whereEqualTo("conversationId", conversationId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(limit.toLong())
        
        val documents = query.get().await()
        return documents.mapNotNull { doc ->
            doc.toObject(Message::class.java)
        }.reversed() // Reverse to get chronological order
    }

    /**
     * Get messages for a conversation with real-time updates
     */
    fun getMessagesFlow(conversationId: String, limit: Int = 100): kotlinx.coroutines.flow.Flow<List<Message>> {
        return messagesCollection
            .whereEqualTo("conversationId", conversationId)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .limit(limit.toLong())
            .dataObjects()
    }

    /**
     * Mark messages as read
     */
    suspend fun markMessagesAsRead(conversationId: String, userId: String) {
        val query = messagesCollection
            .whereEqualTo("conversationId", conversationId)
            .whereNotEqualTo("senderId", userId)
            .whereEqualTo("isRead", false)
        
        val documents = query.get().await()
        val batch = firestore.batch()
        
        documents.forEach { doc ->
            batch.update(doc.reference, "isRead", true)
        }
        
        batch.commit().await()
    }

    companion object {
        private const val CONVERSATIONS_COLLECTION = "conversations"
        private const val MESSAGES_COLLECTION = "messages"
        private const val MATCHES_COLLECTION = "matches"
    }
}
