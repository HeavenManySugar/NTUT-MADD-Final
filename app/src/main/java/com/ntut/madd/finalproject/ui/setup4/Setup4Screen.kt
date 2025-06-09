package com.ntut.madd.finalproject.ui.setup4

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
            SetupFieldLabel(text = stringResource(R.string.setup4_field_label))
            
            InterestGrid(
                selectedInterests = selectedInterests,
                onInterestToggle = onInterestToggle
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Selected count indicator with better styling
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (selectedInterests.size >= 3) 
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
                    text = if (selectedInterests.size >= 3) "✅" else "⚠️",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Column {
                    Text(
                        text = stringResource(R.string.setup4_selected_count, selectedInterests.size),
                        color = if (selectedInterests.size >= 3)
                            Color(0xFF2E7D32) else Color(0xFFE65100),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (selectedInterests.size < 3) {
                        Text(
                            text = stringResource(R.string.setup4_need_more, 3 - selectedInterests.size),
                            color = Color(0xFFBF360C),
                            fontSize = 14.sp
                        )
                    } else if (selectedInterests.size < 5) {
                        Text(
                            text = stringResource(R.string.setup4_can_select_more, 5 - selectedInterests.size),
                            color = Color(0xFF1B5E20),
                            fontSize = 14.sp
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.setup4_max_selected),
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

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        interests.forEach { interest ->
            val isSelected = selectedInterests.contains(interest)
            val canSelect = selectedInterests.size < 5 || isSelected
            
            Card(
                modifier = Modifier
                    .clickable(enabled = canSelect) {
                        if (canSelect) onInterestToggle(interest)
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
                        text = interest,
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
fun Setup4ScreenPreview() {
    MakeItSoTheme(darkTheme = false) {
        Setup4ScreenContent(
            selectedInterests = listOf(
                stringResource(R.string.interest_travel), 
                stringResource(R.string.interest_food), 
                stringResource(R.string.interest_movie), 
                stringResource(R.string.interest_music)
            ),
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
                    text = stringResource(R.string.setup4_field_label),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E3440),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                InterestGrid(
                    selectedInterests = listOf(
                        stringResource(R.string.interest_travel), 
                        stringResource(R.string.interest_food), 
                        stringResource(R.string.interest_movie), 
                        stringResource(R.string.interest_music)
                    ),
                    onInterestToggle = {}
                )
            }
        }
    }
}
