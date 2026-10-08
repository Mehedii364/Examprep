package com.example.engine

object YearExtractor {
    private val YEAR_REGEX = Regex("(?:২০|20)[0-3][0-9]|19[8-9][0-9]")

    fun extractYears(text: String): List<Int> {
        val converted = BengaliNormalizer.toEnglishDigits(text)
        val matches = YEAR_REGEX.findAll(converted)
        val years = matches.mapNotNull { it.value.toIntOrNull() }
            .filter { it in 1990..2030 }
            .distinct()
            .sorted()
            .toList()
        return years
    }

    fun formatYearDisplay(years: List<Int>): String {
        if (years.isEmpty()) {
            return "বছর স্পষ্ট নয় (Year unclear)"
        }
        val bnYears = years.map { BengaliNormalizer.toBengaliDigits(it.toString()) }
        return "বিগত বছর: " + bnYears.joinToString(", ")
    }
}
