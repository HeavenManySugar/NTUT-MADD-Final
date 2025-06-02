package com.ntut.madd.finalproject.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Divider(Modifier.weight(1f), color = textColor)
    }
}
/* 中間可以輸入的電子信箱 😍🥰😘 */

@Composable
fun LabeledInputBox(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 20.sp
) {
    Box(
        modifier = modifier
            .height(64.dp)
            .border(
                width = 1.dp,
                color = Color(0xFFEAECEF),
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = Color(0xFFF8F9FA),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 8.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart // ✅ 垂直置中 placeholder
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = LocalTextStyle.current.copy(
                    color = Color.Black,
                    fontSize = fontSize
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = Color(0xFFB0B0B0),
                            fontSize = fontSize
                        )
                    }
                    innerTextField()
                }
            )
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
            modifier = Modifier.padding(start = 12.dp) // 對齊下方 padding 的 Box 輸入框
        )
    }
}

@Composable
fun LabeledInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // ✅ Label 包在 Box 中，用同樣 padding
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, bottom = 4.dp)
        ) {
            Text(
                text = label,
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        Box(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
                .border(1.dp, Color(0xFFEAECEF), shape = RoundedCornerShape(8.dp))
                .background(Color(0xFFF8F9FA), shape = RoundedCornerShape(8.dp))
                .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = LocalTextStyle.current.copy(
                    color = Color.Black,
                    fontSize = 16.sp
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(text = placeholder, color = Color(0xFFB0B0B0), fontSize = 16.sp)
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Composable
fun LabeledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    InputFieldLabel(
        text = label,
        modifier = Modifier.fillMaxWidth(0.85f)
    )
    LabeledInputBox(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        modifier = Modifier.fillMaxWidth(0.8f)
    )
    Spacer(Modifier.height(8.dp))
}