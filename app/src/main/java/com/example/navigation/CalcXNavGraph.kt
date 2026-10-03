package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.calculator.AngleMode
import com.example.ui.screens.CalculatorScreen
import com.example.ui.screens.EquationSolverScreen
import com.example.ui.screens.FinanceCalculatorScreen
import com.example.ui.screens.GraphCalculatorScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.MatrixCalculatorScreen
import com.example.ui.screens.ModulesScreen
import com.example.ui.screens.ProgrammerCalculatorScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StatisticsCalculatorScreen
import com.example.ui.screens.UnitConverterScreen
import com.example.ui.theme.ThemeMode
import com.example.viewmodel.CalculatorViewModel

@Composable
fun CalcXNavGraph(
    navController: NavHostController,
    calculatorViewModel: CalculatorViewModel,
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    hapticsEnabled: Boolean,
    onHapticsChange: (Boolean) -> Unit,
    defaultAngleMode: AngleMode,
    onAngleModeChange: (AngleMode) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = CalcXDestination.Calculator.route,
        modifier = modifier
    ) {
        composable(CalcXDestination.Calculator.route) {
            CalculatorScreen(
                viewModel = calculatorViewModel,
                onNavigateToHistory = { navController.navigate(CalcXDestination.History.route) },
                onNavigateToModules = { navController.navigate(CalcXDestination.Modules.route) },
                onNavigateToSettings = { navController.navigate(CalcXDestination.Settings.route) }
            )
        }

        composable(CalcXDestination.History.route) {
            HistoryScreen(
                viewModel = calculatorViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(CalcXDestination.Modules.route) {
            ModulesScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToRoute = { route ->
                    if (route == CalcXDestination.Calculator.route) {
                        navController.popBackStack(CalcXDestination.Calculator.route, false)
                    } else {
                        navController.navigate(route)
                    }
                }
            )
        }

        composable(CalcXDestination.UnitConverter.route) {
            UnitConverterScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(CalcXDestination.EquationSolver.route) {
            EquationSolverScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(CalcXDestination.MatrixCalculator.route) {
            MatrixCalculatorScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(CalcXDestination.FinanceCalculator.route) {
            FinanceCalculatorScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(CalcXDestination.ProgrammerCalculator.route) {
            ProgrammerCalculatorScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(CalcXDestination.GraphCalculator.route) {
            GraphCalculatorScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(CalcXDestination.StatisticsCalculator.route) {
            StatisticsCalculatorScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(CalcXDestination.Settings.route) {
            SettingsScreen(
                currentThemeMode = themeMode,
                onThemeModeChange = onThemeModeChange,
                hapticsEnabled = hapticsEnabled,
                onHapticsChange = onHapticsChange,
                defaultAngleMode = defaultAngleMode,
                onAngleModeChange = onAngleModeChange,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
