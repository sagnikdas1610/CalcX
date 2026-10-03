package com.example.converter

import com.example.calculator.CalculatorEngine
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

object UnitConverterEngine {

    fun convert(
        value: Double,
        from: UnitDefinition,
        to: UnitDefinition,
        category: UnitCategory
    ): Double {
        if (from.symbol == to.symbol) return value

        if (category == UnitCategory.TEMPERATURE) {
            return convertTemperature(value, from.symbol, to.symbol)
        }

        // Standard proportional conversion
        val baseValue = value * from.factorToBase
        return baseValue / to.factorToBase
    }

    private fun convertTemperature(value: Double, fromSymbol: String, toSymbol: String): Double {
        // Convert to Celsius first
        val celsius = when (fromSymbol) {
            "°C" -> value
            "°F" -> (value - 32.0) * 5.0 / 9.0
            "K" -> value - 273.15
            else -> value
        }

        // Convert Celsius to Target
        return when (toSymbol) {
            "°C" -> celsius
            "°F" -> (celsius * 9.0 / 5.0) + 32.0
            "K" -> celsius + 273.15
            else -> celsius
        }
    }

    fun format(value: Double): String {
        return CalculatorEngine.formatResult(value)
    }
}
