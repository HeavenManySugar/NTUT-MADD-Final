package com.ntut.madd.finalproject.ui.profilepage

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.serialization.Serializable

@Serializable
object ProfilePageScreen

data class BottomNavItem(
    val label: String,
    val iconRes: ImageVector,
    val route: String
)

@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem("Discover", Icons.Filled.Favorite, "discover"),
        BottomNavItem("Matches", Icons.Filled.CardGiftcard, "matches"),
        BottomNavItem("Messages", Icons.Filled.Mail, "messages"),
        BottomNavItem("Profile", Icons.Filled.Person, "profile")
    )

    NavigationBar(containerColor = Color.White) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.iconRes,
                        contentDescription = item.label,
                        tint = if (isSelected) Color(0xFF5B72F2) else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (isSelected) Color(0xFF5B72F2) else Color.Gray
                    )
                },
                alwaysShowLabel = true
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    BottomNavBar(
        currentRoute = "profile",
        onNavigate = {} // 預覽中不需要實作跳轉
    )
}