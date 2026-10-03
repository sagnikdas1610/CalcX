package com.example

import com.example.calculator.AngleMode
import com.example.calculator.CalculatorEngine
import com.example.converter.UnitCategory
import com.example.converter.UnitConverterEngine
import com.example.converter.UnitDatabase
import com.example.equation.EquationSolver
import com.example.equation.LinearSolution
import com.example.equation.QuadraticSolution
import com.example.finance.FinanceEngine
import com.example.matrix.MatrixEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs

class ExampleUnitTest {

    @Test
    fun testBasicCalculations() {
        // 2 + 2 = 4
        val res1 = CalculatorEngine.evaluate("2 + 2")
        assertTrue(res1 is CalculatorEngine.Result.Success)
        assertEquals("4", (res1 as CalculatorEngine.Result.Success).formatted)

        // 2 + 3 × 4 = 14
        val res2 = CalculatorEngine.evaluate("2 + 3 × 4")
        assertTrue(res2 is CalculatorEngine.Result.Success)
        assertEquals("14", (res2 as CalculatorEngine.Result.Success).formatted)

        // (2 + 3) × 4 = 20
        val res3 = CalculatorEngine.evaluate("(2 + 3) × 4")
        assertTrue(res3 is CalculatorEngine.Result.Success)
        assertEquals("20", (res3 as CalculatorEngine.Result.Success).formatted)

        // 10 ÷ 4 = 2.5
        val res4 = CalculatorEngine.evaluate("10 ÷ 4")
        assertTrue(res4 is CalculatorEngine.Result.Success)
        assertEquals("2.5", (res4 as CalculatorEngine.Result.Success).formatted)

        // 5 × -2 = -10
        val res5 = CalculatorEngine.evaluate("5 × -2")
        assertTrue(res5 is CalculatorEngine.Result.Success)
        assertEquals("-10", (res5 as CalculatorEngine.Result.Success).formatted)

        // 0.1 + 0.2 should display 0.3 without floating point artifacts
        val res6 = CalculatorEngine.evaluate("0.1 + 0.2")
        assertTrue(res6 is CalculatorEngine.Result.Success)
        assertEquals("0.3", (res6 as CalculatorEngine.Result.Success).formatted)
    }

    @Test
    fun testDivisionByZeroHandledSafely() {
        // 1 ÷ 0 should not crash
        val res = CalculatorEngine.evaluate("1 ÷ 0")
        assertTrue(res is CalculatorEngine.Result.Error)
        assertEquals("Cannot divide by zero", (res as CalculatorEngine.Result.Error).message)
    }

    @Test
    fun testScientificFunctions() {
        // sin(90°) = 1 in DEG mode
        val sinDeg = CalculatorEngine.evaluate("sin(90)", AngleMode.DEG)
        assertTrue(sinDeg is CalculatorEngine.Result.Success)
        assertEquals("1", (sinDeg as CalculatorEngine.Result.Success).formatted)

        // sqrt(144) = 12
        val sqrtVal = CalculatorEngine.evaluate("sqrt(144)")
        assertTrue(sqrtVal is CalculatorEngine.Result.Success)
        assertEquals("12", (sqrtVal as CalculatorEngine.Result.Success).formatted)

        // log(100) = 2
        val logVal = CalculatorEngine.evaluate("log(100)")
        assertTrue(logVal is CalculatorEngine.Result.Success)
        assertEquals("2", (logVal as CalculatorEngine.Result.Success).formatted)

        // 2 × sin(45) + √144
        val combo = CalculatorEngine.evaluate("2 × sin(45) + √144", AngleMode.DEG)
        assertTrue(combo is CalculatorEngine.Result.Success)
        // 2 * (sqrt(2)/2) + 12 = 1.41421356 + 12 = 13.414...
        val comboVal = (combo as CalculatorEngine.Result.Success).value
        assertTrue(abs(comboVal - 13.41421356) < 0.001)
    }

    @Test
    fun testUnitConversion() {
        // 10 km ≈ 6.21371 mi
        val lengthUnits = UnitDatabase.categories[UnitCategory.LENGTH]!!
        val km = lengthUnits.first { it.symbol == "km" }
        val mi = lengthUnits.first { it.symbol == "mi" }

        val converted = UnitConverterEngine.convert(10.0, km, mi, UnitCategory.LENGTH)
        assertTrue(abs(converted - 6.21371) < 0.001)
    }

    @Test
    fun testEquationSolver() {
        // Linear: 2x + 5 = 15 -> x = 5
        val linSol = EquationSolver.solveLinear(2.0, 5.0, 15.0)
        assertTrue(linSol is LinearSolution.Single)
        assertEquals("5", (linSol as LinearSolution.Single).formattedX)

        // Quadratic: x² - 5x + 6 = 0 -> x₁ = 3, x₂ = 2
        val quadSol = EquationSolver.solveQuadratic(1.0, -5.0, 6.0)
        assertTrue(quadSol is QuadraticSolution.TwoRealRoots)
        val realRoots = quadSol as QuadraticSolution.TwoRealRoots
        assertEquals("3", realRoots.formattedX1)
        assertEquals("2", realRoots.formattedX2)
    }

    @Test
    fun testFinanceEmi() {
        // Loan: 500,000, Rate: 8%, Term: 5 years -> EMI ~ 10,138
        val emi = FinanceEngine.calculateEmi(500000.0, 8.0, 5.0)
        assertTrue(abs(emi.monthlyEmi - 10138.2) < 1.0)
    }

    @Test
    fun testMatrixEngine() {
        // 2x2 Matrix: [[1, 2], [3, 4]] -> det = 1*4 - 2*3 = -2
        val m = listOf(
            listOf(1.0, 2.0),
            listOf(3.0, 4.0)
        )
        val det = MatrixEngine.determinant2x2(m)
        assertEquals(-2.0, det, 1e-9)
    }
}
