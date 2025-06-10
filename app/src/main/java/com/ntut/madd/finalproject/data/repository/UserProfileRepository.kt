package com.ntut.madd.finalproject.data.repository

import com.ntut.madd.finalproject.data.datasource.UserProfileRemoteDataSource
import com.ntut.madd.finalproject.data.model.User
import com.ntut.madd.finalproject.data.model.UserProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileRepository @Inject constructor(
    private val userProfileRemoteDataSource: UserProfileRemoteDataSource,
    private val authRepository: AuthRepository
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
}
