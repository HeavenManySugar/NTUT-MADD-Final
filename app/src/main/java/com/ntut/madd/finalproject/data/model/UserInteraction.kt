package com.ntut.madd.finalproject.data.model

import com.google.firebase.firestore.DocumentId

/**
 * 用戶互動記錄數據模型
 * 記錄用戶對其他用戶的喜歡/不喜歡行為，用於改善推薦算法
 */
data class UserInteraction(
    @DocumentId val id: String = "",
    val userId: String = "", // 執行操作的用戶ID
    val targetUserId: String = "", // 被操作的目標用戶ID
    val action: InteractionType = InteractionType.UNKNOWN,
    val timestamp: Long = System.currentTimeMillis(),
    val sessionId: String = "", // 會話ID，可用於分析用戶行為模式
)

enum class InteractionType {
    APPROVE, // 用戶點擊"喜歡"
    REJECT,  // 用戶點擊"不喜歡"
    VIEW,    // 用戶查看了個人資料（可選用於分析）
    UNKNOWN
}
