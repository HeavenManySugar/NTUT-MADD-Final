package com.ntut.madd.finalproject.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.*

// 通知数据类
data class Notification(
    val id: String,
    val userId: String,
    val userName: String,
    val userAvatar: String,
    val type: NotificationType,
    val time: Date,
    val content: String? = null
)

// 通知类型
enum class NotificationType {
    LIKE, COMMENT, FOLLOW
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier
) {
    // 模拟通知数据
    val notifications = listOf(
        Notification(
            id = "1",
            userId = "user1",
            userName = "張瑞恩",
            userAvatar = "https://i.pravatar.cc/150?img=1",
            type = NotificationType.LIKE,
            time = Date(System.currentTimeMillis() - 3600000), // 1小时前
            content = "今天在台北科技大學上課，學到了很多關於移動應用開發的知識！"
        ),
        Notification(
            id = "2",
            userId = "user2",
            userName = "林小美",
            userAvatar = "https://i.pravatar.cc/150?img=5",
            type = NotificationType.COMMENT,
            time = Date(System.currentTimeMillis() - 7200000), // 2小时前
            content = "这个项目看起来很有趣！我也想参加！"
        ),
        Notification(
            id = "3",
            userId = "user3",
            userName = "王大明",
            userAvatar = "https://i.pravatar.cc/150?img=8",
            type = NotificationType.FOLLOW,
            time = Date(System.currentTimeMillis() - 86400000) // 1天前
        )
    )

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("通知") }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "暂无通知",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(notifications) { notification ->
                    NotificationItem(
                        notification = notification,
                        dateFormat = dateFormat,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Divider()
                }
            }
        }
    }
}

@Composable
fun NotificationItem(
    notification: Notification,
    dateFormat: SimpleDateFormat,
    modifier: Modifier = Modifier
) {
    // 根据通知类型确定图标和文字
    val (icon, iconTint, actionText) = when (notification.type) {
        NotificationType.LIKE -> Triple(
            Icons.Default.ThumbUp,
            Color(0xFFE91E63),
            "赞了你的帖子"
        )
        NotificationType.COMMENT -> Triple(
            Icons.Outlined.ChatBubble,
            Color(0xFF2196F3),
            "评论了你的帖子"
        )
        NotificationType.FOLLOW -> Triple(
            Icons.Default.Person,
            Color(0xFF4CAF50),
            "关注了你"
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 用户头像
        AsyncImage(
            model = notification.userAvatar,
            contentDescription = "用户头像",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        // 通知内容
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notification.userName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = actionText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (notification.content != null) {
                Text(
                    text = notification.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = 1,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Text(
                text = dateFormat.format(notification.time),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // 通知类型图标
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
    }
}
