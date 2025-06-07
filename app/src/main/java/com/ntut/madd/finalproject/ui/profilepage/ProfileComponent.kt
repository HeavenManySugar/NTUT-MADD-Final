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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.filled.Star
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.ui.graphics.Brush


/** Profile Page 紫色區塊的 Hashtag **/
@Composable
fun HighlightTag(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = Color(0xFFFFC107), // 金黃色
            modifier = Modifier
                .size(16.dp)
                .padding(end = 6.dp)
        )
        Text(
            text = text,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

/** 查看次數、收到愛心、配對成功 **/

@Composable
fun StatCard(
    icon: ImageVector,
    valueText: String,
    labelText: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .padding(vertical = 16.dp, horizontal = 20.dp)
            .width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = valueText,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = labelText,
            fontSize = 14.sp,
            color = Color(0xFF7180AD), // 淡藍紫色（可調整）
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun StatCardRow() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        StatCard(
            icon = Icons.Filled.Visibility,
            valueText = "1,247",
            labelText = "查看次數",
            modifier = Modifier.weight(1f)
        )
        StatCard(
            icon = Icons.Filled.Favorite,
            valueText = "89",
            labelText = "收到愛心",
            modifier = Modifier.weight(1f)
        )
        StatCard(
            icon = Icons.Filled.Star,
            valueText = "23",
            labelText = "配對成功",
            modifier = Modifier.weight(1f)
        )
    }
}

/** 興趣標籤 **/

@Composable
fun InterestTag(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        modifier = modifier
            .background(
                brush = Brush.horizontalGradient(
                    listOf(Color(0xFF667EEA), Color(0xFF764BA2))
                ),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}