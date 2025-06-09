package com.ntut.madd.finalproject

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ntut.madd.finalproject.data.model.ErrorMessage

import com.ntut.madd.finalproject.ui.main.MainPageRoute
import com.ntut.madd.finalproject.ui.main.MainPageScreen
import com.ntut.madd.finalproject.ui.settings.SettingsRoute
import com.ntut.madd.finalproject.ui.settings.SettingsScreen
import com.ntut.madd.finalproject.ui.setup.SetupRoute
import com.ntut.madd.finalproject.ui.setup.SetupScreen
import com.ntut.madd.finalproject.ui.setup2.Setup2Route
import com.ntut.madd.finalproject.ui.setup2.Setup2Screen
import com.ntut.madd.finalproject.ui.setup3.Setup3Route
import com.ntut.madd.finalproject.ui.setup3.Setup3Screen
import com.ntut.madd.finalproject.ui.setup4.Setup4Route
import com.ntut.madd.finalproject.ui.setup4.Setup4Screen
import com.ntut.madd.finalproject.ui.setup5.Setup5Route
import com.ntut.madd.finalproject.ui.setup5.Setup5Screen
import com.ntut.madd.finalproject.ui.setup6.Setup6Route
import com.ntut.madd.finalproject.ui.setup6.Setup6Screen
import com.ntut.madd.finalproject.ui.signin.SignInRoute
import com.ntut.madd.finalproject.ui.signin.SignInScreen
import com.ntut.madd.finalproject.ui.signup.SignUpRoute
import com.ntut.madd.finalproject.ui.signup.SignUpScreen
import com.ntut.madd.finalproject.ui.splash.SplashRoute
import com.ntut.madd.finalproject.ui.splash.SplashScreen
import com.ntut.madd.finalproject.ui.theme.MakeItSoTheme

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setSoftInputMode()

        setContent {
            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }
            val navController = rememberNavController()

            MakeItSoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = SplashRoute,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable<SplashRoute> { SplashScreen(
                                onNavigateToHome = {
                                    navController.navigate(MainPageRoute) {
                                        popUpTo<SplashRoute> { inclusive = true }
                                        launchSingleTop = true
                                    }
                                },
                                onNavigateToSignIn = {
                                    navController.navigate(SignInRoute) {
                                        popUpTo<SplashRoute> { inclusive = true }
                                        launchSingleTop = true
                                    }
                                },
                                onNavigateToSetup = {
                                    navController.navigate(SetupRoute) {
                                        popUpTo<SplashRoute> { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            ) }
                            composable<MainPageRoute> { MainPageScreen(
                                openHomeScreen = {
                                    navController.navigate(MainPageRoute) { launchSingleTop = true }
                                },
                                openSettingsScreen = {
                                    navController.navigate(SettingsRoute) { launchSingleTop = true }
                                },
                                showErrorSnackbar = { errorMessage ->
                                    val message = getErrorMessage(errorMessage)
                                    scope.launch { snackbarHostState.showSnackbar(message) }
                                }
                            ) }
                            composable<SettingsRoute> { SettingsScreen(
                                openHomeScreen = {
                                    navController.navigate(MainPageRoute) { launchSingleTop = true }
                                },
                                openSignInScreen = {
                                    navController.navigate(SignInRoute) { launchSingleTop = true }
                                }
                            ) }
                            composable<SignInRoute> { SignInScreen(
                                openHomeScreen = {
                                    navController.navigate(MainPageRoute) { launchSingleTop = true }
                                },
                                openSignUpScreen = {
                                    navController.navigate(SignUpRoute) { launchSingleTop = true }
                                },
                                openSetupScreen = {
                                    navController.navigate(SetupRoute) { launchSingleTop = true }
                                },
                                showErrorSnackbar = { errorMessage ->
                                    val message = getErrorMessage(errorMessage)
                                    scope.launch { snackbarHostState.showSnackbar(message) }
                                }
                            ) }
                            composable<SignUpRoute> { SignUpScreen(
                                openHomeScreen = {
                                    navController.navigate(MainPageRoute) { launchSingleTop = true }
                                },
                                openSignInScreen = {
                                    navController.navigate(SignInRoute) { launchSingleTop = true }
                                },
                                showErrorSnackbar = { errorMessage ->
                                    val message = getErrorMessage(errorMessage)
                                    scope.launch { snackbarHostState.showSnackbar(message) }
                                }
                                ) }
                            composable<SetupRoute> { SetupScreen(
                                onBackClick = {
                                    navController.popBackStack()
                                },
                                onNextClick = {
                                    navController.navigate(Setup2Route) { launchSingleTop = true }
                                }
                            ) }
                            composable<Setup2Route> { Setup2Screen(
                                onBackClick = {
                                    navController.popBackStack()
                                },
                                onNextClick = {
                                    navController.navigate(Setup3Route) { launchSingleTop = true }
                                }
                            ) }
                            composable<Setup3Route> { Setup3Screen(
                                onBackClick = {
                                    navController.popBackStack()
                                },
                                onNextClick = {
                                    navController.navigate(Setup4Route) { launchSingleTop = true }
                                }
                            ) }
                            composable<Setup4Route> { Setup4Screen(
                                onBackClick = {
                                    navController.popBackStack()
                                },
                                onNextClick = {
                                    navController.navigate(Setup5Route) { launchSingleTop = true }
                                }
                            ) }
                            composable<Setup5Route> { Setup5Screen(
                                onBackClick = {
                                    navController.popBackStack()
                                },
                                onNextClick = {
                                    navController.navigate(Setup6Route) { launchSingleTop = true }
                                }
                            ) }
                            composable<Setup6Route> { Setup6Screen(
                                onNext = {
                                    // 完成設定，導航到主頁面 (Discover page)
                                    navController.navigate(MainPageRoute) { launchSingleTop = true }
                                },
                                onBack = {
                                    navController.popBackStack()
                                }
                            ) }
                        }
                    }
                }
            }
        }
    }

    private fun setSoftInputMode() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    private fun getErrorMessage(error: ErrorMessage): String {
        return when (error) {
            is ErrorMessage.StringError -> error.message
            is ErrorMessage.IdError -> this@MainActivity.getString(error.message)
        }
    }
}