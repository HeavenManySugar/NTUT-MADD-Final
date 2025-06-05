package com.ntut.madd.finalproject.ui.setup.components

import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ntut.madd.finalproject.R
import com.ntut.madd.finalproject.ui.shared.StandardButton
import com.ntut.madd.finalproject.ui.shared.SecondaryButton
import com.ntut.madd.finalproject.ui.theme.DarkBlue
import com.ntut.madd.finalproject.ui.theme.LightGray
import com.ntut.madd.finalproject.ui.theme.purpleGradient
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun SetupHeader(
    icon: String = "",
    title: String = "",
    subtitle: String = ""
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp) // 縮小從280dp到180dp，節省100dp空間
            .background(purpleGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp), // 縮小vertical padding
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
                if (icon.isNotEmpty()) {
                    Text(
                        text = icon,
                        fontSize = 48.sp, // 縮小從60sp到48sp
                        modifier = Modifier.padding(bottom = 16.dp) // 縮小從24dp到16dp
                    )
                } else {
                    Card(
                        modifier = Modifier.size(80.dp), // 縮小從100dp到80dp
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.8f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        // 空的占位符
                    }
                    Spacer(modifier = Modifier.height(16.dp)) // 縮小從24dp到16dp
                }

                Text(
                    text = title.ifEmpty { stringResource(R.string.setup_title) },
                    color = Color.White,
                    fontSize = 22.sp, // 縮小從24sp到22sp
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp)) // 縮小從8dp到6dp

                Text(
                    text = subtitle.ifEmpty { stringResource(R.string.setup_subtitle) },
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp, // 縮小從16sp到14sp
                    textAlign = TextAlign.Center
                )
        }
    }
}

