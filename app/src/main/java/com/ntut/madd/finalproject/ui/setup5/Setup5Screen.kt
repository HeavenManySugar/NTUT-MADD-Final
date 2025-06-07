package com.ntut.madd.finalproject.ui.setup5

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.ntut.madd.finalproject.ui.theme.MakeItSoTheme
import kotlinx.serialization.Serializable

@Serializable
object Setup5Route

@Composable
fun Setup5Screen(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    viewModel: Setup5ViewModel = hiltViewModel()
) {
    val selectedTraits by viewModel.selectedTraits.collectAsStateWithLifecycle()
    val isFormValid by viewModel.isFormValid.collectAsStateWithLifecycle()
    val navigateToNext by viewModel.navigateToNext.collectAsStateWithLifecycle()
    
    if (navigateToNext) {
        LaunchedEffect(navigateToNext) {
            onNextClick()
            viewModel.onNavigateHandled()
        }
    }

    Setup5ScreenContent(
        selectedTraits = selectedTraits,
        isFormValid = isFormValid,
        onBackClick = onBackClick,
        onTraitToggle = viewModel::toggleTrait,
        onNextClick = { viewModel.onNextClicked() }
    )
}

@Composable
fun Setup5ScreenContent(
    selectedTraits: List<String>,
    isFormValid: Boolean,
    onBackClick: () -> Unit,
    onTraitToggle: (String) -> Unit,
    onNextClick: () -> Unit
) {
    SetupPageContainer(
        currentStep = 5,
        totalSteps = 6,
        onBackClick = onBackClick,
        headerIcon = "✨",
        headerTitle = stringResource(R.string.personality_question),
        headerSubtitle = stringResource(R.string.personality_description),
        isFormValid = isFormValid,
        onNextClick = onNextClick
    ) {
        SetupContentCard {
            SetupFieldLabel(text = "個人特質")
            
            PersonalityTraitGrid(
                selectedTraits = selectedTraits,
                onTraitToggle = onTraitToggle
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Selected count indicator
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (selectedTraits.size >= 3)
                    Color(0xFFE8F5E8) else Color(0xFFFFF3E0)
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedTraits.size >= 3) "✅" else "💡",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Column {
                    Text(
                        text = "已選擇 ${selectedTraits.size} 個特質",
                        color = if (selectedTraits.size >= 3)
                            Color(0xFF2E7D32) else Color(0xFFE65100),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (selectedTraits.size < 3) {
                        Text(
                            text = "還需要選擇 ${3 - selectedTraits.size} 個特質",
                            color = if (selectedTraits.size >= 3)
                                Color(0xFF2E7D32) else Color(0xFFE65100),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else if (selectedTraits.size < 5) {
                        Text(
                            text = "還可以選擇 ${5 - selectedTraits.size} 個特質",
                            color = Color(0xFF1B5E20),
                            fontSize = 14.sp
                        )
                    } else {
                        Text(
                            text = "已選擇最大數量的特質",
                            color = Color(0xFF1B5E20),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PersonalityTraitGrid(
    selectedTraits: List<String>,
    onTraitToggle: (String) -> Unit
) {
    val traits = listOf(
        stringResource(R.string.trait_humorous),
        stringResource(R.string.trait_warm),
        stringResource(R.string.trait_active),
        stringResource(R.string.trait_calm),
        stringResource(R.string.trait_creative),
        stringResource(R.string.trait_honest),
        stringResource(R.string.trait_independent),
        stringResource(R.string.trait_kind),
        stringResource(R.string.trait_passionate),
        stringResource(R.string.trait_careful),
        stringResource(R.string.trait_adventurous),
        stringResource(R.string.trait_wise),
        stringResource(R.string.trait_athletic),
        stringResource(R.string.trait_knowledgeable),
        stringResource(R.string.trait_social),
        stringResource(R.string.trait_secure),
        stringResource(R.string.trait_romantic),
        stringResource(R.string.trait_practical)
    )

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        traits.forEach { trait ->
            val isSelected = selectedTraits.contains(trait)
            val canSelect = selectedTraits.size < 5 || isSelected
            
            Card(
                modifier = Modifier
                    .clickable(enabled = canSelect) { 
                        if (canSelect) onTraitToggle(trait) 
                    },
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        isSelected -> Color(0xFF6C63FF) // Modern purple
                        canSelect -> Color.White
                        else -> Color(0xFFF5F5F5) // Disabled gray
                    }
                ),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (isSelected) 4.dp else if (canSelect) 1.dp else 0.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .border(
                            width = if (isSelected) 0.dp else if (canSelect) 1.dp else 0.dp,
                            color = if (isSelected) Color.Transparent 
                                   else if (canSelect) Color(0xFFE1E5E9) 
                                   else Color.Transparent,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = trait,
                        color = when {
                            isSelected -> Color.White
                            canSelect -> Color(0xFF4A5568)
                            else -> Color(0xFFBDBDBD)
                        },
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun Setup5ScreenPreview() {
    MakeItSoTheme(darkTheme = false) {
        Setup5ScreenContent(
            selectedTraits = listOf("😊 幽默風趣", "🧡 溫柔體貼", "😄 積極樂觀"),
            isFormValid = true,
            onBackClick = {},
            onTraitToggle = {},
            onNextClick = {}
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFAFBFC)
fun PersonalityTraitGridPreview() {
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
                    text = "個人特質",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E3440),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                PersonalityTraitGrid(
                    selectedTraits = listOf("😊 幽默風趣", "🧡 溫柔體貼", "😄 積極樂觀"),
                    onTraitToggle = {}
                )
            }
        }
    }
}
