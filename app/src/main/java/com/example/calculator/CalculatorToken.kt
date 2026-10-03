package com.example.calculator

sealed class CalculatorToken {
    data class NumberToken(val value: Double) : CalculatorToken()
    data class OperatorToken(val symbol: String, val precedence: Int, val isRightAssociative: Boolean = false) : CalculatorToken()
    data class FunctionToken(val name: String) : CalculatorToken()
    object OpenParen : CalculatorToken()
    object CloseParen : CalculatorToken()
    object PostfixFactorial : CalculatorToken()
    object PostfixPercent : CalculatorToken()
    object PostfixSquare : CalculatorToken()
    object PostfixCube : CalculatorToken()
}

enum class AngleMode {
    DEG, RAD
}
