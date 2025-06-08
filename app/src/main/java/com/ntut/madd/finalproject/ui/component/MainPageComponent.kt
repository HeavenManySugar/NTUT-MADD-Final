package com.ntut.madd.finalproject.ui.component


import android.text.Layout
import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.TextUnit


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
                        color = if (isSelected) Color(0xFF5B72F2) else Color.Gray,
                        fontWeight = FontWeight.Normal
                    )
                },
                alwaysShowLabel = true
            )
        }
    }
}

/** 主畫面上面那塊的東西 **/
@Composable
fun GradientBackgroundBox(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    useGradient: Boolean = true,
    content: @Composable () -> Unit
) {
    val gradientModifier = if (useGradient) {
        modifier
            .fillMaxWidth()
            .drawWithCache {
                val gradient = Brush.linearGradient(
                    colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2)),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height)
                )
                onDrawBehind {
                    drawRect(brush = gradient)
                }
            }
            .padding(vertical = 48.dp)
    } else {
        modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 48.dp)
    }

    Box(
        modifier = gradientModifier,
        contentAlignment = contentAlignment
    ) {
        content()
    }
}

/** 標題 **/

@Composable
fun SectionTitle(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 18.sp,
    iconSize: Dp = 20.dp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(iconSize)
                .padding(end = 4.dp)
        )
        Text(
            text = title,
            fontSize = fontSize,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2D3748)
        )
    }
}