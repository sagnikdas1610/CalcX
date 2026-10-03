package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.example.calculator.AngleMode
import com.example.data.PreferencesRepository
import com.example.navigation.CalcXNavGraph
import com.example.ui.theme.CalcXTheme
import com.example.ui.theme.ThemeMode
import com.example.viewmodel.CalculatorViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val calculatorViewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val preferencesRepository = PreferencesRepository(applicationContext)

        setContent {
            val scope = rememberCoroutineScope()
            val themeMode by preferencesRepository.themeModeFlow.collectAsState(initial = ThemeMode.SYSTEM)
            val hapticsEnabled by preferencesRepository.hapticsEnabledFlow.collectAsState(initial = true)
            val defaultAngleMode by preferencesRepository.defaultAngleModeFlow.collectAsState(initial = AngleMode.DEG)

            val isSystemDark = isSystemInDarkTheme()
            val isDarkTheme = when (themeMode) {
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
                ThemeMode.SYSTEM -> isSystemDark
            }

            CalcXTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()
                CalcXNavGraph(
                    navController = navController,
                    calculatorViewModel = calculatorViewModel,
                    themeMode = themeMode,
                    onThemeModeChange = { newMode ->
                        scope.launch { preferencesRepository.setThemeMode(newMode) }
                    },
                    hapticsEnabled = hapticsEnabled,
                    onHapticsChange = { enabled ->
                        scope.launch { preferencesRepository.setHapticsEnabled(enabled) }
                    },
                    defaultAngleMode = defaultAngleMode,
                    onAngleModeChange = { mode ->
                        scope.launch { preferencesRepository.setDefaultAngleMode(mode) }
                    }
                )
            }
        }
    }
}
