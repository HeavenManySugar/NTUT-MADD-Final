package com.ntut.madd.finalproject.ui.setup6

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ntut.madd.finalproject.R
import com.ntut.madd.finalproject.ui.setup.components.SetupPageContainer
import com.ntut.madd.finalproject.ui.theme.MakeItSoTheme
import kotlinx.serialization.Serializable

@Serializable
object Setup6Route

@Composable
fun Setup6Screen(
    onNext: () -> Unit,
    onBack: () -> Unit,
    viewModel: Setup6ViewModel = hiltViewModel()
) {
    val aboutMe by viewModel.aboutMe.collectAsStateWithLifecycle()
    val lookingFor by viewModel.lookingFor.collectAsStateWithLifecycle()
    val isFormValid by viewModel.isFormValid.collectAsStateWithLifecycle()

    Setup6ScreenContent(
        aboutMe = aboutMe,
        lookingFor = lookingFor,
        isFormValid = isFormValid,
        onAboutMeChange = viewModel::updateAboutMe,
        onLookingForChange = viewModel::updateLookingFor,
        getAboutMeCharacterCount = viewModel::getAboutMeCharacterCount,
        getLookingForCharacterCount = viewModel::getLookingForCharacterCount,
        onNext = onNext,
        onBack = onBack
    )
}

@Composable
fun Setup6ScreenContent(
    aboutMe: String,
    lookingFor: String,
    isFormValid: Boolean,
    onAboutMeChange: (String) -> Unit,
    onLookingForChange: (String) -> Unit,
    getAboutMeCharacterCount: () -> String,
    getLookingForCharacterCount: () -> String,
    onNext: () -> Unit,
    onBack: () -> Unit
) {

    SetupPageContainer(
        currentStep = 6,
        totalSteps = 6,
        onBackClick = onBack,
        headerIcon = "📝",
        headerTitle = stringResource(R.string.setup6_title),
        headerSubtitle = "完善您的個人檔案",
        isFormValid = isFormValid,
        onNextClick = onNext,
        nextButtonText = R.string.setup6_complete_button
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 關於我輸入區域
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.setup6_about_me_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                OutlinedTextField(
                    value = aboutMe,
                    onValueChange = onAboutMeChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    placeholder = { 
                        Text(
                            text = stringResource(R.string.setup6_about_me_placeholder),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        ) 
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    maxLines = 6,
                    supportingText = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = getAboutMeCharacterCount(),
                                style = MaterialTheme.typography.bodySmall,
                                color = if (aboutMe.length > 450) 
                                    MaterialTheme.colorScheme.error 
                                else 
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }

            // 我在尋找輸入區域
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.setup6_looking_for_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                OutlinedTextField(
                    value = lookingFor,
                    onValueChange = onLookingForChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    placeholder = { 
                        Text(
                            text = stringResource(R.string.setup6_looking_for_placeholder),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        ) 
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    maxLines = 4,
                    supportingText = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = getLookingForCharacterCount(),
                                style = MaterialTheme.typography.bodySmall,
                                color = if (lookingFor.length > 270) 
                                    MaterialTheme.colorScheme.error 
                                else 
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }

            // 提示文字
            Text(
                text = stringResource(R.string.setup6_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun Setup6ScreenPreview() {
    MakeItSoTheme {
        Setup6ScreenContent(
            aboutMe = "我是一個熱愛學習新技術的軟體工程師，喜歡閱讀、旅行和攝影。平時喜歡參與開源專案，希望能透過技術讓世界變得更美好。",
            lookingFor = "尋找志同道合的夥伴，一起討論技術、分享經驗。",
            isFormValid = true,
            onAboutMeChange = { },
            onLookingForChange = { },
            getAboutMeCharacterCount = { "87/500" },
            getLookingForCharacterCount = { "24/300" },
            onNext = { },
            onBack = { }
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun Setup6ScreenEmptyPreview() {
    MakeItSoTheme {
        Setup6ScreenContent(
            aboutMe = "",
            lookingFor = "",
            isFormValid = false,
            onAboutMeChange = { },
            onLookingForChange = { },
            getAboutMeCharacterCount = { "0/500" },
            getLookingForCharacterCount = { "0/300" },
            onNext = { },
            onBack = { }
        )
    }
}
