package com.example.calculator

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.*

object CalculatorEngine {

    sealed class Result {
        data class Success(val value: Double, val formatted: String) : Result()
        data class Error(val message: String) : Result()
    }

    fun evaluate(expression: String, angleMode: AngleMode = AngleMode.DEG): Result {
        if (expression.isBlank()) {
            return Result.Success(0.0, "0")
        }

        return try {
            val sanitized = sanitize(expression)
            val tokens = tokenize(sanitized)
            if (tokens.isEmpty()) {
                return Result.Success(0.0, "0")
            }
            val rpn = toRpn(tokens)
            val value = evaluateRpn(rpn, angleMode)

            if (value.isNaN()) {
                Result.Error("Undefined result")
            } else if (value.isInfinite()) {
                Result.Error("Cannot divide by zero")
            } else {
                Result.Success(value, formatResult(value))
            }
        } catch (e: ArithmeticException) {
            Result.Error(e.message ?: "Calculation error")
        } catch (e: IllegalArgumentException) {
            Result.Error(e.message ?: "Invalid expression")
        } catch (e: Exception) {
            Result.Error("Invalid expression")
        }
    }

    private fun sanitize(expr: String): String {
        return expr
            .replace("×", "*")
            .replace("÷", "/")
            .replace("−", "-")
            .replace("π", "PI")
            .replace("√", "sqrt")
            .replace(" ", "")
    }

    private fun tokenize(input: String): List<CalculatorToken> {
        val tokens = mutableListOf<CalculatorToken>()
        var i = 0
        var canBeUnary = true

        while (i < input.length) {
            val c = input[i]

            when {
                c.isDigit() || c == '.' -> {
                    // Check if previous token needs implicit multiplication: e.g., )2 or π2
                    if (tokens.isNotEmpty()) {
                        val prev = tokens.last()
                        if (prev is CalculatorToken.CloseParen ||
                            prev is CalculatorToken.PostfixPercent ||
                            prev is CalculatorToken.PostfixFactorial ||
                            prev is CalculatorToken.PostfixSquare ||
                            prev is CalculatorToken.PostfixCube
                        ) {
                            tokens.add(CalculatorToken.OperatorToken("*", 2))
                        }
                    }

                    val sb = StringBuilder()
                    var hasDot = false
                    while (i < input.length && (input[i].isDigit() || input[i] == '.')) {
                        if (input[i] == '.') {
                            if (hasDot) break
                            hasDot = true
                        }
                        sb.append(input[i])
                        i++
                    }
                    val num = sb.toString().toDoubleOrNull() ?: throw IllegalArgumentException("Invalid number")
                    tokens.add(CalculatorToken.NumberToken(num))
                    canBeUnary = false
                    continue
                }

                c == '(' -> {
                    if (tokens.isNotEmpty()) {
                        val prev = tokens.last()
                        if (prev is CalculatorToken.NumberToken ||
                            prev is CalculatorToken.CloseParen ||
                            prev is CalculatorToken.PostfixPercent ||
                            prev is CalculatorToken.PostfixFactorial ||
                            prev is CalculatorToken.PostfixSquare ||
                            prev is CalculatorToken.PostfixCube
                        ) {
                            tokens.add(CalculatorToken.OperatorToken("*", 2))
                        }
                    }
                    tokens.add(CalculatorToken.OpenParen)
                    canBeUnary = true
                    i++
                }

                c == ')' -> {
                    tokens.add(CalculatorToken.CloseParen)
                    canBeUnary = false
                    i++
                }

                c == '!' -> {
                    tokens.add(CalculatorToken.PostfixFactorial)
                    canBeUnary = false
                    i++
                }

                c == '%' -> {
                    tokens.add(CalculatorToken.PostfixPercent)
                    canBeUnary = false
                    i++
                }

                c == '²' -> {
                    tokens.add(CalculatorToken.PostfixSquare)
                    canBeUnary = false
                    i++
                }

                c == '³' -> {
                    tokens.add(CalculatorToken.PostfixCube)
                    canBeUnary = false
                    i++
                }

                c == '+' -> {
                    if (canBeUnary) {
                        // Unary plus is ignored
                    } else {
                        tokens.add(CalculatorToken.OperatorToken("+", 1))
                        canBeUnary = true
                    }
                    i++
                }

                c == '-' -> {
                    if (canBeUnary) {
                        // Unary minus: represented as (0 - 1) * ... or unary negation function "neg"
                        tokens.add(CalculatorToken.FunctionToken("neg"))
                    } else {
                        tokens.add(CalculatorToken.OperatorToken("-", 1))
                        canBeUnary = true
                    }
                    i++
                }

                c == '*' -> {
                    tokens.add(CalculatorToken.OperatorToken("*", 2))
                    canBeUnary = true
                    i++
                }

                c == '/' -> {
                    tokens.add(CalculatorToken.OperatorToken("/", 2))
                    canBeUnary = true
                    i++
                }

                c == '^' -> {
                    tokens.add(CalculatorToken.OperatorToken("^", 3, isRightAssociative = true))
                    canBeUnary = true
                    i++
                }

                c.isLetter() -> {
                    // Could be function or constant
                    val sb = StringBuilder()
                    while (i < input.length && input[i].isLetter()) {
                        sb.append(input[i])
                        i++
                    }
                    val word = sb.toString()

                    if (word.equals("PI", ignoreCase = true)) {
                        if (tokens.isNotEmpty()) {
                            val prev = tokens.last()
                            if (prev is CalculatorToken.NumberToken || prev is CalculatorToken.CloseParen) {
                                tokens.add(CalculatorToken.OperatorToken("*", 2))
                            }
                        }
                        tokens.add(CalculatorToken.NumberToken(Math.PI))
                        canBeUnary = false
                    } else if (word == "e" || word == "E") {
                        if (tokens.isNotEmpty()) {
                            val prev = tokens.last()
                            if (prev is CalculatorToken.NumberToken || prev is CalculatorToken.CloseParen) {
                                tokens.add(CalculatorToken.OperatorToken("*", 2))
                            }
                        }
                        tokens.add(CalculatorToken.NumberToken(Math.E))
                        canBeUnary = false
                    } else {
                        // Functions: sin, cos, tan, asin, acos, atan, ln, log, sqrt, inv
                        if (tokens.isNotEmpty()) {
                            val prev = tokens.last()
                            if (prev is CalculatorToken.NumberToken || prev is CalculatorToken.CloseParen) {
                                tokens.add(CalculatorToken.OperatorToken("*", 2))
                            }
                        }
                        tokens.add(CalculatorToken.FunctionToken(word.lowercase()))
                        canBeUnary = true
                    }
                    continue
                }

                else -> {
                    // Ignore unrecognized characters or throw
                    i++
                }
            }
        }

        return tokens
    }

