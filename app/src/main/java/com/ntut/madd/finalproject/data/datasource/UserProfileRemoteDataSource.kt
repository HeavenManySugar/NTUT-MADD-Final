package com.ntut.madd.finalproject.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ntut.madd.finalproject.data.model.User
import com.ntut.madd.finalproject.data.model.UserProfile
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserProfileRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val usersCollection = firestore.collection(USERS_COLLECTION)

    suspend fun saveUserProfile(userId: String, profile: UserProfile, email: String, displayName: String, isAnonymous: Boolean) {
        println("UserProfileRemoteDataSource: Starting saveUserProfile")
        println("UserProfileRemoteDataSource: userId = $userId")
        println("UserProfileRemoteDataSource: email = $email")
        println("UserProfileRemoteDataSource: isAnonymous = $isAnonymous")
        
        // Check current auth state
        val currentUser = auth.currentUser
        println("UserProfileRemoteDataSource: Current auth user = $currentUser")
        println("UserProfileRemoteDataSource: Current auth user ID = ${currentUser?.uid}")
        println("UserProfileRemoteDataSource: Auth user equals provided userId = ${currentUser?.uid == userId}")
        
        if (currentUser == null) {
            throw Exception("No authenticated user found")
        }
        
        if (currentUser.uid != userId) {
            throw Exception("User ID mismatch: provided=$userId, authenticated=${currentUser.uid}")
        }
        
        val completeProfile = profile.copy(
            isProfileComplete = true,
            profileCompletedAt = System.currentTimeMillis()
        )
        
        val userData = hashMapOf(
            "ownerId" to userId,
            "profile" to completeProfile,
            "email" to email,
            "displayName" to displayName,
            "isAnonymous" to isAnonymous
        )
        
        println("UserProfileRemoteDataSource: Attempting to save to Firestore document: users/$userId")
        println("UserProfileRemoteDataSource: Data to save: $userData")
        
        try {
            usersCollection.document(userId)
                .set(userData)
                .await()
            
            println("UserProfileRemoteDataSource: Save completed successfully")
        } catch (e: Exception) {
            println("UserProfileRemoteDataSource: Save failed with error: ${e.message}")
            println("UserProfileRemoteDataSource: Error cause: ${e.cause}")
            throw e
        }
    }

    suspend fun getUserProfile(userId: String): User? {
        val document = usersCollection.document(userId).get().await()
        return if (document.exists()) {
            document.toObject(User::class.java)
        } else {
            null
        }
    }

    suspend fun updateUserProfile(userId: String, profile: UserProfile) {
        usersCollection.document(userId)
            .update("profile", profile)
            .await()
    }

    suspend fun updateUserProfileAndDisplayName(userId: String, profile: UserProfile, displayName: String) {
        usersCollection.document(userId)
            .update(
                mapOf(
                    "profile" to profile,
                    "displayName" to displayName
                )
            )
            .await()
    }

    suspend fun getDiscoverableUsers(currentUserId: String, limit: Int = 10): List<User> {
        return try {
            println("UserProfileRemoteDataSource: Getting discoverable users for user $currentUserId, limit: $limit")
            
            // 首先檢查總共有多少用戶
            val totalUsersQuery = usersCollection.limit(100)
            val totalDocuments = totalUsersQuery.get().await()
            println("UserProfileRemoteDataSource: Total users in database: ${totalDocuments.size()}")
            
            // 檢查實際的資料結構
            if (totalDocuments.size() > 0) {
                totalDocuments.documents.take(3).forEach { doc ->
                    println("UserProfileRemoteDataSource: Sample document ${doc.id}:")
                    println("UserProfileRemoteDataSource: - ownerId: ${doc.getString("ownerId")}")
                    println("UserProfileRemoteDataSource: - displayName: ${doc.getString("displayName")}")
                    val profile = doc.get("profile") as? Map<*, *>
                    if (profile != null) {
                        println("UserProfileRemoteDataSource: - profile exists: true")
                        println("UserProfileRemoteDataSource: - profile.isProfileComplete: ${profile["isProfileComplete"]}")
                        println("UserProfileRemoteDataSource: - profile.profileComplete: ${profile["profileComplete"]}")
                        println("UserProfileRemoteDataSource: - profile keys: ${profile.keys}")
                    } else {
                        println("UserProfileRemoteDataSource: - profile exists: false")
                    }
                }
            }
            
            // 嘗試不同的查詢條件
            val queries = listOf(
                // 原本的查詢
                Triple("profile.isProfileComplete", true, "isProfileComplete"),
                // 嘗試 profileComplete
                Triple("profile.profileComplete", true, "profileComplete"),
                // 嘗試只檢查 profile 存在
                Triple("profile", null, "profile exists")
            )
            
            var users = emptyList<User>()
            var successfulQuery = ""
            
            for ((field, value, description) in queries) {
                try {
                    println("UserProfileRemoteDataSource: Trying query with $description...")
                    
                    val query = if (value != null) {
                        usersCollection
                            .whereNotEqualTo("ownerId", currentUserId)
                            .whereEqualTo(field, value)
                            .limit(limit.toLong())
                    } else {
                        // 只檢查 profile 欄位存在
                        usersCollection
                            .whereNotEqualTo("ownerId", currentUserId)
                            .limit(limit.toLong())
                    }
                    
                    val documents = query.get().await()
                    val candidateUsers = documents.mapNotNull { document ->
                        try {
                            val user = document.toObject(User::class.java)
                            // 如果是檢查 profile 存在，需要額外過濾
                            if (value == null && user.profile == null) {
                                null
                            } else {
                                user
                            }
                        } catch (e: Exception) {
                            println("UserProfileRemoteDataSource: Failed to parse user document ${document.id}: ${e.message}")
                            null
                        }
                    }
                    
                    println("UserProfileRemoteDataSource: Query '$description' found ${candidateUsers.size} users")
                    
                    if (candidateUsers.isNotEmpty()) {
                        users = candidateUsers
                        successfulQuery = description
                        break
                    }
                } catch (e: Exception) {
                    println("UserProfileRemoteDataSource: Query '$description' failed: ${e.message}")
                }
            }
            
            if (users.isNotEmpty()) {
                println("UserProfileRemoteDataSource: Using query '$successfulQuery', found ${users.size} users")
                users.forEach { user ->
                    println("UserProfileRemoteDataSource: - User: ${user.displayName} (ID: ${user.id}, Profile: ${user.profile != null})")
                }
            } else {
                println("UserProfileRemoteDataSource: No users found with any query method")
                
                // 最後的嘗試：不排除任何用戶，看看到底有什麼
                println("UserProfileRemoteDataSource: Trying final query without exclusions...")
                val allUsersQuery = usersCollection.limit(10)
                val allDocs = allUsersQuery.get().await()
                val allUsers = allDocs.mapNotNull { doc ->
                    try {
                        val user = doc.toObject(User::class.java)
                        println("UserProfileRemoteDataSource: All users - ${user.displayName} (ID: ${user.id}, OwnerID: ${user.ownerId})")
                        if (user.ownerId != currentUserId && user.profile != null) {
                            user
                        } else {
                            null
                        }
                    } catch (e: Exception) {
                        println("UserProfileRemoteDataSource: Failed to parse all users document: ${e.message}")
                        null
                    }
                }
                
                if (allUsers.isNotEmpty()) {
                    println("UserProfileRemoteDataSource: Found ${allUsers.size} users without query restrictions")
                    users = allUsers
                }
            }
            
            users
        } catch (e: Exception) {
            println("UserProfileRemoteDataSource: Error fetching discoverable users: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getAllUsers(limit: Int = 50): List<User> {
        return try {
            // 先嘗試 isProfileComplete
            var query = usersCollection
                .whereEqualTo("profile.isProfileComplete", true)
                .limit(limit.toLong())
            
            var documents = query.get().await()
            var users = documents.mapNotNull { document ->
                document.toObject(User::class.java)
            }
            
            // 如果沒有找到，嘗試 profileComplete
            if (users.isEmpty()) {
                println("UserProfileRemoteDataSource: Trying profileComplete field for getAllUsers")
                query = usersCollection
                    .whereEqualTo("profile.profileComplete", true)
                    .limit(limit.toLong())
                
                documents = query.get().await()
                users = documents.mapNotNull { document ->
                    document.toObject(User::class.java)
                }
            }
            
            // 如果還是沒有找到，返回所有有 profile 的用戶
            if (users.isEmpty()) {
                println("UserProfileRemoteDataSource: Falling back to all users with profiles")
                val allQuery = usersCollection.limit(limit.toLong())
                val allDocuments = allQuery.get().await()
                users = allDocuments.mapNotNull { document ->
                    val user = document.toObject(User::class.java)
                    if (user.profile != null) user else null
                }
            }
            
            users
        } catch (e: Exception) {
            println("UserProfileRemoteDataSource: Error fetching all users: ${e.message}")
            emptyList()
        }
    }

    companion object {
        private const val USERS_COLLECTION = "users"
    }
}
