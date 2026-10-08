package com.example.engine

object BengaliNormalizer {
    private val BENGALI_DIGITS = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
    private val ENGLISH_DIGITS = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')

    fun toEnglishDigits(input: String): String {
        var result = input
        for (i in 0..9) {
            result = result.replace(BENGALI_DIGITS[i], ENGLISH_DIGITS[i])
        }
        return result
    }

    fun toBengaliDigits(input: String): String {
        var result = input
        for (i in 0..9) {
            result = result.replace(ENGLISH_DIGITS[i], BENGALI_DIGITS[i])
        }
        return result
    }

    fun normalizeText(input: String): String {
        return input
            .replace(Regex("[\\r\\t\\u00A0]"), " ")
            .replace(Regex("[\\p{Punct}&&[^?]]"), " ")
            .replace("?", " ")
            .replace("।", " ")
            .replace(Regex("\\s+"), " ")
            .trim()
            .lowercase()
    }

    fun stemWord(word: String): String {
        var w = word
        val suffixes = listOf("গুলো", "গুলি", "সমূহ", "দের", "টির", "টি", "টা", "ের", "র")
        for (suffix in suffixes) {
            if (w.endsWith(suffix) && w.length - suffix.length >= 3) {
                w = w.dropLast(suffix.length)
                break
            }
        }
        return w
    }

    fun extractKeywords(text: String): Set<String> {
        val stopWords = setOf(
            "কি", "কী", "কেন", "কাকে", "বলে", "বলতে", "কার", "কোথায়", "কোন", "কয়টি", "কয়", "এবং",
            "বা", "হলো", "হলে", "হতে", "হবে", "করে", "করা", "কর", "লিখ", "লিখুন", "ব্যাখ্যা",
            "আলোচনা", "নিরূপণ", "বর্ণনা", "সম্পর্কে", "একটি", "দুটি", "তিনটি", "নাম", "এর", "দেও",
            "উক্তিটি", "উক্তি", "গ্রন্থটি", "লেখক", "শব্দের", "অর্থ", "কত", "কোনটি", "কীভাবে", "হিসেবে"
        )
        val normalized = normalizeText(text)
        return normalized.split(" ")
            .filter { it.length > 2 && !stopWords.contains(it) }
            .map { stemWord(it) }
            .toSet()
    }

    fun cleanOcrArtifacts(text: String): String {
        return text
            .replace("জা.বি.", "জাতীয় বিশ্ববিদ্যালয়")
            .replace("জা,বি,", "জাতীয় বিশ্ববিদ্যালয়")
            .replace("ডি.গ্রি", "ডিগ্রি")
            .replace("অনার্স-নন-মেজর", "অনার্স নন-মেজর")
            .replace(Regex("([০-৯]{4})\\s*সালে"), "$1 সালে")
            .trim()
    }
}
