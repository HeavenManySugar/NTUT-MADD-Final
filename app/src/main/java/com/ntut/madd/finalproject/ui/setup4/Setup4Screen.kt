package com.ntut.madd.finalproject.ui.setup4

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ntut.madd.finalproject.R
import com.ntut.madd.finalproject.ui.setup.components.SetupPageContainer
import com.ntut.madd.finalproject.ui.setup.components.SetupFieldLabel
import com.ntut.madd.finalproject.ui.setup.components.SetupContentCard
import com.ntut.madd.finalproject.ui.setup.components.EnhancedInterestButton
import com.ntut.madd.finalproject.ui.setup.components.TraitSelectionProgress
import com.ntut.madd.finalproject.ui.setup.components.SuccessMessageCard
import com.ntut.madd.finalproject.ui.shared.StandardButton
import com.ntut.madd.finalproject.ui.shared.SecondaryButton
import com.ntut.madd.finalproject.ui.theme.MakeItSoTheme
import kotlinx.serialization.Serializable

@Serializable
object Setup4Route

@Composable
fun Setup4Screen(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    viewModel: Setup4ViewModel = hiltViewModel()
) {
    val selectedInterests by viewModel.selectedInterests.collectAsStateWithLifecycle()
    val isFormValid by viewModel.isFormValid.collectAsStateWithLifecycle()
    val navigateToNext by viewModel.navigateToNext.collectAsStateWithLifecycle()
    
    if (navigateToNext) {
        LaunchedEffect(navigateToNext) {
            onNextClick()
            viewModel.onNavigateHandled()
        }
    }

    Setup4ScreenContent(
        selectedInterests = selectedInterests,
        isFormValid = isFormValid,
        onBackClick = onBackClick,
        onInterestToggle = viewModel::toggleInterest,
        onNextClick = { viewModel.onNextClicked() }
    )
}

@Composable
fun Setup4ScreenContent(
    selectedInterests: List<String>,
    isFormValid: Boolean,
    onBackClick: () -> Unit,
    onInterestToggle: (String) -> Unit,
    onNextClick: () -> Unit
) {
    SetupPageContainer(
        currentStep = 4,
        totalSteps = 6,
        onBackClick = onBackClick,
        headerIcon = "❤️",
        headerTitle = stringResource(R.string.interests_question),
        headerSubtitle = stringResource(R.string.interests_description),
        isFormValid = isFormValid,
        onNextClick = onNextClick
    ) {
        SetupContentCard {
            SetupFieldLabel(text = "請選擇您感興趣的領域")
            
            InterestGrid(
                selectedInterests = selectedInterests,
                onInterestToggle = onInterestToggle
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Enhanced selection counter with circular progress  
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when {
                    selectedInterests.isEmpty() -> Color(0xFFFFF3E0)
                    selectedInterests.size >= 3 -> Color(0xFFE8F5E8)
                    else -> Color(0xFFF3E5F5)
                }
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Circular progress indicator with count
                    Box(
                        modifier = Modifier.size(60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        TraitSelectionProgress(
                            selected = selectedInterests.size,
                            total = 5
                        )
                        Text(
                            text = "${selectedInterests.size}/5",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                selectedInterests.isEmpty() -> Color(0xFFE65100)
                                selectedInterests.size >= 3 -> Color(0xFF2E7D32)
                                else -> Color(0xFF7B1FA2)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        val (title, subtitle, titleColor, subtitleColor) = when {
                            selectedInterests.isEmpty() -> listOf(
                                "還沒有選擇興趣",
                                "請選擇 3-5 個您感興趣的領域",
                                Color(0xFFE65100),
                                Color(0xFFBF360C)
                            )
                            selectedInterests.size < 3 -> listOf(
                                "再選擇 ${3 - selectedInterests.size} 個興趣",
                                "至少需要選擇 3 個興趣才能繼續",
                                Color(0xFF7B1FA2),
                                Color(0xFF4A148C)
                            )
                            selectedInterests.size == 5 -> listOf(
                                "已選滿 5 個興趣！",
                                "您的興趣組合很完整",
                                Color(0xFF2E7D32),
                                Color(0xFF1B5E20)
                            )
                            else -> listOf(
                                "已選擇 ${selectedInterests.size} 個興趣",
                                "還可以再選擇 ${5 - selectedInterests.size} 個興趣",
                                Color(0xFF2E7D32),
                                Color(0xFF1B5E20)
                            )
                        }
                        
                        Text(
                            text = title as String,
                            color = titleColor as Color,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = subtitle as String,
                            color = subtitleColor as Color,
                            fontSize = 14.sp,
                            lineHeight = 18.sp
                        )
                    }
                
                    // Status emoji
                    Text(
                        text = when {
                            selectedInterests.isEmpty() -> "💭"
                            selectedInterests.size < 3 -> "🎯"
                            selectedInterests.size == 5 -> "🎉"
                            else -> "✨"
                        },
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InterestGrid(
    selectedInterests: List<String>,
    onInterestToggle: (String) -> Unit
) {
    val interests = listOf(
        stringResource(R.string.interest_travel),
        stringResource(R.string.interest_food),
        stringResource(R.string.interest_movie),
        stringResource(R.string.interest_music),
        stringResource(R.string.interest_reading),
        stringResource(R.string.interest_sports),
        stringResource(R.string.interest_fitness),
        stringResource(R.string.interest_yoga),
        stringResource(R.string.interest_swimming),
        stringResource(R.string.interest_hiking),
        stringResource(R.string.interest_photography),
        stringResource(R.string.interest_drawing),
        stringResource(R.string.interest_cooking),
        stringResource(R.string.interest_coffee),
        stringResource(R.string.interest_wine),
        stringResource(R.string.interest_pets),
        stringResource(R.string.interest_gardening),
        stringResource(R.string.interest_technology),
        stringResource(R.string.interest_gaming),
        stringResource(R.string.interest_dancing),
        stringResource(R.string.interest_instrument),
        stringResource(R.string.interest_crafts),
        stringResource(R.string.interest_investment),
        stringResource(R.string.interest_learning)
    )

    Column {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            interests.forEach { interest ->
                val isSelected = selectedInterests.contains(interest)
                val canSelect = selectedInterests.size < 5 || isSelected
                
                EnhancedInterestButton(
                    interest = interest,
                    isSelected = isSelected,
                    isEnabled = canSelect,
                    onToggle = { if (canSelect) onInterestToggle(interest) }
                )
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun Setup4ScreenPreview() {
    MakeItSoTheme(darkTheme = false) {
        Setup4ScreenContent(
            selectedInterests = listOf("🌍 旅行", "🍕 美食", "🎬 電影", "🎵 音樂"),
            isFormValid = true,
            onBackClick = {},
            onInterestToggle = {},
            onNextClick = {}
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFBFC)
fun InterestGridPreview() {
    MakeItSoTheme(darkTheme = false) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFAFBFC)
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "請選擇您感興趣的領域",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E3440),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                InterestGrid(
                    selectedInterests = listOf("🌍 旅行", "🍕 美食", "🎬 電影", "🎵 音樂"),
                    onInterestToggle = {}
                )
            }
        }
    }
}
