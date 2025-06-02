package com.ntut.madd.finalproject.ui.signup


import com.ntut.madd.finalproject.R

import com.ntut.madd.finalproject.data.model.ErrorMessage
import com.ntut.madd.finalproject.ui.component.*
import com.ntut.madd.finalproject.ui.theme.MakeItSoTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable
import androidx.compose.foundation.border
import androidx.compose.material3.Checkbox
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.layout.*
import androidx.compose.ui.text.*

@Serializable
object SignUpRoute

@Composable
fun SignUpScreen(
    openHomeScreen: () -> Unit,
    openSignInScreen: () -> Unit,
    showErrorSnackbar: (ErrorMessage) -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val shouldRestartApp by viewModel.shouldRestartApp.collectAsStateWithLifecycle()

    if (shouldRestartApp) {
        openHomeScreen()
    } else {
        SignUpScreenContent(
            openSignInScreen = openSignInScreen,
            signUp = viewModel::signUp,
            showErrorSnackbar = showErrorSnackbar
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SignUpScreenContent(
    openSignInScreen: () -> Unit,
    signUp: (String, String, String, (ErrorMessage) -> Unit) -> Unit,
    showErrorSnackbar: (ErrorMessage) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeat_password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var first_name by remember { mutableStateOf("") }
    var last_name by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }

    // 事件 lambda
    val onEmailChange: (String) -> Unit = { email = it }
    val onPasswordChange: (String) -> Unit = { password = it }
    val onSignUpClick: () -> Unit = { signUp(email, password, repeat_password, showErrorSnackbar) }
    val onGoogleSignInClick: () -> Unit = { /* TODO: Google login */ }
    val onForgotPasswordClick: () -> Unit = { /* TODO: Forgot password */ }
    val onSignInClick: () -> Unit = openSignInScreen
    val onPhoneChange: (String) -> Unit = { phone = it }
    val onFNameChange: (String) -> Unit = { first_name = it }
    val onLNameChange: (String) -> Unit = { last_name = it }
    val onRPasswordChange: (String) -> Unit = { repeat_password = it }
    val onIsCheckedChange: (Boolean) -> Unit = { isChecked = it }


    // 可以滾動
    val scrollState = rememberScrollState()

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF764BA2),
                            Color(0xFF667EEA)
                        )
                    )
                )
                .padding(vertical = 48.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(128.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text("This is our app", fontSize = 24.sp, color = Color.White)
                Text("Quick for match", fontSize = 16.sp, color = Color.White)
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    LabeledInputField(
                        label = "名字",
                        value = first_name,
                        onValueChange = onFNameChange,
                        placeholder = "請輸入名字"
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    LabeledInputField(
                        label = "姓氏",
                        value = last_name,
                        onValueChange = onLNameChange,
                        placeholder = "請輸入姓氏"
                    )
                }
            }

            // Email
            InputFieldLabel(
                text = "電子信箱",
                modifier = Modifier.fillMaxWidth(0.85f)
            )

            Spacer(Modifier.height(4.dp))

            LabeledInputBox(
                value = email,
                onValueChange = onEmailChange,
                placeholder = "請輸入您的電子郵件",
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Spacer(Modifier.height(16.dp))

            // Email
            InputFieldLabel(
                text = "手機號碼",
                modifier = Modifier.fillMaxWidth(0.85f)
            )

            Spacer(Modifier.height(4.dp))

            LabeledInputBox(
                value = phone,
                onValueChange = onPhoneChange,
                placeholder = "請輸入您的手機號碼",
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Spacer(Modifier.height(16.dp))

            // Password
            InputFieldLabel(
                text = "密碼",
                modifier = Modifier.fillMaxWidth(0.85f)
            )

            LabeledInputBox(
                value = password,
                onValueChange = onPasswordChange,
                placeholder = "請輸入您的密碼",
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Spacer(Modifier.height(8.dp))

            // Privacy Check
            TermsCheckbox(
                checked = isChecked,
                onCheckedChange = onIsCheckedChange,
                onTermsClick = { /* 開啟使用條款 */ },
                onPrivacyClick = { /* 開啟隱私權政策 */ }
            )

            // Check Password
            InputFieldLabel(
                text = "確認密碼",
                modifier = Modifier.fillMaxWidth(0.85f)
            )

            LabeledInputBox(
                value = repeat_password,
                onValueChange = onRPasswordChange,
                placeholder = "再次確認密碼",
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Spacer(Modifier.height(8.dp))

            // Sign-in button
            Button(
                onClick = onSignUpClick,
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
                    text = "立即註冊",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(8.dp))

            DividerWithText(
                "或使用以下方式註冊",
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
                    painter = painterResource(id = R.drawable.sign_up_with_google4),
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
                    text = "已經有帳號了嗎？",
                    fontSize = 14.sp
                )
                TextButton(
                    onClick = onSignInClick,
                    contentPadding = PaddingValues(0.dp), // ✅ 移除內邊距讓字靠近
                    modifier = Modifier.padding(start = 4.dp) // ✅ 加一點間距
                ) {
                    Text(
                        text = "立刻登入",
                        color = Color(0xFF5A5AFF),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}


/* 說明隱私權  😍🥰😘 */
@Composable
fun TermsCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit
) {
    val annotatedText = buildAnnotatedString {
        append("我已閱讀並同意")

        pushStringAnnotation(tag = "TERMS", annotation = "terms")
        withStyle(SpanStyle(color = Color(0xFF5A5AFF), fontWeight = FontWeight.Medium)) {
            append(" 使用條款")
        }
        pop()

        append(" 和 ")

        pushStringAnnotation(tag = "PRIVACY", annotation = "privacy")
        withStyle(SpanStyle(color = Color(0xFF5A5AFF), fontWeight = FontWeight.Medium)) {
            append("隱私權政策")
        }
        pop()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .padding(vertical = 8.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Box(modifier = Modifier.weight(1f)) {
            ClickableText(
                text = annotatedText,
                style = TextStyle(fontSize = 14.sp, color = Color.Black),
                modifier = Modifier.padding(top = 14.dp),
                onClick = { offset ->
                    annotatedText.getStringAnnotations("TERMS", offset, offset).firstOrNull()?.let {
                        onTermsClick()
                    }
                    annotatedText.getStringAnnotations("PRIVACY", offset, offset).firstOrNull()?.let {
                        onPrivacyClick()
                    }
                }
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun SignUpScreenPreview() {
    MakeItSoTheme(darkTheme = true) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(1200.dp) // ✅ 預估高度夠容納整個註冊表單
        ) {
            SignUpScreenContent(
                openSignInScreen = {},
                signUp = { _, _, _, _ -> },
                showErrorSnackbar = {}
            )
        }
    }
}