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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.*
import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import java.text.SimpleDateFormat
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.res.stringResource
import java.util.Calendar
import java.util.Locale

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
    var repeatPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var first_name by remember { mutableStateOf("") }
    var last_name by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }
    var birthday by remember { mutableStateOf<Calendar?>(null) }
    var showPassword by remember { mutableStateOf(false) }
    var showRepeatPassword by remember { mutableStateOf(false) }

    var shouldValidate by remember { mutableStateOf(true) }

    val onSignUpClick: () -> Unit = {
        if (
            email.isNotBlank() &&
            password.isNotBlank() &&
            repeatPassword.isNotBlank() &&
            phone.isNotBlank() &&
            isChecked &&
            password == repeatPassword
        ) {
            signUp(email, password, repeatPassword, showErrorSnackbar)
        } else {
            shouldValidate = false
            showErrorSnackbar(ErrorMessage.StringError("Please Fill All Blank And Check The Password Is Same"))
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        AppHeaderBanner(
            title = "This is our App",
            subtitle = "Quick for match"
        )

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
                    LabeledFieldMedium(
                        label = stringResource(R.string.last_name),
                        value = first_name,
                        onValueChange = { first_name = it },
                        placeholder = stringResource(R.string.enter_last_name),
                        fontSize = 14.sp
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    LabeledFieldMedium(
                        label = stringResource(R.string.first_name),
                        value = last_name,
                        onValueChange = { last_name = it },
                        placeholder = stringResource(R.string.enter_first_name),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            LabeledField(
                label = stringResource(R.string.email),
                value = email,
                onValueChange = {
                    email = it
                },
                placeholder = stringResource(R.string.enter_email),
                fontSize = 18.sp,
                shouldShowError = shouldValidate || email.isNotBlank(),
            )

            LabeledField(
                label = stringResource(R.string.phone_number),
                value = phone,
                onValueChange = {
                    phone = it
                },
                placeholder = stringResource(R.string.enter_phone_number),
                fontSize = 18.sp,
                shouldShowError = shouldValidate || phone.isNotBlank(),
            )

            InputFieldLabel(
                text = stringResource(R.string.birthday),
                modifier = Modifier.fillMaxWidth(0.85f)
            )

            BirthdayInput(
                birthday = birthday,
                onDateSelected = { birthday = it },
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Spacer(Modifier.height(16.dp))

            LabeledField(
                label = stringResource(R.string.password),
                value = password,
                onValueChange = {
                    password = it
                },
                placeholder = stringResource(R.string.enter_password),
                fontSize = 18.sp,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                shouldShowError = shouldValidate || password.isNotBlank(),
            )

            if (password.isNotEmpty()) {
                PasswordStrengthIndicator(password)
            }

            LabeledField(
                label = stringResource(R.string.confirm_password),
                value = repeatPassword,
                onValueChange = {
                    repeatPassword = it
                },
                placeholder = stringResource(R.string.enter_confirm_password),
                fontSize = 18.sp,
                visualTransformation = if (showRepeatPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showRepeatPassword = !showRepeatPassword }) {
                        Icon(
                            imageVector = if (showRepeatPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                shouldShowError = shouldValidate || repeatPassword.isNotBlank(),
                shouldRepeatPasswordError = password.isNotBlank() &&
                        repeatPassword.isNotBlank() &&
                        password != repeatPassword
            )

            TermsCheckbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                onTermsClick = { },
                onPrivacyClick = { }
            )

            PressButton(
                text = stringResource(R.string.sign_up),
                onClick = onSignUpClick,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(50.dp)
            )

            Spacer(Modifier.height(8.dp))

            DividerWithText(
                stringResource(R.string.using_other_way_to_sign_up),
                textColor = Color(0xFF6C757D)
            )

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = { /* TODO: Google Sign-In */ },
                modifier = Modifier
                    .wrapContentSize()
                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sign_up_with_google4),
                    contentDescription = "Google Sign-In",
                    modifier = Modifier
                        .height(40.dp)
                        .wrapContentWidth(),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.have_account),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
                TextButton(
                    onClick = openSignInScreen,
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.click_here_sign_in),
                        color = Color(0xFF5A5AFF),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


@Composable
fun BirthdayInput(
    birthday: Calendar?,
    onDateSelected: (Calendar) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val formatter = remember {
        SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    }

    val datePickerDialog = remember {
        val cal = birthday ?: Calendar.getInstance().apply {
            set(2025, 1, 1)
        }

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val newCal = Calendar.getInstance()
                newCal.set(year, month, dayOfMonth)
                onDateSelected(newCal)
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .border(1.dp, Color(0xFFEAECEF), RoundedCornerShape(8.dp))
            .background(Color(0xFFF8F9FA), RoundedCornerShape(8.dp))
            .clickable { datePickerDialog.show() }
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = birthday?.let { formatter.format(it.time) } ?: "YY/MM/DD",
                fontSize = 18.sp,
                color = if (birthday == null) Color(0xFFB0B0B0) else Color.Black
            )

            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Choose your birthday",
                tint = Color.Black
            )
        }
    }
}



/* Privacy  😍🥰😘 */
@Composable
fun TermsCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit
) {
    val annotatedText = buildAnnotatedString {
        append("I have read and agree")

        pushStringAnnotation(tag = "TERMS", annotation = "terms")
        withStyle(SpanStyle(color = Color(0xFF5A5AFF), fontWeight = FontWeight.Medium)) {
            append(" Terms of Use")
        }
        pop()

        append(" and ")

        pushStringAnnotation(tag = "PRIVACY", annotation = "privacy")
        withStyle(SpanStyle(color = Color(0xFF5A5AFF), fontWeight = FontWeight.Medium)) {
            append("Privacy Policy")
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

/* Strong for password */

enum class PasswordStrength(val label: String, val level: Int, val color: Color) {
    WEAK("Weak", 1, Color.Red),
    MEDIUM("Medium", 2, Color(0xFFFFC107)), // Amber
    STRONG("Strong", 3, Color(0xFF4CAF50))  // Green
}

fun getPasswordStrength(password: String): PasswordStrength {
    val length = password.length

    if (length < 6) return PasswordStrength.WEAK

    val hasLetter = password.any { it.isLetter() }
    val hasDigit = password.any { it.isDigit() }
    val hasSpecial = password.any { "!@#\$%^&*()_+".contains(it) }

    return when {
        length >= 8 && hasLetter && hasDigit && hasSpecial -> PasswordStrength.STRONG
        length >= 6 && hasLetter && hasDigit -> PasswordStrength.MEDIUM
        else -> PasswordStrength.WEAK
    }
}

@Composable
fun PasswordStrengthIndicator(password: String) {
    val strength = remember(password) { getPasswordStrength(password) }

    Column {
        // 顏色條
        Row(
            Modifier
                .fillMaxWidth(0.8f)
                .height(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray)
        ) {
            repeat(3) { index ->
                val active = index < strength.level
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            if (active) strength.color else Color.LightGray
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 強度文字
        Text(
            text = "Password Strength：${strength.label}",
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun SignUpScreenPreview() {
    MakeItSoTheme(darkTheme = true) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(1200.dp)
        ) {
            SignUpScreenContent(
                openSignInScreen = {},
                signUp = { _, _, _, _ -> },
                showErrorSnackbar = {}
            )
        }
    }
}