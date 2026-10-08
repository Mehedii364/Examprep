package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.ui.AppScreen

@Composable
fun AppBottomNav(
    currentScreen: AppScreen,
    onSelectScreen: (AppScreen) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentScreen == AppScreen.HOME,
            onClick = { onSelectScreen(AppScreen.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = "মূলপাতা") },
            label = { Text("মূলপাতা") },
            modifier = Modifier.testTag("nav_home")
        )
        NavigationBarItem(
            selected = currentScreen == AppScreen.SUGGESTIONS,
            onClick = { onSelectScreen(AppScreen.SUGGESTIONS) },
            icon = { Icon(Icons.Default.MenuBook, contentDescription = "সাজেশন") },
            label = { Text("সাজেশন") },
            modifier = Modifier.testTag("nav_suggestions")
        )
        NavigationBarItem(
            selected = currentScreen == AppScreen.BOOKMARKS,
            onClick = { onSelectScreen(AppScreen.BOOKMARKS) },
            icon = { Icon(Icons.Default.Bookmark, contentDescription = "বুকমার্ক") },
            label = { Text("বুকমার্ক") },
            modifier = Modifier.testTag("nav_bookmarks")
        )
        NavigationBarItem(
            selected = currentScreen == AppScreen.DOWNLOADS,
            onClick = { onSelectScreen(AppScreen.DOWNLOADS) },
            icon = { Icon(Icons.Default.FileDownload, contentDescription = "ডাউনলোড") },
            label = { Text("ডাউনলোড") },
            modifier = Modifier.testTag("nav_downloads")
        )
        NavigationBarItem(
            selected = currentScreen == AppScreen.SETTINGS,
            onClick = { onSelectScreen(AppScreen.SETTINGS) },
            icon = { Icon(Icons.Default.Settings, contentDescription = "সেটিংস") },
            label = { Text("সেটিংস") },
            modifier = Modifier.testTag("nav_settings")
        )
    }
}
