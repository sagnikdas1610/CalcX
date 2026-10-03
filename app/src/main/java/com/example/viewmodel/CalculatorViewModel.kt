package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculator.AngleMode
import com.example.calculator.CalculatorAction
import com.example.calculator.CalculatorEngine
import com.example.calculator.CalculatorState
import com.example.data.HistoryRepository
import com.example.data.InMemoryHistoryRepository
import com.example.model.CalculationHistoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CalculatorViewModel(
    private val historyRepository: HistoryRepository = defaultHistoryRepository
) : ViewModel() {

    companion object {
        val defaultHistoryRepository = InMemoryHistoryRepository()
    }

    private val _uiState = MutableStateFlow(CalculatorState())
    val uiState: StateFlow<CalculatorState> = _uiState.asStateFlow()

    val historyItems: StateFlow<List<CalculationHistoryItem>> = historyRepository.historyFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Operator -> enterOperator(action.op)
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Clear -> clearExpression()
            is CalculatorAction.ClearAll -> clearAll()
            is CalculatorAction.Backspace -> deleteLast()
            is CalculatorAction.Calculate -> calculateResult()
            is CalculatorAction.TogglePlusMinus -> toggleSign()
            is CalculatorAction.Percentage -> enterPercentage()
            is CalculatorAction.Parenthesis -> enterParenthesis()
            is CalculatorAction.ToggleAngleMode -> toggleAngleMode()
            is CalculatorAction.ToggleScientific -> toggleScientific()
            is CalculatorAction.ScientificFunction -> enterScientificFunction(action.func)
            is CalculatorAction.Constant -> enterConstant(action.symbol)
            is CalculatorAction.ReuseHistory -> reuseHistory(action.expression, action.result)
        }
    }

    private fun enterNumber(number: Int) {
        _uiState.update { state ->
            val newExpr = if (state.isResultEvaluated) {
                number.toString()
            } else {
                state.expression + number
            }
            state.copy(
                expression = newExpr,
                isResultEvaluated = false,
                error = null,
                previewResult = computeLivePreview(newExpr, state.angleMode)
            )
        }
    }

    private fun enterOperator(op: String) {
        _uiState.update { state ->
            val currentExpr = if (state.isResultEvaluated) {
                state.result
            } else {
                state.expression
            }

            if (currentExpr.isEmpty()) {
                if (op == "−" || op == "-") {
                    return@update state.copy(expression = "−", isResultEvaluated = false, error = null)
                }
                return@update state
            }

            val lastChar = currentExpr.last().toString()
            val operators = listOf("+", "−", "-", "×", "*", "÷", "/", "^")

            val newExpr = if (operators.contains(lastChar)) {
                // Replace previous operator
                currentExpr.dropLast(1) + op
            } else {
                currentExpr + op
            }

            state.copy(
                expression = newExpr,
                isResultEvaluated = false,
                error = null,
                previewResult = computeLivePreview(newExpr, state.angleMode)
            )
        }
    }

    private fun enterDecimal() {
        _uiState.update { state ->
            val expr = if (state.isResultEvaluated) "0" else state.expression
            if (expr.isEmpty()) {
                return@update state.copy(expression = "0.", isResultEvaluated = false)
            }

            // Check if current active number segment already has a decimal
            val lastTokens = expr.split("+", "−", "-", "×", "*", "÷", "/", "(", ")", "^")
            val currentNumberSegment = lastTokens.lastOrNull() ?: ""

            if (!currentNumberSegment.contains(".")) {
                val newExpr = if (expr.last().isDigit()) "$expr." else "${expr}0."
                state.copy(
                    expression = newExpr,
                    isResultEvaluated = false,
                    previewResult = computeLivePreview(newExpr, state.angleMode)
                )
            } else {
                state
            }
        }
    }

    private fun enterParenthesis() {
        _uiState.update { state ->
            val expr = state.expression
            val openCount = expr.count { it == '(' }
            val closeCount = expr.count { it == ')' }

            val newExpr = if (openCount > closeCount && expr.isNotEmpty() && (expr.last().isDigit() || expr.last() == ')' || expr.last() == 'π' || expr.last() == 'e')) {
                "$expr)"
            } else {
                "$expr("
            }

            state.copy(
                expression = newExpr,
                isResultEvaluated = false,
                previewResult = computeLivePreview(newExpr, state.angleMode)
            )
        }
    }

    private fun enterPercentage() {
        _uiState.update { state ->
            if (state.expression.isEmpty()) return@update state
            val lastChar = state.expression.last()
            if (lastChar.isDigit() || lastChar == ')' || lastChar == 'π' || lastChar == 'e') {
                val newExpr = state.expression + "%"
                state.copy(
                    expression = newExpr,
                    isResultEvaluated = false,
                    previewResult = computeLivePreview(newExpr, state.angleMode)
                )
            } else {
                state
            }
        }
    }

    private fun toggleSign() {
        _uiState.update { state ->
            val expr = state.expression
            if (expr.isEmpty()) {
                return@update state.copy(expression = "−")
            }
            // Negate whole expression or last number
            val newExpr = if (expr.startsWith("−")) {
                expr.substring(1)
            } else if (expr.startsWith("-")) {
                expr.substring(1)
            } else {
                "−$expr"
            }
            state.copy(
                expression = newExpr,
                previewResult = computeLivePreview(newExpr, state.angleMode)
            )
        }
    }

    private fun deleteLast() {
        _uiState.update { state ->
            if (state.expression.isNotEmpty()) {
                val newExpr = state.expression.dropLast(1)
                state.copy(
                    expression = newExpr,
                    error = null,
                    isResultEvaluated = false,
                    previewResult = computeLivePreview(newExpr, state.angleMode)
                )
            } else {
                state
            }
        }
    }

    private fun clearExpression() {
        _uiState.update { state ->
            state.copy(
                expression = "",
                result = "0",
                previewResult = "",
                error = null,
                isResultEvaluated = false
            )
        }
    }

    private fun clearAll() {
        clearExpression()
    }

    private fun calculateResult() {
        val state = _uiState.value
        val expr = state.expression.trim()
        if (expr.isEmpty()) return

        when (val eval = CalculatorEngine.evaluate(expr, state.angleMode)) {
            is CalculatorEngine.Result.Success -> {
                _uiState.update {
                    it.copy(
                        result = eval.formatted,
                        previewResult = "",
                        error = null,
                        isResultEvaluated = true
                    )
                }
                viewModelScope.launch {
                    historyRepository.addCalculation(expr, eval.formatted)
                }
            }
            is CalculatorEngine.Result.Error -> {
                _uiState.update {
                    it.copy(
                        error = eval.message,
                        result = eval.message,
                        isResultEvaluated = false
                    )
                }
            }
        }
    }

    private fun enterScientificFunction(func: String) {
        _uiState.update { state ->
            val expr = if (state.isResultEvaluated) "" else state.expression
            val newExpr = when (func) {
                "x²" -> if (expr.isNotEmpty()) "${expr}²" else expr
                "x³" -> if (expr.isNotEmpty()) "${expr}³" else expr
                "xʸ" -> if (expr.isNotEmpty()) "$expr^" else expr
                "x!" -> if (expr.isNotEmpty()) "$expr!" else expr
                "1/x" -> if (expr.isNotEmpty()) "inv($expr)" else "inv("
                "sqrt", "√" -> "${expr}√("
                "sin" -> "${expr}sin("
                "cos" -> "${expr}cos("
                "tan" -> "${expr}tan("
                "asin" -> "${expr}asin("
                "acos" -> "${expr}acos("
                "atan" -> "${expr}atan("
                "ln" -> "${expr}ln("
                "log" -> "${expr}log("
                else -> "$expr$func("
            }
            state.copy(
                expression = newExpr,
                isResultEvaluated = false,
                previewResult = computeLivePreview(newExpr, state.angleMode)
            )
        }
    }

    private fun enterConstant(symbol: String) {
        _uiState.update { state ->
            val expr = if (state.isResultEvaluated) "" else state.expression
            val newExpr = expr + symbol
            state.copy(
                expression = newExpr,
                isResultEvaluated = false,
                previewResult = computeLivePreview(newExpr, state.angleMode)
            )
        }
    }

    private fun toggleAngleMode() {
        _uiState.update { state ->
            val newMode = if (state.angleMode == AngleMode.DEG) AngleMode.RAD else AngleMode.DEG
            state.copy(
                angleMode = newMode,
                previewResult = computeLivePreview(state.expression, newMode)
            )
        }
    }

    private fun toggleScientific() {
        _uiState.update { state ->
            state.copy(isScientific = !state.isScientific)
        }
    }

    private fun reuseHistory(expression: String, result: String) {
        _uiState.update { state ->
            state.copy(
                expression = expression,
                result = result,
                previewResult = "",
                isResultEvaluated = true,
                error = null
            )
        }
    }

    fun deleteHistoryItem(id: String) {
        viewModelScope.launch {
            historyRepository.deleteItem(id)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            historyRepository.clearHistory()
        }
    }

    private fun computeLivePreview(expr: String, angleMode: AngleMode): String {
        if (expr.length < 2) return ""
        val hasOp = expr.any { it in "+−-×*÷/^%!(" }
        if (!hasOp) return ""

        return when (val res = CalculatorEngine.evaluate(expr, angleMode)) {
            is CalculatorEngine.Result.Success -> res.formatted
            is CalculatorEngine.Result.Error -> ""
        }
    }
}
