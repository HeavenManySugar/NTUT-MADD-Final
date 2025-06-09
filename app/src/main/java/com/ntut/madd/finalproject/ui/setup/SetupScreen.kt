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
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.ntut.madd.finalproject.ui.setup.components.SetupPageContainer
import com.ntut.madd.finalproject.ui.setup.components.SetupFieldLabel
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
    SetupPageContainer(
        currentStep = 1,
        totalSteps = 6,
        headerIcon = "📍",
        headerTitle = stringResource(R.string.location_question),
        headerSubtitle = stringResource(R.string.location_description),
        isFormValid = isFormValid,
        onBackClick = onBackClick,
        onNextClick = onNextClick,
        showBackButton = false
    ) {
        // City selection
        SetupFieldLabel(
            text = stringResource(R.string.city_label),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF8F9FA)
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            CityDropdown(
                selectedCity = selectedCity,
                onCitySelected = onCitySelected
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        // District input
        SetupFieldLabel(
            text = stringResource(R.string.district_label),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        SetupInputField(
            value = district,
            onValueChange = onDistrictChanged,
            placeholder = R.string.district_placeholder
        )
    }
}

@Composable
fun CityDropdown(
    selectedCity: String,
    onCitySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val allCities = listOf(
        stringResource(R.string.city_taipei),
        stringResource(R.string.city_new_taipei),
        stringResource(R.string.city_taoyuan),
        stringResource(R.string.city_taichung),
        stringResource(R.string.city_tainan),
        stringResource(R.string.city_kaohsiung),
        stringResource(R.string.city_keelung),
        stringResource(R.string.city_hsinchu_city),
        stringResource(R.string.city_chiayi_city),
        stringResource(R.string.city_hsinchu_county),
        stringResource(R.string.city_miaoli),
        stringResource(R.string.city_changhua),
        stringResource(R.string.city_nantou),
        stringResource(R.string.city_yunlin),
        stringResource(R.string.city_chiayi_county),
        stringResource(R.string.city_pingtung),
        stringResource(R.string.city_yilan),
        stringResource(R.string.city_hualien),
        stringResource(R.string.city_taitung),
        stringResource(R.string.city_penghu),
        stringResource(R.string.city_kinmen),
        stringResource(R.string.city_lienchiang)
    )

    val popularCities = listOf(
        stringResource(R.string.city_taipei),
        stringResource(R.string.city_new_taipei),
        stringResource(R.string.city_taoyuan),
        stringResource(R.string.city_taichung),
        stringResource(R.string.city_tainan),
        stringResource(R.string.city_kaohsiung)
    )

    val filteredCities = if (searchText.isEmpty()) {
        allCities
    } else {
        allCities.filter { it.contains(searchText, ignoreCase = true) }
    }

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
                        contentDescription = stringResource(R.string.dropdown_arrow),
                        tint = Color.Gray
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedBorderColor = Color(0xFFEAECEF), // Border color EAECEF
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
            onDismissRequest = {
                expanded = false
                searchText = ""
            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(400.dp)
        ) {
            // Search box
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                placeholder = {
                    Text(
                        stringResource(R.string.city_search_placeholder),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedBorderColor = Color(0xFF6B46C1)
                )
            )

            // If no search, display popular cities
            if (searchText.isEmpty()) {
                Text(
                    text = stringResource(R.string.popular_cities),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF6B46C1),
                    fontWeight = FontWeight.SemiBold
                )

                popularCities.forEach { city ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                Icon(
                                    Icons.Filled.LocationOn,
                                    contentDescription = null,
                                    tint = Color(0xFF6B46C1),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = city,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        },
                        onClick = {
                            onCitySelected(city)
                            expanded = false
                            searchText = ""
                        },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                    color = Color(0xFFE2E8F0)
                )

                Text(
                    text = stringResource(R.string.all_cities),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF718096),
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Show filtered cities
            filteredCities.forEach { city ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = city,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF2D3748)
                            )
                        }
                    },
                    onClick = {
                        onCitySelected(city)
                        expanded = false
                        searchText = ""
                    },
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            if (filteredCities.isEmpty() && searchText.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🔍",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = stringResource(R.string.no_cities_found),
                            fontSize = 14.sp,
                            color = Color(0xFF718096),
                            textAlign = TextAlign.Center
                        )
                    }
                }
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
