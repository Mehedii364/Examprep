package com.example.engine

import com.example.data.model.QuestionSection

object QuestionClassifier {
    fun detectSection(text: String, headerContext: String = ""): QuestionSection {
        val combined = "$headerContext $text".lowercase()

        if (combined.contains("বহুনির্বাচনি") || combined.contains("mcq") ||
            (text.contains("(ক)") && text.contains("(খ)") && text.contains("(গ)") && text.contains("(ঘ)")) ||
            (text.contains("ক.") && text.contains("খ.") && text.contains("গ.") && text.contains("ঘ."))
        ) {
            return QuestionSection.MCQ
        }

        if (combined.contains("গ-বিভাগ") || combined.contains("রচনামূলক") || combined.contains("broad questions") || combined.contains("section c")) {
            return QuestionSection.GA
        }

        if (combined.contains("ক-বিভাগ") || combined.contains("অতি সংক্ষিপ্ত") || combined.contains("brief questions") || combined.contains("section a")) {
            return QuestionSection.KA
        }

        if (combined.contains("খ-বিভাগ") || combined.contains("সংক্ষিপ্ত প্রশ্ন") || combined.contains("short questions") || combined.contains("section b")) {
            return QuestionSection.KHA
        }

        // Heuristics based on question length and phrasing
        val norm = BengaliNormalizer.normalizeText(text)
        if (norm.contains("বিস্তারিত আলোচনা কর") || norm.contains("বিশ্লেষণ কর") || norm.contains("তুলনামূলক আলোচনা") || norm.contains("সমালোচনা সহ") || norm.length > 90) {
            return QuestionSection.GA
        }
        if (norm.contains("কী বোঝায়") || norm.contains("পার্থক্য নিরূপণ") || norm.contains("কারণগুলো কী") || norm.contains("বৈশিষ্ট্যগুলো লিখ") || norm.length > 45) {
            return QuestionSection.KHA
        }

        return QuestionSection.KA
    }

    fun extractTopic(text: String): String {
        val lower = text.lowercase()
        return when {
            lower.contains("সংবিধান") || lower.contains("constitution") -> "সংবিধান (Constitution)"
            lower.contains("সরকার") || lower.contains("একনায়কতন্ত্র") || lower.contains("গণতন্ত্র") || lower.contains("যুক্তরাষ্ট্র") -> "সরকারের প্রকারভেদ (Forms of Government)"
            lower.contains("ক্ষমতা স্বতন্ত্রীকরণ") || lower.contains("মন্টেস্কু") || lower.contains("spirit of laws") -> "ক্ষমতা স্বতন্ত্রীকরণ নীতি (Separation of Powers)"
            lower.contains("আইনসভা") || lower.contains("শাসন বিভাগ") || lower.contains("বিচার বিভাগ") || lower.contains("পার্লামেন্ট") -> "সরকারের অঙ্গসমূহ (Organs of Government)"
            lower.contains("রাজনৈতিক দল") || lower.contains("চাপসৃষ্টিকারী") || lower.contains("জনমত") || lower.contains("আমলাতন্ত্র") || lower.contains("নির্বাচকমণ্ডলী") -> "রাজনৈতিক আচরণ (Political Behaviour)"
            lower.contains("ব্রিটেন") || lower.contains("ব্রিটিশ") || lower.contains("কমনওয়েলথ") || lower.contains("ম্যাগনা কার্টা") || lower.contains("রাজতন্ত্র") || lower.contains("লর্ডসভা") -> "ব্রিটিশ রাজনৈতিক ব্যবস্থা (UK System)"
            lower.contains("মার্কিন") || lower.contains("আমেরিকা") || lower.contains("সিনেট") || lower.contains("কংগ্রেস") || lower.contains("ইলেকটোরাল") || lower.contains("ভেটো") -> "মার্কিন রাজনৈতিক ব্যবস্থা (USA System)"
            else -> "রাষ্ট্রবিজ্ঞান ২য় পত্র সাধারণ"
        }
    }
}
