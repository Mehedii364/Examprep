package com.example.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PriorityLevel
import com.example.data.model.QuestionItem
import com.example.data.model.QuestionSection
import com.example.engine.BengaliNormalizer
import com.example.ui.components.QuestionCard

@Composable
fun SuggestionScreen(
    questions: List<QuestionItem>,
    searchQuery: String,
    selectedSection: QuestionSection?,
    selectedPriority: PriorityLevel?,
    onlyRepeated: Boolean,
    isAudioPlaying: Boolean,
    playingQuestionId: Long?,
    enrichingQuestionId: Long?,
    onSearchChange: (String) -> Unit,
    onSelectSection: (QuestionSection?) -> Unit,
    onSelectPriority: (PriorityLevel?) -> Unit,
    onToggleRepeated: (Boolean) -> Unit,
    onSpeak: (QuestionItem) -> Unit,
    onStopSpeak: () -> Unit,
    onToggleBookmark: (QuestionItem) -> Unit,
    onToggleCompleted: (QuestionItem) -> Unit,
    onViewSource: (QuestionItem) -> Unit,
    onEnrichAi: (QuestionItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("suggestion_screen")
    ) {
        // Search TextField
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("প্রশ্ন, উত্তর, টপিক বা বছর খুঁজুন...", fontSize = 14.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "পরিষ্কার করুন")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("search_text_field"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        // Section Filter Chips Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedSection == null,
                onClick = { onSelectSection(null) },
                label = { Text("সকল বিভাগ") },
                modifier = Modifier.testTag("filter_section_all")
            )
            FilterChip(
                selected = selectedSection == QuestionSection.KA,
                onClick = { onSelectSection(if (selectedSection == QuestionSection.KA) null else QuestionSection.KA) },
                label = { Text("ক-বিভাগ (অতি সংক্ষিপ্ত)") },
                modifier = Modifier.testTag("filter_section_ka")
            )
            FilterChip(
                selected = selectedSection == QuestionSection.KHA,
                onClick = { onSelectSection(if (selectedSection == QuestionSection.KHA) null else QuestionSection.KHA) },
                label = { Text("খ-বিভাগ (সংক্ষিপ্ত)") },
                modifier = Modifier.testTag("filter_section_kha")
            )
            FilterChip(
                selected = selectedSection == QuestionSection.GA,
                onClick = { onSelectSection(if (selectedSection == QuestionSection.GA) null else QuestionSection.GA) },
                label = { Text("গ-বিভাগ (রচনামূলক)") },
                modifier = Modifier.testTag("filter_section_ga")
            )
            FilterChip(
                selected = selectedSection == QuestionSection.MCQ,
                onClick = { onSelectSection(if (selectedSection == QuestionSection.MCQ) null else QuestionSection.MCQ) },
                label = { Text("MCQ মাস্টার") },
                modifier = Modifier.testTag("filter_section_mcq")
            )
        }

        // Priority Filter Chips Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedPriority == null && !onlyRepeated,
                onClick = {
                    onSelectPriority(null)
                    onToggleRepeated(false)
                },
                label = { Text("সকল অগ্রাধিকার") }
            )
            FilterChip(
                selected = selectedPriority == PriorityLevel.VERY_HIGH,
                onClick = {
                    onSelectPriority(if (selectedPriority == PriorityLevel.VERY_HIGH) null else PriorityLevel.VERY_HIGH)
                },
                label = { Text("🔥 অতি গুরুত্বপূর্ণ") }
            )
            FilterChip(
                selected = selectedPriority == PriorityLevel.HIGH,
                onClick = {
                    onSelectPriority(if (selectedPriority == PriorityLevel.HIGH) null else PriorityLevel.HIGH)
                },
                label = { Text("⭐ গুরুত্বপূর্ণ") }
            )
            FilterChip(
                selected = onlyRepeated,
                onClick = { onToggleRepeated(!onlyRepeated) },
                label = { Text("🔁 বারবার আসা প্রশ্ন") }
            )
            FilterChip(
                selected = selectedPriority == PriorityLevel.IMPORTANT,
                onClick = {
                    onSelectPriority(if (selectedPriority == PriorityLevel.IMPORTANT) null else PriorityLevel.IMPORTANT)
                },
                label = { Text("📌 সাধারণ বিবেচ্য") }
            )
        }

        // Results Count Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${BengaliNormalizer.toBengaliDigits(questions.size.toString())} টি প্রশ্ন দৃশ্যমান",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        // Questions List
        if (questions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "কোনো প্রশ্ন পাওয়া যায়নি",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "অনুসন্ধান বা ফিল্টার পরিবর্তন করে পুনরায় চেষ্টা করুন।",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        onSearchChange("")
                        onSelectSection(null)
                        onSelectPriority(null)
                        onToggleRepeated(false)
                    }) {
                        Text("সকল ফিল্টার মুছুন")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(questions, key = { it.id }) { item ->
                    val isSpeaking = isAudioPlaying && playingQuestionId == item.id
                    val isEnriching = enrichingQuestionId == item.id

                    QuestionCard(
                        question = item,
                        isSpeaking = isSpeaking,
                        isEnrichingAi = isEnriching,
                        onSpeak = { onSpeak(item) },
                        onStopSpeak = onStopSpeak,
                        onToggleBookmark = { onToggleBookmark(item) },
                        onToggleCompleted = { onToggleCompleted(item) },
                        onViewSource = { onViewSource(item) },
                        onEnrichAi = { onEnrichAi(item) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}
