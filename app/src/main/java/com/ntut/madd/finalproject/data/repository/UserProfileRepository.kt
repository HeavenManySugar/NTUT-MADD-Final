package com.ntut.madd.finalproject.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.ntut.madd.finalproject.data.model.User
import com.ntut.madd.finalproject.data.model.UserProfile
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) {
    private val usersCollection = firestore.collection("users")

    suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            val currentUser = authRepository.currentUser
            if (currentUser == null) {
                Result.failure(Exception("User not authenticated"))
            } else {
                val completeProfile = profile.copy(
                    isProfileComplete = true,
                    profileCompletedAt = System.currentTimeMillis()
                )
                
                usersCollection.document(currentUser.uid)
                    .set(hashMapOf(
                        "profile" to completeProfile,
                        "email" to (currentUser.email ?: ""),
                        "displayName" to (currentUser.displayName ?: ""),
                        "isAnonymous" to currentUser.isAnonymous
                    ))
                    .await()
                
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(): Result<User?> {
        return try {
            val currentUser = authRepository.currentUser
            if (currentUser == null) {
                Result.failure(Exception("User not authenticated"))
            } else {
                val document = usersCollection.document(currentUser.uid).get().await()
                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    Result.success(user)
                } else {
                    Result.success(null)
                }
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
                usersCollection.document(currentUser.uid)
                    .update("profile", profile)
                    .await()
                
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
