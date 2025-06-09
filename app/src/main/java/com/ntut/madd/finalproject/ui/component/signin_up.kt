package com.ntut.madd.finalproject.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas // 安全補上
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.painterResource
import com.ntut.madd.finalproject.R

val LightBlue = Color(0xFF90CAF9)

/** Purple Bar **/
@Composable
fun AppHeaderBanner(
    modifier: Modifier = Modifier,
    title: String = "This is our app",
    subtitle: String = "Quick for match"
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .drawWithCache {
                val gradient = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF667EEA),
                        Color(0xFF764BA2)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height)
                )
                onDrawBehind {
                    drawRect(brush = gradient)
                }
            }
            .padding(vertical = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = "App Logo",
                modifier = Modifier.size(128.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(title, fontSize = 24.sp, color = Color.White)
            Text(subtitle, fontSize = 16.sp, color = Color.White)
        }
    }
}

/**　With word divider　*/
@Composable
fun DividerWithText(
    text: String,
    textColor: Color = Color.White
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Divider(Modifier.weight(1f), color = textColor)
        Text(
            text,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp),
            fontWeight = FontWeight.Normal
        )
        Divider(Modifier.weight(1f), color = textColor)
    }
}
/* Input Box 😍🥰😘 */

@Composable
fun LabeledInputBox(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 20.sp,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable () -> Unit)? = null,
    shouldShowError: Boolean = false,
    shouldRepeatPasswordError: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor = when {
        !shouldShowError || (shouldRepeatPasswordError) -> Color(0xFFFFCDD2)
        isFocused -> Color(0xFF90CAF9)
        else -> Color(0xFFEAECEF)
    }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .height(64.dp)
                .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                .background(Color(0xFFF8F9FA), RoundedCornerShape(8.dp))
                .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 8.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        interactionSource = interactionSource,
                        textStyle = LocalTextStyle.current.copy(
                            color = Color.Black,
                            fontSize = fontSize
                        ),
                        singleLine = true,
                        visualTransformation = visualTransformation,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .padding(start = 5.dp),
                        decorationBox = { innerTextField ->
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    color = Color(0xFFB0B0B0),
                                    fontSize = fontSize,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            innerTextField()
                        }
                    )
                }

                if (trailingIcon != null) {
                    Box(modifier = Modifier.padding(start = 8.dp)) {
                        trailingIcon()
                    }
                }
            }
        }

        if (!shouldShowError) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Must be filled",
                color = Color(0xFFFF6B6B),
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        if (shouldRepeatPasswordError){
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Password is different for two fields",
                color = Color(0xFFFF6B6B),
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}


/* Left-Top word 😍🥰😘 */

@Composable
fun InputFieldLabel(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 20.sp,
    textColor: Color = Color.Black
) {
    Box(
        modifier = modifier
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            color = textColor,
            modifier = Modifier.padding(start = 12.dp),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun LabeledFieldMedium(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    fontSize: TextUnit = 20.sp,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val borderColor = when {
        isFocused -> Color(0xFF90CAF9)
        else -> Color(0xFFEAECEF)
    }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, bottom = 4.dp)
        ) {
            Text(
                text = label,
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        Box(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
                .border(1.dp, borderColor, shape = RoundedCornerShape(8.dp))
                .background(Color(0xFFF8F9FA), shape = RoundedCornerShape(8.dp))
                .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                interactionSource = interactionSource,
                textStyle = LocalTextStyle.current.copy(
                    color = Color.Black,
                    fontSize = 20.sp
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = Color(0xFFB0B0B0),
                            fontSize = fontSize,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Composable
fun PressButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = Color.White,
    colors: List<Color> = listOf(
        Color(0xFF667EEA),
        Color(0xFF764BA2)
    ),
    cornerRadius: Dp = 12.dp,
    fontSize: TextUnit = 18.sp,
    fontWeight: FontWeight = FontWeight.SemiBold
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .drawBehind {
                val gradient = Brush.linearGradient(
                    colors = colors,
                    start = Offset.Zero,
                    end = Offset(size.width, size.height)
                )
                drawRoundRect(
                    brush = gradient,
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            },
        shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = textColor
        ),
        contentPadding = PaddingValues()
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = textColor,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        )
    }
}

@Composable
fun LabeledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 20.sp,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable () -> Unit)? = null,
    shouldShowError: Boolean = false,
    shouldRepeatPasswordError: Boolean = false,
) {
    InputFieldLabel(
        text = label,
        modifier = Modifier.fillMaxWidth(0.85f)
    )

    LabeledInputBox(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        fontSize = fontSize,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        shouldShowError = shouldShowError,
        modifier = Modifier.fillMaxWidth(0.8f),
        shouldRepeatPasswordError = shouldRepeatPasswordError
    )

    Spacer(Modifier.height(8.dp))
}