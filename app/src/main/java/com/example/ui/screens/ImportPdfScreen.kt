package com.example.ui.screens

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.PdfProcessor
import com.example.ui.theme.EmeraldTertiary
import com.example.ui.theme.GoldSecondary
import com.example.ui.theme.NavyPrimary

@Composable
fun ImportPdfScreen(
    isProcessing: Boolean,
    stage: PdfProcessor.ProcessingStage?,
    onProcessPdf: (Uri, String, Long) -> Unit,
    onViewSuggestions: () -> Unit
) {
    val context = LocalContext.current
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    var selectedFileSize by remember { mutableStateOf<Long?>(null) }

    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            var name = "Document.pdf"
            var size = 0L
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (cursor.moveToFirst()) {
                    if (nameIndex != -1) name = cursor.getString(nameIndex)
                    if (sizeIndex != -1) size = cursor.getLong(sizeIndex)
                }
            }
            selectedFileName = name
            selectedFileSize = size
            onProcessPdf(uri, name, size)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("import_pdf_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "পিডিএফ প্রশ্নপত্র আমদানি ও এআই বিশ্লেষণ",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ),
            textAlign = TextAlign.Center
        )

        Text(
            text = "জাতীয় বিশ্ববিদ্যালয়ের বিগত বছরের প্রশ্নপত্র (স্ক্যান বা টেক্সট) আপলোড করুন। অ্যাপটি স্বয়ংক্রিয়ভাবে প্রশ্ন, বছর, বিভাগ ও পুনরাবৃত্তি বিশ্লেষণ করে চূড়ান্ত সাজেশন তৈরি করবে।",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // PDF Upload Box / Dropzone
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(20.dp))
                .clickable(enabled = !isProcessing) {
                    pdfPickerLauncher.launch(arrayOf("application/pdf"))
                }
                .testTag("pdf_picker_card"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "ডিভাইস থেকে PDF নির্বাচন করুন",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Text(
                    text = "সমর্থিত: বাংলা ও ইংরেজি PDF, স্ক্যানকৃত প্রশ্নপত্র, বহুনির্বাচনি ও বর্ণনামূলক",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.5.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { pdfPickerLauncher.launch(arrayOf("application/pdf")) },
                    enabled = !isProcessing,
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("btn_select_pdf")
                ) {
                    Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("ফাইল বাছুন (Select PDF)")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Processing Pipeline Stages
        if (isProcessing || stage != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "বিশ্লেষণ অগ্রগতি (Processing Pipeline)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        if (isProcessing) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        }
                    }

                    if (selectedFileName != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "ফাইল: $selectedFileName",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val steps = listOf(
                        Pair(1, "পৃষ্ঠা পাঠ ও স্ট্রাকচার বিশ্লেষণ"),
                        Pair(2, "টেক্সট রূপান্তর ও পৃষ্ঠা বিভাজন"),
                        Pair(3, "ওসিআর (OCR) স্ক্যান ও ক্যারেক্টার বিশ্লেষণ"),
                        Pair(4, "বাংলা ব্যাকরণ ও প্রশ্নবিন্যাস শুদ্ধিকরণ"),
                        Pair(5, "বিভাগভিত্তিক প্রশ্ন শনাক্তকরণ (ক, খ, গ, MCQ)"),
                        Pair(6, "শিক্ষাবর্ষ ভিত্তিক পুনরাবৃত্তি ও সাদৃশ্য নিরূপণ"),
                        Pair(7, "বিশ্লেষণমূলক অগ্রাধিকার স্কোর ও সাজেশন প্রস্তুত")
                    )

                    val currentStep = stage?.step ?: 0

                    steps.forEach { (num, desc) ->
                        val isDone = currentStep > num || stage is PdfProcessor.ProcessingStage.Completed
                        val isCurrent = currentStep == num && isProcessing

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when {
                                    isDone -> Icons.Default.CheckCircle
                                    isCurrent -> Icons.Default.RadioButtonUnchecked
                                    else -> Icons.Default.RadioButtonUnchecked
                                },
                                contentDescription = null,
                                tint = when {
                                    isDone -> EmeraldTertiary
                                    isCurrent -> GoldSecondary
                                    else -> MaterialTheme.colorScheme.outlineVariant
                                },
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = desc,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isDone || isCurrent) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
                                )
                            )
                        }
                    }

                    if (stage is PdfProcessor.ProcessingStage.Completed) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(EmeraldTertiary.copy(alpha = 0.15f))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "সফল! প্রশ্নপত্র সফলভাবে বিশ্লেষণ সম্পন্ন হয়েছে।",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = EmeraldTertiary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = onViewSuggestions,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("btn_view_imported_suggestions"),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldTertiary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.MenuBook, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("চূড়ান্ত সাজেশন ও প্রশ্নোত্তর দেখুন")
                        }
                    }

                    if (stage is PdfProcessor.ProcessingStage.Error) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.errorContainer)
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stage.errorMessage,
                                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.error)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}
