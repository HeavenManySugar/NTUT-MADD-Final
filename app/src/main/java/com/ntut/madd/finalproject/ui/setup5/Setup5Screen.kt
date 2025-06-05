package com.ntut.madd.finalproject.ui.setup5

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.ntut.madd.finalproject.ui.setup.components.SuccessMessageCard
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
            
            Spacer(modifier = Modifier.height(16.dp))
            
            PersonalityTraitGrid(
                selectedTraits = selectedTraits,
                onTraitToggle = onTraitToggle
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Enhanced selection counter with circular progress
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when {
                    selectedTraits.isEmpty() -> Color(0xFFFFF3E0)
                    selectedTraits.size >= 3 -> Color(0xFFE8F5E8)
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
                // Circular progress indicator
                Box(
                    modifier = Modifier.size(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TraitSelectionProgress(
                        selected = selectedTraits.size,
                        total = 5
                    )
                    Text(
                        text = "${selectedTraits.size}/5",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            selectedTraits.isEmpty() -> Color(0xFFE65100)
                            selectedTraits.size >= 3 -> Color(0xFF2E7D32)
                            else -> Color(0xFF7B1FA2)
                        }
                    )
                }
                
                Column(modifier = Modifier.weight(1f)) {
                    val (title, subtitle, titleColor, subtitleColor) = when {
                        selectedTraits.isEmpty() -> listOf(
                            "還沒有選擇特質",
                            "請選擇 3-5 個能描述您的個人特質",
                            Color(0xFFE65100),
                            Color(0xFFBF360C)
                        )
                        selectedTraits.size < 3 -> listOf(
                            "再選擇 ${3 - selectedTraits.size} 個特質",
                            "至少需要選擇 3 個特質才能繼續",
                            Color(0xFF7B1FA2),
                            Color(0xFF4A148C)
                        )
                        selectedTraits.size == 5 -> listOf(
                            "已選滿 5 個特質！",
                            "您的個人特質組合很完整",
                            Color(0xFF2E7D32),
                            Color(0xFF1B5E20)
                        )
                        else -> listOf(
                            "已選擇 ${selectedTraits.size} 個特質",
                            "還可以再選擇 ${5 - selectedTraits.size} 個特質",
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
                        selectedTraits.isEmpty() -> "💭"
                        selectedTraits.size < 3 -> "🎯"
                        selectedTraits.size == 5 -> "🎉"
                        else -> "✨"
                    },
                    fontSize = 24.sp
                )
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
    var showSuccessMessage by remember { mutableStateOf(false) }
    
    // Show success message when reaching optimal selection
    LaunchedEffect(selectedTraits.size) {
        if (selectedTraits.size >= 3 && selectedTraits.size <= 3) {
            showSuccessMessage = true
        }
    }
    
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

    Column {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            traits.forEach { trait ->
                val isSelected = selectedTraits.contains(trait)
                val canSelect = selectedTraits.size < 5 || isSelected
                
                EnhancedTraitButton(
                    text = trait,
                    isSelected = isSelected,
                    isEnabled = canSelect,
                    onClick = { if (canSelect) onTraitToggle(trait) }
                )
            }
        }
        
        // Success message
        if (showSuccessMessage && selectedTraits.size >= 3) {
            Spacer(modifier = Modifier.height(16.dp))
            SuccessMessageCard(
                message = "完美！您已選擇了 ${selectedTraits.size} 個個人特質",
                visible = showSuccessMessage
            )
        }
    }
}

@Composable
fun EnhancedTraitButton(
    text: String,
    isSelected: Boolean,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "scale"
    )
    
    val animatedContainerColor by animateColorAsState(
        targetValue = when {
            isSelected -> MaterialTheme.colorScheme.primary
            isEnabled -> MaterialTheme.colorScheme.surface
            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        },
        animationSpec = tween(durationMillis = 300),
        label = "containerColor"
    )
    
    Card(
        modifier = modifier
            .scale(animatedScale)
            .clickable(enabled = isEnabled) { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = animatedContainerColor
        ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 6.dp else if (isEnabled) 2.dp else 0.dp
        )
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary 
                       else if (isEnabled) MaterialTheme.colorScheme.onSurface 
                       else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun TraitSelectionProgress(
    selected: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    val progress = selected.toFloat() / total.toFloat()
    val progressColor = when {
        selected == 0 -> Color(0xFFE65100)
        selected < 3 -> Color(0xFF7B1FA2)
        else -> Color(0xFF2E7D32)
    }
    
    Canvas(
        modifier = modifier.size(50.dp)
    ) {
        val strokeWidth = 6.dp.toPx()
        val radius = (size.minDimension - strokeWidth) / 2
        val center = Offset(size.width / 2, size.height / 2)
        
        // Background circle
        drawCircle(
            color = Color(0xFFE0E0E0),
            radius = radius,
            center = center,
            style = Stroke(width = strokeWidth)
        )
        
        // Progress arc
        if (progress > 0) {
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                topLeft = Offset(
                    center.x - radius,
                    center.y - radius
                ),
                size = Size(radius * 2, radius * 2),
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )
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
