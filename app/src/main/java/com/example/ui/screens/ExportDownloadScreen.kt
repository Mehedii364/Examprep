package com.example.ui.screens

import android.app.Activity
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldTertiary
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.PriorityVeryHighRed

@Composable
fun ExportDownloadScreen(
    exportStatusMessage: String?,
    onExportPdf: (String) -> Unit,
    onPrint: (Activity) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("export_download_screen")
    ) {
        Text(
            text = "পিডিএফ এক্সপোর্ট ও প্রিন্ট (Export & Print)",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            text = "পরীক্ষার্থীবান্ধব এ৪ (A4) সাইজের ঝকঝকে বাংলা ফন্টে তৈরি পিডিএফ ডাউনলোড করুন অথবা সরাসরি প্রিন্টারে প্রিন্ট দিন।",
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (exportStatusMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(EmeraldTertiary.copy(alpha = 0.15f))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldTertiary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = exportStatusMessage,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = EmeraldTertiary
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Option 1: Full Suggestion PDF
        ExportOptionCard(
            title = "পূর্ণাঙ্গ সাজেশন পিডিএফ (Full Suggestion)",
            description = "কভার পেজ, পরিসংখ্যান সারাংশ, অতি গুরুত্বপূর্ণ প্রশ্নাবলি, ক, খ, গ এবং বহুনির্বাচনি মডেল উত্তরপত্র সহ সম্পূর্ণ গাইড।",
            badge = "সর্বাধিক জনপ্রিয়",
            buttonLabel = "পূর্ণাঙ্গ PDF ডাউনলোড",
            onAction = { onExportPdf("all") },
            testTag = "btn_export_full_pdf"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Option 2: High Priority Revision PDF
        ExportOptionCard(
            title = "রিভিশন ক্যাপসুল PDF (High Priority Only)",
            description = "কেবলমাত্র ৯৯% এবং ৯০%+ অগ্রাধিকারপ্রাপ্ত প্রশ্নাবলি ও মডেল উত্তর; পরীক্ষার পূর্বরাতের দ্রুত রিভিশনের উপযোগী।",
            badge = "ক্যাপসুল রিভিশন",
            buttonLabel = "রিভিশন PDF ডাউনলোড",
            onAction = { onExportPdf("very_high") },
            testTag = "btn_export_revision_pdf"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Option 3: Section Specific PDFs
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "বিভাগভিত্তিক পৃথক হ্যান্ডনোট PDF",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "আপনার নির্দিষ্ট দুর্বলতার অংশ অনুযায়ী পৃথক পিডিএফ ডাউনলোড করুন:",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalButton(
                        onClick = { onExportPdf("ka") },
                        modifier = Modifier.weight(1f).testTag("btn_export_ka_pdf")
                    ) {
                        Text("ক-বিভাগ", fontSize = 12.sp)
                    }

                    FilledTonalButton(
                        onClick = { onExportPdf("kha") },
                        modifier = Modifier.weight(1f).testTag("btn_export_kha_pdf")
                    ) {
                        Text("খ-বিভাগ", fontSize = 12.sp)
                    }

                    FilledTonalButton(
                        onClick = { onExportPdf("ga") },
                        modifier = Modifier.weight(1f).testTag("btn_export_ga_pdf")
                    ) {
                        Text("গ-বিভাগ", fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Option 4: Direct Print Support
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "সরাসরি প্রিন্ট করুন (Print Support)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ওয়াইফাই বা সংযুক্ত প্রিন্টারের মাধ্যমে প্রিন্ট-বান্ধব লেআউটে তাৎক্ষণিক প্রিন্ট নিন।",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = {
                        if (activity != null) {
                            onPrint(activity)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("btn_print_suggestions")
                ) {
                    Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("প্রিন্ট")
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
private fun ExportOptionCard(
    title: String,
    description: String,
    badge: String,
    buttonLabel: String,
    onAction: () -> Unit,
    testTag: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(EmeraldTertiary.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = EmeraldTertiary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onAction,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(testTag),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
            ) {
                Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(buttonLabel)
            }
        }
    }
}
