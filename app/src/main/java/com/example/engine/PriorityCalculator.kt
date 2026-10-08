package com.example.engine

import com.example.data.model.PriorityLevel
import com.example.data.model.QuestionSection
import com.example.data.model.RepeatType

object PriorityCalculator {
    data class PriorityAssessment(
        val level: PriorityLevel,
        val score: Int,
        val explanation: String
    )

    fun calculatePriority(
        years: List<Int>,
        repeatCount: Int,
        repeatType: RepeatType,
        section: QuestionSection,
        hasOfficialHighlight: Boolean = false
    ): PriorityAssessment {
        var score = 50 // Base score

        val uniqueYearCount = years.distinct().size
        val latestYear = years.maxOrNull() ?: 0

        // Year frequency weighting
        score += when {
            uniqueYearCount >= 5 -> 30
            uniqueYearCount in 3..4 -> 22
            uniqueYearCount == 2 -> 14
            uniqueYearCount == 1 -> 8
            else -> 0
        }

        // Recency weighting (examined in 2022, 2023, 2024 or projected 2026 alternate rotation)
        if (latestYear in 2022..2024) {
            score += 10
        }

        // Repetition type weighting
        score += when (repeatType) {
            RepeatType.EXACT -> 10
            RepeatType.REWORDED -> 8
            RepeatType.CONCEPTUAL -> 5
            RepeatType.UNIQUE -> 0
        }

        // Section importance
        score += when (section) {
            QuestionSection.GA -> 8  // 10 marks essay
            QuestionSection.KHA -> 6 // 4 marks
            QuestionSection.KA -> 5  // 1 mark
            QuestionSection.MCQ -> 4
        }

        if (hasOfficialHighlight) {
            score += 5
        }

        val clampedScore = score.coerceIn(40, 99)

        val level = when {
            clampedScore >= 88 -> PriorityLevel.VERY_HIGH
            clampedScore >= 75 -> PriorityLevel.HIGH
            clampedScore >= 60 -> PriorityLevel.IMPORTANT
            else -> PriorityLevel.REVISION
        }

        val bnYears = if (years.isNotEmpty()) {
            years.sorted().joinToString(", ") { BengaliNormalizer.toBengaliDigits(it.toString()) }
        } else {
            "পূর্ববর্তী বোর্ড প্রশ্নাবলী"
        }

        val explanation = buildString {
            append("বিশ্লেষণমূলক অগ্রাধিকার স্কোর: ${clampedScore} / ১০০। ")
            append("এই প্রশ্নটি বিগত ${BengaliNormalizer.toBengaliDigits(uniqueYearCount.toString())} টি ভিন্ন শিক্ষাবর্ষে (${bnYears}) উপস্থাপিত হয়েছে। ")
            when (repeatType) {
                RepeatType.EXACT -> append("প্রশ্নের ভাষা একাধিক বছর অপরিবর্তিত ছিল। ")
                RepeatType.REWORDED -> append("একই মূল প্রশ্ন সামান্য শব্দগত পরিবর্তনে পুনরায় এসেছে। ")
                RepeatType.CONCEPTUAL -> append("ধারণাগতভাবে এটি পাঠ্যক্রমের একটি মূল ভিত্তি। ")
                else -> {}
            }
            append("(বিজ্ঞপ্তি: এটি পরিসংখ্যানগত উপাত্তের ভিত্তিতে নির্ধারিত অগ্রাধিকার স্কোর, কোনো সুনির্দিষ্ট পরীক্ষা নিশ্চয়তা নয়)।")
        }

        return PriorityAssessment(
            level = level,
            score = clampedScore,
            explanation = explanation
        )
    }
}
