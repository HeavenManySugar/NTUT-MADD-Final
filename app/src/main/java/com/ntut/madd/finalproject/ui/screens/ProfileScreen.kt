package com.ntut.madd.finalproject.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ntut.madd.finalproject.model.Post
import com.ntut.madd.finalproject.ui.components.PostCard
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onPostClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // 模拟用户数据
    val user = User(
        id = "current_user",
        name = "張瑞恩",
        avatar = "https://i.pravatar.cc/150?img=1",
        bio = "台北科技大學學生 | 移動應用開發愛好者 | 熱愛編程和設計",
        followers = 128,
        following = 87
    )

    // 模拟用户帖子数据
    val userPosts = listOf(
        Post(
            userId = user.id,
            userName = user.name,
            userAvatar = user.avatar,
            content = "今天完成了期末項目的基本框架，感覺很有成就感！",
            imageUrl = "https://picsum.photos/id/237/500/300",
            likes = 25,
            comments = 5,
            createdAt = Date(System.currentTimeMillis() - 86400000) // 1天前
        ),
        Post(
            userId = user.id,
            userName = user.name,
            userAvatar = user.avatar,
            content = "在圖書館學習Jetpack Compose，這個框架真的很強大！",
            likes = 18,
            comments = 3,
            createdAt = Date(System.currentTimeMillis() - 172800000) // 2天前
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("个人资料") },
                actions = {
                    IconButton(onClick = { /* 设置页面 */ }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "设置"
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 个人资料头部
            item {
                ProfileHeader(
                    user = user,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            // 用户帖子
            items(userPosts) { post ->
                PostCard(
                    post = post,
                    onLikeClick = { /* 点赞功能 */ },
                    onCommentClick = { postId -> onPostClick(postId) }
                )
            }
        }
    }
}

// 用户数据类
data class User(
    val id: String,
    val name: String,
    val avatar: String,
    val bio: String,
    val followers: Int,
    val following: Int
)

@Composable
fun ProfileHeader(
    user: User,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 用户头像
        AsyncImage(
            model = user.avatar,
            contentDescription = "用户头像",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 用户名
        Text(
            text = user.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 用户简介
        Text(
            text = user.bio,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 粉丝和关注数据
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = user.followers.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "粉丝",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = user.following.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "关注",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 编辑资料按钮
        Button(
            onClick = { /* 编辑资料 */ },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("编辑资料")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Divider()
    }
}
