package com.example.calculator

data class CalculatorState(
    val expression: String = "",
    val result: String = "0",
    val previewResult: String = "",
    val angleMode: AngleMode = AngleMode.DEG,
    val isScientific: Boolean = false,
    val error: String? = null,
    val isResultEvaluated: Boolean = false,
    val memoryValue: Double = 0.0
)
