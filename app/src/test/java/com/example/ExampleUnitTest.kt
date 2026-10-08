package com.example

import com.example.data.local.BuiltInQuestions
import com.example.data.model.PriorityLevel
import com.example.data.model.QuestionSection
import com.example.data.model.RepeatType
import com.example.engine.BengaliNormalizer
import com.example.engine.PriorityCalculator
import com.example.engine.QuestionClassifier
import com.example.engine.SimilarityEngine
import com.example.engine.YearExtractor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testBengaliNormalizerDigits() {
        val bnDigits = "২০২৪"
        val enDigits = BengaliNormalizer.toEnglishDigits(bnDigits)
        assertEquals("2024", enDigits)

        val convertedBack = BengaliNormalizer.toBengaliDigits("2026")
        assertEquals("২০২৬", convertedBack)
    }

    @Test
    fun testBengaliNormalizerTextCleaning() {
        val raw = "জা.বি. ২০১২, ২০১৪? এবং   ২০২৪  সালে এসেছে।"
        val cleaned = BengaliNormalizer.cleanOcrArtifacts(raw)
        assertTrue(cleaned.contains("জাতীয় বিশ্ববিদ্যালয়"))

        val keywords = BengaliNormalizer.extractKeywords("সংসদীয় সরকারের প্রধান বৈশিষ্ট্যগুলো আলোচনা কর")
        assertTrue(keywords.contains("সংসদীয়"))
        assertTrue(keywords.contains("সরকারের"))
        // stop words should be stripped
        assertFalse(keywords.contains("কর"))
        assertFalse(keywords.contains("আলোচনা"))
    }

    @Test
    fun testYearExtractor() {
        val text = "জা.বি. ২০১১, ২০১৩, ২০১৪, ২০১৮, ২০২০ (ডিগ্রি); ২০২৪ সালে যা এসেছিল"
        val years = YearExtractor.extractYears(text)
        assertTrue(years.contains(2011))
        assertTrue(years.contains(2013))
        assertTrue(years.contains(2014))
        assertTrue(years.contains(2018))
        assertTrue(years.contains(2020))
        assertTrue(years.contains(2024))
        assertEquals(6, years.size)

        val emptyYears = YearExtractor.extractYears("কোনো শিক্ষাবর্ষের উল্লেখ নেই")
        assertTrue(emptyYears.isEmpty())
        val display = YearExtractor.formatYearDisplay(emptyYears)
        assertTrue(display.contains("Year unclear"))
    }

    @Test
    fun testQuestionClassifier() {
        val mcqText = "‘The Spirit of Laws’ গ্রন্থের রচয়িতা কে? (ক) জন লক (খ) মন্টেস্কু (গ) রুশো (ঘ) এরিস্টটল"
        assertEquals(QuestionSection.MCQ, QuestionClassifier.detectSection(mcqText))

        val kaText = "সংবিধান কি?"
        assertEquals(QuestionSection.KA, QuestionClassifier.detectSection(kaText, "ক-বিভাগ: অতি সংক্ষিপ্ত প্রশ্নাবলি"))

        val khaText = "রাজনৈতিক দল ও চাপসৃষ্টিকারী গোষ্ঠীর মধ্যে পার্থক্য নিরূপণ কর।"
        assertEquals(QuestionSection.KHA, QuestionClassifier.detectSection(khaText, "খ-বিভাগ: সংক্ষিপ্ত প্রশ্নাবলী"))

        val gaText = "গণতন্ত্রের সফলতার পূর্বশর্তগুলো বিস্তারিত ব্যাখ্যা কর এবং এর প্রায়োগিক দিক বিশ্লেষণ কর।"
        assertEquals(QuestionSection.GA, QuestionClassifier.detectSection(gaText, "গ-বিভাগ: রচনামূলক প্রশ্নাবলি"))
    }

    @Test
    fun testTopicExtraction() {
        val t1 = QuestionClassifier.extractTopic("একটি উত্তম সংবিধানের বৈশিষ্ট্যসমূহ লিখ।")
        assertTrue(t1.contains("সংবিধান"))

        val t2 = QuestionClassifier.extractTopic("মার্কিন সিনেটের ক্ষমতা ও গঠন আলোচনা কর।")
        assertTrue(t2.contains("মার্কিন"))

        val t3 = QuestionClassifier.extractTopic("ব্রিটিশ প্রধানমন্ত্রীর ক্ষমতা ও কার্যাবলি ব্যাখ্যা কর।")
        assertTrue(t3.contains("ব্রিটিশ"))
    }

    @Test
    fun testSimilarityEngineExactAndReworded() {
        val q1 = "সংসদীয় সরকারের বৈশিষ্ট্য আলোচনা কর।"
        val q2 = "সংসদীয় শাসনব্যবস্থার প্রধান বৈশিষ্ট্যগুলো কী?"

        val result = SimilarityEngine.calculateSimilarity(q1, q2)
        assertTrue(result.isMatch)
        assertTrue(result.repeatType == RepeatType.REWORDED || result.repeatType == RepeatType.CONCEPTUAL)

        val qIdentical1 = "সংবিধান কি?"
        val qIdentical2 = "সংবিধান কি?"
        val exactResult = SimilarityEngine.calculateSimilarity(qIdentical1, qIdentical2)
        assertTrue(exactResult.isMatch)
        assertEquals(RepeatType.EXACT, exactResult.repeatType)

        val unrelated1 = "ম্যাগনা কার্টা কত সালে স্বাক্ষরিত হয়?"
        val unrelated2 = "মার্কিন সিনেটের সদস্য সংখ্যা কত?"
        val noMatch = SimilarityEngine.calculateSimilarity(unrelated1, unrelated2)
        assertFalse(noMatch.isMatch)
    }

    @Test
    fun testPriorityCalculatorExplainability() {
        val assessment = PriorityCalculator.calculatePriority(
            years = listOf(2011, 2013, 2015, 2017, 2020, 2022, 2024),
            repeatCount = 7,
            repeatType = RepeatType.EXACT,
            section = QuestionSection.GA,
            hasOfficialHighlight = true
        )

        assertEquals(PriorityLevel.VERY_HIGH, assessment.level)
        assertTrue(assessment.score in 88..99)
        // Academic rule verification: must explain analytical basis and never claim certainty
        assertTrue(assessment.explanation.contains("অগ্রাধিকার"))
        assertTrue(assessment.explanation.contains("পরিসংখ্যানগত"))
        assertFalse(assessment.explanation.contains("১০০% কমন"))
        assertFalse(assessment.explanation.contains("অবশ্যই আসবে"))
    }

    @Test
    fun testBuiltInQuestionsCompleteness() {
        val questions = BuiltInQuestions.getAllQuestions()
        assertTrue("Built-in questions should contain questions from the 20-page booklet", questions.size >= 35)

        val hasKa = questions.any { it.section == QuestionSection.KA }
        val hasKha = questions.any { it.section == QuestionSection.KHA }
        val hasGa = questions.any { it.section == QuestionSection.GA }
        val hasMcq = questions.any { it.section == QuestionSection.MCQ }

        assertTrue(hasKa)
        assertTrue(hasKha)
        assertTrue(hasGa)
        assertTrue(hasMcq)
    }
}
