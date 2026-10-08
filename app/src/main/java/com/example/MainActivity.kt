package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.data.model.AppThemeMode
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.AppBottomNav
import com.example.ui.components.AppDrawer
import com.example.ui.components.AppTopBar
import com.example.ui.components.QuestionDetailDialog
import com.example.ui.screens.BookmarksScreen
import com.example.ui.screens.ExportDownloadScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ImportPdfScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StudyProgressScreen
import com.example.ui.screens.SuggestionScreen
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val themeMode by viewModel.appThemeMode.collectAsState()
            val isDark = when (themeMode) {
                AppThemeMode.LIGHT -> false
                AppThemeMode.DARK -> true
                AppThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            MyApplicationTheme(darkTheme = isDark) {
                MainAppContent(viewModel = viewModel, activity = this)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MainViewModel, activity: MainActivity) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val isAudioPlaying by viewModel.speechManager.isPlaying.collectAsState()
    val playingQuestionId by viewModel.speechManager.currentlyPlayingId.collectAsState()
    val enrichingQuestionId by viewModel.isEnrichingAi.collectAsState()
    val selectedDetail by viewModel.selectedQuestionDetail.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Back button handling
    BackHandler(enabled = drawerState.isOpen || currentScreen != AppScreen.HOME) {
        if (drawerState.isOpen) {
            scope.launch { drawerState.close() }
        } else if (currentScreen != AppScreen.HOME) {
            viewModel.navigateTo(AppScreen.HOME)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentScreen = currentScreen,
                onSelectScreen = { screen ->
                    viewModel.navigateTo(screen)
                },
                onSelectSection = { section ->
                    viewModel.setSectionFilter(section)
                    viewModel.navigateTo(AppScreen.SUGGESTIONS)
                },
                onSelectRepeated = {
                    viewModel.setOnlyRepeated(true)
                    viewModel.navigateTo(AppScreen.SUGGESTIONS)
                },
                onCloseDrawer = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                AppTopBar(
                    currentScreen = currentScreen,
                    isAudioPlaying = isAudioPlaying,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onSearchClick = {
                        viewModel.navigateTo(AppScreen.SUGGESTIONS)
                    },
                    onSettingsClick = {
                        viewModel.navigateTo(AppScreen.SETTINGS)
                    }
                )
            },
            bottomBar = {
                AppBottomNav(
                    currentScreen = currentScreen,
                    onSelectScreen = { viewModel.navigateTo(it) }
                )
            },
            floatingActionButton = {
                if (currentScreen == AppScreen.HOME || currentScreen == AppScreen.SUGGESTIONS) {
                    FloatingActionButton(
                        onClick = { viewModel.navigateTo(AppScreen.IMPORT_PDF) },
                        modifier = Modifier.testTag("fab_import_pdf")
                    ) {
                        Icon(Icons.Default.UploadFile, contentDescription = "পিডিএফ আমদানি করুন")
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentScreen) {
                    AppScreen.HOME -> {
                        val stats by viewModel.studyStats.collectAsState()
                        val recent by viewModel.allQuestions.collectAsState()

                        HomeScreen(
                            stats = stats,
                            recentQuestions = recent,
                            isAudioPlaying = isAudioPlaying,
                            playingQuestionId = playingQuestionId,
                            enrichingQuestionId = enrichingQuestionId,
                            onNavigate = { viewModel.navigateTo(it) },
                            onFilterSection = {
                                viewModel.setSectionFilter(it)
                                viewModel.navigateTo(AppScreen.SUGGESTIONS)
                            },
                            onFilterPriority = {
                                viewModel.setPriorityFilter(it)
                                viewModel.navigateTo(AppScreen.SUGGESTIONS)
                            },
                            onFilterRepeated = {
                                viewModel.setOnlyRepeated(true)
                                viewModel.navigateTo(AppScreen.SUGGESTIONS)
                            },
                            onSpeak = { viewModel.speakQuestion(it) },
                            onStopSpeak = { viewModel.stopSpeaking() },
                            onToggleBookmark = { viewModel.toggleBookmark(it) },
                            onToggleCompleted = { viewModel.toggleCompleted(it) },
                            onViewSource = { viewModel.showQuestionDetail(it) },
                            onEnrichAi = { viewModel.enrichWithAi(it) }
                        )
                    }

                    AppScreen.SUGGESTIONS -> {
                        val questions by viewModel.filteredQuestions.collectAsState()
                        val query by viewModel.searchQuery.collectAsState()
                        val section by viewModel.selectedSectionFilter.collectAsState()
                        val priority by viewModel.selectedPriorityFilter.collectAsState()
                        val onlyRepeated by viewModel.onlyRepeatedFilter.collectAsState()

                        SuggestionScreen(
                            questions = questions,
                            searchQuery = query,
                            selectedSection = section,
                            selectedPriority = priority,
                            onlyRepeated = onlyRepeated,
                            isAudioPlaying = isAudioPlaying,
                            playingQuestionId = playingQuestionId,
                            enrichingQuestionId = enrichingQuestionId,
                            onSearchChange = { viewModel.setSearchQuery(it) },
                            onSelectSection = { viewModel.setSectionFilter(it) },
                            onSelectPriority = { viewModel.setPriorityFilter(it) },
                            onToggleRepeated = { viewModel.setOnlyRepeated(it) },
                            onSpeak = { viewModel.speakQuestion(it) },
                            onStopSpeak = { viewModel.stopSpeaking() },
                            onToggleBookmark = { viewModel.toggleBookmark(it) },
                            onToggleCompleted = { viewModel.toggleCompleted(it) },
                            onViewSource = { viewModel.showQuestionDetail(it) },
                            onEnrichAi = { viewModel.enrichWithAi(it) }
                        )
                    }

                    AppScreen.IMPORT_PDF -> {
                        val isProcessing by viewModel.isProcessingPdf.collectAsState()
                        val stage by viewModel.pdfProcessingStage.collectAsState()

                        ImportPdfScreen(
                            isProcessing = isProcessing,
                            stage = stage,
                            onProcessPdf = { uri, name, size ->
                                viewModel.processPdf(activity, uri, name, size)
                            },
                            onViewSuggestions = {
                                viewModel.navigateTo(AppScreen.SUGGESTIONS)
                            }
                        )
                    }

                    AppScreen.BOOKMARKS -> {
                        val bookmarks by viewModel.bookmarkedQuestions.collectAsState()

                        BookmarksScreen(
                            bookmarkedQuestions = bookmarks,
                            isAudioPlaying = isAudioPlaying,
                            playingQuestionId = playingQuestionId,
                            enrichingQuestionId = enrichingQuestionId,
                            onSpeak = { viewModel.speakQuestion(it) },
                            onStopSpeak = { viewModel.stopSpeaking() },
                            onToggleBookmark = { viewModel.toggleBookmark(it) },
                            onToggleCompleted = { viewModel.toggleCompleted(it) },
                            onViewSource = { viewModel.showQuestionDetail(it) },
                            onEnrichAi = { viewModel.enrichWithAi(it) },
                            onExploreSuggestions = { viewModel.navigateTo(AppScreen.SUGGESTIONS) }
                        )
                    }

                    AppScreen.PROGRESS -> {
                        val stats by viewModel.studyStats.collectAsState()
                        StudyProgressScreen(stats = stats)
                    }

                    AppScreen.DOWNLOADS -> {
                        val exportMessage by viewModel.exportStatusMessage.collectAsState()

                        ExportDownloadScreen(
                            exportStatusMessage = exportMessage,
                            onExportPdf = { type -> viewModel.exportPdf(activity, type) },
                            onPrint = { act -> viewModel.printCurrent(act) }
                        )
                    }

                    AppScreen.SETTINGS -> {
                        val lang by viewModel.appLanguage.collectAsState()
                        val theme by viewModel.appThemeMode.collectAsState()
                        val aiProvider by viewModel.aiProviderType.collectAsState()
                        val openRouterKey by viewModel.openRouterApiKey.collectAsState()
                        val geminiKey by viewModel.geminiApiKey.collectAsState()

                        SettingsScreen(
                            currentLanguage = lang,
                            currentTheme = theme,
                            currentAiProvider = aiProvider,
                            openRouterKey = openRouterKey,
                            geminiKey = geminiKey,
                            onSetLanguage = { viewModel.setAppLanguage(it) },
                            onSetTheme = { viewModel.setAppTheme(it) },
                            onSetAiProvider = { viewModel.setAiProvider(it) },
                            onSetOpenRouterKey = { viewModel.setOpenRouterKey(it) },
                            onSetGeminiKey = { viewModel.setGeminiKey(it) },
                            onResetDatabase = { viewModel.resetData() }
                        )
                    }
                }
            }
        }
    }

    // Modal Question Detail & Source Traceability Dialog
    selectedDetail?.let { question ->
        val isSpeaking = isAudioPlaying && playingQuestionId == question.id
        QuestionDetailDialog(
            question = question,
            isSpeaking = isSpeaking,
            onDismiss = { viewModel.showQuestionDetail(null) },
            onSpeak = {
                if (isSpeaking) viewModel.stopSpeaking() else viewModel.speakQuestion(question)
            },
            onEnrichAi = { viewModel.enrichWithAi(question) }
        )
    }
}