@Composable
fun SetupProgressBar(
    currentStep: Int,
    totalSteps: Int = 6
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 10.dp) // 縮小vertical padding從16dp到10dp
    ) {
        Column {
            // 視覺化進度點指示器
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(totalSteps) { index ->
                    val stepNumber = index + 1
                    val isCompleted = stepNumber < currentStep
                    val isCurrent = stepNumber == currentStep

                    Box(
                        modifier = Modifier
                            .size(28.dp) // 縮小從32dp到28dp
                            .background(
                                color = when {
                                    isCompleted -> Color(0xFF4CAF50) // 綠色表示已完成
                                    isCurrent -> DarkBlue // 藍色表示當前步驟
                                    else -> Color.Gray.copy(alpha = 0.3f) // 灰色表示未完成
                                },
                                shape = androidx.compose.foundation.shape.CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            // 已完成顯示勾選圖標
                            Text(
                                text = "✓",
                                color = Color.White,
                                fontSize = 14.sp, // 縮小從16sp到14sp
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            // 未完成或當前步驟顯示數字
                            Text(
                                text = stepNumber.toString(),
                                color = Color.White,
                                fontSize = 12.sp, // 縮小從14sp到12sp
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // 添加連接線（除了最後一個點）
                    if (index < totalSteps - 1) {
                        Box(
                            modifier = Modifier
                                .height(2.dp)
                                .weight(1f)
                                .background(
                                    if (stepNumber < currentStep) Color(0xFF4CAF50)
                                    else Color.Gray.copy(alpha = 0.3f)
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp)) // 縮小從12dp到8dp

            // 進度條和文字描述
            LinearProgressIndicator(
                progress = { currentStep.toFloat() / totalSteps.toFloat() },
                modifier = Modifier.fillMaxWidth(),
                color = DarkBlue,
                trackColor = Color.Gray.copy(alpha = 0.3f)
            )

            Spacer(modifier = Modifier.height(6.dp)) // 縮小從8dp到6dp

            Text(
                text = "步驟 $currentStep / $totalSteps",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.Gray,
                fontSize = 12.sp, // 縮小從14sp到12sp
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupInputField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes placeholder: Int,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    hasError: Boolean = false,
    showSuccess: Boolean = false,
    maxLength: Int = Int.MAX_VALUE,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE
) {
    var isFocused by remember { mutableStateOf(false) }

    // Animated border color
    val borderColor by animateColorAsState(
        targetValue = when {
            hasError -> Color(0xFFE53E3E)
            showSuccess -> Color(0xFF4CAF50)
            isFocused -> Color(0xFF6B46C1)
            else -> Color(0xFFEAECEF)
        },
        animationSpec = tween(200),
        label = "border_color"
    )

    // Animated container elevation
    val elevation by animateDpAsState(
        targetValue = if (isFocused) 4.dp else 1.dp,
        animationSpec = tween(200),
        label = "container_elevation"
    )

    // Scale animation for focus
    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.02f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "input_scale"
    )

    Column(modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .scale(scale),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            border = BorderStroke(2.dp, borderColor),
            elevation = CardDefaults.cardElevation(defaultElevation = elevation),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = value,
                    onValueChange = { newValue ->
                        if (newValue.length <= maxLength) {
                            onValueChange(newValue)
                        }
                    },
                    placeholder = {
                        Text(
                            text = stringResource(placeholder),
                            color = Color.Gray.copy(alpha = 0.6f)
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                        },
                    singleLine = singleLine,
                    minLines = minLines,
                    maxLines = maxLines,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        cursorColor = Color(0xFF6B46C1)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                // Success indicator with animation
                AnimatedVisibility(
                    visible = showSuccess,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Valid input",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier
                            .size(24.dp)
                            .padding(start = 8.dp)
                    )
                }
            }
        }

        // Character count and error message
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Error message with animation
            AnimatedVisibility(
                visible = hasError && errorMessage != null,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Text(
                    text = errorMessage ?: "",
                    color = Color(0xFFE53E3E),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Character count if there's a limit
            if (maxLength != Int.MAX_VALUE) {
                Text(
                    text = "${value.length}/$maxLength",
                    color = when {
                        value.length > maxLength * 0.9 -> Color(0xFFFF9800)
                        else -> Color.Gray.copy(alpha = 0.6f)
                    },
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun SetupDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(LightGray)
    )
}

/**
 * 通用的Setup頁面容器組件
 * 統一視覺風格並提供一致的布局結構
 */
@Composable
fun SetupPageContainer(
    currentStep: Int,
    totalSteps: Int = 6,
    headerIcon: String = "",
    headerTitle: String = "",
    headerSubtitle: String = "",
    isFormValid: Boolean = false,
    showBackButton: Boolean = true,
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    @StringRes nextButtonText: Int = R.string.next,
    isLoading: Boolean = false,
    loadingText: String = "處理中...",
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFBFC))
    ) {
        // Progress indicator
        SetupProgressBar(currentStep = currentStep, totalSteps = totalSteps)

        // Header section with animation
        val headerScale by animateFloatAsState(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            label = "header_scale"
        )

        SetupHeader(
            icon = headerIcon,
            title = headerTitle,
            subtitle = headerSubtitle
        )

        // Content area with enhanced padding and spacing
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = 20.dp) // 縮小從24dp到20dp
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp) // 縮小從16dp到12dp
        ) {
            Spacer(modifier = Modifier.height(4.dp)) // 縮小從8dp到4dp

            // Animated content entrance
            val contentAlpha by animateFloatAsState(
                targetValue = 1f,
                animationSpec = tween(600, delayMillis = 200),
                label = "content_alpha"
            )

            Column(
                modifier = Modifier.alpha(contentAlpha),
                verticalArrangement = Arrangement.spacedBy(12.dp) // 縮小從16dp到12dp
            ) {
                content()
            }

            Spacer(modifier = Modifier.height(16.dp)) // 縮小從24dp到16dp
        }

        // Bottom navigation with enhanced styling but reduced size
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp), // 縮小從24dp到20dp horizontal, 16dp vertical
                horizontalArrangement = Arrangement.spacedBy(12.dp), // 縮小從16dp到12dp
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showBackButton) {
                    SecondaryButton(
                        label = R.string.back,
                        onButtonClick = onBackClick,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Enhanced next button with loading state
                if (isLoading) {
                    LoadingStateButton(
                        label = nextButtonText,
                        onClick = onNextClick,
                        isLoading = true,
                        enabled = isFormValid,
                        loadingText = loadingText,
                        modifier = Modifier.weight(if (showBackButton) 1f else 1f)
                    )
                } else {
                    StandardButton(
                        label = nextButtonText,
                        onButtonClick = onNextClick,
                        enabled = isFormValid,
                        modifier = Modifier.weight(if (showBackButton) 1f else 1f)
                    )
                }
            }
        }
    }
}

/**
 * 統一的導航按鈕組件
 */
@Composable
fun SetupNavigationButtons(
    showBackButton: Boolean = true,
    isFormValid: Boolean,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    nextButtonText: Int = R.string.next_step
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (showBackButton) {
            SecondaryButton(
                label = R.string.previous_step,
                onButtonClick = onBackClick,
                modifier = Modifier.weight(1f)
            )
        }

        StandardButton(
            label = nextButtonText,
            onButtonClick = onNextClick,
            enabled = isFormValid,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * 統一的欄位標題組件
 */
@Composable
fun SetupFieldLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.Black,
        modifier = modifier
    )
}

/**
 * 統一的內容卡片組件
 */
@Composable
fun SetupContentCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFAFBFC)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            content()
        }
    }
}

