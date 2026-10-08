package com.example.engine

import com.example.data.model.RepeatType

object SimilarityEngine {
    data class MatchResult(
        val isMatch: Boolean,
        val repeatType: RepeatType,
        val score: Float
    )

    fun calculateSimilarity(q1: String, q2: String): MatchResult {
        val norm1 = BengaliNormalizer.normalizeText(q1)
        val norm2 = BengaliNormalizer.normalizeText(q2)

        if (norm1 == norm2) {
            return MatchResult(true, RepeatType.EXACT, 1.0f)
        }

        val keywords1 = BengaliNormalizer.extractKeywords(q1)
        val keywords2 = BengaliNormalizer.extractKeywords(q2)

        if (keywords1.isEmpty() || keywords2.isEmpty()) {
            return MatchResult(false, RepeatType.UNIQUE, 0f)
        }

        val intersection = keywords1.intersect(keywords2).size
        val union = keywords1.union(keywords2).size
        val jaccard = if (union > 0) intersection.toFloat() / union.toFloat() else 0f

        // Levenshtein character similarity on normalized text
        val editDist = minDistance(norm1, norm2)
        val maxLen = maxOf(norm1.length, norm2.length)
        val charSim = if (maxLen > 0) 1.0f - (editDist.toFloat() / maxLen.toFloat()) else 0f

        val combinedScore = (jaccard * 0.65f) + (charSim * 0.35f)

        return when {
            combinedScore >= 0.80f -> MatchResult(true, RepeatType.EXACT, combinedScore)
            combinedScore >= 0.50f -> MatchResult(true, RepeatType.REWORDED, combinedScore)
            combinedScore >= 0.30f && intersection >= 1 -> MatchResult(true, RepeatType.CONCEPTUAL, combinedScore)
            else -> MatchResult(false, RepeatType.UNIQUE, combinedScore)
        }
    }

    private fun minDistance(word1: String, word2: String): Int {
        val dp = Array(word1.length + 1) { IntArray(word2.length + 1) }
        for (i in 0..word1.length) dp[i][0] = i
        for (j in 0..word2.length) dp[0][j] = j

        for (i in 1..word1.length) {
            for (j in 1..word2.length) {
                if (word1[i - 1] == word2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1]
                } else {
                    dp[i][j] = 1 + minOf(dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1])
                }
            }
        }
        return dp[word1.length][word2.length]
    }
}
