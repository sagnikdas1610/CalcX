package com.example.equation

import com.example.calculator.CalculatorEngine
import kotlin.math.abs
import kotlin.math.sqrt

sealed class LinearSolution {
    data class Single(val x: Double, val formattedX: String, val steps: List<String>) : LinearSolution()
    data class Infinite(val message: String, val steps: List<String>) : LinearSolution()
    data class NoSolution(val message: String, val steps: List<String>) : LinearSolution()
    data class Error(val message: String) : LinearSolution()
}

sealed class QuadraticSolution {
    data class TwoRealRoots(
        val x1: Double,
        val x2: Double,
        val formattedX1: String,
        val formattedX2: String,
        val discriminant: Double,
        val steps: List<String>
    ) : QuadraticSolution()

    data class OneRealRoot(
        val x: Double,
        val formattedX: String,
        val discriminant: Double,
        val steps: List<String>
    ) : QuadraticSolution()

    data class ComplexRoots(
        val realPart: Double,
        val imagPart: Double,
        val formattedX1: String,
        val formattedX2: String,
        val discriminant: Double,
        val steps: List<String>
    ) : QuadraticSolution()

    data class DegenerateLinear(val linear: LinearSolution) : QuadraticSolution()
    data class Error(val message: String) : QuadraticSolution()
}

object EquationSolver {

    fun solveLinear(a: Double, b: Double, c: Double = 0.0): LinearSolution {
        val steps = mutableListOf<String>()
        steps.add("Equation: ${formatCoeff(a)}x + ${formatNum(b)} = ${formatNum(c)}")

        if (abs(a) < 1e-12) {
            return if (abs(b - c) < 1e-12) {
                steps.add("0 = 0 is always true.")
                LinearSolution.Infinite("Infinite solutions (identity equation)", steps)
            } else {
                steps.add("${formatNum(b)} = ${formatNum(c)} is impossible.")
                LinearSolution.NoSolution("No solution (contradiction)", steps)
            }
        }

        val rhs = c - b
        steps.add("Subtract ${formatNum(b)} from both sides: ${formatCoeff(a)}x = ${formatNum(rhs)}")
        val x = rhs / a
        steps.add("Divide both sides by ${formatNum(a)}: x = ${formatNum(rhs)} / ${formatNum(a)}")
        val formatted = CalculatorEngine.formatResult(x)
        steps.add("Result: x = $formatted")

        return LinearSolution.Single(x, formatted, steps)
    }

    fun solveQuadratic(a: Double, b: Double, c: Double): QuadraticSolution {
        val steps = mutableListOf<String>()
        steps.add("Equation: ${formatCoeff(a)}x² + ${formatCoeff(b)}x + ${formatNum(c)} = 0")

        if (abs(a) < 1e-12) {
            steps.add("Coefficient a = 0; equation is linear: ${formatCoeff(b)}x + ${formatNum(c)} = 0")
            return QuadraticSolution.DegenerateLinear(solveLinear(b, c, 0.0))
        }

        val disc = b * b - 4.0 * a * c
        val formattedDisc = CalculatorEngine.formatResult(disc)
        steps.add("Discriminant Δ = b² - 4ac = (${formatNum(b)})² - 4(${formatNum(a)})(${formatNum(c)}) = $formattedDisc")

        return when {
            disc > 1e-12 -> {
                val sqrtDisc = sqrt(disc)
                val x1 = (-b + sqrtDisc) / (2.0 * a)
                val x2 = (-b - sqrtDisc) / (2.0 * a)
                val f1 = CalculatorEngine.formatResult(x1)
                val f2 = CalculatorEngine.formatResult(x2)
                steps.add("Δ > 0 → Two distinct real roots exist.")
                steps.add("x₁ = (-b + √Δ) / 2a = (${formatNum(-b)} + ${CalculatorEngine.formatResult(sqrtDisc)}) / ${formatNum(2 * a)} = $f1")
                steps.add("x₂ = (-b - √Δ) / 2a = (${formatNum(-b)} - ${CalculatorEngine.formatResult(sqrtDisc)}) / ${formatNum(2 * a)} = $f2")
                QuadraticSolution.TwoRealRoots(x1, x2, f1, f2, disc, steps)
            }
            abs(disc) <= 1e-12 -> {
                val x = -b / (2.0 * a)
                val f = CalculatorEngine.formatResult(x)
                steps.add("Δ = 0 → Exactly one repeated real root.")
                steps.add("x = -b / 2a = ${formatNum(-b)} / ${formatNum(2 * a)} = $f")
                QuadraticSolution.OneRealRoot(x, f, disc, steps)
            }
            else -> {
                val real = -b / (2.0 * a)
                val imag = sqrt(-disc) / (2.0 * a)
                val fReal = CalculatorEngine.formatResult(real)
                val fImag = CalculatorEngine.formatResult(abs(imag))
                val f1 = "$fReal + ${fImag}i"
                val f2 = "$fReal - ${fImag}i"
                steps.add("Δ < 0 → Two complex conjugate roots exist.")
                steps.add("x = (-b ± i√|Δ|) / 2a")
                steps.add("x₁ = $f1")
                steps.add("x₂ = $f2")
                QuadraticSolution.ComplexRoots(real, abs(imag), f1, f2, disc, steps)
            }
        }
    }

    private fun formatCoeff(v: Double): String = CalculatorEngine.formatResult(v)
    private fun formatNum(v: Double): String = CalculatorEngine.formatResult(v)
}
