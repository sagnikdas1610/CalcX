package com.example.matrix

import com.example.calculator.CalculatorEngine
import kotlin.math.abs

object MatrixEngine {

    fun determinant2x2(m: List<List<Double>>): Double {
        return m[0][0] * m[1][1] - m[0][1] * m[1][0]
    }

    fun determinant3x3(m: List<List<Double>>): Double {
        val a = m[0][0] * (m[1][1] * m[2][2] - m[1][2] * m[2][1])
        val b = m[0][1] * (m[1][0] * m[2][2] - m[1][2] * m[2][0])
        val c = m[0][2] * (m[1][0] * m[2][1] - m[1][1] * m[2][0])
        return a - b + c
    }

    fun transpose(m: List<List<Double>>): List<List<Double>> {
        val rows = m.size
        val cols = m[0].size
        return List(cols) { col ->
            List(rows) { row -> m[row][col] }
        }
    }

    fun inverse2x2(m: List<List<Double>>): List<List<Double>>? {
        val det = determinant2x2(m)
        if (abs(det) < 1e-12) return null
        return listOf(
            listOf(m[1][1] / det, -m[0][1] / det),
            listOf(-m[1][0] / det, m[0][0] / det)
        )
    }

    fun inverse3x3(m: List<List<Double>>): List<List<Double>>? {
        val det = determinant3x3(m)
        if (abs(det) < 1e-12) return null

        // Adjugate matrix / det
        val adj = List(3) { r ->
            List(3) { c ->
                val minor = minor2x2(m, c, r) // transposed row and col for adjugate
                val sign = if ((r + c) % 2 == 0) 1.0 else -1.0
                (sign * determinant2x2(minor)) / det
            }
        }
        return adj
    }

    private fun minor2x2(m: List<List<Double>>, skipRow: Int, skipCol: Int): List<List<Double>> {
        val sub = mutableListOf<List<Double>>()
        for (r in 0 until 3) {
            if (r == skipRow) continue
            val row = mutableListOf<Double>()
            for (c in 0 until 3) {
                if (c == skipCol) continue
                row.add(m[r][c])
            }
            sub.add(row)
        }
        return sub
    }
}
