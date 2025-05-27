package com.ntut.madd.finalproject.viewmodel

import androidx.lifecycle.ViewModel
import com.ntut.madd.finalproject.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    // 存储首页帖子列表的状态
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    // 加载状态
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        // 初始化时加载数据
        loadPosts()
    }

    fun loadPosts() {
        _isLoading.value = true

        // 模拟从后端获取帖子数据
        val mockPosts = listOf(
            Post(
                userId = "user1",
                userName = "張瑞恩",
                userAvatar = "https://i.pravatar.cc/150?img=1",
                content = "今天在台北科技大學上課，學到了很多關於移動應用開發的知識！",
                imageUrl = "https://picsum.photos/id/1/500/300",
                likes = 42,
                comments = 8
            ),
            Post(
                userId = "user2",
                userName = "林小美",
                userAvatar = "https://i.pravatar.cc/150?img=5",
                content = "分享一下我的期末專案進度，已經完成了基本UI設計！",
                imageUrl = "https://picsum.photos/id/20/500/300",
                likes = 28,
                comments = 5
            ),
            Post(
                userId = "user3",
                userName = "王大明",
                userAvatar = "https://i.pravatar.cc/150?img=8",
                content = "有誰知道Jetpack Compose的Navigation怎麼用嗎？有點搞不懂...",
                likes = 15,
                comments = 21
            ),
            Post(
                userId = "user4",
                userName = "陳小華",
                userAvatar = "https://i.pravatar.cc/150?img=10",
                content = "終於解決了那個煩人的bug！花了我三天時間，但學到很多。",
                imageUrl = "https://picsum.photos/id/30/500/300",
                likes = 56,
                comments = 12
            )
        )

        _posts.value = mockPosts
        _isLoading.value = false
    }

    fun likePost(postId: String) {
        val updatedPosts = _posts.value.map { post ->
            if (post.id == postId) {
                post.copy(likes = post.likes + 1)
            } else {
                post
            }
        }
        _posts.value = updatedPosts
    }
}
