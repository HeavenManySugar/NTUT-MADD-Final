package com.ntut.madd.finalproject.ui.setup2

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ntut.madd.finalproject.R
import com.ntut.madd.finalproject.ui.setup.components.SetupInputField
import com.ntut.madd.finalproject.ui.setup.components.SetupPageContainer
import com.ntut.madd.finalproject.ui.setup.components.SetupFieldLabel
import com.ntut.madd.finalproject.ui.theme.MakeItSoTheme
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
        // Position input
        SetupFieldLabel(
            text = stringResource(R.string.position_label),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        SetupInputField(
            value = position,
            onValueChange = onPositionChanged,
            placeholder = R.string.position_placeholder,
            errorMessage = if (position.isNotBlank() && position.length < 2) stringResource(R.string.position_validation) else null,
            hasError = position.isNotBlank() && position.length < 2,
            showSuccess = position.length >= 2,
            maxLength = 50
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Company input
        SetupFieldLabel(
            text = stringResource(R.string.company_label),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        SetupInputField(
            value = company,
            onValueChange = onCompanyChanged,
            placeholder = R.string.company_placeholder,
            errorMessage = if (company.isNotBlank() && company.length < 2) stringResource(R.string.company_validation) else null,
            hasError = company.isNotBlank() && company.length < 2,
            showSuccess = company.length >= 2,
            maxLength = 100
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