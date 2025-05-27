package com.ntut.madd.finalproject.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination

// 底部导航栏项目类
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: @Composable () -> Unit,
    val unselectedIcon: @Composable () -> Unit
) {
    object Home : BottomNavItem(
        route = "home",
        title = "首页",
        selectedIcon = { Icon(Icons.Filled.Home, contentDescription = "首页") },
        unselectedIcon = { Icon(Icons.Outlined.Home, contentDescription = "首页") }
    )

    object Create : BottomNavItem(
        route = "create",
        title = "发布",
        selectedIcon = { Icon(Icons.Filled.Add, contentDescription = "发布") },
        unselectedIcon = { Icon(Icons.Outlined.Add, contentDescription = "发布") }
    )

    object Notification : BottomNavItem(
        route = "notification",
        title = "通知",
        selectedIcon = { Icon(Icons.Filled.Notifications, contentDescription = "通知") },
        unselectedIcon = { Icon(Icons.Outlined.Notifications, contentDescription = "通知") }
    )

    object Profile : BottomNavItem(
        route = "profile",
        title = "我的",
        selectedIcon = { Icon(Icons.Filled.AccountCircle, contentDescription = "我的") },
        unselectedIcon = { Icon(Icons.Outlined.AccountCircle, contentDescription = "我的") }
    )
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Create,
        BottomNavItem.Notification,
        BottomNavItem.Profile
    )

    NavigationBar(modifier = modifier) {
        items.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true

            NavigationBarItem(
                icon = { if (selected) item.selectedIcon() else item.unselectedIcon() },
                label = { Text(item.title) },
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        // 避免创建同一目的地的多个副本
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // 避免同一目的地的多个副本
                        launchSingleTop = true
                        // 恢复状态
                        restoreState = true
                    }
                }
            )
        }
    }
}
