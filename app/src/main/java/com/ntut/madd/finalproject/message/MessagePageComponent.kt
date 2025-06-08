package com.ntut.madd.finalproject.messages


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults

/** 搜尋欄、選取訊息 **/

@Composable
fun SearchBarWithFilters(
    query: String,
    onQueryChange: (String) -> Unit,
    selectedFilter: String,
    onFilterSelect: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        SearchInputField(query = query, onQueryChange = onQueryChange)
        Spacer(modifier = Modifier.height(12.dp))
        FilterChips(selected = selectedFilter, onSelect = onFilterSelect)
    }
}
@Composable
fun SearchInputField(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search Conversations...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        shape = RoundedCornerShape(20.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFF2F2F2),
            focusedContainerColor = Color(0xFFF2F2F2),
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            cursorColor = Color.Black
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
@Composable
fun FilterChips(
    selected: String,
    onSelect: (String) -> Unit
) {
    val filters = listOf("All", "Unread", "Online")

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        filters.forEach { label ->
            val isSelected = selected == label
            FilterChip(
                text = label,
                selected = isSelected,
                onClick = { onSelect(label) }
            )
        }
    }
}
@Composable
fun FilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) Brush.horizontalGradient(
        listOf(Color(0xFF6A88F7), Color(0xFF9F6DE9))
    ) else Brush.linearGradient(listOf(Color(0xFFECECEC), Color(0xFFECECEC)))

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(brush = backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

/** 灰色線線 **/

@Composable
fun TopFadeOverlay(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(Color(0xFFE9ECEF))
    )
}

/** 朋友欄位 **/

@Composable
fun MessagePreviewCard(
    initials: String,
    userName: String, // 👈 新增使用者名稱
    message: String,
    timeAgo: String,
    isOnline: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp), // 放大 padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 頭像 + 線上狀態
        Box(modifier = Modifier.size(56.dp)) { // 放大頭像
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            listOf(Color(0xFF6A88F7), Color(0xFF9F6DE9))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp // 放大字體
                )
            }

            if (isOnline) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .background(Color.Green, shape = CircleShape)
                        .border(2.dp, Color.White, shape = CircleShape)
                        .align(Alignment.BottomEnd)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = userName, // 👈 顯示名稱
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message,
                color = Color.DarkGray,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = timeAgo,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

data class MessagePreview(
    val chatId: String,
    val initials: String,
    val userName: String,
    val lastMessage: String,
    val timeAgo: String,
    val isOnline: Boolean
)

@Composable
fun MessageListWithSeparators(
    messages: List<MessagePreview>,
    onChatClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        messages.forEachIndexed { index, msg ->
            item {
                if (index != 0) {
                    TopFadeOverlay()
                }

                MessagePreviewCard(
                    initials = msg.initials,
                    userName = msg.userName,
                    message = msg.lastMessage,
                    timeAgo = msg.timeAgo,
                    isOnline = msg.isOnline,
                    onClick = {} // or onChatClick(msg.chatId)
                )
            }
        }
    }
}