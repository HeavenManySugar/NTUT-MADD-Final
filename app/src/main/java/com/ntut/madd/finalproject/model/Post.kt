package com.ntut.madd.finalproject.model

import java.util.Date
import java.util.UUID

data class Post(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val userName: String,
    val userAvatar: String,
    val content: String,
    val imageUrl: String? = null,
    val likes: Int = 0,
    val comments: Int = 0,
    val createdAt: Date = Date()
)
