package com.ntut.madd.finalproject.ui.matches

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.clickable

/** Upper Matching Page **/

@Composable
fun MyMatchesStats(
    newMatches: Int,
    totalLikes: Int,
    superLikes: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "My Matches",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C2C2C)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "People who liked you",
            fontSize = 14.sp,
            color = Color(0xFF888888)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem(count = newMatches, label = "New Matches")
            StatItem(count = totalLikes, label = "Total Likes")
            StatItem(count = superLikes, label = "Super Likes")
        }
    }
}

@Composable
fun StatItem(count: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count.toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF8A94F4)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF444444)
        )
    }
}

/** New Matches **/

@Composable
fun MatchCard(
    initials: String,
    name: String,
    age: Int,
    city: String,
    isOnline: Boolean,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .width(120.dp)
            .padding(4.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(52.dp), // 確保外層Box大小固定，避免對齊偏差
                contentAlignment = Alignment.TopEnd
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(128.dp)
                        .border(
                            width = 2.dp,
                            color = Color(0xFFC8D2F6),
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                ) {
                    Text(
                        text = initials,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF667EEA)
                    )
                }

                if (isOnline) {
                    Box(
                        modifier = Modifier
                            .offset(x = 18.dp, y = (-8).dp)
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF38C976))
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color(0xFF2D3748),
                textAlign = TextAlign.Center
            )
            Text(
                text = "${age}y",
                fontSize = 12.sp,
                color = Color(0xFF718096),
                textAlign = TextAlign.Center
            )
            Text(
                text = city,
                fontSize = 12.sp,
                color = Color(0xFF718096),
                textAlign = TextAlign.Center
            )
        }
    }
}

data class MatchProfile(
    val initials: String,
    val name: String,
    val age: Int,
    val city: String,
    val isOnline: Boolean
)

@Composable
fun MatchesSection(
    matches: List<MatchProfile>,
    users: List<com.ntut.madd.finalproject.data.model.User>,
    onUserClick: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(matches.size) { index ->
            val match = matches[index]
            val user = users.getOrNull(index)
            MatchCard(
                initials = match.initials,
                name = match.name,
                age = match.age,
                city = match.city,
                isOnline = match.isOnline,
                onClick = { 
                    user?.let { onUserClick(it.id) }
                }
            )
        }
    }
}

/** People Who Likes Me **/

data class MatchRequest(
    val initials: String,
    val name: String,
    val age: Int,
    val city: String,
    val timeAgo: String
)

@Composable
fun MatchRequestCard(
    request: MatchRequest,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onUserClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            // Clickable profile area
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onUserClick() }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .border(2.dp, Color(0xFFCBD5E0), CircleShape)
                        .clip(CircleShape)
                ) {
                    Text(
                        text = request.initials,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF4C5C7A)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = request.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2D3748)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = request.age.toString(),
                            fontSize = 14.sp,
                            color = Color(0xFF718096)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "📍 ${request.city}",
                            fontSize = 13.sp,
                            color = Color(0xFF718096)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = request.timeAgo,
                            fontSize = 13.sp,
                            color = Color(0xFF5B72F2)
                        )
                    }
                }
            }

            // Action buttons
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF6B6B))
                        .clickable { onReject() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("X", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF7EDE92))
                        .clickable { onAccept() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("💖", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun MatchRequestList(requests: List<MatchRequest>) {
    LazyColumn {
        items(requests) { request ->
            MatchRequestCard(
                request = request,
                onAccept = { println("Accepted ${request.name}") },
                onReject = { println("Rejected ${request.name}") }
            )
        }
    }
}
