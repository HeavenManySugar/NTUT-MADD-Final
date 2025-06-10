package com.ntut.madd.finalproject.ui.profilepage

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.ntut.madd.finalproject.data.model.ErrorMessage
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ntut.madd.finalproject.R
import com.ntut.madd.finalproject.ui.component.*
import com.ntut.madd.finalproject.ui.theme.MakeItSoTheme
import kotlinx.serialization.Serializable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Person2
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import com.ntut.madd.finalproject.data.model.User
import com.ntut.madd.finalproject.data.model.UserProfile

@Serializable
object ProfilePageRoute

@Composable
fun ProfilePageScreen(
    openHomeScreen: () -> Unit,
    openSettingsScreen: () -> Unit,
    showErrorSnackbar: (ErrorMessage) -> Unit,
    currentRoute: String = "profile",
    onNavigate: (String) -> Unit = {},
    viewModel: ProfilePageViewModel = hiltViewModel()
) {
    val shouldRestartApp by viewModel.shouldRestartApp.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (shouldRestartApp) {
        openHomeScreen()
    } else {
        ProfilePageScreenContent(
            uiState = uiState,
            openSettingsScreen = openSettingsScreen,
            currentRoute = currentRoute,
            onNavigate = onNavigate,
            onRefresh = viewModel::refreshProfile,
            showErrorSnackbar = showErrorSnackbar
        )
    }
}

@Composable
fun ProfilePageScreenContent(
    uiState: ProfileUiState,
    openSettingsScreen: () -> Unit,
    currentRoute: String = "profile",
    onNavigate: (String) -> Unit = {},
    onRefresh: () -> Unit = {},
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

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        androidx.compose.material3.CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Loading profile...")
                    }
                }
            }
            uiState.errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Failed to load profile",
                            style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.errorMessage,
                            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        androidx.compose.material3.Button(onClick = onRefresh) {
                            Text("Retry")
                        }
                    }
                }
            }
            uiState.user != null -> {
                ProfileContent(
                    user = uiState.user,
                    openSettingsScreen = openSettingsScreen,
                    innerPadding = innerPadding
                )
            }
            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No profile found",
                            style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Please complete your profile setup",
                            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        androidx.compose.material3.Button(onClick = onRefresh) {
                            Text("Refresh")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileContent(
    user: User,
    openSettingsScreen: () -> Unit,
    innerPadding: PaddingValues
) {
    val profile = user.profile
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = innerPadding.calculateBottomPadding())
            .verticalScroll(scrollState)
    ) {
        GradientBackgroundBox {
            // Settings button in top right
            IconButton(
                onClick = openSettingsScreen,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = Color.White
                )
            }

            // Profile content centered
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.Center)
            ) {
                // Display first letter of displayName or "U" for unknown
                val initial = if (user.displayName.isNotEmpty()) {
                    user.displayName.first().uppercase()
                } else {
                    "U"
                }
                InitialAvatar(initial = initial)
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = user.displayName.ifEmpty { "Unknown User" },
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Display location info
                val locationText = if (profile != null && profile.city.isNotEmpty()) {
                    if (profile.district.isNotEmpty()) {
                        "${profile.district}, ${profile.city}"
                    } else {
                        profile.city
                    }
                } else {
                    "Location not set"
                }

                Text(
                    text = locationText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Display what they're looking for if available
                if (profile?.lookingFor?.isNotEmpty() == true) {
                    HighlightTag(profile.lookingFor)
                } else {
                    HighlightTag(stringResource(R.string.finding_love))
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            /** Stats Section **/
            StatCardRow(modifier = Modifier.fillMaxWidth())

            if (profile != null) {
                /** Interests Section **/
                if (profile.interests.isNotEmpty()) {
                    SectionTitle(
                        icon = Icons.Filled.TrackChanges,
                        title = stringResource(R.string.my_interest),
                        modifier = Modifier.padding(horizontal = 0.dp)
                    )
                    InterestTagSection(tags = profile.interests)
                }

                /** Personality Traits Section **/
                if (profile.personalityTraits.isNotEmpty()) {
                    PersonalityTagSection(traits = profile.personalityTraits)
                }

                /** Location Card **/
                LocationCard(city = profile.city, district = profile.district)

                /** Career Card **/
                CareerCard(position = profile.position, company = profile.company)

                /** Education Card **/
                EducationCard(
                    degree = profile.degree,
                    school = profile.school,
                    major = profile.major
                )

                /** About Me Card **/
                AboutMeCard(aboutMe = profile.aboutMe)

                /** Looking For Card **/
                LookingForCard(lookingFor = profile.lookingFor)
            } else {
                // If no profile data, show a message
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = Color(0xFFFFF3E0)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person2,
                            contentDescription = null,
                            tint = Color(0xFFFF9800),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Profile Not Complete",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF333333)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Please complete your profile setup to show more information",
                            fontSize = 14.sp,
                            color = Color(0xFF666666),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            // Add some bottom padding
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePageScreenPreview() {
    MakeItSoTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1200.dp)
        ) {
            // Create sample user data for preview
            val sampleProfile = UserProfile(
                city = "Kaohsiung City",
                district = "123",
                position = "123",
                company = "123",
                degree = "High School",
                school = "123",
                major = "123",
                interests = listOf("🎬 Movies", "💪 Fitness", "📚 Reading"),
                personalityTraits = listOf("🧡 Warm & Caring", "😄 Positive & Active", "🧘 Calm & Rational"),
                aboutMe = "3213",
                lookingFor = "123"
            )
            
            val sampleUser = User(
                id = "sample123",
                displayName = "",
                email = "sample@example.com",
                profile = sampleProfile
            )
            
            val sampleUiState = ProfileUiState(
                isLoading = false,
                user = sampleUser,
                errorMessage = null
            )
            
            ProfilePageScreenContent(
                uiState = sampleUiState,
                currentRoute = "profile",
                openSettingsScreen = {},
                onNavigate = {},
                onRefresh = {},
                showErrorSnackbar = {}
            )
        }
    }
}