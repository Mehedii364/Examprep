package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class QuestionSection(val code: String, val bnLabel: String, val enLabel: String) {
    MCQ("mcq", "বহুনির্বাচনি (MCQ)", "Multiple Choice (MCQ)"),
    KA("ka", "ক-বিভাগ (অতি সংক্ষিপ্ত)", "Section A (Brief)"),
    KHA("kha", "খ-বিভাগ (সংক্ষিপ্ত)", "Section B (Short)"),
    GA("ga", "গ-বিভাগ (রচনামূলক)", "Section C (Broad)")
}

enum class PriorityLevel(val code: String, val bnLabel: String, val enLabel: String) {
    VERY_HIGH("very_high", "🔥 অতি গুরুত্বপূর্ণ (Very High Priority)", "Very High Priority"),
    HIGH("high", "⭐ গুরুত্বপূর্ণ (High Priority)", "High Priority"),
    IMPORTANT("important", "📌 বিবেচ্য (Important)", "Important"),
    REVISION("revision", "📖 সাধারণ পুনরাবৃত্তি (Revision)", "Revision")
}

enum class RepeatType(val bnLabel: String, val enLabel: String) {
    EXACT("হুবহু পুনরাবৃত্তি (Exact Repeat)", "Exact Repeat"),
    REWORDED("ভাষান্তরিত পুনরাবৃত্তি (Reworded)", "Reworded Repeat"),
    CONCEPTUAL("ধারণাগত পুনরাবৃত্তি (Conceptual)", "Conceptual Repeat"),
    UNIQUE("একক প্রশ্ন (Unique)", "Unique Question")
}

@Entity(tableName = "questions")
data class QuestionItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val documentId: Long = 0,
    val documentName: String = "জাতীয় বিশ্ববিদ্যালয় রাষ্ট্রবিজ্ঞান ২য় পত্র",
    val section: QuestionSection = QuestionSection.KA,
    val questionNumber: String = "",
    val questionText: String = "",
    val originalText: String = "",
    val answerText: String = "",
    val structuredPoints: String = "", // JSON or bulleted points
    val years: List<Int> = emptyList(),
    val yearDisplay: String = "",
    val repeatCount: Int = 1,
    val repeatType: RepeatType = RepeatType.UNIQUE,
    val relatedQuestionIds: List<Long> = emptyList(),
    val topic: String = "রাষ্ট্রবিজ্ঞান ২য় পত্র",
    val priorityLevel: PriorityLevel = PriorityLevel.HIGH,
    val priorityScore: Int = 85, // 0-100 Analytical Priority Score
    val whyImportant: String = "",
    val sourcePage: Int = 1,
    val confidence: Float = 0.95f,
    val isBookmarked: Boolean = false,
    val isCompleted: Boolean = false,
    val isAudioListened: Boolean = false,
    val revisionCount: Int = 0,
    val mcqOptions: List<String> = emptyList(),
    val mcqCorrectAnswer: String = ""
)

@Entity(tableName = "documents")
data class DocumentItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fileName: String,
    val fileSizeFormatted: String,
    val pageCount: Int,
    val importedAt: Long = System.currentTimeMillis(),
    val isScanned: Boolean = false,
    val questionCount: Int = 0,
    val status: String = "বিশ্লেষণ সম্পন্ন"
)

enum class AppLanguage(val code: String, val displayName: String) {
    BENGALI("bn", "বাংলা"),
    ENGLISH("en", "English"),
    ARABIC("ar", "العربية")
}

enum class AppThemeMode(val code: String, val displayName: String) {
    SYSTEM("system", "সিস্টেম ডিফল্ট (System)"),
    LIGHT("light", "লাইট মোড (Light)"),
    DARK("dark", "ডার্ক মোড (Dark)")
}

enum class AiProviderType(val code: String, val displayName: String) {
    OPEN_ROUTER("openrouter", "OpenRouter AI"),
    GEMINI("gemini", "Google Gemini AI"),
    LOCAL_ANALYTICAL("local", "ইন-অ্যাপ বিশ্লেষণ ইঞ্জিন (Offline)")
}
