package com.ntut.madd.finalproject.ui.setup6

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    val isSubmitting by viewModel.isSubmitting.collectAsStateWithLifecycle()
    val setupCompleted by viewModel.setupCompleted.collectAsStateWithLifecycle()
    
    val coroutineScope = rememberCoroutineScope()
    var showCelebration by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    // Handle setup completion
    LaunchedEffect(setupCompleted) {
        if (setupCompleted) {
            showCelebration = true
        }
    }

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
            coroutineScope.launch {
                val result = viewModel.saveProfileAndComplete()
                if (result.isFailure) {
                    errorMessage = result.exceptionOrNull()?.message ?: context.getString(R.string.setup_save_failed)
                }
            }
        },
        onBack = onBack
    )
    
    // Error handling
    errorMessage?.let { message ->
        LaunchedEffect(message) {
            // Show error snackbar or dialog
            // For now, we'll just log it and clear
            println("Setup save error: $message")
            errorMessage = null
        }
    }
    
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
        headerSubtitle = stringResource(R.string.setup6_subtitle),
        isFormValid = isFormValid,
        onNextClick = onNext,
        nextButtonText = R.string.setup6_complete_button,
        isLoading = isSubmitting,
        loadingText = stringResource(R.string.setup6_saving_settings)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // About Me input section
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
                    
                    // Character count suggestion
                    Text(
                        text = stringResource(R.string.setup6_char_limit_suggestion_about),
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
                        onValueChange = onAboutMeChange, // Remove character limit restriction
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
                            unfocusedBorderColor = when {
                                aboutMe.length > 500 -> Color(0xFFE53E3E) // Red when over limit
                                aboutMe.length > 450 -> Color(0xFFFF9800) // Orange when approaching limit
                                aboutMe.length < 50 && aboutMe.isNotEmpty() -> Color(0xFFFF9800)
                                else -> Color(0xFFEAECEF)
                            },
                            focusedBorderColor = when {
                                aboutMe.length > 500 -> Color(0xFFE53E3E) // Red when over limit
                                aboutMe.length > 450 -> Color(0xFFFF9800) // Orange when approaching limit
                                aboutMe.length >= 50 -> Color(0xFF4CAF50)
                                aboutMe.isNotEmpty() -> Color(0xFFFF9800)
                                else -> Color(0xFF6B46C1).copy(alpha = 0.5f)
                            },
                            cursorColor = Color(0xFF6B46C1)
                        )
                    )
                }
                
                // Character count and suggestions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when {
                            aboutMe.isEmpty() -> stringResource(R.string.setup6_start_intro)
                            aboutMe.length > 500 -> stringResource(R.string.setup6_over_limit)
                            aboutMe.length < 50 -> stringResource(R.string.setup6_write_more)
                            aboutMe.length >= 50 -> stringResource(R.string.setup6_content_great)
                            else -> ""
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = when {
                            aboutMe.isEmpty() -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            aboutMe.length > 500 -> Color(0xFFE53E3E) // Red when over limit
                            aboutMe.length < 50 -> Color(0xFFFF9800)
                            else -> Color(0xFF4CAF50)
                        }
                    )
                    
                    Text(
                        text = "${aboutMe.length}/500",
                        style = MaterialTheme.typography.bodySmall,
                        color = when {
                            aboutMe.length > 500 -> Color(0xFFE53E3E) // Red when over limit
                            aboutMe.length > 450 -> Color(0xFFFF9800) // Orange when approaching limit
                            aboutMe.length >= 50 -> Color(0xFF4CAF50)
                            else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        }
                    )
                }
            }

            // Looking For input section
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
                    
                    // Character count suggestion
                    Text(
                        text = stringResource(R.string.setup6_char_limit_suggestion_looking),
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
                        onValueChange = onLookingForChange, // Remove character limit restriction
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
                            unfocusedBorderColor = when {
                                lookingFor.length > 300 -> Color(0xFFE53E3E) // Red when over limit
                                lookingFor.length > 270 -> Color(0xFFFF9800) // Orange when approaching limit
                                lookingFor.length < 20 && lookingFor.isNotEmpty() -> Color(0xFFFF9800)
                                else -> Color(0xFFEAECEF)
                            },
                            focusedBorderColor = when {
                                lookingFor.length > 300 -> Color(0xFFE53E3E) // Red when over limit
                                lookingFor.length > 270 -> Color(0xFFFF9800) // Orange when approaching limit
                                lookingFor.length >= 20 -> Color(0xFF4CAF50)
                                lookingFor.isNotEmpty() -> Color(0xFFFF9800)
                                else -> Color(0xFF6B46C1).copy(alpha = 0.5f)
                            },
                            cursorColor = Color(0xFF6B46C1)
                        )
                    )
                }
                
                // Character count and suggestions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when {
                            lookingFor.isEmpty() -> stringResource(R.string.setup6_describe_looking)
                            lookingFor.length > 300 -> stringResource(R.string.setup6_over_limit)
                            lookingFor.length < 20 -> stringResource(R.string.setup6_more_details)
                            lookingFor.length >= 20 -> stringResource(R.string.setup6_description_clear)
                            else -> ""
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = when {
                            lookingFor.isEmpty() -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            lookingFor.length > 300 -> Color(0xFFE53E3E) // Red when over limit
                            lookingFor.length < 20 -> Color(0xFFFF9800)
                            else -> Color(0xFF4CAF50)
                        }
                    )
                    
                    Text(
                        text = "${lookingFor.length}/300",
                        style = MaterialTheme.typography.bodySmall,
                        color = when {
                            lookingFor.length > 300 -> Color(0xFFE53E3E) // Red when over limit
                            lookingFor.length > 270 -> Color(0xFFFF9800) // Orange when approaching limit
                            lookingFor.length >= 20 -> Color(0xFF4CAF50)
                            else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        }
                    )
                }
            }

            // Hint text
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
            aboutMe = "I'm a software engineer who loves learning new technologies, enjoys reading, traveling, and photography. I like participating in open source projects and hope to make the world better through technology.",
            lookingFor = "Looking for like-minded partners to discuss technology and share experiences together.",
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