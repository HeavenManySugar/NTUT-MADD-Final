package com.ntut.madd.finalproject.ui.main

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ntut.madd.finalproject.data.model.ErrorMessage
import com.ntut.madd.finalproject.ui.discover.DiscoverPageScreen
import com.ntut.madd.finalproject.ui.matches.MatchesPageScreen
import com.ntut.madd.finalproject.ui.message.MessagePageScreen
import com.ntut.madd.finalproject.ui.profilepage.ProfilePageScreen
import kotlinx.serialization.Serializable

@Serializable
object MainPageRoute

@Composable
fun MainPageScreen(
    openHomeScreen: () -> Unit,
    openSettingsScreen: () -> Unit,
    openChatScreen: (String) -> Unit,
    openUserProfile: (String) -> Unit,
    showErrorSnackbar: (ErrorMessage) -> Unit,
    viewModel: MainPageViewModel = hiltViewModel()
) {
    val currentRoute by viewModel.currentRoute.collectAsStateWithLifecycle()
    val shouldRestartApp by viewModel.shouldRestartApp.collectAsStateWithLifecycle()

    if (shouldRestartApp) {
        openHomeScreen()
    } else {
        MainPageContent(
            currentRoute = currentRoute,
            onNavigate = viewModel::navigateTo,
            openHomeScreen = openHomeScreen,
            openSettingsScreen = openSettingsScreen,
            openChatScreen = openChatScreen,
            openUserProfile = openUserProfile,
            showErrorSnackbar = showErrorSnackbar
        )
    }
}

@Composable
private fun MainPageContent(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    openHomeScreen: () -> Unit,
    openSettingsScreen: () -> Unit,
    openChatScreen: (String) -> Unit,
    openUserProfile: (String) -> Unit,
    showErrorSnackbar: (ErrorMessage) -> Unit
) {
    when (currentRoute) {
        "discover" -> DiscoverPageScreen(
            openHomeScreen = openHomeScreen,
            showErrorSnackbar = showErrorSnackbar,
            currentRoute = currentRoute,
            onNavigate = onNavigate
        )
        "matches" -> MatchesPageScreen(
            openHomeScreen = openHomeScreen,
            openUserProfile = openUserProfile,
            showErrorSnackbar = showErrorSnackbar,
            currentRoute = currentRoute,
            onNavigate = onNavigate
        )
        "messages" -> MessagePageScreen(
            openHomeScreen = openHomeScreen,
            showErrorSnackbar = showErrorSnackbar,
            currentRoute = currentRoute,
            onNavigate = onNavigate,
            openChatScreen = openChatScreen
        )
        "profile" -> ProfilePageScreen(
            openHomeScreen = openHomeScreen,
            openSettingsScreen = openSettingsScreen,
            showErrorSnackbar = showErrorSnackbar,
            currentRoute = currentRoute,
            onNavigate = onNavigate
        )
    }
}
