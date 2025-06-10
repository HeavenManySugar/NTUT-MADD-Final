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
import androidx.compose.material3.TextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.animation.core.*
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged


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

/** Editable Profile Components **/

@Composable
fun SetupStyleInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    maxLength: Int = Int.MAX_VALUE,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE
) {
    var isFocused by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue = when {
            isFocused -> Color(0xFF6B46C1)
            else -> Color(0xFFEAECEF)
        },
        animationSpec = tween(200),
        label = "border_color"
    )

    val elevation by animateDpAsState(
        targetValue = if (isFocused) 2.dp else 0.dp,
        animationSpec = tween(200),
        label = "elevation"
    )

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.01f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )

    Column(modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .scale(scale),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
            border = BorderStroke(1.dp, borderColor),
            elevation = CardDefaults.cardElevation(defaultElevation = elevation),
            shape = RoundedCornerShape(12.dp)
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = { newValue ->
                    if (newValue.length <= maxLength) {
                        onValueChange(newValue)
                    }
                },
                placeholder = {
                    Text(
                        text = label,
                        color = Color.Gray.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    }
                    .defaultMinSize(minHeight = 48.dp),
                singleLine = singleLine,
                minLines = minLines,
                maxLines = maxLines,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    cursorColor = Color(0xFF6B46C1)
                ),
                shape = RoundedCornerShape(8.dp)
            )
        }

        if (maxLength != Int.MAX_VALUE) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "${value.length}/$maxLength",
                    color = if (value.length > maxLength * 0.9) Color(0xFFFF9800)
                    else Color.Gray.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun EditableFieldLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.Black,
        modifier = modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun EditableLocationCard(
    city: String,
    district: String,
    isEditing: Boolean,
    onUpdateCity: (String) -> Unit,
    onUpdateDistrict: (String) -> Unit
) {
    if (city.isNotEmpty() || district.isNotEmpty() || isEditing) {
        ProfileCard(
            title = "Location",
            backgroundColor = Color(0xFFE3F2FD),
            icon = Icons.Filled.LocationOn
        ) {
            if (isEditing) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column {
                        EditableFieldLabel(
                            text = "City",
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        SetupStyleInputField(
                            value = city,
                            onValueChange = onUpdateCity,
                            label = "Enter your city"
                        )
                    }
                    
                    Column {
                        EditableFieldLabel(
                            text = "District",
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        SetupStyleInputField(
                            value = district,
                            onValueChange = onUpdateDistrict,
                            label = "Enter your district"
                        )
                    }
                }
            } else {
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
}

@Composable
fun EditableCareerCard(
    position: String,
    company: String,
    isEditing: Boolean,
    onUpdatePosition: (String) -> Unit,
    onUpdateCompany: (String) -> Unit
) {
    if (position.isNotEmpty() || company.isNotEmpty() || isEditing) {
        ProfileCard(
            title = "Career",
            backgroundColor = Color(0xFFF5F5DC),
            icon = Icons.Filled.Business
        ) {
            if (isEditing) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column {
                        EditableFieldLabel(
                            text = "Position",
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        SetupStyleInputField(
                            value = position,
                            onValueChange = onUpdatePosition,
                            label = "Enter your job position"
                        )
                    }
                    
                    Column {
                        EditableFieldLabel(
                            text = "Company",
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        SetupStyleInputField(
                            value = company,
                            onValueChange = onUpdateCompany,
                            label = "Enter your company name"
                        )
                    }
                }
            } else {
                if (position.isNotEmpty()) {
                    InfoRow("💼", "Position", position)
                }
                if (company.isNotEmpty()) {
                    InfoRow("🏢", "Company", company)
                }
            }
        }
    }
}

@Composable
fun EditableEducationCard(
    degree: String,
    school: String,
    major: String,
    isEditing: Boolean,
    onUpdateDegree: (String) -> Unit,
    onUpdateSchool: (String) -> Unit,
    onUpdateMajor: (String) -> Unit
) {
    if (degree.isNotEmpty() || school.isNotEmpty() || major.isNotEmpty() || isEditing) {
        ProfileCard(
            title = "Education",
            backgroundColor = Color(0xFFFFF8DC),
            icon = Icons.Filled.School
        ) {
            if (isEditing) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column {
                        EditableFieldLabel(
                            text = "Degree",
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        SetupStyleInputField(
                            value = degree,
                            onValueChange = onUpdateDegree,
                            label = "Enter your degree level"
                        )
                    }
                    
                    Column {
                        EditableFieldLabel(
                            text = "School",
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        SetupStyleInputField(
                            value = school,
                            onValueChange = onUpdateSchool,
                            label = "Enter your school name"
                        )
                    }
                    
                    Column {
                        EditableFieldLabel(
                            text = "Major",
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        SetupStyleInputField(
                            value = major,
                            onValueChange = onUpdateMajor,
                            label = "Enter your major field"
                        )
                    }
                }
            } else {
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
}

@Composable
fun EditableAboutMeCard(
    aboutMe: String,
    isEditing: Boolean,
    onUpdateAboutMe: (String) -> Unit
) {
    if (aboutMe.isNotEmpty() || isEditing) {
        ProfileCard(
            title = "About Me",
            backgroundColor = Color(0xFFFFF0F5),
            icon = Icons.Filled.Info
        ) {
            if (isEditing) {
                Column {
                    EditableFieldLabel(
                        text = "About Me",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    SetupStyleInputField(
                        value = aboutMe,
                        onValueChange = onUpdateAboutMe,
                        label = "Tell us about yourself",
                        singleLine = false,
                        minLines = 3,
                        maxLines = 5,
                        maxLength = 500
                    )
                }
            } else {
                Text(
                    text = aboutMe,
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun EditableLookingForCard(
    lookingFor: String,
    isEditing: Boolean,
    onUpdateLookingFor: (String) -> Unit
) {
    if (lookingFor.isNotEmpty() || isEditing) {
        ProfileCard(
            title = "Looking For",
            backgroundColor = Color(0xFFE6E6FA),
            icon = Icons.Filled.Favorite
        ) {
            if (isEditing) {
                Column {
                    EditableFieldLabel(
                        text = "Looking For",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    SetupStyleInputField(
                        value = lookingFor,
                        onValueChange = onUpdateLookingFor,
                        label = "What are you looking for?",
                        singleLine = false,
                        minLines = 2,
                        maxLines = 4,
                        maxLength = 300
                    )
                }
            } else {
                Text(
                    text = lookingFor,
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditableInterestTagSection(
    tags: List<String>,
    isEditing: Boolean,
    onUpdateInterests: (List<String>) -> Unit
) {
    val commonInterests = listOf(
        "🎬 Movies", "📚 Reading", "🎵 Music", "💪 Fitness", "🍳 Cooking",
        "✈️ Travel", "🎨 Art", "🎮 Gaming", "📸 Photography", "🏃 Running",
        "🧘 Yoga", "🌱 Gardening", "💻 Technology", "🏀 Sports", "🎪 Dancing"
    )
    
    if (isEditing) {
        Column {
            Text(
                text = "Select your interests:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF666666),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                commonInterests.forEach { interest ->
                    FilterChip(
                        onClick = { 
                            val updatedTags = if (tags.contains(interest)) {
                                tags - interest
                            } else {
                                tags + interest
                            }
                            onUpdateInterests(updatedTags)
                        },
                        label = { Text(interest, fontSize = 12.sp) },
                        selected = tags.contains(interest),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF667EEA),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }
    } else if (tags.isNotEmpty()) {
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
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditablePersonalityTagSection(
    traits: List<String>,
    isEditing: Boolean,
    onUpdatePersonalityTraits: (List<String>) -> Unit
) {
    val commonTraits = listOf(
        "🧡 Warm & Caring", "😄 Positive & Active", "🧘 Calm & Rational",
        "🎭 Creative & Artistic", "💪 Strong & Independent", "🤝 Social & Outgoing",
        "📚 Intellectual", "🌟 Ambitious", "😊 Funny & Humorous", "💝 Romantic",
        "🌿 Nature Lover", "🎯 Goal-Oriented", "🤗 Empathetic", "🚀 Adventurous"
    )
    
    if (traits.isNotEmpty() || isEditing) {
        Column {
            SectionTitle(
                icon = Icons.Filled.Psychology,
                title = "Personality",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (isEditing) {
                Text(
                    text = "Select your personality traits:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    commonTraits.forEach { trait ->
                        FilterChip(
                            onClick = { 
                                val updatedTraits = if (traits.contains(trait)) {
                                    traits - trait
                                } else {
                                    traits + trait
                                }
                                onUpdatePersonalityTraits(updatedTraits)
                            },
                            label = { Text(trait, fontSize = 12.sp) },
                            selected = traits.contains(trait),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF9C88FF),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            } else {
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
}