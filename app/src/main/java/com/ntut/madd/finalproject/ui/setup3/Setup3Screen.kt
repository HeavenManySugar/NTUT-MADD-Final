package com.ntut.madd.finalproject.ui.setup3

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.ntut.madd.finalproject.ui.shared.SecondaryButton
import com.ntut.madd.finalproject.ui.theme.DarkBlue
import com.ntut.madd.finalproject.ui.theme.LightGray
import com.ntut.madd.finalproject.ui.theme.MakeItSoTheme
import com.ntut.madd.finalproject.ui.theme.purpleGradient
import kotlinx.serialization.Serializable

@Serializable
object Setup3Route

@Composable
fun Setup3Screen(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    viewModel: Setup3ViewModel = hiltViewModel()
) {
    val selectedDegree by viewModel.selectedDegree.collectAsStateWithLifecycle()
    val school by viewModel.school.collectAsStateWithLifecycle()
    val major by viewModel.major.collectAsStateWithLifecycle()
    val isFormValid by viewModel.isFormValid.collectAsStateWithLifecycle()
    val navigateToNext by viewModel.navigateToNext.collectAsStateWithLifecycle()
    
    if (navigateToNext) {
        LaunchedEffect(navigateToNext) {
            onNextClick()
            viewModel.onNavigateHandled()
        }
    }

    Setup3ScreenContent(
        selectedDegree = selectedDegree,
        school = school,
        major = major,
        isFormValid = isFormValid,
        onBackClick = onBackClick,
        onDegreeChange = viewModel::updateDegree,
        onSchoolChange = viewModel::updateSchool,
        onMajorChange = viewModel::updateMajor,
        onNextClick = { viewModel.onNextClicked() }
    )
}

@Composable
fun Setup3ScreenContent(
    selectedDegree: String,
    school: String,
    major: String,
    isFormValid: Boolean,
    onBackClick: () -> Unit,
    onDegreeChange: (String) -> Unit,
    onSchoolChange: (String) -> Unit,
    onMajorChange: (String) -> Unit,
    onNextClick: () -> Unit
) {
    SetupPageContainer(
        currentStep = 3,
        totalSteps = 6,
        headerIcon = "🎓",
        headerTitle = stringResource(R.string.education_question),
        headerSubtitle = stringResource(R.string.education_description),
        isFormValid = isFormValid,
        onBackClick = onBackClick,
        onNextClick = onNextClick
    ) {
        // 學歷選擇
        SetupFieldLabel(
            text = stringResource(R.string.degree_label),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        DegreeOptionButtons(
            selectedDegree = selectedDegree,
            onDegreeSelected = onDegreeChange
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // 學校輸入
        SetupFieldLabel(
            text = stringResource(R.string.school_label),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        SetupInputField(
            value = school,
            onValueChange = onSchoolChange,
            placeholder = R.string.school_placeholder,
            errorMessage = if (school.isNotBlank() && school.length < 2) "學校名稱至少需要2個字符" else null,
            hasError = school.isNotBlank() && school.length < 2,
            showSuccess = school.length >= 2,
            maxLength = 100
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // 主修輸入
        SetupFieldLabel(
            text = stringResource(R.string.major_label),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        SetupInputField(
            value = major,
            onValueChange = onMajorChange,
            placeholder = R.string.major_placeholder,
            errorMessage = if (major.isNotBlank() && major.length < 2) "科系名稱至少需要2個字符" else null,
            hasError = major.isNotBlank() && major.length < 2,
            showSuccess = major.length >= 2,
            maxLength = 100
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DegreeOptionButtons(
    selectedDegree: String,
    onDegreeSelected: (String) -> Unit
) {
    val degreeOptions = listOf(
        stringResource(R.string.degree_high_school),
        stringResource(R.string.degree_college),
        stringResource(R.string.degree_bachelor),
        stringResource(R.string.degree_master),
        stringResource(R.string.degree_doctor),
        stringResource(R.string.degree_other)
    )

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        degreeOptions.forEach { degree ->
            val isSelected = degree == selectedDegree
            
            Card(
                modifier = Modifier
                    .clickable { onDegreeSelected(degree) },
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFFE3F2FD) else Color(0xFFF8F9FA)
                ),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) Color(0xFF1976D2) else Color(0xFFEAECEF),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = degree,
                        color = if (isSelected) Color(0xFF1976D2) else Color.Black,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun Setup3ScreenPreview() {
    MakeItSoTheme(darkTheme = false) {
        Setup3ScreenContent(
            selectedDegree = "大學",
            school = "",
            major = "",
            isFormValid = false,
            onBackClick = {},
            onDegreeChange = {},
            onSchoolChange = {},
            onMajorChange = {},
            onNextClick = {}
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
@Preview(showBackground = true)
fun DegreeOptionButtonsPreview() {
    MakeItSoTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "最高學歷",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            DegreeOptionButtons(
                selectedDegree = "大學",
                onDegreeSelected = {}
            )
        }
    }
}
