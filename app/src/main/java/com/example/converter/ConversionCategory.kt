package com.example.converter

data class UnitDefinition(
    val name: String,
    val symbol: String,
    val factorToBase: Double // multiplier to convert to base unit
)

enum class UnitCategory(val displayName: String) {
    LENGTH("Length"),
    MASS("Mass"),
    TEMPERATURE("Temperature"),
    AREA("Area"),
    VOLUME("Volume"),
    SPEED("Speed"),
    TIME("Time"),
    DATA("Data Storage"),
    PRESSURE("Pressure"),
    ENERGY("Energy"),
    POWER("Power"),
    ANGLE("Angle")
}

object UnitDatabase {

    val categories: Map<UnitCategory, List<UnitDefinition>> = mapOf(
        UnitCategory.LENGTH to listOf(
            UnitDefinition("Kilometer", "km", 1000.0),
            UnitDefinition("Meter", "m", 1.0),
            UnitDefinition("Centimeter", "cm", 0.01),
            UnitDefinition("Millimeter", "mm", 0.001),
            UnitDefinition("Mile", "mi", 1609.344),
            UnitDefinition("Yard", "yd", 0.9144),
            UnitDefinition("Foot", "ft", 0.3048),
            UnitDefinition("Inch", "in", 0.0254),
            UnitDefinition("Nautical Mile", "nmi", 1852.0)
        ),
        UnitCategory.MASS to listOf(
            UnitDefinition("Kilogram", "kg", 1.0),
            UnitDefinition("Gram", "g", 0.001),
            UnitDefinition("Milligram", "mg", 0.000001),
            UnitDefinition("Metric Ton", "t", 1000.0),
            UnitDefinition("Pound", "lb", 0.45359237),
            UnitDefinition("Ounce", "oz", 0.028349523125),
            UnitDefinition("Stone", "st", 6.35029318)
        ),
        UnitCategory.TEMPERATURE to listOf(
            UnitDefinition("Celsius", "°C", 1.0),
            UnitDefinition("Fahrenheit", "°F", 1.0),
            UnitDefinition("Kelvin", "K", 1.0)
        ),
        UnitCategory.AREA to listOf(
            UnitDefinition("Square Meter", "m²", 1.0),
            UnitDefinition("Square Kilometer", "km²", 1_000_000.0),
            UnitDefinition("Hectare", "ha", 10_000.0),
            UnitDefinition("Acre", "ac", 4046.8564224),
            UnitDefinition("Square Foot", "ft²", 0.09290304),
            UnitDefinition("Square Mile", "mi²", 2589988.110336)
        ),
        UnitCategory.VOLUME to listOf(
            UnitDefinition("Liter", "L", 1.0),
            UnitDefinition("Milliliter", "mL", 0.001),
            UnitDefinition("Cubic Meter", "m³", 1000.0),
            UnitDefinition("Gallon (US)", "gal", 3.785411784),
            UnitDefinition("Quart (US)", "qt", 0.946352946),
            UnitDefinition("Pint (US)", "pt", 0.473176473),
            UnitDefinition("Cup (US)", "cup", 0.2365882365),
            UnitDefinition("Fluid Ounce", "fl oz", 0.0295735295625)
        ),
        UnitCategory.SPEED to listOf(
            UnitDefinition("Kilometer/hour", "km/h", 0.277777778),
            UnitDefinition("Meter/second", "m/s", 1.0),
            UnitDefinition("Mile/hour", "mph", 0.44704),
            UnitDefinition("Knot", "kn", 0.514444444),
            UnitDefinition("Foot/second", "ft/s", 0.3048)
        ),
        UnitCategory.TIME to listOf(
            UnitDefinition("Second", "s", 1.0),
            UnitDefinition("Minute", "min", 60.0),
            UnitDefinition("Hour", "h", 3600.0),
            UnitDefinition("Day", "d", 86400.0),
            UnitDefinition("Week", "wk", 604800.0),
            UnitDefinition("Year (365d)", "yr", 31536000.0)
        ),
        UnitCategory.DATA to listOf(
            UnitDefinition("Byte", "B", 1.0),
            UnitDefinition("Kilobyte", "KB", 1024.0),
            UnitDefinition("Megabyte", "MB", 1024.0 * 1024.0),
            UnitDefinition("Gigabyte", "GB", 1024.0 * 1024.0 * 1024.0),
            UnitDefinition("Terabyte", "TB", 1024.0 * 1024.0 * 1024.0 * 1024.0)
        ),
        UnitCategory.PRESSURE to listOf(
            UnitDefinition("Pascal", "Pa", 1.0),
            UnitDefinition("Bar", "bar", 100000.0),
            UnitDefinition("PSI", "psi", 6894.75729),
            UnitDefinition("Atmosphere", "atm", 101325.0)
        ),
        UnitCategory.ENERGY to listOf(
            UnitDefinition("Joule", "J", 1.0),
            UnitDefinition("Kilojoule", "kJ", 1000.0),
            UnitDefinition("Calorie", "cal", 4.184),
            UnitDefinition("Kilocalorie", "kcal", 4184.0),
            UnitDefinition("Kilowatt-hour", "kWh", 3600000.0)
        ),
        UnitCategory.POWER to listOf(
            UnitDefinition("Watt", "W", 1.0),
            UnitDefinition("Kilowatt", "kW", 1000.0),
            UnitDefinition("Horsepower", "hp", 745.699872),
            UnitDefinition("Megawatt", "MW", 1000000.0)
        ),
        UnitCategory.ANGLE to listOf(
            UnitDefinition("Degree", "°", 1.0),
            UnitDefinition("Radian", "rad", 180.0 / Math.PI),
            UnitDefinition("Gradian", "grad", 0.9)
        )
    )
}