@Composable
fun CompletionCelebration(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            // Confetti animation
            ConfettiAnimation(isPlaying = visible)

            // Success card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Animated success icon
                    val scale by animateFloatAsState(
                        targetValue = if (visible) 1f else 0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "success_icon_scale"
                    )

                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Success",
                        modifier = Modifier
                            .size(80.dp)
                            .scale(scale),
                        tint = Color(0xFF4CAF50)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "設定完成！",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "恭喜您已完成所有設定步驟！\n現在可以開始使用應用程式了。",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Animated continue button
                    val buttonScale by animateFloatAsState(
                        targetValue = if (visible) 1f else 0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "button_scale"
                    )

                    StandardButton(
                        label = R.string.setup_complete_continue,
                        onButtonClick = onDismiss,
                        modifier = Modifier.scale(buttonScale)
                    )
                }
            }
        }
    }
}

@Composable
fun ConfettiAnimation(
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val confettiPieces = remember {
        List(30) { ConfettiPiece() }
    }

    val animationProgress by animateFloatAsState(
        targetValue = if (isPlaying) 1f else 0f,
        animationSpec = tween(3000),
        label = "confetti_animation"
    )

    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        confettiPieces.forEach { piece ->
            val x = piece.startX * size.width + piece.velocityX * animationProgress * 200
            val y = piece.startY * size.height + piece.velocityY * animationProgress * 800 +
                    0.5f * 500 * animationProgress * animationProgress // gravity effect

            val rotation = piece.rotation * animationProgress * 720 // multiple rotations

            if (y < size.height && x >= 0 && x <= size.width) {
                drawRect(
                    color = piece.color,
                    topLeft = androidx.compose.ui.geometry.Offset(x, y),
                    size = androidx.compose.ui.geometry.Size(piece.size, piece.size),
                    alpha = 1f - animationProgress * 0.3f
                )
            }
        }
    }
}

data class ConfettiPiece(
    val startX: Float = Random.nextFloat(),
    val startY: Float = Random.nextFloat() * 0.3f,
    val velocityX: Float = (Random.nextFloat() - 0.5f) * 2f,
    val velocityY: Float = Random.nextFloat() * -2f - 1f,
    val rotation: Float = Random.nextFloat(),
    val size: Float = Random.nextFloat() * 8f + 4f,
    val color: Color = listOf(
        Color(0xFFFF6B6B),
        Color(0xFF4ECDC4),
        Color(0xFF45B7D1),
        Color(0xFF96CEB4),
        Color(0xFFFDA085),
        Color(0xFFFFD93D),
        Color(0xFF6C5CE7),
        Color(0xFFFD79A8)
    ).random()
)

