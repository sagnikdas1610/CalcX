package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calculator.CalculatorAction
import com.example.calculator.CalculatorState
import com.example.ui.components.CalcXTopBar
import com.example.ui.components.CalculatorDisplay
import com.example.ui.components.CalculatorKeypad
import com.example.ui.components.ScientificKeypad
import com.example.ui.theme.CalcXTheme
import com.example.viewmodel.CalculatorViewModel
import kotlinx.coroutines.launch

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    onNavigateToHistory: () -> Unit,
    onNavigateToModules: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("screen_calculator"),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CalcXTopBar(
                onOpenHistory = onNavigateToHistory,
                onToggleScientific = { viewModel.onAction(CalculatorAction.ToggleScientific) },
                onOpenModules = onNavigateToModules,
                onOpenSettings = onNavigateToSettings,
                isScientificActive = state.isScientific
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Display Area (takes available vertical space above keypads)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                CalculatorDisplay(
                    expression = state.expression,
                    result = state.result,
                    previewResult = state.previewResult,
                    error = state.error,
                    angleMode = state.angleMode,
                    isScientific = state.isScientific,
                    onResultCopy = { copiedText ->
                        clipboardManager.setText(AnnotatedString(copiedText))
                        scope.launch {
                            snackbarHostState.showSnackbar("Result copied: $copiedText")
                        }
                    },
                    onBackspace = { viewModel.onAction(CalculatorAction.Backspace) },
                    onClearAll = { viewModel.onAction(CalculatorAction.ClearAll) },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Scientific Keypad (Expandable)
            AnimatedVisibility(
                visible = state.isScientific,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                ScientificKeypad(
                    angleMode = state.angleMode,
                    onAction = viewModel::onAction,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            // Basic Keypad
            CalculatorKeypad(
                onAction = viewModel::onAction,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
    }
}

@Preview(name = "Calculator Screen Dark", showBackground = true)
@Composable
private fun CalculatorScreenDarkPreview() {
    CalcXTheme(darkTheme = true) {
        CalculatorScreenContentPreview(state = CalculatorState(expression = "25 × 18 + 40", result = "490"))
    }
}

@Preview(name = "Calculator Screen Light", showBackground = true)
@Composable
private fun CalculatorScreenLightPreview() {
    CalcXTheme(darkTheme = false) {
        CalculatorScreenContentPreview(state = CalculatorState(expression = "2 × sin(45) + √144", result = "13.414"))
    }
}

@Composable
private fun CalculatorScreenContentPreview(state: CalculatorState) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        CalcXTopBar(
            onOpenHistory = {},
            onToggleScientific = {},
            onOpenModules = {},
            onOpenSettings = {}
        )
        Box(modifier = Modifier.weight(1f)) {
            CalculatorDisplay(
                expression = state.expression,
                result = state.result,
                previewResult = state.previewResult,
                error = null,
                angleMode = state.angleMode,
                isScientific = false,
                onResultCopy = {},
                onBackspace = {},
                onClearAll = {},
                modifier = Modifier.fillMaxSize()
            )
        }
        CalculatorKeypad(onAction = {})
    }
}
