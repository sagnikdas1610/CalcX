package com.example.navigation

sealed class CalcXDestination(val route: String) {
    object Calculator : CalcXDestination("calculator")
    object History : CalcXDestination("history")
    object Modules : CalcXDestination("modules")
    object UnitConverter : CalcXDestination("unit_converter")
    object EquationSolver : CalcXDestination("equation_solver")
    object MatrixCalculator : CalcXDestination("matrix_calc")
    object FinanceCalculator : CalcXDestination("finance_calc")
    object ProgrammerCalculator : CalcXDestination("programmer_calc")
    object GraphCalculator : CalcXDestination("graph_calc")
    object StatisticsCalculator : CalcXDestination("statistics_calc")
    object Settings : CalcXDestination("settings")
}