@Composable
fun SuccessMessageCard(
    message: String,
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(300)
        ) + fadeOut(),
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8F5E8)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Success",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = message,
                    color = Color(0xFF1B5E20),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun LoadingStateButton(
    @StringRes label: Int,
    onClick: () -> Unit,
    isLoading: Boolean,
    enabled: Boolean = true,
    loadingText: String = "處理中...",
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(
                brush = if (enabled && !isLoading) purpleGradient else SolidColor(Color.Gray),
                shape = RoundedCornerShape(12.dp)
            ),
        onClick = onClick,
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.White
        ),
        border = BorderStroke(1.dp, Color.Transparent),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 6.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = loadingText,
                    fontSize = 16.sp,
                    color = Color.White
                )
            } else {
                Text(
                    text = stringResource(label),
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}

// Enhanced interest selection button with hover effects
@Composable
fun EnhancedInterestButton(
    interest: String,
    isSelected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF6B46C1) else Color.White,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "background_color"
    )

    val animatedTextColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color(0xFF4A5568),
        animationSpec = tween(200),
        label = "text_color"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "button_scale"
    )

    val elevation by animateDpAsState(
        targetValue = if (isSelected) 6.dp else 2.dp,
        animationSpec = tween(200),
        label = "button_elevation"
    )

    Card(
        modifier = modifier
            .scale(scale)
            .clickable { onToggle() },
        colors = CardDefaults.cardColors(
            containerColor = animatedBackgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = interest,
                color = animatedTextColor,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                lineHeight = 16.sp
            )
        }
    }
}

// Progress indicator with enhanced visual feedback
@Composable
fun EnhancedProgressIndicator(
    current: Int,
    total: Int,
    target: Int,
    modifier: Modifier = Modifier
) {
    val progress = current.toFloat() / total.toFloat()
    val targetProgress = target.toFloat() / total.toFloat()

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "progress_animation"
    )

    Box(
        modifier = modifier.size(60.dp),
        contentAlignment = Alignment.Center
    ) {
        // Background circle
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.size(60.dp),
            color = Color.Gray.copy(alpha = 0.2f),
            strokeWidth = 6.dp,
            trackColor = Color.Transparent
        )

        // Target progress circle (lighter)
        CircularProgressIndicator(
            progress = { targetProgress },
            modifier = Modifier.size(60.dp),
            color = Color(0xFF6B46C1).copy(alpha = 0.3f),
            strokeWidth = 6.dp,
            trackColor = Color.Transparent
        )

        // Actual progress circle
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.size(60.dp),
            color = when {
                current < target -> Color(0xFFFF9800)
                current >= target -> Color(0xFF4CAF50)
                else -> Color(0xFF6B46C1)
            },
            strokeWidth = 6.dp,
            trackColor = Color.Transparent,
            strokeCap = StrokeCap.Round
        )

        // Count text with animation
        val animatedCount by animateIntAsState(
            targetValue = current,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            label = "count_animation"
        )

        Text(
            text = "$animatedCount/$total",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = when {
                current < target -> Color(0xFFFF9800)
                current >= target -> Color(0xFF4CAF50)
                else -> Color(0xFF6B46C1)
            }
        )
    }
}

// Enhanced form validation feedback
@Composable
fun FormValidationFeedback(
    isValid: Boolean,
    validMessage: String,
    invalidMessage: String,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isValid) Color(0xFFE8F5E8) else Color(0xFFFFF3E0),
        animationSpec = tween(300),
        label = "feedback_background"
    )

    val textColor by animateColorAsState(
        targetValue = if (isValid) Color(0xFF1B5E20) else Color(0xFFE65100),
        animationSpec = tween(300),
        label = "feedback_text"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Animated icon
            val iconScale by animateFloatAsState(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                ),
                label = "icon_scale"
            )

            Icon(
                imageVector = if (isValid) Icons.Filled.CheckCircle else Icons.Default.Info,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier
                    .size(24.dp)
                    .scale(iconScale)
            )

            Text(
                text = if (isValid) validMessage else invalidMessage,
                color = textColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
