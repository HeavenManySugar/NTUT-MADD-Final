package com.ntut.madd.finalproject.data.model

import androidx.compose.ui.graphics.Color
import com.ntut.madd.finalproject.ui.theme.LightGreen
import com.ntut.madd.finalproject.ui.theme.LightOrange
import com.ntut.madd.finalproject.ui.theme.LightRed

enum class Priority(val title: String, val selectedColor: Color, val value: Int) {
    NONE("None", Color.White, 0),
    LOW("Low", LightGreen, 1),
    MEDIUM("Medium", LightOrange, 2),
    HIGH("High", LightRed, 3)
}