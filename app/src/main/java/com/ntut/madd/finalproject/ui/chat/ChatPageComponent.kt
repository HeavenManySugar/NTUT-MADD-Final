package com.ntut.madd.finalproject.ui.chat

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material.icons.filled.Send
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.TextField
import androidx.compose.material3.*


/** 對方資訊欄 **/
@Composable
fun ChatHeader(
    name: String,
    isOnline: Boolean,
    initials: String,
    onBackClick: () -> Unit,
    onInfoClick: () -> Unit,
    onExitClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(132.dp) // 💥 設定整體高度更高
            .background(Color.White)
            .padding(horizontal = 16.dp), // 保持左右 padding
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF5B72F2),
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF6A88F7)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp
                    )
                    Text(
                        text = if (isOnline) "Online" else "Offline",
                        color = if (isOnline) Color(0xFF3AC27D) else Color.Gray,
                        fontSize = 20.sp
                    )
                }
            }
        }

        Row {
            IconButton(onClick = onInfoClick) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info",
                    tint = Color.Black,
                    modifier = Modifier.size(60.dp)
                )
            }

            IconButton(onClick = onExitClick) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Exit",
                    tint = Color.Black,
                    modifier = Modifier.size(60.dp)
                )
            }
        }
    }
}

/** Chat Bar **/

data class ChatMessage(
    val content: String,
    val timestamp: String,
    val isMe: Boolean,
    val isRead: Boolean = false
)

@Composable
fun ChatBubble(message: ChatMessage) {
    val bubbleColor = if (message.isMe) Brush.horizontalGradient(
        listOf(Color(0xFF8F6FE8), Color(0xFF6A88F7))
    ) else Brush.horizontalGradient(
        listOf(Color.White, Color.White)
    )

    val textColor = if (message.isMe) Color.White else Color.Black

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = if (message.isMe) Arrangement.End else Arrangement.Start
    ) {
        Column(horizontalAlignment = if (message.isMe) Alignment.End else Alignment.Start) {

            // Read + Timestamp 排在泡泡左下
            if (message.isMe) {
                // ✅ 左邊 Read + 時間，右邊是氣泡
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        if (message.isRead) {
                            Text(
                                text = "Read",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                lineHeight = 12.sp
                            )
                        }
                        Text(
                            text = message.timestamp,
                            color = Color.Gray,
                            fontSize = 12.sp,
                            lineHeight = 12.sp
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.Transparent,
                        shadowElevation = 1.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .background(brush = bubbleColor)
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = message.content,
                                color = textColor,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
            else{
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        shadowElevation = 1.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = message.content,
                                color = Color.Black,
                                fontSize = 16.sp
                            )
                        }
                    }

                    // 時間貼泡泡右下角
                    Text(
                        text = message.timestamp,
                        color = Color.Gray,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }
        }
    }
}




/** Iuput Bar **/

@Composable
fun ChatInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 輸入框
        TextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = { Text("Say Something...", color = Color.Gray) },
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            textStyle = TextStyle(fontSize = 16.sp),
            maxLines = 1,
            singleLine = true
        )

        Spacer(modifier = Modifier.width(8.dp))

        // 送出按鈕（右側）
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF3AC27D), Color(0xFF3CB4F1))
                    )
                )
                .clickable { onSendClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "Send",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}