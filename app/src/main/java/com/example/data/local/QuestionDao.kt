package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.DocumentItem
import com.example.data.model.PriorityLevel
import com.example.data.model.QuestionItem
import com.example.data.model.QuestionSection
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions ORDER BY priorityScore DESC, repeatCount DESC")
    fun getAllQuestions(): Flow<List<QuestionItem>>

    @Query("SELECT * FROM questions WHERE section = :section ORDER BY priorityScore DESC")
    fun getQuestionsBySection(section: QuestionSection): Flow<List<QuestionItem>>

    @Query("SELECT * FROM questions WHERE priorityLevel = :priority ORDER BY priorityScore DESC")
    fun getQuestionsByPriority(priority: PriorityLevel): Flow<List<QuestionItem>>

    @Query("SELECT * FROM questions WHERE repeatCount > 1 ORDER BY repeatCount DESC, priorityScore DESC")
    fun getRepeatedQuestions(): Flow<List<QuestionItem>>

    @Query("SELECT * FROM questions WHERE isBookmarked = 1 ORDER BY priorityScore DESC")
    fun getBookmarkedQuestions(): Flow<List<QuestionItem>>

    @Query("SELECT * FROM questions WHERE questionText LIKE '%' || :query || '%' OR answerText LIKE '%' || :query || '%' OR topic LIKE '%' || :query || '%' OR yearDisplay LIKE '%' || :query || '%'")
    fun searchQuestions(query: String): Flow<List<QuestionItem>>

    @Query("SELECT * FROM questions WHERE id = :id LIMIT 1")
    suspend fun getQuestionById(id: Long): QuestionItem?

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getQuestionCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionItem>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionItem): Long

    @Update
    suspend fun updateQuestion(question: QuestionItem)

    @Query("UPDATE questions SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun setBookmark(id: Long, isBookmarked: Boolean)

    @Query("UPDATE questions SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun setCompleted(id: Long, isCompleted: Boolean)

    @Query("UPDATE questions SET isAudioListened = 1 WHERE id = :id")
    suspend fun setAudioListened(id: Long)

    @Query("UPDATE questions SET revisionCount = revisionCount + 1 WHERE id = :id")
    suspend fun incrementRevision(id: Long)

    @Query("DELETE FROM questions")
    suspend fun clearQuestions()

    // Document queries
    @Query("SELECT * FROM documents ORDER BY importedAt DESC")
    fun getAllDocuments(): Flow<List<DocumentItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: DocumentItem): Long

    @Query("DELETE FROM documents")
    suspend fun clearDocuments()
}