    private fun toRpn(tokens: List<CalculatorToken>): List<CalculatorToken> {
        val output = mutableListOf<CalculatorToken>()
        val stack = ArrayDeque<CalculatorToken>()

        for (token in tokens) {
            when (token) {
                is CalculatorToken.NumberToken -> output.add(token)

                is CalculatorToken.FunctionToken -> stack.addLast(token)

                is CalculatorToken.PostfixFactorial,
                is CalculatorToken.PostfixPercent,
                is CalculatorToken.PostfixSquare,
                is CalculatorToken.PostfixCube -> output.add(token)

                is CalculatorToken.OperatorToken -> {
                    while (stack.isNotEmpty()) {
                        val top = stack.last()
                        if (top is CalculatorToken.OperatorToken) {
                            if ((!token.isRightAssociative && token.precedence <= top.precedence) ||
                                (token.isRightAssociative && token.precedence < top.precedence)
                            ) {
                                output.add(stack.removeLast())
                                continue
                            }
                        } else if (top is CalculatorToken.FunctionToken) {
                            output.add(stack.removeLast())
                            continue
                        }
                        break
                    }
                    stack.addLast(token)
                }

                is CalculatorToken.OpenParen -> stack.addLast(token)

                is CalculatorToken.CloseParen -> {
                    var foundOpen = false
                    while (stack.isNotEmpty()) {
                        val top = stack.removeLast()
                        if (top is CalculatorToken.OpenParen) {
                            foundOpen = true
                            break
                        }
                        output.add(top)
                    }
                    if (!foundOpen) {
                        // Unmatched paren, handle gracefully
                    }
                    if (stack.isNotEmpty() && stack.last() is CalculatorToken.FunctionToken) {
                        output.add(stack.removeLast())
                    }
                }
            }
        }

        while (stack.isNotEmpty()) {
            val top = stack.removeLast()
            if (top !is CalculatorToken.OpenParen && top !is CalculatorToken.CloseParen) {
                output.add(top)
            }
        }

        return output
    }

