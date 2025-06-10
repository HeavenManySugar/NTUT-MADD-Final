package com.ntut.madd.finalproject.ui.userprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ntut.madd.finalproject.data.model.ErrorMessage
import com.ntut.madd.finalproject.data.model.User
import com.ntut.madd.finalproject.data.model.UserProfile
import com.ntut.madd.finalproject.ui.profilepage.*
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDetailRoute(val userId: String)

@Composable
fun UserProfileDetailScreen(
    onBackClick: () -> Unit,
    showErrorSnackbar: (ErrorMessage) -> Unit,
    viewModel: UserProfileDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    UserProfileDetailScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        showErrorSnackbar = showErrorSnackbar,
        onRefresh = viewModel::refreshProfile
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileDetailScreenContent(
    uiState: UserProfileDetailUiState,
    onBackClick: () -> Unit,
    showErrorSnackbar: (ErrorMessage) -> Unit,
    onRefresh: () -> Unit
) {
    // Show error if any
    uiState.errorMessage?.let { message ->
        showErrorSnackbar(ErrorMessage.StringError(message))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.user?.displayName ?: "Profile",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF2D3748)
                )
            )
        }
    ) { innerPadding ->

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.user == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Profile not found",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF718096)
                        )
                        Button(onClick = onRefresh) {
                            Text("Retry")
                        }
                    }
                }
            }
            else -> {
                val user = uiState.user!!
                val profile = user.profile

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header with user info
                    UserProfileHeader(
                        user = user,
                        profile = profile
                    )

                    if (profile != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Interests Section
                            if (profile.interests.isNotEmpty()) {
                                InterestTagSection(tags = profile.interests)
                            }

                            // Personality Traits Section
                            if (profile.personalityTraits.isNotEmpty()) {
                                PersonalityTagSection(traits = profile.personalityTraits)
                            }

                            // Location Card
                            if (profile.city.isNotEmpty() || profile.district.isNotEmpty()) {
                                LocationCard(
                                    city = profile.city,
                                    district = profile.district
                                )
                            }

                            // Career Card
                            if (profile.position.isNotEmpty() || profile.company.isNotEmpty()) {
                                CareerCard(
                                    position = profile.position,
                                    company = profile.company
                                )
                            }

                            // Education Card
                            if (profile.degree.isNotEmpty() || profile.school.isNotEmpty() || profile.major.isNotEmpty()) {
                                EducationCard(
                                    degree = profile.degree,
                                    school = profile.school,
                                    major = profile.major
                                )
                            }

                            // About Me Section
                            if (profile.aboutMe.isNotEmpty()) {
                                AboutMeCard(aboutMe = profile.aboutMe)
                            }

                            // Looking For Section
                            if (profile.lookingFor.isNotEmpty()) {
                                LookingForCard(lookingFor = profile.lookingFor)
                            }
                        }
                    }

                    // Bottom spacing
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun UserProfileHeader(
    user: User,
    profile: UserProfile?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile initials circle
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        color = Color(0xFF667EEA),
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.displayName.take(2).uppercase(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Name
            Text(
                text = user.displayName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3748),
                textAlign = TextAlign.Center
            )

            // Location
            if (profile?.city?.isNotEmpty() == true) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (profile.district.isNotEmpty()) {
                        "${profile.city}, ${profile.district}"
                    } else {
                        profile.city
                    },
                    fontSize = 16.sp,
                    color = Color(0xFF718096),
                    textAlign = TextAlign.Center
                )
            }

            // Position & Company
            if (profile?.position?.isNotEmpty() == true || profile?.company?.isNotEmpty() == true) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = when {
                        profile.position.isNotEmpty() && profile.company.isNotEmpty() -> 
                            "${profile.position} at ${profile.company}"
                        profile.position.isNotEmpty() -> profile.position
                        profile.company.isNotEmpty() -> profile.company
                        else -> ""
                    },
                    fontSize = 14.sp,
                    color = Color(0xFF4A5568),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
