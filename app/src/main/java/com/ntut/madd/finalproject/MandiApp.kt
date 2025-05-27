package com.ntut.madd.finalproject

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ntut.madd.finalproject.navigation.NavGraph
import com.ntut.madd.finalproject.ui.components.BottomNavItem
import com.ntut.madd.finalproject.ui.components.BottomNavigationBar

@Composable
fun MandiApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            // 只在主要导航页面显示底部导航栏
            val currentRoute = currentDestination?.route
            if (currentRoute in listOf(
                    BottomNavItem.Home.route,
                    BottomNavItem.Create.route,
                    BottomNavItem.Notification.route,
                    BottomNavItem.Profile.route
                )
            ) {
                BottomNavigationBar(
                    navController = navController,
                    currentDestination = currentDestination
                )
            }
        }
    ) { paddingValues ->
        NavGraph(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}
