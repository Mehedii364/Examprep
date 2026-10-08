package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PriorityLevel
import com.example.data.model.QuestionItem
import com.example.data.model.QuestionSection
import com.example.data.repository.StudyStats
import com.example.engine.BengaliNormalizer
import com.example.ui.AppScreen
import com.example.ui.components.QuestionCard
import com.example.ui.theme.EmeraldTertiary
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.PriorityHighAmber
import com.example.ui.theme.PriorityVeryHighRed

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    stats: StudyStats,
    recentQuestions: List<QuestionItem>,
    isAudioPlaying: Boolean,
    playingQuestionId: Long?,
    enrichingQuestionId: Long?,
    onNavigate: (AppScreen) -> Unit,
    onFilterSection: (QuestionSection) -> Unit,
    onFilterPriority: (PriorityLevel) -> Unit,
    onFilterRepeated: () -> Unit,
    onSpeak: (QuestionItem) -> Unit,
    onStopSpeak: () -> Unit,
    onToggleBookmark: (QuestionItem) -> Unit,
    onToggleCompleted: (QuestionItem) -> Unit,
    onViewSource: (QuestionItem) -> Unit,
    onEnrichAi: (QuestionItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen_lazy_column")
    ) {
        // Hero Academic Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                NavyPrimary,
                                Color(0xFF1E293B)
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(GoldSecondary)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "ডিগ্রি ও অনার্স ১ম বর্ষ • বিষয় কোড: ১১১৯০৩",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "রাষ্ট্রবিজ্ঞান দ্বিতীয় পত্র সাজেশন প্রো",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    )

                    Text(
                        text = "রাজনৈতিক সংগঠন এবং ব্রিটেন ও মার্কিন যুক্তরাষ্ট্রের রাজনৈতিক ব্যবস্থা",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { onNavigate(AppScreen.IMPORT_PDF) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldSecondary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_home_import_pdf")
                        ) {
                            Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("পিডিএফ আমদানি", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { onNavigate(AppScreen.SUGGESTIONS) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.2f),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_home_view_suggestions")
                        ) {
                            Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("সাজেশন পড়ুন", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Analytical Statistics Dashboard Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "পরিসংখ্যান ও বিশ্লেষণ সামারি",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "অগ্রগতি: ${BengaliNormalizer.toBengaliDigits(stats.progressPercentage.toString())}%",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmeraldTertiary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { stats.progressPercentage / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = EmeraldTertiary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StatTile(
                            number = stats.totalQuestions.toString(),
                            label = "মোট প্রশ্ন",
                            color = NavyPrimary
                        )
                        StatTile(
                            number = stats.veryHighCount.toString(),
                            label = "অতি গুরুত্বপূর্ণ",
                            color = PriorityVeryHighRed
                        )
                        StatTile(
                            number = stats.repeatedCount.toString(),
                            label = "পুনরাবৃত্ত প্রশ্ন",
                            color = PriorityHighAmber
                        )
                        StatTile(
                            number = stats.completedCount.toString(),
                            label = "সম্পন্ন হয়েছে",
                            color = EmeraldTertiary
                        )
                    }
                }
            }
        }

        // Fast Section & Category Shortcuts
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "দ্রুত অনুসন্ধান ও বিভাগসমূহ",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CategoryChip(
                        icon = Icons.Default.LocalFireDepartment,
                        label = "🔥 অতি গুরুত্বপূর্ণ (${BengaliNormalizer.toBengaliDigits(stats.veryHighCount.toString())})",
                        color = PriorityVeryHighRed,
                        onClick = { onFilterPriority(PriorityLevel.VERY_HIGH) }
                    )

                    CategoryChip(
                        icon = Icons.Default.Repeat,
                        label = "🔁 সর্বাধিক পুনরাবৃত্ত (${BengaliNormalizer.toBengaliDigits(stats.repeatedCount.toString())})",
                        color = PriorityHighAmber,
                        onClick = onFilterRepeated
                    )

                    CategoryChip(
                        icon = Icons.Default.Description,
                        label = "ক-বিভাগ (${BengaliNormalizer.toBengaliDigits(stats.kaCount.toString())})",
                        color = NavyPrimary,
                        onClick = { onFilterSection(QuestionSection.KA) }
                    )

                    CategoryChip(
                        icon = Icons.Default.Description,
                        label = "খ-বিভাগ (${BengaliNormalizer.toBengaliDigits(stats.khaCount.toString())})",
                        color = NavyPrimary,
                        onClick = { onFilterSection(QuestionSection.KHA) }
                    )

                    CategoryChip(
                        icon = Icons.Default.Description,
                        label = "গ-বিভাগ (${BengaliNormalizer.toBengaliDigits(stats.gaCount.toString())})",
                        color = NavyPrimary,
                        onClick = { onFilterSection(QuestionSection.GA) }
                    )

                    CategoryChip(
                        icon = Icons.Default.Star,
                        label = "MCQ মাস্টার (${BengaliNormalizer.toBengaliDigits(stats.mcqCount.toString())})",
                        color = EmeraldTertiary,
                        onClick = { onFilterSection(QuestionSection.MCQ) }
                    )
                }
            }
        }

        // Section Title for Highlighted Questions
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "শীর্ষ অগ্রাধিকার প্রশ্নসমূহ (Top Suggestions)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "সকল দেখুন >",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .clickable { onNavigate(AppScreen.SUGGESTIONS) }
                        .padding(4.dp)
                        .testTag("btn_see_all_suggestions")
                )
            }
        }

        // Questions List
        items(recentQuestions.take(8)) { item ->
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

@Composable
private fun StatTile(number: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = BengaliNormalizer.toBengaliDigits(number),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = color,
                fontSize = 20.sp
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Composable
private fun CategoryChip(
    icon: ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.1f))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = color,
                    fontSize = 12.sp
                )
            )
        }
    }
}
