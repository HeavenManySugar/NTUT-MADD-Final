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
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

import com.ntut.madd.finalproject.data.model.ErrorMessage // 你的 ErrorMessage 定義

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
    currentRoute: String = "profile",
    onNavigate: (String) -> Unit = {},
    viewModel: MatchesPageViewModel = hiltViewModel()
) {
    val shouldRestartApp by viewModel.shouldRestartApp.collectAsStateWithLifecycle()

    if (shouldRestartApp) {
        openHomeScreen()
    } else {
        MatchesPageScreenContent(
            currentRoute = currentRoute,
            onNavigate = onNavigate
        )
    }
}

@Composable
fun MatchesPageScreenContent(
    currentRoute: String = "matches",
    onNavigate: (String) -> Unit = {}
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
            GradientBackgroundBox(useGradient = false) {
                MyMatchesStats(
                    newMatches = 12,
                    totalLikes = 45,
                    superLikes = 8
                )
            }

            SectionTitle(
                icon = Icons.Filled.AutoAwesome,
                title = "New Matches",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 26.sp,
                iconSize = 26.dp
            )

            MatchesSection(matches = listOf(
                MatchProfile("A", "Alex Chen", 25, "Taipei City", true),
                MatchProfile("R", "Ryan", 26, "New Taipei City", false),
                MatchProfile("A", "Sophie", 25, "Taichung City", false)
            ))

            SectionTitle(
                icon = Icons.Filled.Favorite,
                title = "People Who Liked You",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 26.sp,
                iconSize = 26.dp
            )

            MatchRequestList(requests = listOf(
                MatchRequest("J", "Jessica", 24, "Taipei City", "2 hours ago"),
                MatchRequest("E", "Ethan", 26, "Kaohsiung City", "10 mins ago"),
                MatchRequest("M", "Mina", 22, "Taichung City", "1 hour ago")
            ))
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
            MatchesPageScreenContent(
                currentRoute = "matches",
                onNavigate = {}
            )
        }
    }
}