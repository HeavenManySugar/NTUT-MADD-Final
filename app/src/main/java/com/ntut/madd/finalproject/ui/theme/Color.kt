package com.ntut.madd.finalproject.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val LightGreen = Color(0xFFB1F1C1)
val LightOrange = Color(0xFFFFD399)
val LightRed = Color(0xFFFFDBD0)
val LightBlue = Color(0xFFC4CFDD)
val LightYellow = Color(0xFFFFF9E5)
val LightGray = Color(0xFFBFBFBF)
val MediumYellow = Color(0xFFFFF3CC)
val MediumGrey = Color(0xFF4C4C4C)
val DarkBlue = Color(0xFF3D5F90)
val DarkGrey = Color(0xFF141218)

val purpleGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF667eea),
        Color(0xFF764ba2)
    ),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
)