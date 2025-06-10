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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.ntut.madd.finalproject.data.model.ErrorMessage
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ntut.madd.finalproject.R
import com.ntut.madd.finalproject.ui.component.*
import com.ntut.madd.finalproject.ui.setup.components.SetupInputField
import com.ntut.madd.finalproject.ui.setup.components.SetupFieldLabel
import com.ntut.madd.finalproject.ui.theme.MakeItSoTheme
import kotlinx.serialization.Serializable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Person2
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
            showErrorSnackbar = showErrorSnackbar,
            onStartEditing = viewModel::startEditing,
            onCancelEditing = viewModel::cancelEditing,
            onSaveProfile = viewModel::saveProfile,
            onUpdateDisplayName = viewModel::updateDisplayName,
            onUpdateCity = viewModel::updateCity,
            onUpdateDistrict = viewModel::updateDistrict,
            onUpdatePosition = viewModel::updatePosition,
            onUpdateCompany = viewModel::updateCompany,
            onUpdateDegree = viewModel::updateDegree,
            onUpdateSchool = viewModel::updateSchool,
            onUpdateMajor = viewModel::updateMajor,
            onUpdateAboutMe = viewModel::updateAboutMe,
            onUpdateLookingFor = viewModel::updateLookingFor,
            onUpdateInterests = viewModel::updateInterests,
            onUpdatePersonalityTraits = viewModel::updatePersonalityTraits
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
    showErrorSnackbar: (ErrorMessage) -> Unit = {},
    onStartEditing: () -> Unit = {},
    onCancelEditing: () -> Unit = {},
    onSaveProfile: () -> Unit = {},
    onUpdateDisplayName: (String) -> Unit = {},
    onUpdateCity: (String) -> Unit = {},
    onUpdateDistrict: (String) -> Unit = {},
    onUpdatePosition: (String) -> Unit = {},
    onUpdateCompany: (String) -> Unit = {},
    onUpdateDegree: (String) -> Unit = {},
    onUpdateSchool: (String) -> Unit = {},
    onUpdateMajor: (String) -> Unit = {},
    onUpdateAboutMe: (String) -> Unit = {},
    onUpdateLookingFor: (String) -> Unit = {},
    onUpdateInterests: (List<String>) -> Unit = {},
    onUpdatePersonalityTraits: (List<String>) -> Unit = {}
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
                    uiState = uiState,
                    openSettingsScreen = openSettingsScreen,
                    innerPadding = innerPadding,
                    onStartEditing = onStartEditing,
                    onCancelEditing = onCancelEditing,
                    onSaveProfile = onSaveProfile,
                    onUpdateDisplayName = onUpdateDisplayName,
                    onUpdateCity = onUpdateCity,
                    onUpdateDistrict = onUpdateDistrict,
                    onUpdatePosition = onUpdatePosition,
                    onUpdateCompany = onUpdateCompany,
                    onUpdateDegree = onUpdateDegree,
                    onUpdateSchool = onUpdateSchool,
                    onUpdateMajor = onUpdateMajor,
                    onUpdateAboutMe = onUpdateAboutMe,
                    onUpdateLookingFor = onUpdateLookingFor,
                    onUpdateInterests = onUpdateInterests,
                    onUpdatePersonalityTraits = onUpdatePersonalityTraits
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
    uiState: ProfileUiState,
    openSettingsScreen: () -> Unit,
    innerPadding: PaddingValues,
    onStartEditing: () -> Unit,
    onCancelEditing: () -> Unit,
    onSaveProfile: () -> Unit,
    onUpdateDisplayName: (String) -> Unit,
    onUpdateCity: (String) -> Unit,
    onUpdateDistrict: (String) -> Unit,
    onUpdatePosition: (String) -> Unit,
    onUpdateCompany: (String) -> Unit,
    onUpdateDegree: (String) -> Unit,
    onUpdateSchool: (String) -> Unit,
    onUpdateMajor: (String) -> Unit,
    onUpdateAboutMe: (String) -> Unit,
    onUpdateLookingFor: (String) -> Unit,
    onUpdateInterests: (List<String>) -> Unit,
    onUpdatePersonalityTraits: (List<String>) -> Unit
) {
    val user = uiState.user!!
    val profile = uiState.editableProfile ?: user.profile
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = innerPadding.calculateBottomPadding())
            .verticalScroll(scrollState)
    ) {
        GradientBackgroundBox {
            // Settings button and Edit/Save controls in top row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Edit/Save/Cancel buttons on the left
                if (uiState.isEditing) {
                    Row {
                        IconButton(onClick = onCancelEditing) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = "Cancel",
                                tint = Color.White
                            )
                        }
                        IconButton(
                            onClick = onSaveProfile,
                            enabled = !uiState.isSaving
                        ) {
                            if (uiState.isSaving) {
                                androidx.compose.material3.CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Save,
                                    contentDescription = "Save",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                } else {
                    IconButton(onClick = onStartEditing) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            tint = Color.White
                        )
                    }
                }

                // Settings button on the right
                IconButton(onClick = openSettingsScreen) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.White
                    )
                }
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

                // Editable display name
                if (uiState.isEditing) {
                    Card(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        colors = androidx.compose.material3.CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.15f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        OutlinedTextField(
                            value = user.displayName,
                            onValueChange = onUpdateDisplayName,
                            placeholder = { 
                                Text(
                                    "Enter display name", 
                                    color = Color.White.copy(alpha = 0.6f),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                ) 
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = androidx.compose.material3.TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            ),
                            singleLine = true
                        )
                    }
                } else {
                    Text(
                        text = user.displayName.ifEmpty { "Unknown User" },
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
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
                if (profile.interests.isNotEmpty() || uiState.isEditing) {
                    EditableInterestTagSection(
                        tags = profile.interests,
                        isEditing = uiState.isEditing,
                        onUpdateInterests = onUpdateInterests
                    )
                }

                /** Personality Traits Section **/
                if (profile.personalityTraits.isNotEmpty() || uiState.isEditing) {
                    EditablePersonalityTagSection(
                        traits = profile.personalityTraits,
                        isEditing = uiState.isEditing,
                        onUpdatePersonalityTraits = onUpdatePersonalityTraits
                    )
                }

                /** Location Card **/
                EditableLocationCard(
                    city = profile.city,
                    district = profile.district,
                    isEditing = uiState.isEditing,
                    onUpdateCity = onUpdateCity,
                    onUpdateDistrict = onUpdateDistrict
                )

                /** Career Card **/
                EditableCareerCard(
                    position = profile.position,
                    company = profile.company,
                    isEditing = uiState.isEditing,
                    onUpdatePosition = onUpdatePosition,
                    onUpdateCompany = onUpdateCompany
                )

                /** Education Card **/
                EditableEducationCard(
                    degree = profile.degree,
                    school = profile.school,
                    major = profile.major,
                    isEditing = uiState.isEditing,
                    onUpdateDegree = onUpdateDegree,
                    onUpdateSchool = onUpdateSchool,
                    onUpdateMajor = onUpdateMajor
                )

                /** About Me Card **/
                EditableAboutMeCard(
                    aboutMe = profile.aboutMe,
                    isEditing = uiState.isEditing,
                    onUpdateAboutMe = onUpdateAboutMe
                )

                /** Looking For Card **/
                EditableLookingForCard(
                    lookingFor = profile.lookingFor,
                    isEditing = uiState.isEditing,
                    onUpdateLookingFor = onUpdateLookingFor
                )
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
                showErrorSnackbar = {},
                onStartEditing = {},
                onCancelEditing = {},
                onSaveProfile = {},
                onUpdateDisplayName = {},
                onUpdateCity = {},
                onUpdateDistrict = {},
                onUpdatePosition = {},
                onUpdateCompany = {},
                onUpdateDegree = {},
                onUpdateSchool = {},
                onUpdateMajor = {},
                onUpdateAboutMe = {},
                onUpdateLookingFor = {},
                onUpdateInterests = {},
                onUpdatePersonalityTraits = {}
            )
        }
    }
}