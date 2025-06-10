package com.ntut.madd.finalproject.ui.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import com.ntut.madd.finalproject.data.model.User
import com.ntut.madd.finalproject.data.model.UserProfile


/** Matching Information **/
@Composable
fun RoundedWhiteCard(content: @Composable ColumnScope.() -> Unit) {
    val scrollState = rememberScrollState()
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .heightIn(
                min = LocalConfiguration.current.screenHeightDp.dp * 0.4f,
                max = LocalConfiguration.current.screenHeightDp.dp * 0.7f
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(24.dp), // 內部 padding
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}

// Header Component
@Composable
fun UserProfileHeader(displayName: String) {
    Text(
        text = displayName.ifEmpty { "Anonymous User" },
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF1C1C1E)
    )
}



// Career Information Card
@Composable
fun CareerCard(profile: UserProfile) {
    if (profile.position.isNotEmpty() || profile.company.isNotEmpty()) {
        ProfileCard(
            title = "Career",
            backgroundColor = Color(0xFFF5F5DC),
            icon = "💼"
        ) {
            if (profile.position.isNotEmpty()) {
                InfoRow("💼", "Position", profile.position)
            }
            if (profile.company.isNotEmpty()) {
                InfoRow("🏢", "Company", profile.company)
            }
        }
    }
}

// Education Information Card
@Composable
fun EducationCard(profile: UserProfile) {
    if (profile.degree.isNotEmpty() || profile.school.isNotEmpty() || profile.major.isNotEmpty()) {
        ProfileCard(
            title = "Education",
            backgroundColor = Color(0xFFFFF8DC),
            icon = "🎓"
        ) {
            if (profile.degree.isNotEmpty()) {
                InfoRow("🎓", "Degree", profile.degree)
            }
            if (profile.school.isNotEmpty()) {
                InfoRow("🏫", "School", profile.school)
            }
            if (profile.major.isNotEmpty()) {
                InfoRow("📚", "Major", profile.major)
            }
        }
    }
}

// About Me Card
@Composable
fun AboutMeCard(profile: UserProfile) {
    if (profile.aboutMe.isNotEmpty()) {
        ProfileCard(
            title = "About Me",
            backgroundColor = Color(0xFFFFF0F5),
            icon = "💭"
        ) {
            Text(
                text = profile.aboutMe,
                fontSize = 14.sp,
                color = Color(0xFF666666),
                lineHeight = 20.sp
            )
        }
    }
}

// Looking For Card
@Composable
fun LookingForCard(profile: UserProfile) {
    if (profile.lookingFor.isNotEmpty()) {
        ProfileCard(
            title = "Looking For",
            backgroundColor = Color(0xFFE6E6FA),
            icon = "🎯"
        ) {
            Text(
                text = profile.lookingFor,
                fontSize = 14.sp,
                color = Color(0xFF666666),
                lineHeight = 20.sp
            )
        }
    }
}

// Reusable Profile Card Component
@Composable
fun ProfileCard(
    title: String,
    backgroundColor: Color,
    icon: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = icon,
                    fontSize = 18.sp
                )
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1C1C1E)
                )
            }
            
            content()
        }
    }
}

// Legacy TopSection for backward compatibility
@Composable
fun TopSection(user: User) {
    UserProfileHeader(user.displayName)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        val profile = user.profile ?: UserProfile()
        
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            InfoItem("📍", if (profile.city.isNotEmpty() && profile.district.isNotEmpty()) {
                "${profile.district}, ${profile.city}"
            } else if (profile.city.isNotEmpty()) {
                profile.city
            } else {
                "Location not specified"
            })
        }
        
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            InfoItem("💼", profile.position.ifEmpty { "Position not specified" })
        }
        
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            InfoItem("🎓", if (profile.degree.isNotEmpty()) {
                profile.degree
            } else if (profile.school.isNotEmpty()) {
                profile.school
            } else {
                "Education not specified"
            })
        }
    }
}

@Composable
fun InfoRow(
    icon: String,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            fontSize = 16.sp,
            modifier = Modifier.width(24.dp)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = "$label:",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF333333),
            modifier = Modifier.weight(0.4f)
        )
        
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color(0xFF666666),
            modifier = Modifier.weight(0.6f)
        )
    }
}

@Composable
fun InfoItem(icon: String, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(icon, fontSize = 16.sp)
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color.DarkGray,
            fontWeight = FontWeight.Normal,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InterestSection(interests: List<String>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Interests & Hobbies", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(Modifier.height(12.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            interests.forEach { interest ->
                InterestItem(interest)
            }
        }
    }
}

@Composable
fun InterestItem(text: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF2F5F9))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Normal)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PersonalitySection(traits: List<String>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Personality", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(Modifier.height(12.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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

/** Like & Dislike Button **/
@Composable
fun DecisionButtons(
    onDislike: () -> Unit,
    onLike: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(50.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onDislike,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            shape = RoundedCornerShape(50),
            elevation = ButtonDefaults.buttonElevation(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE0E0E0),
                contentColor = Color.Black
            )
        ) {
            Text("Dislike", fontWeight = FontWeight.Medium, fontSize = 20.sp)
        }
        Button(
            onClick = onLike,
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
            Text("Like", fontWeight = FontWeight.Medium, fontSize = 20.sp)
        }
    }
}