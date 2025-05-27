package com.ntut.madd.finalproject.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ntut.madd.finalproject.ui.components.BottomNavItem
import com.ntut.madd.finalproject.ui.screens.CreatePostScreen
import com.ntut.madd.finalproject.ui.screens.HomeScreen
import com.ntut.madd.finalproject.ui.screens.NotificationScreen
import com.ntut.madd.finalproject.ui.screens.ProfileScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // 记住导航操作
    val navActions = remember(navController) {
        NavActions(navController)
    }

    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier
    ) {
        // 首页
        composable(BottomNavItem.Home.route) {
            HomeScreen(
                onPostClick = { postId ->
                    // 实际应用中，这里应该导航到帖子详情页面
                    // navActions.navigateToPostDetail(postId)
                }
            )
        }

        // 发布页
        composable(BottomNavItem.Create.route) {
            CreatePostScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onPostSubmit = { content, imageUrl ->
                    // 实际应用中，这里应该提交帖子到后端
                    // 然后导航回首页
                    navController.navigate(BottomNavItem.Home.route) {
                        launchSingleTop = true
                        popUpTo(BottomNavItem.Home.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // 通知页
        composable(BottomNavItem.Notification.route) {
            NotificationScreen()
        }

        // 个人页
        composable(BottomNavItem.Profile.route) {
            ProfileScreen(
                onPostClick = { postId ->
                    // 实际应用中，这里应该导航到帖子详情页面
                    // navActions.navigateToPostDetail(postId)
                }
            )
        }
    }
}

/**
 * 导航操作封装类
 */
class NavActions(private val navController: NavHostController) {

    fun navigateToHome() {
        navController.navigate(BottomNavItem.Home.route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }

    fun navigateToCreate() {
        navController.navigate(BottomNavItem.Create.route) {
            launchSingleTop = true
        }
    }

    fun navigateToNotification() {
        navController.navigate(BottomNavItem.Notification.route) {
            launchSingleTop = true
        }
    }

    fun navigateToProfile() {
        navController.navigate(BottomNavItem.Profile.route) {
            launchSingleTop = true
        }
    }

    // 其他可能的导航方法，如导航到帖子详情等
    fun navigateToPostDetail(postId: String) {
        // 未实现，实际应用中应导航到帖子详情页
        // navController.navigate("post_detail/$postId")
    }
}
