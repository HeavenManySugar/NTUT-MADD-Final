package com.ntut.madd.finalproject.ui.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ntut.madd.finalproject.R
import com.ntut.madd.finalproject.ui.setup.components.SetupDivider
import com.ntut.madd.finalproject.ui.setup.components.SetupHeader
import com.ntut.madd.finalproject.ui.setup.components.SetupInputField
import com.ntut.madd.finalproject.ui.setup.components.SetupProgressBar
import com.ntut.madd.finalproject.ui.shared.StandardButton
import com.ntut.madd.finalproject.ui.theme.DarkBlue
import com.ntut.madd.finalproject.ui.theme.LightBlue
import com.ntut.madd.finalproject.ui.theme.LightGray
import com.ntut.madd.finalproject.ui.theme.MakeItSoTheme
import com.ntut.madd.finalproject.ui.theme.MediumGrey
import com.ntut.madd.finalproject.ui.theme.purpleGradient
import kotlinx.serialization.Serializable

@Serializable
object SetupRoute

@Composable
fun SetupScreen(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    viewModel: SetupViewModel = hiltViewModel()
) {
    val selectedCity by viewModel.selectedCity.collectAsStateWithLifecycle()
    val district by viewModel.district.collectAsStateWithLifecycle()
    val isFormValid by viewModel.isFormValid.collectAsStateWithLifecycle()
    val navigateToNext by viewModel.navigateToNext.collectAsStateWithLifecycle()
    
    if (navigateToNext) {
        LaunchedEffect(navigateToNext) {
            onNextClick()
            viewModel.onNavigateHandled()
        }
    }
    
    SetupScreenContent(
        selectedCity = selectedCity,
        district = district,
        isFormValid = isFormValid,
        onBackClick = onBackClick,
        onCitySelected = viewModel::updateCity,
        onDistrictChanged = viewModel::updateDistrict,
        onNextClick = viewModel::onNextClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreenContent(
    selectedCity: String,
    district: String,
    isFormValid: Boolean,
    onBackClick: () -> Unit,
    onCitySelected: (String) -> Unit,
    onDistrictChanged: (String) -> Unit,
    onNextClick: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // 紫色漸變頭部區域
            SetupHeader(
                onBackClick = onBackClick,
                icon = "📍",
                title = stringResource(R.string.location_question),
                subtitle = stringResource(R.string.location_description)
            )
            
            // 進度條區域
            SetupProgressBar(currentStep = 1)

            // 分隔線
            SetupDivider()
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                Text(
                    text = stringResource(R.string.city_label),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 城市選擇框 - 使用統一的灰色背景樣式
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF8F9FA) // 背景色 F8F9FA
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    CityDropdown(
                        selectedCity = selectedCity,
                        onCitySelected = onCitySelected,
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = stringResource(R.string.district_label),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 區域輸入框 - 使用重用組件
                SetupInputField(
                    value = district,
                    onValueChange = onDistrictChanged,
                    placeholder = R.string.district_placeholder
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // 下一步按钮
                StandardButton(
                    label = R.string.next_step,
                    onButtonClick = onNextClick,
                    enabled = isFormValid
                )
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun CityDropdown(
    selectedCity: String,
    onCitySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val cities = listOf("台北市", "新北市", "桃園市", "台中市", "台南市", "高雄市")
    
    Box {
        OutlinedTextField(
            value = selectedCity,
            onValueChange = { },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = stringResource(R.string.city_placeholder),
                    color = Color.Gray.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            },
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = "下拉",
                        tint = Color.Gray
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedBorderColor = Color(0xFFEAECEF), // 框線色 EAECEF
                focusedBorderColor = Color(0xFF6B46C1).copy(alpha = 0.5f),
                cursorColor = Color(0xFF6B46C1)
            )
        )
        
        // Invisible clickable overlay
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { expanded = true }
        )
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            cities.forEach { city ->
                DropdownMenuItem(
                    text = { Text(city) },
                    onClick = {
                        onCitySelected(city)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun SetupScreenPreview() {
    MakeItSoTheme(darkTheme = false) {
        SetupScreenContent(
            selectedCity = "",
            district = "",
            isFormValid = false,
            onBackClick = {},
            onCitySelected = {},
            onDistrictChanged = {},
            onNextClick = {}
        )
    }
}
