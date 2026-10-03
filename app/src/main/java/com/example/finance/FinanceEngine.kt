package com.example.finance

import com.example.calculator.CalculatorEngine
import kotlin.math.pow

data class EmiResult(
    val monthlyEmi: Double,
    val totalInterest: Double,
    val totalPayment: Double,
    val formattedEmi: String,
    val formattedInterest: String,
    val formattedTotal: String
)

object FinanceEngine {

    fun calculateEmi(principal: Double, annualRatePercent: Double, years: Double): EmiResult {
        if (principal <= 0 || years <= 0) {
            return EmiResult(0.0, 0.0, 0.0, "0", "0", "0")
        }

        val months = (years * 12.0)
        val monthlyRate = (annualRatePercent / 12.0) / 100.0

        val emi = if (monthlyRate == 0.0) {
            principal / months
        } else {
            val factor = (1.0 + monthlyRate).pow(months)
            (principal * monthlyRate * factor) / (factor - 1.0)
        }

        val totalPayment = emi * months
        val totalInterest = totalPayment - principal

        return EmiResult(
            monthlyEmi = emi,
            totalInterest = totalInterest,
            totalPayment = totalPayment,
            formattedEmi = formatCurrency(emi),
            formattedInterest = formatCurrency(totalInterest),
            formattedTotal = formatCurrency(totalPayment)
        )
    }

    private fun formatCurrency(amount: Double): String {
        return CalculatorEngine.formatResult(amount)
    }
}
