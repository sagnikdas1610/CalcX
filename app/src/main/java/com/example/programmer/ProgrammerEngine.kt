package com.example.programmer

enum class NumberBase(val radix: Int, val prefix: String) {
    HEX(16, "0x"),
    DEC(10, ""),
    OCT(8, "0o"),
    BIN(2, "0b")
}

data class ProgrammerValues(
    val dec: String,
    val hex: String,
    val oct: String,
    val bin: String
)

object ProgrammerEngine {

    fun parseInput(input: String, base: NumberBase): Long? {
        val clean = input.trim().removePrefix("0x").removePrefix("0o").removePrefix("0b")
        if (clean.isEmpty()) return 0L
        return try {
            java.lang.Long.parseUnsignedLong(clean, base.radix)
        } catch (e: Exception) {
            null
        }
    }

    fun getValues(value: Long): ProgrammerValues {
        return ProgrammerValues(
            dec = java.lang.Long.toUnsignedString(value, 10),
            hex = java.lang.Long.toHexString(value).uppercase(),
            oct = java.lang.Long.toOctalString(value),
            bin = java.lang.Long.toBinaryString(value)
        )
    }

    fun bitwiseAnd(a: Long, b: Long): Long = a and b
    fun bitwiseOr(a: Long, b: Long): Long = a or b
    fun bitwiseXor(a: Long, b: Long): Long = a xor b
    fun bitwiseNot(a: Long): Long = a.inv()
    fun shiftLeft(a: Long, bits: Int): Long = a shl bits
    fun shiftRight(a: Long, bits: Int): Long = a ushr bits
}
