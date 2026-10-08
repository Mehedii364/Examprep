package com.example.ui

import android.app.Activity
import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.ai.AIProvider
import com.example.ai.GeminiProvider
import com.example.ai.LocalAnalyticalProvider
import com.example.ai.OpenRouterProvider
import com.example.data.local.AppDatabase
import com.example.data.model.AiProviderType
import com.example.data.model.AppLanguage
import com.example.data.model.AppThemeMode
import com.example.data.model.PriorityLevel
import com.example.data.model.QuestionItem
import com.example.data.model.QuestionSection
import com.example.data.repository.StudyStats
import com.example.data.repository.SuggestionRepository
import com.example.engine.PdfProcessor
import com.example.export.PdfExporter
import com.example.export.PrintHelper
import com.example.tts.SpeechManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen(val titleBn: String, val titleEn: String) {
    HOME("মূলপাতা", "Home"),
    SUGGESTIONS("চূড়ান্ত সাজেশন", "Suggestions"),
    IMPORT_PDF("পিডিএফ আমদানি", "Import PDF"),
    BOOKMARKS("বুকমার্ক তালিকা", "Bookmarks"),
    PROGRESS("পড়ার অগ্রগতি", "Study Progress"),
    DOWNLOADS("ডাউনলোড ও প্রিন্ট", "Export & Print"),
    SETTINGS("সেটিংস", "Settings")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    val repository = SuggestionRepository(database)
    val speechManager = SpeechManager(application)

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedSectionFilter = MutableStateFlow<QuestionSection?>(null)
    val selectedSectionFilter: StateFlow<QuestionSection?> = _selectedSectionFilter.asStateFlow()

    private val _selectedPriorityFilter = MutableStateFlow<PriorityLevel?>(null)
    val selectedPriorityFilter: StateFlow<PriorityLevel?> = _selectedPriorityFilter.asStateFlow()

    private val _onlyRepeatedFilter = MutableStateFlow(false)
    val onlyRepeatedFilter: StateFlow<Boolean> = _onlyRepeatedFilter.asStateFlow()

    private val _selectedQuestionDetail = MutableStateFlow<QuestionItem?>(null)
    val selectedQuestionDetail: StateFlow<QuestionItem?> = _selectedQuestionDetail.asStateFlow()

    private val _pdfProcessingStage = MutableStateFlow<PdfProcessor.ProcessingStage?>(null)
    val pdfProcessingStage: StateFlow<PdfProcessor.ProcessingStage?> = _pdfProcessingStage.asStateFlow()

    private val _isProcessingPdf = MutableStateFlow(false)
    val isProcessingPdf: StateFlow<Boolean> = _isProcessingPdf.asStateFlow()

    private val _studyStats = MutableStateFlow(
        StudyStats(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    )
    val studyStats: StateFlow<StudyStats> = _studyStats.asStateFlow()

    private val _appLanguage = MutableStateFlow(AppLanguage.BENGALI)
    val appLanguage: StateFlow<AppLanguage> = _appLanguage.asStateFlow()

    private val _appThemeMode = MutableStateFlow(AppThemeMode.SYSTEM)
    val appThemeMode: StateFlow<AppThemeMode> = _appThemeMode.asStateFlow()

    private val _aiProviderType = MutableStateFlow(AiProviderType.LOCAL_ANALYTICAL)
    val aiProviderType: StateFlow<AiProviderType> = _aiProviderType.asStateFlow()

    private val _openRouterApiKey = MutableStateFlow("")
    val openRouterApiKey: StateFlow<String> = _openRouterApiKey.asStateFlow()

    private val _geminiApiKey = MutableStateFlow(getInjectedGeminiKey())
    val geminiApiKey: StateFlow<String> = _geminiApiKey.asStateFlow()

    private val _aiStatusMessage = MutableStateFlow<String?>(null)
    val aiStatusMessage: StateFlow<String?> = _aiStatusMessage.asStateFlow()

    private val _isEnrichingAi = MutableStateFlow<Long?>(null)
    val isEnrichingAi: StateFlow<Long?> = _isEnrichingAi.asStateFlow()

    private val _exportStatusMessage = MutableStateFlow<String?>(null)
    val exportStatusMessage: StateFlow<String?> = _exportStatusMessage.asStateFlow()

    val allQuestions: StateFlow<List<QuestionItem>> = repository.allQuestions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarkedQuestions: StateFlow<List<QuestionItem>> = repository.bookmarkedQuestions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredQuestions: StateFlow<List<QuestionItem>> = combine(
        allQuestions,
        _searchQuery,
        _selectedSectionFilter,
        _selectedPriorityFilter,
        _onlyRepeatedFilter
    ) { questions, query, section, priority, onlyRepeated ->
        questions.filter { item ->
            val matchesQuery = query.isBlank() ||
                item.questionText.contains(query, ignoreCase = true) ||
                item.answerText.contains(query, ignoreCase = true) ||
                item.topic.contains(query, ignoreCase = true) ||
                item.yearDisplay.contains(query, ignoreCase = true)

            val matchesSection = section == null || item.section == section
            val matchesPriority = priority == null || item.priorityLevel == priority
            val matchesRepeated = !onlyRepeated || item.repeatCount > 1

            matchesQuery && matchesSection && matchesPriority && matchesRepeated
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.ensureInitialized()
            refreshStats()
        }
    }

    fun refreshStats() {
        viewModelScope.launch {
            _studyStats.value = repository.getStats()
        }
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSectionFilter(section: QuestionSection?) {
        _selectedSectionFilter.value = section
    }

    fun setPriorityFilter(priority: PriorityLevel?) {
        _selectedPriorityFilter.value = priority
    }

    fun setOnlyRepeated(onlyRepeated: Boolean) {
        _onlyRepeatedFilter.value = onlyRepeated
    }

    fun showQuestionDetail(question: QuestionItem?) {
        _selectedQuestionDetail.value = question
    }

    fun toggleBookmark(question: QuestionItem) {
        viewModelScope.launch {
            repository.toggleBookmark(question.id, question.isBookmarked)
            refreshStats()
        }
    }

    fun toggleCompleted(question: QuestionItem) {
        viewModelScope.launch {
            repository.toggleCompleted(question.id, question.isCompleted)
            refreshStats()
        }
    }

    fun speakQuestion(question: QuestionItem) {
        val textToSpeak = buildString {
            append("প্রশ্ন: ${question.questionText}। ")
            if (question.answerText.isNotBlank()) {
                append("উত্তর: ${question.answerText}")
            }
        }
        speechManager.speak(question.id, textToSpeak)
        viewModelScope.launch {
            repository.markAudioListened(question.id)
            refreshStats()
        }
    }

    fun stopSpeaking() {
        speechManager.stop()
    }

    fun processPdf(context: android.content.Context, uri: Uri, fileName: String, fileSize: Long) {
        viewModelScope.launch {
            _isProcessingPdf.value = true
            try {
                val result = PdfProcessor.processPdfUri(
                    context = context,
                    uri = uri,
                    fileName = fileName,
                    fileSizeBytes = fileSize,
                    onProgress = { stage ->
                        _pdfProcessingStage.value = stage
                    }
                )
                repository.saveImportedPdf(result.document, result.questions)
                refreshStats()
                _currentScreen.value = AppScreen.SUGGESTIONS
            } catch (e: Exception) {
                _pdfProcessingStage.value = PdfProcessor.ProcessingStage.Error("পিডিএফ প্রক্রিয়া ব্যর্থ হয়েছে: ${e.message}")
            } finally {
                _isProcessingPdf.value = false
            }
        }
    }

    fun enrichWithAi(question: QuestionItem) {
        viewModelScope.launch {
            _isEnrichingAi.value = question.id
            _aiStatusMessage.value = "AI উত্তর প্রস্তুত করছে..."

            val provider: AIProvider = when (_aiProviderType.value) {
                AiProviderType.GEMINI -> {
                    val key = _geminiApiKey.value
                    if (key.isNotBlank()) GeminiProvider(key) else LocalAnalyticalProvider()
                }
                AiProviderType.OPEN_ROUTER -> {
                    val key = _openRouterApiKey.value
                    if (key.isNotBlank()) OpenRouterProvider(key) else LocalAnalyticalProvider()
                }
                AiProviderType.LOCAL_ANALYTICAL -> LocalAnalyticalProvider()
            }

            val result = provider.generateAnswer(
                question = question.questionText,
                section = question.section.code,
                topic = question.topic
            )

            result.onSuccess { answer ->
                val updated = question.copy(
                    answerText = answer,
                    confidence = 0.99f
                )
                repository.updateQuestion(updated)
                _selectedQuestionDetail.value = updated
                _aiStatusMessage.value = "AI উত্তর সফলভাবে আপডেট হয়েছে!"
            }.onFailure { err ->
                _aiStatusMessage.value = "AI ব্যর্থ: ${err.message}"
            }

            _isEnrichingAi.value = null
        }
    }

    fun exportPdf(context: android.content.Context, filterType: String) {
        viewModelScope.launch {
            _exportStatusMessage.value = "পিডিএফ প্রস্তুত করা হচ্ছে..."
            val questionsToExport = when (filterType) {
                "very_high" -> allQuestions.value.filter { it.priorityLevel == PriorityLevel.VERY_HIGH }
                "ka" -> allQuestions.value.filter { it.section == QuestionSection.KA }
                "kha" -> allQuestions.value.filter { it.section == QuestionSection.KHA }
                "ga" -> allQuestions.value.filter { it.section == QuestionSection.GA }
                else -> allQuestions.value
            }

            val result = PdfExporter.generateSuggestionPdf(
                context = context,
                title = "রাষ্ট্রবিজ্ঞান ২য় পত্র সাজেশন",
                questions = questionsToExport,
                exportTypeLabel = when (filterType) {
                    "very_high" -> "অতি গুরুত্বপূর্ণ অংশ"
                    "ka" -> "ক-বিভাগ"
                    "kha" -> "খ-বিভাগ"
                    "ga" -> "গ-বিভাগ"
                    else -> "পূর্ণাঙ্গ সংস্করণ"
                }
            )

            _exportStatusMessage.value = if (result.success) {
                "সফলভাবে তৈরি হয়েছে: ${result.fileName} (${result.fileSizeFormatted})"
            } else {
                "পিডিএফ তৈরি ব্যর্থ হয়েছে।"
            }
        }
    }

    fun printCurrent(activity: Activity) {
        val list = filteredQuestions.value.ifEmpty { allQuestions.value }
        PrintHelper.printSuggestions(activity, list)
    }

    fun setAppLanguage(lang: AppLanguage) {
        _appLanguage.value = lang
    }

    fun setAppTheme(theme: AppThemeMode) {
        _appThemeMode.value = theme
    }

    fun setAiProvider(provider: AiProviderType) {
        _aiProviderType.value = provider
    }

    fun setOpenRouterKey(key: String) {
        _openRouterApiKey.value = key
    }

    fun setGeminiKey(key: String) {
        _geminiApiKey.value = key
    }

    fun resetData() {
        viewModelScope.launch {
            repository.resetToDefault()
            refreshStats()
        }
    }

    private fun getInjectedGeminiKey(): String {
        return try {
            val field = BuildConfig::class.java.getField("GEMINI_API_KEY")
            (field.get(null) as? String) ?: ""
        } catch (_: Exception) {
            ""
        }
    }

    override fun onCleared() {
        super.onCleared()
        speechManager.release()
    }
}
