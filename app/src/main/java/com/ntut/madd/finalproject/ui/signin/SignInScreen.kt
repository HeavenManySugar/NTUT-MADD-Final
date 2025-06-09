package com.ntut.madd.finalproject.ui.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ntut.madd.finalproject.R
import com.ntut.madd.finalproject.data.model.ErrorMessage
import com.ntut.madd.finalproject.ui.theme.MakeItSoTheme
import com.ntut.madd.finalproject.ui.component.*
import kotlinx.serialization.Serializable
import androidx.compose.material3.TextButton
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.res.stringResource

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
    val onGoogleSignInClick: () -> Unit = { /* TODO: Google login */ }
    val onForgotPasswordClick: () -> Unit = { /* TODO: Forgot password */ }
    val onSignUpClick: () -> Unit = openSignUpScreen
    var showPassword by remember { mutableStateOf(false) }

    var shouldValidate by remember { mutableStateOf(true) }

    val onSignInClick: () -> Unit = {
        if (
            email.isNotBlank() &&
            password.isNotBlank()
        ) {
            signIn(email, password, showErrorSnackbar)
        } else {
            shouldValidate = false
            showErrorSnackbar(ErrorMessage.StringError("Please Fill All Blank"))
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeaderBanner(
            title = "This is our App",
            subtitle = "Quick for match"
        )

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
                // Email
                LabeledField(
                    label = stringResource(R.string.email),
                    value = email,
                    onValueChange = onEmailChange,
                    placeholder = stringResource(R.string.enter_email),
                    fontSize = 18.sp,
                    shouldShowError = shouldValidate || email.isNotBlank()
                )

                // Password

                LabeledField(
                    label = stringResource(R.string.password),
                    value = password,
                    onValueChange = onPasswordChange,
                    placeholder = stringResource(R.string.enter_password),
                    fontSize = 18.sp,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (showPassword) stringResource(R.string.hide_password) else stringResource(R.string.show_password)
                            )
                        }
                    },
                    shouldShowError = shouldValidate || password.isNotBlank()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onForgotPasswordClick,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(stringResource(R.string.forget_password), color = Color(0xFF5A5AFF),fontWeight = FontWeight.Medium)
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Sign-in button
                PressButton(
                    text = stringResource(R.string.sign_in),
                    onClick = onSignInClick,
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(50.dp)
                )

                Spacer(Modifier.height(8.dp))

                DividerWithText(
                    stringResource(R.string.using_other_way_to_sign_in),
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
                        text = stringResource(R.string.have_no_account),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                    TextButton(
                        onClick = onSignUpClick,
                        contentPadding = PaddingValues(0.dp), // ✅ 移除內邊距讓字靠近
                        modifier = Modifier.padding(start = 4.dp) // ✅ 加一點間距
                    ) {
                        Text(
                            text = stringResource(R.string.click_here_sign_up),
                            color = Color(0xFF5A5AFF),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
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
