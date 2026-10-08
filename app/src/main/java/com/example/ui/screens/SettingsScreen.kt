package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AiProviderType
import com.example.data.model.AppLanguage
import com.example.data.model.AppThemeMode
import com.example.engine.BengaliNormalizer
import com.example.ui.theme.EmeraldTertiary
import com.example.ui.theme.NavyPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentLanguage: AppLanguage,
    currentTheme: AppThemeMode,
    currentAiProvider: AiProviderType,
    openRouterKey: String,
    geminiKey: String,
    onSetLanguage: (AppLanguage) -> Unit,
    onSetTheme: (AppThemeMode) -> Unit,
    onSetAiProvider: (AiProviderType) -> Unit,
    onSetOpenRouterKey: (String) -> Unit,
    onSetGeminiKey: (String) -> Unit,
    onResetDatabase: () -> Unit
) {
    val context = LocalContext.current
    var tempOpenRouterKey by remember { mutableStateOf(openRouterKey) }
    var tempGeminiKey by remember { mutableStateOf(geminiKey) }
    var isOpenRouterKeyVisible by remember { mutableStateOf(false) }
    var isGeminiKeyVisible by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var speechSpeed by remember { mutableFloatStateOf(1.0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("settings_screen")
    ) {
        Text(
            text = "সেটিংস ও কনফিগারেশন",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            text = "অ্যাপের ভাষা, থিম, অডিও এবং এআই প্রোভাইডার পছন্দসমূহ নির্ধারণ করুন",
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 1. Language Selection
        SettingsSectionCard(title = "ভাষা নির্বাচন (Language)", icon = Icons.Default.Language) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppLanguage.entries.forEach { lang ->
                    FilterChip(
                        selected = currentLanguage == lang,
                        onClick = { onSetLanguage(lang) },
                        label = { Text(lang.displayName) },
                        modifier = Modifier.testTag("lang_${lang.code}")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 2. Theme Selection
        SettingsSectionCard(title = "অ্যাপ থিম (Theme)", icon = Icons.Default.Brightness4) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppThemeMode.entries.forEach { mode ->
                    FilterChip(
                        selected = currentTheme == mode,
                        onClick = { onSetTheme(mode) },
                        label = { Text(mode.displayName) },
                        modifier = Modifier.testTag("theme_${mode.code}")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3. Audio & TTS Speed
        SettingsSectionCard(title = "অডিও পাঠ গতি (Audio TTS Speed)", icon = Icons.Default.Speed) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("গতি: ${String.format("%.2f", speechSpeed)}x", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        if (speechSpeed == 1.0f) "স্বাভাবিক" else if (speechSpeed > 1.0f) "দ্রুত" else "ধীর",
                        style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.primary)
                    )
                }
                Slider(
                    value = speechSpeed,
                    onValueChange = { speechSpeed = it },
                    valueRange = 0.75f..1.5f,
                    steps = 5,
                    modifier = Modifier.testTag("slider_tts_speed")
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 4. AI Provider Configuration
        SettingsSectionCard(title = "এআই প্রোভাইডার ও মডেল (AI Engine)", icon = Icons.Default.AutoAwesome) {
            Column {
                Text(
                    text = "প্রশ্নের উত্তর প্রস্তুতকরণ ও ব্যাখ্যার জন্য কাঙ্ক্ষিত AI ইঞ্জিন নির্বাচন করুন:",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AiProviderType.entries.forEach { provider ->
                        FilterChip(
                            selected = currentAiProvider == provider,
                            onClick = { onSetAiProvider(provider) },
                            label = { Text(provider.displayName, fontSize = 12.sp) },
                            modifier = Modifier.testTag("ai_provider_${provider.code}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Gemini Key
                OutlinedTextField(
                    value = tempGeminiKey,
                    onValueChange = { tempGeminiKey = it },
                    label = { Text("Google Gemini API Key") },
                    placeholder = { Text("AI Studio ইনজেক্টেড কী অথবা নিজস্ব কী") },
                    singleLine = true,
                    visualTransformation = if (isGeminiKeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { isGeminiKeyVisible = !isGeminiKeyVisible }) {
                            Icon(if (isGeminiKeyVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = null)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_gemini_key")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // OpenRouter Key
                OutlinedTextField(
                    value = tempOpenRouterKey,
                    onValueChange = { tempOpenRouterKey = it },
                    label = { Text("OpenRouter API Key") },
                    placeholder = { Text("sk-or-v1-...") },
                    singleLine = true,
                    visualTransformation = if (isOpenRouterKeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { isOpenRouterKeyVisible = !isOpenRouterKeyVisible }) {
                            Icon(if (isOpenRouterKeyVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = null)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_openrouter_key")
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        onSetGeminiKey(tempGeminiKey)
                        onSetOpenRouterKey(tempOpenRouterKey)
                        Toast.makeText(context, "API কী সফলভাবে সংরক্ষিত হয়েছে", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_save_api_keys"),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("API সেটিংস সেভ করুন")
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 5. Academic Rule & Ethics Notice
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = NavyPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "একাডেমিক সততা ও নিয়মাবলি (Academic Rule)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "অ্যাপে প্রদর্শিত প্রতিটি স্কোর বিগত শিক্ষাবর্ষসমূহের প্রশ্নপত্র বিশ্লেষণের গাণিতিক অগ্রাধিকার স্কোর। কোনো পরীক্ষার্থীকে বিভ্রান্ত না করার স্বার্থে কখনোই ‘১০০% আসবেই’ বা ‘অবশ্যই কমন’ জাতীয় অলীক দাবি করা হয় না। এটি শিক্ষার্থীদের নিয়মমাফিক ও সুবিন্যস্ত প্রস্তুতির সহায়িকা।",
                    style = MaterialTheme.typography.bodySmall.copy(
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 6. Reset Database Action
        SettingsSectionCard(title = "ডাটা রিসেট ও মেরামত (Database)", icon = Icons.Default.DeleteForever) {
            Column {
                Text(
                    text = "প্রয়োজনে সম্পূর্ণ ডাটাবেজ মুছে মূল প্রশ্নব্যাংক পুনরায় লোড করতে পারেন।",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = { showResetDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_reset_database")
                ) {
                    Text("মূল প্রশ্নব্যাংকে রিসেট করুন")
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("ডাটাবেজ রিসেট নিশ্চিতকরণ") },
            text = { Text("আপনি কি নিশ্চিত যে সকল সংরক্ষিত তথ্য রিসেট করে জাতীয় বিশ্ববিদ্যালয়ের মূল সাজেশনে ফিরে যেতে চান?") },
            confirmButton = {
                Button(onClick = {
                    onResetDatabase()
                    showResetDialog = false
                    Toast.makeText(context, "ডাটাবেজ সফলভাবে রিসেট হয়েছে", Toast.LENGTH_SHORT).show()
                }) {
                    Text("হ্যাঁ, রিসেট করুন")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("বাতিল")
                }
            }
        )
    }
}

@Composable
private fun SettingsSectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}
