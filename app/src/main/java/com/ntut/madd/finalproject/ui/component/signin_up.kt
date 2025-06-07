package com.ntut.madd.finalproject.ui.component

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager

val LightBlue = Color(0xFF90CAF9)

/**　中間帶文字的 divider　*/
@Composable
fun DividerWithText(
    text: String,
    textColor: Color = Color.White // 預設白色，可手動指定
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
/* 中間可以輸入的框框 😍🥰😘 */

@Composable
fun LabeledInputBox(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 20.sp,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    // 根據焦點切換框線顏色
    val borderColor = if (isFocused) Color(0xFF90CAF9) else Color(0xFFEAECEF)

    Box(
        modifier = modifier
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
                    interactionSource = interactionSource, // ✅ 加這行才會追蹤焦點
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
}


/* 左上角字體 😍🥰😘 */

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
            modifier = Modifier.padding(start = 12.dp), // 對齊下方 padding 的 Box 輸入框
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
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    // 焦點變色：淡藍 / 淺灰
    val borderColor = if (isFocused) Color(0xFF90CAF9) else Color(0xFFEAECEF)

    Column(modifier = modifier) {
        // Label
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

        // Input box
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
                interactionSource = interactionSource, // ✅ 必加才能偵測 focus
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
                            fontSize = 20.sp,
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
    gradient: Brush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF764BA2),
            Color(0xFF667EEA)
        )
    ),
    cornerRadius: androidx.compose.ui.unit.Dp = 12.dp,
    fontSize: TextUnit = 18.sp,
    fontWeight: FontWeight = FontWeight.SemiBold
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .background(gradient, shape = RoundedCornerShape(cornerRadius)),
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
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
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
    trailingIcon: (@Composable () -> Unit)? = null
) {
    InputFieldLabel(
        text = label,
        modifier = Modifier.fillMaxWidth(0.85f)
    )
    LabeledInputBox(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        modifier = Modifier.fillMaxWidth(0.8f),
        fontSize = fontSize,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon
    )
    Spacer(Modifier.height(8.dp))
}