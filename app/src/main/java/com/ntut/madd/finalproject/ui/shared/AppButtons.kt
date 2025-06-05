package com.ntut.madd.finalproject.ui.shared

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ntut.madd.finalproject.R
import com.ntut.madd.finalproject.ui.theme.DarkBlue
import com.ntut.madd.finalproject.ui.theme.purpleGradient

@Composable
fun AuthWithGoogleButton(@StringRes label: Int, onButtonClick: () -> Unit) {
   OutlinedButton(
       modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
       onClick = onButtonClick,
       colors = ButtonDefaults.outlinedButtonColors(
           containerColor = Color.White,
           contentColor = DarkBlue
       ),
       border = BorderStroke(1.dp, DarkBlue)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.google_g),
            modifier = Modifier.padding(horizontal = 16.dp),
            contentDescription = "Google logo"
        )

        Text(
            text = stringResource(label),
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 6.dp)
        )
    }
}

@Composable
fun StandardButton(
    @StringRes label: Int, 
    onButtonClick: () -> Unit,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        modifier = modifier
            .then(
                if (modifier == Modifier) Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                else Modifier
            )
            .background(
                brush = if (enabled && !isLoading) purpleGradient else SolidColor(Color.Gray),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            ),
        onClick = onButtonClick,
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.White
        ),
        border = BorderStroke(1.dp, if (enabled && !isLoading) Color.Transparent else Color.Gray),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = stringResource(label),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun SecondaryButton(
    @StringRes label: Int,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        modifier = modifier
            .then(
                if (modifier == Modifier) Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                else Modifier
            ),
        onClick = onButtonClick,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = Color.Gray
        ),
        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f)),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
    ) {
        Text(
            text = stringResource(label),
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 6.dp)
        )
    }
}