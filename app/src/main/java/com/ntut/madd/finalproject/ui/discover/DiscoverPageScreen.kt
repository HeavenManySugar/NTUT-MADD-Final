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
import kotlinx.coroutines.*

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
            onDislike = viewModel::onRejectProfile,
            onLike = viewModel::onApproveProfile,
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
    onDislike: () -> Unit = {},
    onLike: () -> Unit = {},
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
                                    onDislike = onDislike,
                                    onLike = onLike
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
    onDislike: () -> Unit,
    onLike: () -> Unit
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
    
    // Animation states - simplified approach
    var offsetX by remember { mutableFloatStateOf(0f) }
    var rotation by remember { mutableFloatStateOf(0f) }
    var isAnimating by remember { mutableStateOf(false) }
    
    // Coroutine scope for animations
    val coroutineScope = rememberCoroutineScope()
    
    // Animate offset and rotation
    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "offsetX"
    )
    
    val animatedRotation by animateFloatAsState(
        targetValue = rotation,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "rotation"
    )
    
    // Swipe threshold
    val swipeThreshold = 150f
    
    // Handle swipe actions - simplified
    fun handleSwipeAction(deltaX: Float) {
        when {
            deltaX > swipeThreshold -> {
                // Right swipe - like with slide out animation
                isAnimating = true
                offsetX = 1000f
                rotation = 30f
                // Trigger action after a delay to show animation
                coroutineScope.launch {
                    delay(300)
                    onLike()
                    // Reset position for next card
                    offsetX = 0f
                    rotation = 0f
                    isAnimating = false
                }
            }
            deltaX < -swipeThreshold -> {
                // Left swipe - dislike with slide out animation
                isAnimating = true
                offsetX = -1000f
                rotation = -30f
                // Trigger action after a delay to show animation
                coroutineScope.launch {
                    delay(300)
                    onDislike()
                    // Reset position for next card
                    offsetX = 0f
                    rotation = 0f
                    isAnimating = false
                }
            }
            else -> {
                // Return to center
                offsetX = 0f
                rotation = 0f
            }
        }
    }
    
    // Handle button actions with animation
    fun handleDislike() {
        isAnimating = true
        offsetX = -1000f
        rotation = -30f
        coroutineScope.launch {
            delay(300)
            onDislike()
            offsetX = 0f
            rotation = 0f
            isAnimating = false
        }
    }
    
    fun handleLike() {
        isAnimating = true
        offsetX = 1000f
        rotation = 30f
        coroutineScope.launch {
            delay(300)
            onLike()
            offsetX = 0f
            rotation = 0f
            isAnimating = false
        }
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
                    rotationZ = animatedRotation
                    val alpha = 1f - (abs(animatedOffsetX) / 1000f).coerceIn(0f, 0.8f)
                    this.alpha = alpha
                    
                    // Add scale effect for more dramatic animation
                    val scale = 1f - (abs(animatedOffsetX) / 2000f).coerceIn(0f, 0.1f)
                    scaleX = scale
                    scaleY = scale
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { 
                            println("DiscoverPage: Drag started")
                        },
                        onDragEnd = {
                            println("DiscoverPage: Drag ended, offsetX = $offsetX")
                            handleSwipeAction(offsetX)
                        }
                    ) { _, dragAmount ->
                        offsetX += dragAmount.x
                        rotation = (offsetX / 15f).coerceIn(-45f, 45f)
                        println("DiscoverPage: Dragging, offsetX = $offsetX")
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
            
            // Swipe indicators - make them more visible
            if (abs(offsetX) > 30f && !isAnimating) {
                SwipeIndicators(offsetX = offsetX)
            }
        }
        
        // Fixed buttons at bottom
        DecisionButtons(
            onDislike = ::handleDislike,
            onLike = ::handleLike
        )
    }
}

@Composable
private fun SwipeIndicators(offsetX: Float) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Like indicator (right swipe)
        if (offsetX > 30f) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(24.dp)
                    .background(
                        Color.Green.copy(alpha = (offsetX / 200f).coerceIn(0.3f, 0.9f)),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(25.dp)
                    )
                    .padding(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "LIKE ❤️",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }
        }
        
        // Dislike indicator (left swipe)
        if (offsetX < -30f) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(24.dp)
                    .background(
                        Color.Red.copy(alpha = (abs(offsetX) / 200f).coerceIn(0.3f, 0.9f)),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(25.dp)
                    )
                    .padding(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "💔 DISLIKE",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
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