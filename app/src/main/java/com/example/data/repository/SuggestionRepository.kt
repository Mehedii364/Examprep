package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.BuiltInQuestions
import com.example.data.model.DocumentItem
import com.example.data.model.PriorityLevel
import com.example.data.model.QuestionItem
import com.example.data.model.QuestionSection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

data class StudyStats(
    val totalQuestions: Int,
    val mcqCount: Int,
    val kaCount: Int,
    val khaCount: Int,
    val gaCount: Int,
    val veryHighCount: Int,
    val highCount: Int,
    val repeatedCount: Int,
    val completedCount: Int,
    val bookmarkedCount: Int,
    val audioListenedCount: Int,
    val progressPercentage: Int
)

class SuggestionRepository(private val database: AppDatabase) {
    private val dao = database.questionDao()

    val allQuestions: Flow<List<QuestionItem>> = dao.getAllQuestions()
    val repeatedQuestions: Flow<List<QuestionItem>> = dao.getRepeatedQuestions()
    val bookmarkedQuestions: Flow<List<QuestionItem>> = dao.getBookmarkedQuestions()
    val allDocuments: Flow<List<DocumentItem>> = dao.getAllDocuments()

    suspend fun ensureInitialized() {
        val count = dao.getQuestionCount()
        if (count == 0) {
            seedBuiltInData()
        }
    }

    suspend fun seedBuiltInData() {
        val questions = BuiltInQuestions.getAllQuestions()
        val defaultDoc = DocumentItem(
            fileName = "জাতীয় বিশ্ববিদ্যালয় রাষ্ট্রবিজ্ঞান ২য় পত্র (১১১৯০৩).pdf",
            fileSizeFormatted = "4.8 MB",
            pageCount = 20,
            questionCount = questions.size,
            isScanned = true,
            status = "মূল বোর্ড প্রশ্নব্যাংক সংযুক্ত (${questions.size} টি প্রশ্ন)"
        )
        val docId = dao.insertDocument(defaultDoc)
        val updatedQuestions = questions.map { it.copy(documentId = docId) }
        dao.insertQuestions(updatedQuestions)
    }

    suspend fun saveImportedPdf(document: DocumentItem, questions: List<QuestionItem>): Long {
        val docId = dao.insertDocument(document)
        val updatedQuestions = questions.map { it.copy(documentId = docId) }
        dao.insertQuestions(updatedQuestions)
        return docId
    }

    fun getQuestionsBySection(section: QuestionSection): Flow<List<QuestionItem>> {
        return dao.getQuestionsBySection(section)
    }

    fun getQuestionsByPriority(priority: PriorityLevel): Flow<List<QuestionItem>> {
        return dao.getQuestionsByPriority(priority)
    }

    fun search(query: String): Flow<List<QuestionItem>> {
        return dao.searchQuestions(query)
    }

    suspend fun toggleBookmark(id: Long, currentStatus: Boolean) {
        dao.setBookmark(id, !currentStatus)
    }

    suspend fun toggleCompleted(id: Long, currentStatus: Boolean) {
        dao.setCompleted(id, !currentStatus)
    }

    suspend fun markAudioListened(id: Long) {
        dao.setAudioListened(id)
    }

    suspend fun updateQuestion(question: QuestionItem) {
        dao.updateQuestion(question)
    }

    suspend fun resetToDefault() {
        dao.clearQuestions()
        dao.clearDocuments()
        seedBuiltInData()
    }

    suspend fun getStats(): StudyStats {
        val questions = dao.getAllQuestions().first()
        val total = questions.size
        val mcq = questions.count { it.section == QuestionSection.MCQ }
        val ka = questions.count { it.section == QuestionSection.KA }
        val kha = questions.count { it.section == QuestionSection.KHA }
        val ga = questions.count { it.section == QuestionSection.GA }
        val veryHigh = questions.count { it.priorityLevel == PriorityLevel.VERY_HIGH }
        val high = questions.count { it.priorityLevel == PriorityLevel.HIGH }
        val repeated = questions.count { it.repeatCount > 1 }
        val completed = questions.count { it.isCompleted }
        val bookmarked = questions.count { it.isBookmarked }
        val audio = questions.count { it.isAudioListened }
        val progress = if (total > 0) ((completed * 100) / total).coerceIn(0, 100) else 0

        return StudyStats(
            totalQuestions = total,
            mcqCount = mcq,
            kaCount = ka,
            khaCount = kha,
            gaCount = ga,
            veryHighCount = veryHigh,
            highCount = high,
            repeatedCount = repeated,
            completedCount = completed,
            bookmarkedCount = bookmarked,
            audioListenedCount = audio,
            progressPercentage = progress
        )
    }
}
