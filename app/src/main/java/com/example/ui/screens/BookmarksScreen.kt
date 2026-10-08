package com.example.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuestionItem
import com.example.engine.BengaliNormalizer
import com.example.ui.components.QuestionCard

@Composable
fun BookmarksScreen(
    bookmarkedQuestions: List<QuestionItem>,
    isAudioPlaying: Boolean,
    playingQuestionId: Long?,
    enrichingQuestionId: Long?,
    onSpeak: (QuestionItem) -> Unit,
    onStopSpeak: () -> Unit,
    onToggleBookmark: (QuestionItem) -> Unit,
    onToggleCompleted: (QuestionItem) -> Unit,
    onViewSource: (QuestionItem) -> Unit,
    onEnrichAi: (QuestionItem) -> Unit,
    onExploreSuggestions: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("bookmarks_screen")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "সংরক্ষিত প্রশ্নাবলি (Bookmarks)",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = "মোট ${BengaliNormalizer.toBengaliDigits(bookmarkedQuestions.size.toString())} টি প্রশ্ন বুকমার্ক করা হয়েছে",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }

        if (bookmarkedQuestions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "কোনো বুকমার্ক করা প্রশ্ন নেই",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "যেকোনো প্রশ্নের পাশে থাকা বুকমার্ক আইকনে ট্যাপ করে এখানে সংরক্ষণ করতে পারেন।",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )
                    Button(onClick = onExploreSuggestions) {
                        Text("সাজেশন এক্সপ্লোর করুন")
                    }
                }
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(bookmarkedQuestions, key = { it.id }) { item ->
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
