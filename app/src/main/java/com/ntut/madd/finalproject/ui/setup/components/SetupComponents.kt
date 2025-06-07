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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
    onBackClick: () -> Unit,
    icon: String = "",
    title: String = "",
    subtitle: String = ""
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .background(purpleGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "返回",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.size(48.dp))
            }
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (icon.isNotEmpty()) {
                    Text(
                        text = icon,
                        fontSize = 60.sp,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                } else {
                    Card(
                        modifier = Modifier.size(100.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.8f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        // 空的占位符
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
                
                Text(
                    text = title.ifEmpty { stringResource(R.string.setup_title) },
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = subtitle.ifEmpty { stringResource(R.string.setup_subtitle) },
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 16.sp
                )
            }
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
            .padding(16.dp)
    ) {
        Column {
            LinearProgressIndicator(
                progress = { currentStep.toFloat() / totalSteps.toFloat() },
                modifier = Modifier.fillMaxWidth(),
                color = DarkBlue,
                trackColor = Color.Gray.copy(alpha = 0.3f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "$currentStep/$totalSteps",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.Gray,
                fontSize = 14.sp
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

    val elevation by animateDpAsState(
        targetValue = if (isFocused) 2.dp else 0.dp,
        animationSpec = tween(200),
        label = "elevation"
    )

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.01f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )

    Column(modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .scale(scale),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
            border = BorderStroke(1.dp, borderColor),
            elevation = CardDefaults.cardElevation(defaultElevation = elevation),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
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
                            color = Color.Gray.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                        }
                        .defaultMinSize(minHeight = 48.dp), // 控制最小高度
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
                // 成功 icon
                androidx.compose.animation.AnimatedVisibility(
                    visible = showSuccess,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut(),
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Valid input",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // 錯誤提示與字數顯示
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
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

            if (maxLength != Int.MAX_VALUE) {
                Text(
                    text = "${value.length}/$maxLength",
                    color = if (value.length > maxLength * 0.9) Color(0xFFFF9800)
                    else Color.Gray.copy(alpha = 0.6f),
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
    totalSteps: Int = 4,
    headerIcon: String,
    headerTitle: String,
    headerSubtitle: String,
    isFormValid: Boolean,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    showBackButton: Boolean = true,
    nextButtonText: Int = R.string.next_step,
    content: @Composable () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // 統一的頭部區域
            SetupHeader(
                onBackClick = onBackClick,
                icon = headerIcon,
                title = headerTitle,
                subtitle = headerSubtitle
            )
            
            // 統一的進度條
            SetupProgressBar(
                currentStep = currentStep,
                totalSteps = totalSteps
            )
            
            // 分隔線
            SetupDivider()
            
            // 主要內容區域 - 統一樣式
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                // 使用者自定義內容
                content()
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // 統一的導航按鈕區域
                SetupNavigationButtons(
                    showBackButton = showBackButton,
                    isFormValid = isFormValid,
                    onBackClick = onBackClick,
                    onNextClick = onNextClick,
                    nextButtonText = nextButtonText
                )
                
                Spacer(modifier = Modifier.height(24.dp))
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
