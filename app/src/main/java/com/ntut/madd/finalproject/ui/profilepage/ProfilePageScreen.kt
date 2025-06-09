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

    if (shouldRestartApp) {
        openHomeScreen()
    } else {
        ProfilePageScreenContent(
            openSettingsScreen = openSettingsScreen,
            currentRoute = currentRoute,
            onNavigate = onNavigate
        )
    }
}

@Composable
fun ProfilePageScreenContent(
    openSettingsScreen: () -> Unit,
    currentRoute: String = "profile",
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
                .padding(bottom = innerPadding.calculateBottomPadding())
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
                    InitialAvatar(initial = "A")
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Alex Chen",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "25y, Taipei City",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    HighlightTag(stringResource(R.string.finding_love))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ){
                /** Data Field **/
                StatCardRow(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(24.dp))

                /** Hoppy Field **/
                SectionTitle(
                    icon = Icons.Filled.TrackChanges,
                    title = stringResource(R.string.my_interest),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                InterestTagSection(
                    tags = listOf(
                        stringResource(R.string.interest_food),
                        stringResource(R.string.interest_technology),
                        stringResource(R.string.interest_cooking),
                        stringResource(R.string.interest_coffee),
                        stringResource(R.string.interest_swimming),
                        stringResource(R.string.interest_music),
                        stringResource(R.string.interest_drawing),
                        stringResource(R.string.interest_investment),
                        stringResource(R.string.interest_fitness),
                        stringResource(R.string.interest_gaming),
                    )
                )
            }
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
            ProfilePageScreenContent(
                currentRoute = "profile",
                openSettingsScreen = {},
                onNavigate = {}
            )
        }
    }
}