package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.StudyStats
import com.example.engine.BengaliNormalizer
import com.example.ui.theme.EmeraldTertiary
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.PriorityVeryHighRed

@Composable
fun StudyProgressScreen(
    stats: StudyStats
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("study_progress_screen")
    ) {
        Text(
            text = "পড়ার অগ্রগতি ও বিশ্লেষণ (Study Progress)",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            text = "আপনার ব্যক্তিগত পড়াশোনার প্রস্তুতি ট্র্যাকিং (পরীক্ষা ফলাফলের কোনো ভবিষ্যদ্বাণী নয়)",
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Main Progress Card with Circular Indicator
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { stats.progressPercentage / 100f },
                        modifier = Modifier.size(100.dp),
                        strokeWidth = 10.dp,
                        color = EmeraldTertiary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Text(
                        text = "${BengaliNormalizer.toBengaliDigits(stats.progressPercentage.toString())}%",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    )
                }

                Column {
                    Text(
                        text = "মোট প্রস্তুতি সমাপ্তি",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${BengaliNormalizer.toBengaliDigits(stats.completedCount.toString())} / ${BengaliNormalizer.toBengaliDigits(stats.totalQuestions.toString())} টি প্রশ্ন সম্পন্ন",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(EmeraldTertiary.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (stats.progressPercentage > 70) "উচ্চ প্রস্তুতি পর্যায়" else "নিয়মিত অধ্যয়ন চলমান",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmeraldTertiary
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Breakdown Grid
        Text(
            text = "কার্যক্রম বিভাজন",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProgressStatBox(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.CheckCircle,
                title = "সম্পন্ন প্রশ্ন",
                value = stats.completedCount.toString(),
                color = EmeraldTertiary
            )
            ProgressStatBox(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Bookmark,
                title = "বুকমার্ক করা",
                value = stats.bookmarkedCount.toString(),
                color = GoldSecondary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProgressStatBox(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Headphones,
                title = "অডিও শ্রবণ",
                value = stats.audioListenedCount.toString(),
                color = NavyPrimary
            )
            ProgressStatBox(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.TrendingUp,
                title = "অতি গুরুত্বপূর্ণ",
                value = stats.veryHighCount.toString(),
                color = PriorityVeryHighRed
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Section Wise Readiness
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "বিভাগভিত্তিক প্রশ্ন বণ্টন",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(14.dp))

                SectionProgressRow("ক-বিভাগ (অতি সংক্ষিপ্ত)", stats.kaCount, stats.totalQuestions, NavyPrimary)
                SectionProgressRow("খ-বিভাগ (সংক্ষিপ্ত)", stats.khaCount, stats.totalQuestions, GoldSecondary)
                SectionProgressRow("গ-বিভাগ (রচনামূলক)", stats.gaCount, stats.totalQuestions, PriorityVeryHighRed)
                SectionProgressRow("বহুনির্বাচনি (MCQ)", stats.mcqCount, stats.totalQuestions, EmeraldTertiary)
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
private fun SectionProgressRow(title: String, count: Int, total: Int, color: Color) {
    val pct = if (total > 0) (count.toFloat() / total.toFloat()) else 0f
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))
            Text(
                text = "${BengaliNormalizer.toBengaliDigits(count.toString())} টি প্রশ্ন",
                style = MaterialTheme.typography.bodySmall.copy(color = color, fontWeight = FontWeight.Bold)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { pct },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
private fun ProgressStatBox(
    modifier: Modifier,
    icon: ImageVector,
    title: String,
    value: String,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(color.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = BengaliNormalizer.toBengaliDigits(value),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = color)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }
    }
}
