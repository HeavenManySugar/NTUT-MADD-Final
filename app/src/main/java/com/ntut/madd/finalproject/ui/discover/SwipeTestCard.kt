package com.ntut.madd.finalproject.ui.discover

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

/**
 * Simple test component to verify swipe animations are working
 */
@Composable
fun SwipeTestCard() {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var rotation by remember { mutableFloatStateOf(0f) }
    var isAnimating by remember { mutableStateOf(false) }
    
    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = if (isAnimating) {
            tween(durationMillis = 500, easing = FastOutSlowInEasing)
        } else {
            spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        },
        finishedListener = {
            if (isAnimating) {
                // Reset after animation
                offsetX = 0f
                rotation = 0f
                isAnimating = false
            }
        },
        label = "offsetX"
    )
    
    val animatedRotation by animateFloatAsState(
        targetValue = rotation,
        animationSpec = if (isAnimating) {
            tween(durationMillis = 500, easing = FastOutSlowInEasing)
        } else {
            spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        },
        label = "rotation"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        // Test card
        Card(
            modifier = Modifier
                .size(300.dp, 400.dp)
                .graphicsLayer {
                    translationX = animatedOffsetX
                    rotationZ = animatedRotation
                    val alpha = 1f - (abs(animatedOffsetX) / 1000f).coerceIn(0f, 0.8f)
                    this.alpha = alpha
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            println("SwipeTest: Drag started")
                            isAnimating = false
                        },
                        onDragEnd = {
                            println("SwipeTest: Drag ended, offsetX = $offsetX")
                            when {
                                offsetX > 150f -> {
                                    // Like animation
                                    isAnimating = true
                                    offsetX = 1000f
                                    rotation = 30f
                                    println("SwipeTest: LIKE triggered")
                                }
                                offsetX < -150f -> {
                                    // Dislike animation
                                    isAnimating = true
                                    offsetX = -1000f
                                    rotation = -30f
                                    println("SwipeTest: DISLIKE triggered")
                                }
                                else -> {
                                    // Return to center
                                    offsetX = 0f
                                    rotation = 0f
                                    println("SwipeTest: Return to center")
                                }
                            }
                        }
                    ) { _, dragAmount ->
                        if (!isAnimating) {
                            offsetX += dragAmount.x
                            rotation = (offsetX / 10f).coerceIn(-30f, 30f)
                            println("SwipeTest: Dragging offsetX = $offsetX")
                        }
                    }
                },
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "💝",
                    fontSize = 80.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Test Card",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Swipe me left or right!",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                
                // Show current offset for debugging
                Text(
                    text = "Offset: ${offsetX.toInt()}",
                    fontSize = 14.sp,
                    color = Color.Blue,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
        
        // Action buttons for testing
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    isAnimating = true
                    offsetX = -1000f
                    rotation = -30f
                    println("SwipeTest: Dislike button pressed")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Dislike", color = Color.White)
            }
            
            Button(
                onClick = {
                    isAnimating = true
                    offsetX = 1000f
                    rotation = 30f
                    println("SwipeTest: Like button pressed")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
            ) {
                Text("Like", color = Color.White)
            }
        }
        
        // Swipe indicators
        if (abs(offsetX) > 50f && !isAnimating) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .background(
                        color = if (offsetX > 0) Color.Green.copy(alpha = 0.8f) else Color.Red.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(
                    text = if (offsetX > 0) "LIKE ❤️" else "💔 DISLIKE",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SwipeTestCardPreview() {
    SwipeTestCard()
}
