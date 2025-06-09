package com.ntut.madd.finalproject.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ntut.madd.finalproject.R
import kotlinx.serialization.Serializable

@Serializable
object SplashRoute

@Composable
fun SplashScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    onNavigateToSetup: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val navigationTarget by viewModel.navigationTarget.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.checkAuthStatus()
    }

    LaunchedEffect(navigationTarget) {
        when (navigationTarget) {
            NavigationTarget.Home -> {
                viewModel.onNavigationHandled()
                onNavigateToHome()
            }
            NavigationTarget.SignIn -> {
                viewModel.onNavigationHandled()
                onNavigateToSignIn()
            }
            NavigationTarget.Setup -> {
                viewModel.onNavigationHandled()
                onNavigateToSetup()
            }
            NavigationTarget.None -> {
                // 保持在啟動頁面
            }
        }
    }

    SplashScreenContent()
}

@Composable
private fun SplashScreenContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF667EEA),
                        Color(0xFF764BA2)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = "App Logo",
                modifier = Modifier.size(128.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "This is our App",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Quick for match",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}
