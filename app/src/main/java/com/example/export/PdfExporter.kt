package com.example.export

import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.data.model.QuestionItem
import com.example.engine.BengaliNormalizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfExporter {

    data class ExportResult(
        val success: Boolean,
        val filePath: String,
        val fileName: String,
        val fileSizeFormatted: String,
        val message: String
    )

    suspend fun generateSuggestionPdf(
        context: Context,
        title: String,
        questions: List<QuestionItem>,
        exportTypeLabel: String
    ): ExportResult = withContext(Dispatchers.IO) {
        val document = PdfDocument()
        val pageWidth = 595 // A4 standard width in points (72 dpi)
        val pageHeight = 842 // A4 standard height
        val margin = 40f
        val contentWidth = pageWidth - (2 * margin)

        val titlePaint = Paint().apply {
            color = Color.rgb(30, 58, 138) // Deep Blue
            textSize = 18f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }

        val subtitlePaint = Paint().apply {
            color = Color.rgb(75, 85, 99)
            textSize = 11f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            isAntiAlias = true
        }

        val headerBadgePaint = Paint().apply {
            color = Color.rgb(220, 38, 38)
            textSize = 10f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }

        val questionPaint = Paint().apply {
            color = Color.rgb(17, 24, 39)
            textSize = 12f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }

        val bodyPaint = Paint().apply {
            color = Color.rgb(55, 65, 81)
            textSize = 10.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            isAntiAlias = true
        }

        val metaPaint = Paint().apply {
            color = Color.rgb(107, 114, 128)
            textSize = 9f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            isAntiAlias = true
        }

        val linePaint = Paint().apply {
            color = Color.rgb(229, 231, 235)
            strokeWidth = 1f
        }

        var currentPageNumber = 1
        var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPageNumber).create()
        var page = document.startPage(pageInfo)
        var canvas = page.canvas
        var currentY = margin + 20f

        // 1. Cover / Top Header
        canvas.drawText("জাতীয় বিশ্ববিদ্যালয় • রাষ্ট্রবিজ্ঞান ২য় পত্র", margin, currentY, titlePaint)
        currentY += 20f
        canvas.drawText("চূড়ান্ত সাজেশন ও বিগত বর্ষের প্রশ্নব্যাংক বিশ্লেষণ ($exportTypeLabel)", margin, currentY, subtitlePaint)
        currentY += 16f
        val dateStr = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault()).format(Date())
        canvas.drawText("প্রস্তুতকরণ তারিখ: $dateStr | মোট প্রশ্ন: ${questions.size} টি", margin, currentY, metaPaint)
        currentY += 15f
        canvas.drawLine(margin, currentY, pageWidth - margin, currentY, linePaint)
        currentY += 25f

        // Loop through questions and write
        for ((index, item) in questions.withIndex()) {
            val qHeader = "[প্রশ্ন ${BengaliNormalizer.toBengaliDigits((index + 1).toString())}] ${item.section.bnLabel} • ${item.topic}"
            val qPriority = "${item.priorityLevel.bnLabel} • স্কোর: ${item.priorityScore}%"

            // Estimate required height for question item
            val estimatedHeight = 120f
            if (currentY + estimatedHeight > pageHeight - margin - 30f) {
                // Draw footer
                canvas.drawText("পৃষ্ঠা $currentPageNumber", pageWidth / 2f - 20f, pageHeight - margin + 10f, metaPaint)
                document.finishPage(page)

                currentPageNumber++
                pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPageNumber).create()
                page = document.startPage(pageInfo)
                canvas = page.canvas
                currentY = margin + 20f

                // Mini running header
                canvas.drawText("রাষ্ট্রবিজ্ঞান ২য় পত্র সাজেশন (চলমান)", margin, currentY, subtitlePaint)
                currentY += 15f
                canvas.drawLine(margin, currentY, pageWidth - margin, currentY, linePaint)
                currentY += 20f
            }

            canvas.drawText(qHeader, margin, currentY, headerBadgePaint)
            currentY += 16f
            canvas.drawText(item.questionText.take(65), margin, currentY, questionPaint)
            if (item.questionText.length > 65) {
                currentY += 14f
                canvas.drawText(item.questionText.drop(65).take(65), margin, currentY, questionPaint)
            }
            currentY += 16f

            // Year display & repetition
            canvas.drawText("${item.yearDisplay} | ${item.repeatType.bnLabel}", margin, currentY, metaPaint)
            currentY += 15f

            // Answer snippet
            val ansPreview = "উত্তর: " + item.answerText.replace("\n", " ").take(130) + (if (item.answerText.length > 130) "..." else "")
            canvas.drawText(ansPreview.take(75), margin, currentY, bodyPaint)
            if (ansPreview.length > 75) {
                currentY += 14f
                canvas.drawText(ansPreview.drop(75).take(75), margin, currentY, bodyPaint)
            }
            currentY += 15f

            canvas.drawText(qPriority, margin, currentY, subtitlePaint)
            currentY += 18f
            canvas.drawLine(margin, currentY, pageWidth - margin, currentY, linePaint)
            currentY += 22f
        }

        // Draw last page footer
        canvas.drawText("পৃষ্ঠা $currentPageNumber", pageWidth / 2f - 20f, pageHeight - margin + 10f, metaPaint)
        document.finishPage(page)

        // Save PDF to App Storage / Downloads
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val fileName = "PoliSci_2nd_Paper_Suggestion_$timestamp.pdf"
        var finalFile: File? = null

        val outputDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Suggestions").apply {
            if (!exists()) mkdirs()
        }
        val file = File(outputDir, fileName)
        FileOutputStream(file).use { out ->
            document.writeTo(out)
        }
        document.close()
        finalFile = file

        // Also save to Public MediaStore / Downloads if available so user finds it in Files app
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/PoliticalScience")
                }
                val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                if (uri != null) {
                    context.contentResolver.openOutputStream(uri)?.use { outStream ->
                        file.inputStream().use { inStream ->
                            inStream.copyTo(outStream)
                        }
                    }
                }
            }
        } catch (_: Exception) {
            // Internal file is always preserved safely
        }

        val sizeBytes = finalFile.length()
        val sizeFormatted = "${sizeBytes / 1024} KB"

        ExportResult(
            success = true,
            filePath = finalFile.absolutePath,
            fileName = fileName,
            fileSizeFormatted = sizeFormatted,
            message = "পিডিএফ সফলভাবে তৈরি হয়েছে এবং সেভ করা হয়েছে!"
        )
    }
}
