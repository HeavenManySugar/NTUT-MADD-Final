package com.ntut.madd.finalproject.data.repository

import com.google.firebase.auth.FirebaseUser
import com.ntut.madd.finalproject.data.datasource.AuthRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) {
    val currentUser: FirebaseUser? get() = authRemoteDataSource.currentUser
    val currentUserIdFlow: Flow<String?> get() = authRemoteDataSource.currentUserIdFlow

    suspend fun createGuestAccount() {
        authRemoteDataSource.createGuestAccount()
    }

    suspend fun signIn(email: String, password: String) {
        authRemoteDataSource.signIn(email, password)
    }

    suspend fun signUp(email: String, password: String) {
        val currentUser = authRemoteDataSource.currentUser
        
        if (currentUser != null && currentUser.isAnonymous) {
            // 如果已經有匿名帳號，則連結帳號
            authRemoteDataSource.linkAccount(email, password)
        } else {
            // 如果沒有匿名帳號，則創建新帳號
            authRemoteDataSource.createAccount(email, password)
        }
    }

    fun signOut() {
        authRemoteDataSource.signOut()
    }

    suspend fun deleteAccount() {
        authRemoteDataSource.deleteAccount()
    }
}