    private fun evaluateRpn(rpn: List<CalculatorToken>, angleMode: AngleMode): Double {
        val stack = ArrayDeque<Double>()

        for (token in rpn) {
            when (token) {
                is CalculatorToken.NumberToken -> stack.addLast(token.value)

                is CalculatorToken.PostfixFactorial -> {
                    if (stack.isEmpty()) throw IllegalArgumentException("Missing operand for !")
                    val v = stack.removeLast()
                    stack.addLast(factorial(v))
                }

                is CalculatorToken.PostfixPercent -> {
                    if (stack.isEmpty()) throw IllegalArgumentException("Missing operand for %")
                    val v = stack.removeLast()
                    stack.addLast(v / 100.0)
                }

                is CalculatorToken.PostfixSquare -> {
                    if (stack.isEmpty()) throw IllegalArgumentException("Missing operand")
                    val v = stack.removeLast()
                    stack.addLast(v * v)
                }

                is CalculatorToken.PostfixCube -> {
                    if (stack.isEmpty()) throw IllegalArgumentException("Missing operand")
                    val v = stack.removeLast()
                    stack.addLast(v * v * v)
                }

                is CalculatorToken.FunctionToken -> {
                    if (stack.isEmpty()) throw IllegalArgumentException("Missing parameter for ${token.name}")
                    val arg = stack.removeLast()
                    val res = applyFunction(token.name, arg, angleMode)
                    stack.addLast(res)
                }

                is CalculatorToken.OperatorToken -> {
                    if (stack.size < 2) throw IllegalArgumentException("Missing operand for ${token.symbol}")
                    val b = stack.removeLast()
                    val a = stack.removeLast()
                    val res = when (token.symbol) {
                        "+" -> a + b
                        "-" -> a - b
                        "*" -> a * b
                        "/" -> {
                            if (abs(b) < 1e-15) throw ArithmeticException("Cannot divide by zero")
                            a / b
                        }
                        "^" -> a.pow(b)
                        else -> throw IllegalArgumentException("Unknown operator ${token.symbol}")
                    }
                    stack.addLast(res)
                }

                else -> {}
            }
        }

        if (stack.isEmpty()) return 0.0
        return stack.last()
    }

    private fun applyFunction(name: String, x: Double, angleMode: AngleMode): Double {
        return when (name) {
            "neg" -> -x
            "sin" -> {
                val rad = if (angleMode == AngleMode.DEG) Math.toRadians(x) else x
                // Normalize near-zero values like sin(180°)
                val s = sin(rad)
                if (abs(s) < 1e-15) 0.0 else s
            }
            "cos" -> {
                val rad = if (angleMode == AngleMode.DEG) Math.toRadians(x) else x
                val c = cos(rad)
                if (abs(c) < 1e-15) 0.0 else c
            }
            "tan" -> {
                val rad = if (angleMode == AngleMode.DEG) Math.toRadians(x) else x
                val c = cos(rad)
                if (abs(c) < 1e-15) throw ArithmeticException("Tangent is undefined")
                val t = tan(rad)
                if (abs(t) < 1e-15) 0.0 else t
            }
            "asin" -> {
                if (x < -1.0 || x > 1.0) throw ArithmeticException("Domain error: asin in [-1, 1]")
                val r = asin(x)
                if (angleMode == AngleMode.DEG) Math.toDegrees(r) else r
            }
            "acos" -> {
                if (x < -1.0 || x > 1.0) throw ArithmeticException("Domain error: acos in [-1, 1]")
                val r = acos(x)
                if (angleMode == AngleMode.DEG) Math.toDegrees(r) else r
            }
            "atan" -> {
                val r = atan(x)
                if (angleMode == AngleMode.DEG) Math.toDegrees(r) else r
            }
            "ln" -> {
                if (x <= 0.0) throw ArithmeticException("Domain error: ln(x) for x > 0")
                ln(x)
            }
            "log", "log10" -> {
                if (x <= 0.0) throw ArithmeticException("Domain error: log(x) for x > 0")
                log10(x)
            }
            "sqrt" -> {
                if (x < 0.0) throw ArithmeticException("Domain error: √ of negative number")
                sqrt(x)
            }
            "inv" -> {
                if (abs(x) < 1e-15) throw ArithmeticException("Cannot divide by zero")
                1.0 / x
            }
            else -> throw IllegalArgumentException("Unknown function: $name")
        }
    }

    private fun factorial(n: Double): Double {
        if (n < 0 || n != floor(n)) throw ArithmeticException("Factorial only for non-negative integers")
        val intN = n.toInt()
        if (intN > 170) throw ArithmeticException("Overflow: value too large for !")
        var res = 1.0
        for (i in 2..intN) {
            res *= i
        }
        return res
    }

    fun formatResult(value: Double): String {
        if (value.isNaN()) return "Error"
        if (value.isInfinite()) return "Cannot divide by zero"

        // Handle clean integer display
        if (abs(value - round(value)) < 1e-11) {
            val longVal = round(value).toLong()
            if (abs(value) < 1e15) {
                return longVal.toString()
            }
        }

        // Clean precision: eliminate floating point artifacts like 0.30000000000000004
        val absVal = abs(value)
        if (absVal >= 1e12 || (absVal < 1e-6 && absVal > 0.0)) {
            // Scientific notation
            val symbols = DecimalFormatSymbols(Locale.US)
            val df = DecimalFormat("0.######E0", symbols)
            return df.format(value).replace("E", " × 10^")
        }

        // Round to 10 decimal digits using BigDecimal
        return try {
            val bd = BigDecimal(value, MathContext(10, RoundingMode.HALF_UP))
            val stripped = bd.stripTrailingZeros()
            stripped.toPlainString()
        } catch (e: Exception) {
            val df = DecimalFormat("#.##########", DecimalFormatSymbols(Locale.US))
            df.format(value)
        }
    }
}
