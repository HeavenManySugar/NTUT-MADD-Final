package com.ntut.madd.finalproject.ui.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import com.ntut.madd.finalproject.data.model.ErrorMessage
import com.ntut.madd.finalproject.data.model.User
import com.ntut.madd.finalproject.data.model.UserProfile

import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ntut.madd.finalproject.ui.component.*
import com.ntut.madd.finalproject.ui.theme.MakeItSoTheme
import kotlinx.serialization.Serializable


@Serializable
object DiscoverPageRoute




@Composable
fun DiscoverPageScreen(
    openHomeScreen: () -> Unit,
    showErrorSnackbar: (ErrorMessage) -> Unit,
    currentRoute: String = "discover",
    onNavigate: (String) -> Unit = {},
    viewModel: DiscoverPageViewModel = hiltViewModel()
) {
    val shouldRestartApp by viewModel.shouldRestartApp.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (shouldRestartApp) {
        openHomeScreen()
    } else {
        DiscoverPageScreenContent(
            uiState = uiState,
            currentRoute = currentRoute,
            onNavigate = onNavigate,
            onReject = viewModel::onRejectProfile,
            onApprove = viewModel::onApproveProfile,
            onRetry = viewModel::retryLoading,
            showErrorSnackbar = showErrorSnackbar
        )
    }
}

@Composable
fun DiscoverPageScreenContent(
    uiState: DiscoverUiState,
    currentRoute: String = "discover",
    onNavigate: (String) -> Unit = {},
    onReject: () -> Unit = {},
    onApprove: () -> Unit = {},
    onRetry: () -> Unit = {},
    showErrorSnackbar: (ErrorMessage) -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Page Information
            // Head
            GradientBackgroundBox(useGradient = false) {
                Column(
                    modifier = Modifier.wrapContentSize(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Discover",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Find your perfect match",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                }
            }

            // Content area
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF2F2F2))
            ) {
                when {
                    uiState.isLoading -> {
                        LoadingContent()
                    }
                    uiState.errorMessage != null -> {
                        ErrorContent(
                            message = uiState.errorMessage,
                            onRetry = onRetry,
                            showErrorSnackbar = showErrorSnackbar
                        )
                    }
                    uiState.availableProfiles.isEmpty() -> {
                        EmptyProfilesContent()
                    }
                    else -> {
                        val currentUser = uiState.availableProfiles.getOrNull(uiState.currentProfileIndex)
                        if (currentUser != null) {
                            ProfileContent(
                                user = currentUser,
                                onReject = onReject,
                                onApprove = onApprove
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading profiles...",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    showErrorSnackbar: (ErrorMessage) -> Unit
) {
    // Show error in snackbar
    showErrorSnackbar(ErrorMessage.StringError(message))
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "😔",
                fontSize = 48.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Something went wrong",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onRetry) {
                Text("Try Again")
            }
        }
    }
}

@Composable
private fun EmptyProfilesContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🔍",
                fontSize = 48.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No profiles available",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Check back later for new profiles",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun ProfileContent(
    user: User,
    onReject: () -> Unit,
    onApprove: () -> Unit
) {
    val profile = user.profile
    
    if (profile == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Profile data not available",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
        return
    }
    
    RoundedWhiteCard {
        TopSection(
            name = user.displayName.ifEmpty { "Anonymous User" },
            location = if (profile.city.isNotEmpty() && profile.district.isNotEmpty()) {
                "${profile.district}, ${profile.city}"
            } else if (profile.city.isNotEmpty()) {
                profile.city
            } else {
                "Location not specified"
            },
            jobTitle = profile.position.ifEmpty { "Position not specified" },
            education = if (profile.degree.isNotEmpty()) {
                profile.degree
            } else if (profile.school.isNotEmpty()) {
                profile.school
            } else {
                "Education not specified"
            }
        )
        
        if (profile.interests.isNotEmpty()) {
            InterestSection(
                interests = profile.interests.map { interest ->
                    // Add emoji based on interest or use a default one
                    getEmojiForInterest(interest) to interest
                }
            )
        }
        
        if (profile.personalityTraits.isNotEmpty()) {
            PersonalitySection(
                traits = profile.personalityTraits.map { trait ->
                    if (trait.startsWith("🌟") || trait.startsWith("🎯") || trait.startsWith("🤝")) {
                        trait
                    } else {
                        "✨ $trait"
                    }
                }
            )
        }
        
        if (profile.aboutMe.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "About Me",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = profile.aboutMe,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
    
    DecisionButtons(
        onReject = onReject,
        onApprove = onApprove
    )
}

private fun getEmojiForInterest(interest: String): String {
    return when {
        interest.contains("music", ignoreCase = true) -> "🎵"
        interest.contains("fitness", ignoreCase = true) || interest.contains("gym", ignoreCase = true) -> "🏃"
        interest.contains("reading", ignoreCase = true) || interest.contains("book", ignoreCase = true) -> "📚"
        interest.contains("cooking", ignoreCase = true) -> "👨‍🍳"
        interest.contains("travel", ignoreCase = true) -> "✈️"
        interest.contains("movie", ignoreCase = true) || interest.contains("film", ignoreCase = true) -> "🎬"
        interest.contains("game", ignoreCase = true) || interest.contains("gaming", ignoreCase = true) -> "🎮"
        interest.contains("art", ignoreCase = true) -> "🎨"
        interest.contains("sport", ignoreCase = true) -> "⚽"
        interest.contains("photography", ignoreCase = true) -> "📷"
        else -> "💫"
    }
}

@Preview(showBackground = true)
@Composable
fun DiscoverPageScreenPreview() {
    MakeItSoTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1200.dp)
        ) {
            val sampleProfile = UserProfile(
                city = "Taipei",
                district = "Da'an",
                position = "Software Engineer",
                company = "Tech Corp",
                degree = "Bachelor's",
                school = "NTUT",
                major = "Computer Science",
                interests = listOf("Music", "Fitness", "Reading", "Cooking"),
                personalityTraits = listOf("🌟 Optimistic", "🎯 Ambitious", "🤝 Outgoing"),
                aboutMe = "Love exploring new technologies and meeting new people!",
                lookingFor = "Someone who shares similar interests",
                isProfileComplete = true
            )
            val sampleUser = User(
                id = "sample123",
                displayName = "Alex Chen",
                email = "alex@example.com",
                profile = sampleProfile
            )
            val sampleUiState = DiscoverUiState(
                availableProfiles = listOf(sampleUser),
                currentProfileIndex = 0
            )
            
            DiscoverPageScreenContent(
                uiState = sampleUiState,
                currentRoute = "discover",
                onNavigate = {}
            )
        }
    }
}