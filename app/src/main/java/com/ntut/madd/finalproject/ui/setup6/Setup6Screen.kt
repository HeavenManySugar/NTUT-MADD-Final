package com.ntut.madd.finalproject.ui.setup6

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.ntut.madd.finalproject.ui.setup.components.CompletionCelebration
import com.ntut.madd.finalproject.ui.setup.components.SetupPageContainer
import com.ntut.madd.finalproject.ui.theme.MakeItSoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    
    val coroutineScope = rememberCoroutineScope()
    var showCelebration by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }

    Setup6ScreenContent(
        aboutMe = aboutMe,
        lookingFor = lookingFor,
        isFormValid = isFormValid,
        isSubmitting = isSubmitting,
        onAboutMeChange = viewModel::updateAboutMe,
        onLookingForChange = viewModel::updateLookingFor,
        getAboutMeCharacterCount = viewModel::getAboutMeCharacterCount,
        getLookingForCharacterCount = viewModel::getLookingForCharacterCount,
        onNext = {
            isSubmitting = true
            // Simulate form submission delay
            coroutineScope.launch {
                delay(1500) // Simulate processing
                isSubmitting = false
                showCelebration = true
            }
        },
        onBack = onBack
    )
    
    // Completion celebration overlay
    CompletionCelebration(
        visible = showCelebration,
        onDismiss = {
            showCelebration = false
            onNext()
        }
    )
}

@Composable
fun Setup6ScreenContent(
    aboutMe: String,
    lookingFor: String,
    isFormValid: Boolean,
    isSubmitting: Boolean = false,
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
        nextButtonText = R.string.setup6_complete_button,
        isLoading = isSubmitting,
        loadingText = "正在保存您的設定..."
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 關於我輸入區域
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.setup6_about_me_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    // 建議字數提示
                    Text(
                        text = "建議 50-500 字",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF8F9FA)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    OutlinedTextField(
                        value = aboutMe,
                        onValueChange = { if (it.length <= 500) onAboutMeChange(it) },
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
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedBorderColor = if (aboutMe.length < 50 && aboutMe.isNotEmpty()) 
                                Color(0xFFFF9800) else Color(0xFFEAECEF),
                            focusedBorderColor = when {
                                aboutMe.length >= 50 -> Color(0xFF4CAF50)
                                aboutMe.isNotEmpty() -> Color(0xFFFF9800)
                                else -> Color(0xFF6B46C1).copy(alpha = 0.5f)
                            },
                            cursorColor = Color(0xFF6B46C1)
                        )
                    )
                }
                
                // 字數統計和建議
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when {
                            aboutMe.isEmpty() -> "開始介紹您自己吧！"
                            aboutMe.length < 50 -> "可以再多寫一些哦（至少50字）"
                            aboutMe.length >= 50 -> "太棒了！內容很充實"
                            else -> ""
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = when {
                            aboutMe.isEmpty() -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            aboutMe.length < 50 -> Color(0xFFFF9800)
                            else -> Color(0xFF4CAF50)
                        }
                    )
                    
                    Text(
                        text = "${aboutMe.length}/500",
                        style = MaterialTheme.typography.bodySmall,
                        color = when {
                            aboutMe.length > 450 -> Color(0xFFE53E3E)
                            aboutMe.length >= 50 -> Color(0xFF4CAF50)
                            else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        }
                    )
                }
            }

            // 我在尋找輸入區域
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.setup6_looking_for_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    // 建議字數提示
                    Text(
                        text = "建議 20-300 字",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF8F9FA)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    OutlinedTextField(
                        value = lookingFor,
                        onValueChange = { if (it.length <= 300) onLookingForChange(it) },
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
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedBorderColor = if (lookingFor.length < 20 && lookingFor.isNotEmpty()) 
                                Color(0xFFFF9800) else Color(0xFFEAECEF),
                            focusedBorderColor = when {
                                lookingFor.length >= 20 -> Color(0xFF4CAF50)
                                lookingFor.isNotEmpty() -> Color(0xFFFF9800)
                                else -> Color(0xFF6B46C1).copy(alpha = 0.5f)
                            },
                            cursorColor = Color(0xFF6B46C1)
                        )
                    )
                }
                
                // 字數統計和建議
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when {
                            lookingFor.isEmpty() -> "描述您希望認識的人"
                            lookingFor.length < 20 -> "可以再詳細一些（至少20字）"
                            lookingFor.length >= 20 -> "描述得很清楚！"
                            else -> ""
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = when {
                            lookingFor.isEmpty() -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            lookingFor.length < 20 -> Color(0xFFFF9800)
                            else -> Color(0xFF4CAF50)
                        }
                    )
                    
                    Text(
                        text = "${lookingFor.length}/300",
                        style = MaterialTheme.typography.bodySmall,
                        color = when {
                            lookingFor.length > 270 -> Color(0xFFE53E3E)
                            lookingFor.length >= 20 -> Color(0xFF4CAF50)
                            else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        }
                    )
                }
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