package com.ntut.madd.finalproject.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
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
    // 動畫狀態
    val animationDuration = 1000
    var startAnimation by remember { mutableStateOf(false) }
    
    // Logo 縮放動畫
    val logoScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "logoScale"
    )
    
    // Logo 旋轉動畫
    val logoRotation by animateFloatAsState(
        targetValue = if (startAnimation) 360f else 0f,
        animationSpec = tween(
            durationMillis = animationDuration,
            easing = FastOutSlowInEasing
        ),
        label = "logoRotation"
    )
    
    // 標題漸入動畫
    val titleAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = 300,
            easing = LinearEasing
        ),
        label = "titleAlpha"
    )
    
    // 標題滑入動畫
    val titleOffset by animateIntAsState(
        targetValue = if (startAnimation) 0 else 50,
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = 300,
            easing = FastOutSlowInEasing
        ),
        label = "titleOffset"
    )
    
    // 副標題漸入動畫
    val subtitleAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = 600,
            easing = LinearEasing
        ),
        label = "subtitleAlpha"
    )
    
    // 副標題滑入動畫
    val subtitleOffset by animateIntAsState(
        targetValue = if (startAnimation) 0 else 30,
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = 600,
            easing = FastOutSlowInEasing
        ),
        label = "subtitleOffset"
    )

    // 啟動動畫
    LaunchedEffect(Unit) {
        startAnimation = true
    }

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
            // Logo 動畫 - 縮放 + 旋轉
            Image(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(128.dp)
                    .scale(logoScale)
                    .rotate(logoRotation)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 標題動畫 - 漸入 + 滑入
            Text(
                text = "This is our App",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .alpha(titleAlpha)
                    .offset(y = titleOffset.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 副標題動畫 - 漸入 + 滑入
            Text(
                text = "Quick for match",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier
                    .alpha(subtitleAlpha)
                    .offset(y = subtitleOffset.dp)
            )
        }
    }
}
