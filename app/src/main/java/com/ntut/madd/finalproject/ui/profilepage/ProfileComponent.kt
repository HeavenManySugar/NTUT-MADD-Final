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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import com.ntut.madd.finalproject.R
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.heightIn
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import com.ntut.madd.finalproject.ui.component.SectionTitle


/** Profile Page Hashtag **/
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
            tint = Color(0xFFFFC107),
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

/** Avatar **/

@Composable
fun InitialAvatar(
    initial: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(Color(0xFFF2F5FF))
            .border(
                width = 1.dp,
                color = Color(0xFFD7DFFB),
                shape = CircleShape
            )
    ) {
        Text(
            text = initial,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5B72F2)
        )
    }
}

/** Match,Hearts,Success **/

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
            color = Color(0xFF7180AD),
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun StatCardRow(
    modifier: Modifier = Modifier
)  {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        StatCard(
            icon = Icons.Filled.Visibility,
            valueText = "1,247",
            labelText = stringResource(R.string.view_times),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            icon = Icons.Filled.Favorite,
            valueText = "89",
            labelText = stringResource(R.string.get_heart),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            icon = Icons.Filled.Star,
            valueText = "23",
            labelText = stringResource(R.string.match_times),
            modifier = Modifier.weight(1f)
        )
    }
}

/** Hobby Label **/

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

/** Hobby Field **/

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InterestTagSection(tags: List<String>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        tags.forEach { tag ->
            InterestTag(text = tag)
        }
    }
}

/** Profile Information Cards **/

@Composable
fun ProfileCard(
    title: String,
    backgroundColor: Color,
    icon: ImageVector,
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
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF5B72F2),
                    modifier = Modifier.size(20.dp)
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
            color = Color(0xFF666666),
            modifier = Modifier.width(80.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = value,
            fontSize = 14.sp,
            color = Color(0xFF1C1C1E),
            fontWeight = FontWeight.Normal
        )
    }
}

/** Location Card **/
@Composable
fun LocationCard(city: String, district: String) {
    if (city.isNotEmpty() || district.isNotEmpty()) {
        ProfileCard(
            title = "Location",
            backgroundColor = Color(0xFFE3F2FD),
            icon = Icons.Filled.LocationOn
        ) {
            if (city.isNotEmpty() && district.isNotEmpty()) {
                InfoRow("📍", "Address", "$district, $city")
            } else if (city.isNotEmpty()) {
                InfoRow("📍", "City", city)
            } else if (district.isNotEmpty()) {
                InfoRow("📍", "District", district)
            }
        }
    }
}

/** Career Card **/
@Composable
fun CareerCard(position: String, company: String) {
    if (position.isNotEmpty() || company.isNotEmpty()) {
        ProfileCard(
            title = "Career",
            backgroundColor = Color(0xFFF5F5DC),
            icon = Icons.Filled.Business
        ) {
            if (position.isNotEmpty()) {
                InfoRow("💼", "Position", position)
            }
            if (company.isNotEmpty()) {
                InfoRow("🏢", "Company", company)
            }
        }
    }
}

/** Education Card **/
@Composable
fun EducationCard(degree: String, school: String, major: String) {
    if (degree.isNotEmpty() || school.isNotEmpty() || major.isNotEmpty()) {
        ProfileCard(
            title = "Education",
            backgroundColor = Color(0xFFFFF8DC),
            icon = Icons.Filled.School
        ) {
            if (degree.isNotEmpty()) {
                InfoRow("🎓", "Degree", degree)
            }
            if (school.isNotEmpty()) {
                InfoRow("🏫", "School", school)
            }
            if (major.isNotEmpty()) {
                InfoRow("📚", "Major", major)
            }
        }
    }
}

/** About Me Card **/
@Composable
fun AboutMeCard(aboutMe: String) {
    if (aboutMe.isNotEmpty()) {
        ProfileCard(
            title = "About Me",
            backgroundColor = Color(0xFFFFF0F5),
            icon = Icons.Filled.Info
        ) {
            Text(
                text = aboutMe,
                fontSize = 14.sp,
                color = Color(0xFF666666),
                lineHeight = 20.sp
            )
        }
    }
}

/** Looking For Card **/
@Composable
fun LookingForCard(lookingFor: String) {
    if (lookingFor.isNotEmpty()) {
        ProfileCard(
            title = "Looking For",
            backgroundColor = Color(0xFFE6E6FA),
            icon = Icons.Filled.Favorite
        ) {
            Text(
                text = lookingFor,
                fontSize = 14.sp,
                color = Color(0xFF666666),
                lineHeight = 20.sp
            )
        }
    }
}

/** Personality Traits Section **/
@Composable
fun PersonalityTag(
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
                    listOf(Color(0xFF9C88FF), Color(0xFF667EEA))
                ),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PersonalityTagSection(traits: List<String>) {
    if (traits.isNotEmpty()) {
        Column {
            SectionTitle(
                icon = Icons.Filled.Psychology,
                title = "Personality",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                traits.forEach { trait ->
                    PersonalityTag(text = trait)
                }
            }
        }
    }
}