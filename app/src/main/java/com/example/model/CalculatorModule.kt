package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.CalcXBlue
import com.example.ui.theme.CalcXCyan
import com.example.ui.theme.CalcXGreen
import com.example.ui.theme.CalcXOrange
import com.example.ui.theme.CalcXPink
import com.example.ui.theme.CalcXPurple

data class CalculatorModule(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val route: String,
    val iconName: String,
    val accentColor: Color = CalcXPurple,
    val isReady: Boolean = true
)

object ModuleRegistry {
    val modules = listOf(
        CalculatorModule(
            id = "calc_basic",
            title = "Calculator",
            subtitle = "Basic & Scientific",
            description = "High-precision calculations, trigonometry, logs, and powers with DEG/RAD modes.",
            route = "calculator",
            iconName = "calculate",
            accentColor = CalcXPurple,
            isReady = true
        ),
        CalculatorModule(
            id = "unit_converter",
            title = "Unit Converter",
            subtitle = "Length, Mass, Temp...",
            description = "Convert 12 categories of scientific, metric, and imperial units with instant results.",
            route = "unit_converter",
            iconName = "swap_horiz",
            accentColor = CalcXBlue,
            isReady = true
        ),
        CalculatorModule(
            id = "equation_solver",
            title = "Equation Solver",
            subtitle = "Linear & Quadratic",
            description = "Solve linear and quadratic equations with step-by-step discriminant analysis.",
            route = "equation_solver",
            iconName = "functions",
            accentColor = CalcXPink,
            isReady = true
        ),
        CalculatorModule(
            id = "matrix_calc",
            title = "Matrix Calculator",
            subtitle = "2×2 & 3×3 Matrices",
            description = "Compute determinants, matrix inverses, transposes, and rank with editable cells.",
            route = "matrix_calc",
            iconName = "grid_view",
            accentColor = CalcXCyan,
            isReady = true
        ),
        CalculatorModule(
            id = "finance_calc",
            title = "Finance Calculator",
            subtitle = "Loan EMI & Interest",
            description = "Accurate monthly EMI calculator, total interest breakdown, and loan schedules.",
            route = "finance_calc",
            iconName = "account_balance",
            accentColor = CalcXGreen,
            isReady = true
        ),
        CalculatorModule(
            id = "programmer_calc",
            title = "Programmer Calculator",
            subtitle = "HEX, DEC, OCT, BIN",
            description = "Multi-base conversions and bitwise logic operations (AND, OR, XOR, NOT, shifts).",
            route = "programmer_calc",
            iconName = "code",
            accentColor = CalcXOrange,
            isReady = true
        ),
        CalculatorModule(
            id = "graph_calc",
            title = "Graph Calculator",
            subtitle = "Function Plotter",
            description = "Interactive 2D plotting of mathematical functions like sin(x), cos(x), x² on Canvas.",
            route = "graph_calc",
            iconName = "show_chart",
            accentColor = CalcXPurple,
            isReady = true
        ),
        CalculatorModule(
            id = "statistics_calc",
            title = "Statistics Calculator",
            subtitle = "Mean, Median, StdDev",
            description = "Statistical distribution analysis, variance, range, min/max, and dataset summaries.",
            route = "statistics_calc",
            iconName = "analytics",
            accentColor = CalcXBlue,
            isReady = true
        ),
        CalculatorModule(
            id = "geometry_calc",
            title = "Geometry Calculator",
            subtitle = "Area, Perimeter & Volume",
            description = "Geometric formulas for 2D shapes and 3D solids (spheres, cylinders, cones).",
            route = "geometry_calc",
            iconName = "square_foot",
            accentColor = CalcXPink,
            isReady = true
        ),
        CalculatorModule(
            id = "physics_calc",
            title = "Physics & Engineering",
            subtitle = "Mechanics & Ohm's Law",
            description = "Formulas for Ohm's Law (V=IR), Power (P=VI), kinetic energy, and velocity.",
            route = "physics_calc",
            iconName = "bolt",
            accentColor = CalcXOrange,
            isReady = true
        ),
        CalculatorModule(
            id = "date_calc",
            title = "Date Calculator",
            subtitle = "Difference & Age",
            description = "Calculate days between dates, age in years/months/days, and future milestones.",
            route = "date_calc",
            iconName = "calendar_today",
            accentColor = CalcXGreen,
            isReady = true
        )
    )
}
