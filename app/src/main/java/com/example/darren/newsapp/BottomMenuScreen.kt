package com.example.darren.newsapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomMenuScreen(
    val route: String,
    val icon: ImageVector,
    val title: String
){
    object TopNews: BottomMenuScreen("top news", Icons.Outlined.Home, "Top News")
    object Categories: BottomMenuScreen("categories", Icons.Outlined.Category, "Top News")
    object Sources: BottomMenuScreen("sources", Icons.Outlined.Folder, "Sources")

}
