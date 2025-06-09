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

    suspend fun getDiscoverableUsers(currentUserId: String, limit: Int = 10): List<User> {
        return try {
            val query = usersCollection
                .whereNotEqualTo("ownerId", currentUserId) // Exclude current user
                .whereEqualTo("profile.isProfileComplete", true) // Only complete profiles
                .limit(limit.toLong())
            
            val documents = query.get().await()
            documents.mapNotNull { document ->
                document.toObject(User::class.java)
            }
        } catch (e: Exception) {
            println("UserProfileRemoteDataSource: Error fetching discoverable users: ${e.message}")
            emptyList()
        }
    }

    suspend fun getAllUsers(limit: Int = 50): List<User> {
        return try {
            val query = usersCollection
                .whereEqualTo("profile.isProfileComplete", true)
                .limit(limit.toLong())
            
            val documents = query.get().await()
            documents.mapNotNull { document ->
                document.toObject(User::class.java)
            }
        } catch (e: Exception) {
            println("UserProfileRemoteDataSource: Error fetching all users: ${e.message}")
            emptyList()
        }
    }

    companion object {
        private const val USERS_COLLECTION = "users"
    }
}
