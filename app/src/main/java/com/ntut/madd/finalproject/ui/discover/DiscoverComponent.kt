package com.ntut.madd.finalproject.ui.discover

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
import com.google.accompanist.flowlayout.FlowRow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
/** 配對資訊欄 **/
@Composable
fun RoundedWhiteCard(content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .heightIn(min = LocalConfiguration.current.screenHeightDp.dp * 0.6f)
            .padding(24.dp), // 外部 padding
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}

@Composable
fun TopSection(
    name: String,
    location: String,
    jobTitle: String,
    education: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = name,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1C1C1E)
        )
        Spacer(Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InfoItem("📍", location)
            InfoItem("💼", jobTitle)
            InfoItem("🎓", education)
        }
    }
}

@Composable
fun InfoItem(icon: String, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(icon, fontSize = 16.sp)
        Text(text, fontSize = 16.sp, color = Color.DarkGray, fontWeight = FontWeight.Normal)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InterestSection(interests: List<Pair<String, String>>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Interests & Hobbies", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(Modifier.height(12.dp))
        FlowRow(
            mainAxisSpacing = 12.dp,
            crossAxisSpacing = 12.dp
        ) {
            interests.forEach { (emoji, label) ->
                InterestItem(emoji, label)
            }
        }
    }
}

@Composable
fun InterestItem(emoji: String, label: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF2F5F9))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = emoji, fontSize = 16.sp)
        Spacer(Modifier.width(8.dp))
        Text(text = label, fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Normal)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PersonalitySection(traits: List<String>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Personality", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(Modifier.height(12.dp))
        FlowRow(
            mainAxisSpacing = 12.dp,
            crossAxisSpacing = 12.dp
        ) {
            traits.forEach { trait ->
                TraitPill(trait)
            }
        }
    }
}

@Composable
fun TraitPill(text: String) {
    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF7F7FD5), Color(0xFF86A8E7))
    )
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(brush = gradient)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = text, fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
    }
}

/** 接受拒絕 **/
@Composable
fun DecisionButtons(
    onReject: () -> Unit,
    onApprove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(50.dp), // ⬅️ 控制整體高度
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rejected (灰色)
        Button(
            onClick = onReject,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(), // ⬅️ 拉高和 Row 一樣
            shape = RoundedCornerShape(50),
            elevation = ButtonDefaults.buttonElevation(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE0E0E0),
                contentColor = Color.Black
            )
        ) {
            Text("Rejected", fontWeight = FontWeight.Medium, fontSize = 20.sp)
        }

        // Approved (漸層)
        Button(
            onClick = onApprove,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF7F7FD5), Color(0xFF86A8E7))
                    ),
                    shape = RoundedCornerShape(50)
                ),
            shape = RoundedCornerShape(50),
            elevation = ButtonDefaults.buttonElevation(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            )
        ) {
            Text("Approved", fontWeight = FontWeight.Medium, fontSize = 20.sp)
        }
    }
}