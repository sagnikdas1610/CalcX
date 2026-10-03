package com.example.statistics

import com.example.calculator.CalculatorEngine
import kotlin.math.sqrt

data class StatisticsResult(
    val count: Int,
    val sum: Double,
    val mean: Double,
    val median: Double,
    val mode: List<Double>,
    val min: Double,
    val max: Double,
    val range: Double,
    val variance: Double,
    val stdDev: Double
)

object StatisticsEngine {

    fun calculate(numbers: List<Double>): StatisticsResult? {
        if (numbers.isEmpty()) return null

        val sorted = numbers.sorted()
        val count = sorted.size
        val sum = sorted.sum()
        val mean = sum / count
        val min = sorted.first()
        val max = sorted.last()
        val range = max - min

        val median = if (count % 2 == 1) {
            sorted[count / 2]
        } else {
            (sorted[count / 2 - 1] + sorted[count / 2]) / 2.0
        }

        // Mode calculation
        val frequencies = numbers.groupingBy { it }.eachCount()
        val maxFreq = frequencies.values.maxOrNull() ?: 0
        val mode = if (maxFreq > 1) {
            frequencies.filter { it.value == maxFreq }.keys.toList()
        } else {
            emptyList()
        }

        // Sample Variance & Std Dev (or Population Variance if count == 1)
        val variance = if (count > 1) {
            sorted.sumOf { (it - mean) * (it - mean) } / (count - 1)
        } else {
            0.0
        }
        val stdDev = sqrt(variance)

        return StatisticsResult(
            count = count,
            sum = sum,
            mean = mean,
            median = median,
            mode = mode,
            min = min,
            max = max,
            range = range,
            variance = variance,
            stdDev = stdDev
        )
    }

    fun parseInput(input: String): List<Double> {
        return input.split(",", " ", "\n", "\t")
            .mapNotNull { it.trim().toDoubleOrNull() }
    }
}
