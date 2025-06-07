package com.ntut.madd.finalproject.ui.profilepage

import androidx.compose.foundation.Image
import com.ntut.madd.finalproject.ui.profilepage.*

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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

import com.ntut.madd.finalproject.data.model.ErrorMessage // 你的 ErrorMessage 定義

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
            currentRoute = currentRoute,
            onNavigate = onNavigate
        )
    }
}

@Composable
fun ProfilePageScreenContent(
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
                .padding(innerPadding) // ✅ 讓內容不被導覽列擋到
        ) {
            // 這裡放畫面主內容
            GradientBackgroundBox {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Alex Chen",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Text(
                        text = "25y, Taipei City",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                    HighlightTag("尋找真愛中")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            /** 資料列 **/
            StatCardRow()
            Spacer(modifier = Modifier.height(24.dp))

            /** 興趣列 **/
            SectionTitle(
                icon = Icons.Filled.TrackChanges, // 你想用的 icon
                title = "我的興趣",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            InterestTag(text = "💻 程式設計",modifier = Modifier.padding(horizontal = 16.dp))
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
                .height(1200.dp) // ✅ 預估高度夠容納整個註冊表單
        ) {
            ProfilePageScreenContent(
                currentRoute = "profile",
                onNavigate = {}
            )
        }
    }
}