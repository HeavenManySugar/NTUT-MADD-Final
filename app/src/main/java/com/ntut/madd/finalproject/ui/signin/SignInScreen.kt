package com.ntut.madd.finalproject.ui.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
    showErrorSnackbar: (ErrorMessage) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFF6A8DFF))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(96.dp)
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
                .weight(2f)
                .fillMaxWidth()
                .background(Color(0xFFFFFFF))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("請輸入你的電子郵件") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(8.dp))
                )
                Spacer(Modifier.height(16.dp))

                // Password
                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = { Text("請輸入你的密碼") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(8.dp))
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onForgotPasswordClick) {
                        Text("忘記密碼？", color = Color(0xFF5A5AFF))
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Sign-in button
                Button(
                    onClick = onSignInClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7A63D2),
                        contentColor = Color.White
                    )
                ) { Text("登入") }

                Spacer(Modifier.height(16.dp))

                DividerWithText("或使用以下方式登入")

                Spacer(Modifier.height(16.dp))

                // Google sign-in
                OutlinedButton(
                    onClick = onGoogleSignInClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.app_icon),
                        contentDescription = "Google Logo",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("使用 Google 帳號登入")
                }

                Spacer(Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("還沒有帳號？")
                    TextButton(onClick = onSignUpClick) {
                        Text("立刻註冊", color = Color(0xFF5A5AFF))
                    }
                }
            }
        }
    }
}

/**　中間帶文字的 divider　*/
@Composable
private fun DividerWithText(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Divider(Modifier.weight(1f), color = Color.White.copy(alpha = .3f))
        Text(
            text,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Divider(Modifier.weight(1f), color = Color.White.copy(alpha = .3f))
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SignInScreenPreview() {
    MakeItSoTheme {
        SignInScreenContent(
            openSignUpScreen = {},
            signIn = { _, _, _ -> },
            showErrorSnackbar = {}
        )
    }
}
