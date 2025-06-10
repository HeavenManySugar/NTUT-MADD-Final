package com.ntut.madd.finalproject.ui.matches

import androidx.compose.foundation.background

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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import com.ntut.madd.finalproject.data.model.ErrorMessage
import com.ntut.madd.finalproject.data.model.User

import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ntut.madd.finalproject.ui.component.*
import com.ntut.madd.finalproject.ui.theme.MakeItSoTheme
import kotlinx.serialization.Serializable

@Serializable
object MatchesPagePageRoute

@Composable
fun MatchesPageScreen(
    openHomeScreen: () -> Unit,
    showErrorSnackbar: (ErrorMessage) -> Unit,
    currentRoute: String = "matches",
    onNavigate: (String) -> Unit = {},
    viewModel: MatchesPageViewModel = hiltViewModel()
) {
    val shouldRestartApp by viewModel.shouldRestartApp.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (shouldRestartApp) {
        openHomeScreen()
    } else {
        MatchesPageScreenContent(
            uiState = uiState,
            currentRoute = currentRoute,
            onNavigate = onNavigate,
            onAcceptUser = viewModel::acceptUser,
            onRejectUser = viewModel::rejectUser,
            onRefresh = viewModel::refresh,
            showErrorSnackbar = showErrorSnackbar
        )
    }
}

@Composable
fun MatchesPageScreenContent(
    uiState: MatchesUiState,
    currentRoute: String = "matches",
    onNavigate: (String) -> Unit = {},
    onAcceptUser: (User) -> Unit = {},
    onRejectUser: (User) -> Unit = {},
    onRefresh: () -> Unit = {},
    showErrorSnackbar: (ErrorMessage) -> Unit = {}
) {
    // Show error if any
    uiState.errorMessage?.let { message ->
        showErrorSnackbar(ErrorMessage.StringError(message))
    }

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
            GradientBackgroundBox(useGradient = false) {
                MyMatchesStats(
                    newMatches = uiState.mutualMatches.size,
                    totalLikes = uiState.usersWhoLikedMe.size,
                    superLikes = 0 // Can be implemented later if needed
                )
            }

            SectionTitle(
                icon = Icons.Filled.AutoAwesome,
                title = "New Matches",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 26.sp,
                iconSize = 26.dp
            )

            // Show mutual matches (users who liked each other)
            if (uiState.mutualMatches.isEmpty()) {
                EmptyMatchesMessage(message = "No new matches yet. Keep swiping!")
            } else {
                MatchesSection(matches = uiState.mutualMatches.map { user ->
                    MatchProfile(
                        initials = user.displayName.take(1).uppercase(),
                        name = user.displayName,
                        age = 25, // Age can be calculated from profile if available
                        city = user.profile?.city ?: "Unknown",
                        isOnline = false // Can be implemented later if needed
                    )
                })
            }

            SectionTitle(
                icon = Icons.Filled.Favorite,
                title = "People Who Liked You",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 26.sp,
                iconSize = 26.dp
            )

            // Show users who liked the current user
            if (uiState.isLoading) {
                LoadingMatchesMessage()
            } else if (uiState.usersWhoLikedMe.isEmpty()) {
                EmptyMatchesMessage(message = "No one has liked you yet. Keep improving your profile!")
            } else {
                RealMatchRequestList(
                    users = uiState.usersWhoLikedMe,
                    onAccept = onAcceptUser,
                    onReject = onRejectUser
                )
            }
        }
    }
}

@Composable
private fun EmptyMatchesMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LoadingMatchesMessage() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading matches...",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun RealMatchRequestList(
    users: List<User>,
    onAccept: (User) -> Unit,
    onReject: (User) -> Unit
) {
    Column {
        users.forEach { user ->
            val profile = user.profile
            if (profile != null) {
                MatchRequestCard(
                    request = MatchRequest(
                        initials = user.displayName.take(1).uppercase(),
                        name = user.displayName,
                        age = 25, // Age can be calculated from profile if available
                        city = profile.city,
                        timeAgo = "Recently" // Can be calculated from interaction timestamp
                    ),
                    onAccept = { onAccept(user) },
                    onReject = { onReject(user) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MatchesPageScreenPreview() {
    MakeItSoTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1200.dp)
        ) {
            // Create sample data for preview
            val sampleUiState = MatchesUiState(
                isLoading = false,
                usersWhoLikedMe = emptyList(),
                mutualMatches = emptyList(),
                errorMessage = null
            )
            
            MatchesPageScreenContent(
                uiState = sampleUiState,
                currentRoute = "matches",
                onNavigate = {},
                onAcceptUser = {},
                onRejectUser = {},
                onRefresh = {},
                showErrorSnackbar = {}
            )
        }
    }
}