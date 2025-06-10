package com.ntut.madd.finalproject.ui.discover

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import kotlin.math.abs

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
            onRefreshRecommendations = viewModel::refreshRecommendations,
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
    onRefreshRecommendations: () -> Unit = {},
    openSettingsScreen: () -> Unit = {},
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
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // Page Information
            // Head
            GradientBackgroundBox(useGradient = false) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Settings button on the top right
                    IconButton(
                        onClick = openSettingsScreen,
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.Black
                        )
                    }
                    
                    // Refresh button on the top left
                    IconButton(
                        onClick = onRefreshRecommendations,
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Recommendations",
                            tint = Color.Black
                        )
                    }
                    
                    // Centered content
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center)
                            .padding(top = 24.dp),
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
                        EmptyProfilesContent(onRefresh = onRefreshRecommendations)
                    }
                    else -> {
                        val currentUser = uiState.availableProfiles.getOrNull(uiState.currentProfileIndex)
                        if (currentUser != null) {
                            // Use key to reset animation state when user changes
                            key(currentUser.id) {
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
private fun EmptyProfilesContent(onRefresh: () -> Unit = {}) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "✨",
                fontSize = 48.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "All caught up!",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "You've seen all available profiles.\nCheck back later for new recommendations!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onRefresh) {
                Text("Refresh Recommendations")
            }
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
    
    // Animation states
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var rotation by remember { mutableFloatStateOf(0f) }
    var isAnimating by remember { mutableStateOf(false) }
    
    // Animate back to center position
    val animatedOffsetX by animateFloatAsState(
        targetValue = if (isAnimating) offsetX else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        finishedListener = {
            if (isAnimating) {
                isAnimating = false
                offsetX = 0f
                offsetY = 0f
                rotation = 0f
            }
        },
        label = "offsetX"
    )
    
    val animatedOffsetY by animateFloatAsState(
        targetValue = if (isAnimating) offsetY else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "offsetY"
    )
    
    val animatedRotation by animateFloatAsState(
        targetValue = if (isAnimating) rotation else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "rotation"
    )
    
    // Swipe threshold
    val swipeThreshold = 300f
    
    // Handle swipe actions
    fun handleSwipeAction(deltaX: Float) {
        when {
            deltaX > swipeThreshold -> {
                // Right swipe - approve
                isAnimating = true
                offsetX = 1000f
                onApprove()
            }
            deltaX < -swipeThreshold -> {
                // Left swipe - reject
                isAnimating = true
                offsetX = -1000f
                onReject()
            }
            else -> {
                // Return to center
                isAnimating = true
            }
        }
    }
    
    // Handle button actions with animation
    fun handleReject() {
        isAnimating = true
        offsetX = -1000f
        rotation = -30f
        onReject()
    }
    
    fun handleApprove() {
        isAnimating = true
        offsetX = 1000f
        rotation = 30f
        onApprove()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Swipeable card content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .graphicsLayer {
                    translationX = animatedOffsetX
                    translationY = animatedOffsetY
                    rotationZ = animatedRotation
                    val alpha = 1f - (abs(animatedOffsetX) / 1000f).coerceIn(0f, 0.7f)
                    this.alpha = alpha
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            handleSwipeAction(offsetX)
                        }
                    ) { _, dragAmount ->
                        if (!isAnimating) {
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y * 0.3f // Reduce vertical movement
                            rotation = (offsetX / 20f).coerceIn(-30f, 30f)
                        }
                    }
                }
        ) {
            RoundedWhiteCard {
                TopSection(user = user)
                
                if (profile.interests.isNotEmpty()) {
                    InterestSection(
                        interests = profile.interests
                    )
                }
                
                if (profile.personalityTraits.isNotEmpty()) {
                    PersonalitySection(
                        traits = profile.personalityTraits
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (profile.company.isNotEmpty() || profile.position.isNotEmpty() || profile.city.isNotEmpty() || profile.district.isNotEmpty()) {
                        CareerCard(profile)
                    }
                    if (profile.school.isNotEmpty() || profile.degree.isNotEmpty() || profile.major.isNotEmpty()) {
                        EducationCard(profile)
                    }
                    if (profile.aboutMe.isNotEmpty()) {
                        AboutMeCard(profile)
                    }
                    if (profile.lookingFor.isNotEmpty()) {
                        LookingForCard(profile)
                    }
                }
            }
            
            // Swipe indicators
            if (abs(offsetX) > 50f && !isAnimating) {
                SwipeIndicators(offsetX = offsetX)
            }
        }
        
        // Fixed buttons at bottom
        DecisionButtons(
            onReject = ::handleReject,
            onApprove = ::handleApprove
        )
    }
}

@Composable
private fun SwipeIndicators(offsetX: Float) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Like indicator (right swipe)
        if (offsetX > 50f) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(32.dp)
                    .background(
                        Color.Green.copy(alpha = (offsetX / 300f).coerceIn(0f, 0.8f)),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "LIKE",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
        
        // Reject indicator (left swipe)
        if (offsetX < -50f) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(32.dp)
                    .background(
                        Color.Red.copy(alpha = (abs(offsetX) / 300f).coerceIn(0f, 0.8f)),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "NOPE",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
    }
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
                city = "Kaohsiung City",
                district = "1123",
                position = "123",
                company = "123",
                degree = "College",
                school = "123",
                major = "123",
                interests = listOf("📝 Drawing", "⚽ Sports", "🏔️ Hiking"),
                personalityTraits = listOf("😄 Positive & Active", "💪 Honest & Reliable", "🧘 Calm & Rational"),
                aboutMe = "1231",
                lookingFor = "23",
                isProfileComplete = true,
                profileCompletedAt = 1749486980966
            )
            val sampleUser = User(
                id = "sample123",
                ownerId = "1b0WiotDsfSwDGlbj7CEzhhE3a62",
                displayName = "Alex Chen",
                email = "alex@example.com",
                isAnonymous = false,
                profile = sampleProfile
            )
            val sampleUiState = DiscoverUiState(
                availableProfiles = listOf(sampleUser),
                currentProfileIndex = 0
            )
            
            DiscoverPageScreenContent(
                uiState = sampleUiState,
                currentRoute = "discover",
                onNavigate = {},
                onRefreshRecommendations = {}
            )
        }
    }
}