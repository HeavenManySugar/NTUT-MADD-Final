package com.ntut.madd.finalproject.ui.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ntut.madd.finalproject.R
import com.ntut.madd.finalproject.data.model.ErrorMessage
import com.ntut.madd.finalproject.ui.theme.MakeItSoTheme
import kotlinx.serialization.Serializable
import androidx.compose.material3.TextButton
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Serializable
object SignInRoute

@Composable
fun SignInScreen(
    openHomeScreen: () -> Unit,
    openSignUpScreen: () -> Unit,
    showErrorSnackbar: (ErrorMessage) -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val shouldRestartApp by viewModel.shouldRestartApp.collectAsStateWithLifecycle()

    if (shouldRestartApp) {
        openHomeScreen()
    } else {
        SignInScreenContent(
            openSignUpScreen = openSignUpScreen,
            signIn = viewModel::signIn,
            showErrorSnackbar = showErrorSnackbar
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SignInScreenContent(
    openSignUpScreen: () -> Unit,
    signIn: (String, String, (ErrorMessage) -> Unit) -> Unit,
    showErrorSnackbar: (ErrorMessage) -> Unit,
    isPreview: Boolean = false
) {
    var email by remember { mutableStateOf(if (isPreview) "test@example.com" else "") }
    var password by remember { mutableStateOf(if (isPreview) "12345678" else "") }


    // 事件 lambda
    val onEmailChange: (String) -> Unit = { email = it }
    val onPasswordChange: (String) -> Unit = { password = it }
    val onSignInClick: () -> Unit = { signIn(email, password, showErrorSnackbar) }
    val onGoogleSignInClick: () -> Unit = { /* TODO: Google login */ }
    val onForgotPasswordClick: () -> Unit = { /* TODO: Forgot password */ }
    val onSignUpClick: () -> Unit = openSignUpScreen

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(35f)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF764BA2), // 紫色起點
                            Color(0xFF667EEA)  // 藍色終點
                        )
                    )
                )
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
                Spacer(Modifier.height(16.dp))

                Text("This is our app", fontSize = 24.sp, color = Color.White)
                Text("Quick for match", fontSize = 16.sp, color = Color.White)
                Spacer(Modifier.height(32.dp))
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(65f)
                .fillMaxWidth()
                .background(Color(0xFFFFFFFF))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth(0.85f) // 跟輸入框一樣寬
                ) {
                    Text(
                        text = "電子信箱",
                        fontSize = 20.sp,
                        color = Color(0xFF000000),
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .offset(x = 12.dp, y = 0.dp) // ✅ 對齊下方 box 的內部 padding
                    )
                }

                Spacer(Modifier.height(4.dp))

                // Email
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(64.dp)
                        .border(
                            width = 1.dp,
                            color = Color(0xFFEAECEF),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(
                            color = Color(0xFFF8F9FA),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 8.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Column {

                        BasicTextField(
                            value = email,
                            onValueChange = onEmailChange,
                            textStyle = LocalTextStyle.current.copy(
                                color = Color.Black,
                                fontSize = 14.sp
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            decorationBox = { innerTextField ->
                                if (email.isEmpty()) {
                                    Text(
                                        text = "請輸入您的電子郵件",
                                        color = Color(0xFFB0B0B0),
                                        fontSize = 20.sp
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Box(modifier = Modifier
                    .fillMaxWidth(0.85f) // 跟輸入框一樣寬
                ) {
                    Text(
                        text = "密碼",
                        fontSize = 20.sp,
                        color = Color(0xFF000000),
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .offset(x = 12.dp, y = 0.dp) // ✅ 對齊下方 box 的內部 padding
                    )
                }

                // Password
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(64.dp)
                        .border(
                            width = 1.dp,
                            color = Color(0xFFEAECEF),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(
                            color = Color(0xFFF8F9FA),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 8.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    // 左上角浮標籤
                    Column {
                        if (password.isEmpty()) {
                            Text(
                                text = "請輸入你的密碼",
                                color = Color(0xFFB0B0B0),
                                fontSize = 20.sp
                            )
                        }

                        BasicTextField(
                            value = password,
                            onValueChange = onPasswordChange,
                            textStyle = LocalTextStyle.current.copy(
                                color = Color.Black,
                                fontSize = 14.sp
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onForgotPasswordClick,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("忘記密碼？", color = Color(0xFF5A5AFF))
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Sign-in button
                Button(
                    onClick = onSignInClick,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7A63D2),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "登入",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(8.dp))

                DividerWithText(
                    "或使用以下方式登入",
                        textColor = Color(0xFF6C757D)
                )

                Spacer(Modifier.height(8.dp))

                // Google sign-in
                OutlinedButton(
                    onClick = onGoogleSignInClick,
                    modifier = Modifier
                        .wrapContentSize()
                        .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp), // ✅ 強制取消最小寬高限制
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(0.dp)   // ✅ 拿掉預設 padding，讓圖片剛好貼齊
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sign_in_with_google4),
                        contentDescription = "Google Sign-In",
                        modifier = Modifier
                            .height(40.dp)              // ✅ 圖片給高度
                            .wrapContentWidth(),        // ✅ 宽度按比例自動決定
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically, // ✅ 垂直置中對齊文字與按鈕
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "還沒有帳號？",
                        fontSize = 14.sp
                    )
                    TextButton(
                        onClick = onSignUpClick,
                        contentPadding = PaddingValues(0.dp), // ✅ 移除內邊距讓字靠近
                        modifier = Modifier.padding(start = 4.dp) // ✅ 加一點間距
                    ) {
                        Text(
                            text = "立刻註冊",
                            color = Color(0xFF5A5AFF),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

/**　中間帶文字的 divider　*/
@Composable
private fun DividerWithText(
    text: String,
    textColor: Color = Color.White // 預設白色，可手動指定
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Divider(Modifier.weight(1f), color = textColor)
        Text(
            text,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Divider(Modifier.weight(1f), color = textColor)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SignInScreenPreview() {
    MakeItSoTheme {
        SignInScreenContent(
            openSignUpScreen = {},
            signIn = { _, _, _ -> },
            showErrorSnackbar = {},
            isPreview = false
        )
    }
}
