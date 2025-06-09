package com.ntut.madd.finalproject.ui.discover

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
object DiscoverPageRoute




@Composable
fun DiscoverPageScreen(
    openHomeScreen: () -> Unit,
    showErrorSnackbar: (ErrorMessage) -> Unit,
    currentRoute: String = "profile",
    onNavigate: (String) -> Unit = {},
    viewModel: DiscoverPageViewModel = hiltViewModel()
) {
    val shouldRestartApp by viewModel.shouldRestartApp.collectAsStateWithLifecycle()

    if (shouldRestartApp) {
        openHomeScreen()
    } else {
        DiscoverPageScreenContent(
            currentRoute = currentRoute,
            onNavigate = onNavigate
        )
    }
}

@Composable
fun DiscoverPageScreenContent(
    currentRoute: String = "discover",
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

            // 下面
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF2F2F2))
            ){
                RoundedWhiteCard {
                    TopSection(
                        name = "Alex",
                        location = "New York",
                        jobTitle = "Software Engineer",
                        education = "Bachelor's"
                    )
                    InterestSection(
                        listOf("🎵" to "J-pop Music", "🏃" to "Fitness", "🎤" to "Singing", "📚" to "Reading")
                    )
                    PersonalitySection(listOf("🌟 Optimistic", "🎯 Ambitious", "🤝 Outgoing"))
                }
                DecisionButtons(
                    onReject = { /* TODO: Handle rejection */ },
                    onApprove = { /* TODO: Handle approval */ }
                )
            }
        }
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
            DiscoverPageScreenContent(
                currentRoute = "discover",
                onNavigate = {}
            )
        }
    }
}