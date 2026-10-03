package com.example.calculator

sealed class CalculatorAction {
    data class Number(val number: Int) : CalculatorAction()
    data class Operator(val op: String) : CalculatorAction()
    object Decimal : CalculatorAction()
    object Clear : CalculatorAction()
    object ClearAll : CalculatorAction()
    object Backspace : CalculatorAction()
    object Calculate : CalculatorAction()
    object TogglePlusMinus : CalculatorAction()
    object Percentage : CalculatorAction()
    object Parenthesis : CalculatorAction()
    object ToggleAngleMode : CalculatorAction()
    object ToggleScientific : CalculatorAction()
    data class ScientificFunction(val func: String) : CalculatorAction()
    data class Constant(val symbol: String) : CalculatorAction()
    data class ReuseHistory(val expression: String, val result: String) : CalculatorAction()
}
