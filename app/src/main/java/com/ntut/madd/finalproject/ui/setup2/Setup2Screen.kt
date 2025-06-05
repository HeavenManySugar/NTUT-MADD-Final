package com.ntut.madd.finalproject.ui.setup2

import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
object Setup2Route

@Composable
fun Setup2Screen(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    viewModel: Setup2ViewModel = hiltViewModel()
) {
    val position by viewModel.position.collectAsStateWithLifecycle()
    val company by viewModel.company.collectAsStateWithLifecycle()
    val isFormValid by viewModel.isFormValid.collectAsStateWithLifecycle()
    val navigateToNext by viewModel.navigateToNext.collectAsStateWithLifecycle()
    
    if (navigateToNext) {
        LaunchedEffect(navigateToNext) {
            onNextClick()
            viewModel.onNavigateHandled()
        }
    }
    
    Setup2ScreenContent(
        position = position,
        company = company,
        isFormValid = isFormValid,
        onBackClick = onBackClick,
        onPositionChanged = viewModel::updatePosition,
        onCompanyChanged = viewModel::updateCompany,
        onNextClick = viewModel::onNextClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Setup2ScreenContent(
    position: String,
    company: String,
    isFormValid: Boolean,
    onBackClick: () -> Unit,
    onPositionChanged: (String) -> Unit,
    onCompanyChanged: (String) -> Unit,
    onNextClick: () -> Unit
) {
    SetupPageContainer(
        currentStep = 2,
        totalSteps = 6,
        headerIcon = "👔",
        headerTitle = stringResource(R.string.career_question),
        headerSubtitle = stringResource(R.string.career_description),
        isFormValid = isFormValid,
        onBackClick = onBackClick,
        onNextClick = onNextClick
    ) {
        // 職位輸入
        SetupFieldLabel(
            text = stringResource(R.string.position_label),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        SetupInputField(
            value = position,
            onValueChange = onPositionChanged,
            placeholder = R.string.position_placeholder
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // 公司輸入
        SetupFieldLabel(
            text = stringResource(R.string.company_label),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        SetupInputField(
            value = company,
            onValueChange = onCompanyChanged,
            placeholder = R.string.company_placeholder
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun Setup2ScreenPreview() {
    MakeItSoTheme(darkTheme = false) {
        Setup2ScreenContent(
            position = "",
            company = "",
            isFormValid = false,
            onBackClick = {},
            onPositionChanged = {},
            onCompanyChanged = {},
            onNextClick = {}
        )
    }
}
