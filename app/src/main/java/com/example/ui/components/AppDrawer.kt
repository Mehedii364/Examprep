package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PriorityLevel
import com.example.data.model.QuestionSection
import com.example.ui.AppScreen

@Composable
fun AppDrawer(
    currentScreen: AppScreen,
    onSelectScreen: (AppScreen) -> Unit,
    onSelectSection: (QuestionSection) -> Unit,
    onSelectRepeated: () -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier
            .fillMaxHeight()
            .width(320.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "রাষ্ট্রবিজ্ঞান ২য় পত্র",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "সাজেশন ও প্রশ্নব্যাংক প্রো (১১১৯০৩)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "জাতীয় বিশ্ববিদ্যালয় • ডিগ্রি ও অনার্স ১ম বর্ষ",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f)
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            DrawerSectionHeader("মূল মেনু")

            DrawerItem(
                icon = Icons.Default.Home,
                label = "মূলপাতা (Home)",
                selected = currentScreen == AppScreen.HOME,
                onClick = { onSelectScreen(AppScreen.HOME); onCloseDrawer() }
            )

            DrawerItem(
                icon = Icons.Default.UploadFile,
                label = "পিডিএফ আমদানি (Import PDF)",
                selected = currentScreen == AppScreen.IMPORT_PDF,
                onClick = { onSelectScreen(AppScreen.IMPORT_PDF); onCloseDrawer() }
            )

            DrawerItem(
                icon = Icons.Default.Description,
                label = "আমার প্রশ্নব্যাংক (My PDFs)",
                selected = false,
                onClick = { onSelectScreen(AppScreen.IMPORT_PDF); onCloseDrawer() }
            )

            DrawerItem(
                icon = Icons.Default.History,
                label = "সাম্প্রতিক বিশ্লেষণ (Recent Analysis)",
                selected = false,
                onClick = { onSelectScreen(AppScreen.PROGRESS); onCloseDrawer() }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
            DrawerSectionHeader("বিভাগভিত্তিক প্রশ্নাবলি")

            DrawerItem(
                icon = Icons.Default.MenuBook,
                label = "সকল সাজেশন (All Suggestions)",
                selected = currentScreen == AppScreen.SUGGESTIONS,
                onClick = { onSelectScreen(AppScreen.SUGGESTIONS); onCloseDrawer() }
            )

            DrawerItem(
                icon = Icons.Default.CheckCircle,
                label = "বহুনির্বাচনি (MCQ Master)",
                selected = false,
                onClick = { onSelectSection(QuestionSection.MCQ); onCloseDrawer() }
            )

            DrawerItem(
                icon = Icons.Default.Description,
                label = "ক-বিভাগ (অতি সংক্ষিপ্ত প্রশ্নাবলি)",
                selected = false,
                onClick = { onSelectSection(QuestionSection.KA); onCloseDrawer() }
            )

            DrawerItem(
                icon = Icons.Default.Description,
                label = "খ-বিভাগ (সংক্ষিপ্ত প্রশ্নাবলি)",
                selected = false,
                onClick = { onSelectSection(QuestionSection.KHA); onCloseDrawer() }
            )

            DrawerItem(
                icon = Icons.Default.Description,
                label = "গ-বিভাগ (রচনামূলক প্রশ্নাবলি)",
                selected = false,
                onClick = { onSelectSection(QuestionSection.GA); onCloseDrawer() }
            )

            DrawerItem(
                icon = Icons.Default.Repeat,
                label = "সর্বাধিক পুনরাবৃত্ত প্রশ্ন (Repeated)",
                selected = false,
                onClick = { onSelectRepeated(); onCloseDrawer() }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
            DrawerSectionHeader("অধ্যয়ন ও এক্সপোর্ট")

            DrawerItem(
                icon = Icons.Default.Bookmark,
                label = "বুকমার্ক সংগ্রহ (Bookmarks)",
                selected = currentScreen == AppScreen.BOOKMARKS,
                onClick = { onSelectScreen(AppScreen.BOOKMARKS); onCloseDrawer() }
            )

            DrawerItem(
                icon = Icons.Default.FileDownload,
                label = "ডাউনলোড ও প্রিন্ট (Downloads & Print)",
                selected = currentScreen == AppScreen.DOWNLOADS,
                onClick = { onSelectScreen(AppScreen.DOWNLOADS); onCloseDrawer() }
            )

            DrawerItem(
                icon = Icons.Default.Headphones,
                label = "অডিও রিডার সেটিংস (Audio TTS)",
                selected = false,
                onClick = { onSelectScreen(AppScreen.SETTINGS); onCloseDrawer() }
            )

            DrawerItem(
                icon = Icons.Default.AutoAwesome,
                label = "এআই কনফিগারেশন (AI Settings)",
                selected = false,
                onClick = { onSelectScreen(AppScreen.SETTINGS); onCloseDrawer() }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
            DrawerSectionHeader("অ্যাপ ও সাহায্য")

            DrawerItem(
                icon = Icons.Default.Settings,
                label = "সেটিংস (Settings)",
                selected = currentScreen == AppScreen.SETTINGS,
                onClick = { onSelectScreen(AppScreen.SETTINGS); onCloseDrawer() }
            )

            DrawerItem(
                icon = Icons.Default.Security,
                label = "গোপনীয়তা ও শর্তাবলি (Privacy & Terms)",
                selected = false,
                onClick = { onSelectScreen(AppScreen.SETTINGS); onCloseDrawer() }
            )

            DrawerItem(
                icon = Icons.Default.Info,
                label = "অ্যাপ সম্পর্কিত (About)",
                selected = false,
                onClick = { onSelectScreen(AppScreen.SETTINGS); onCloseDrawer() }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DrawerSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
        ),
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
    )
}

@Composable
private fun DrawerItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = { Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp)) },
        label = { Text(label, fontSize = 13.5.sp) },
        selected = selected,
        onClick = onClick,
        modifier = Modifier
            .padding(vertical = 2.dp)
            .testTag("drawer_item_${label.take(8).trim()}"),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            selectedIconColor = MaterialTheme.colorScheme.primary
        )
    )
}
