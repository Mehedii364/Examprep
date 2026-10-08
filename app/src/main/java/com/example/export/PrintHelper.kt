package com.example.export

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import com.example.data.model.QuestionItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object PrintHelper {

    fun printSuggestions(activity: Activity, questions: List<QuestionItem>, jobName: String = "Political Science 2nd Paper Suggestions") {
        val printManager = activity.getSystemService(Context.PRINT_SERVICE) as? PrintManager ?: return

        CoroutineScope(Dispatchers.IO).launch {
            val exportResult = PdfExporter.generateSuggestionPdf(
                context = activity,
                title = "রাষ্ট্রবিজ্ঞান ২য় পত্র সাজেশন",
                questions = questions,
                exportTypeLabel = "প্রিন্ট সংস্করণ"
            )

            val generatedFile = File(exportResult.filePath)
            if (!generatedFile.exists()) return@launch

            activity.runOnUiThread {
                printManager.print(
                    jobName,
                    object : PrintDocumentAdapter() {
                        override fun onLayout(
                            oldAttributes: PrintAttributes?,
                            newAttributes: PrintAttributes?,
                            cancellationSignal: CancellationSignal?,
                            callback: LayoutResultCallback?,
                            extras: Bundle?
                        ) {
                            if (cancellationSignal?.isCanceled == true) {
                                callback?.onLayoutCancelled()
                                return
                            }
                            val pdi = PrintDocumentInfo.Builder("Suggestion.pdf")
                                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                                .build()
                            callback?.onLayoutFinished(pdi, true)
                        }

                        override fun onWrite(
                            pages: Array<out PageRange>?,
                            destination: ParcelFileDescriptor?,
                            cancellationSignal: CancellationSignal?,
                            callback: WriteResultCallback?
                        ) {
                            try {
                                val input = FileInputStream(generatedFile)
                                val output = FileOutputStream(destination?.fileDescriptor)
                                input.copyTo(output)
                                input.close()
                                output.close()
                                callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                            } catch (e: Exception) {
                                callback?.onWriteFailed(e.message)
                            }
                        }
                    },
                    PrintAttributes.Builder()
                        .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                        .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                        .build()
                )
            }
        }
    }
}
