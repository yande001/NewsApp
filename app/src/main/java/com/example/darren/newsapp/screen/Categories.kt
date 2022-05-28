package com.example.darren.newsapp.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.darren.newsapp.R
import com.example.darren.newsapp.models.getAllArticleCategory
import com.example.darren.newsapp.network.NewsManager

@Composable
fun Categories(onFetchCategory: (String) -> Unit ={}, newsManager: NewsManager){
    val tabsItems = getAllArticleCategory()
    Column() {
        LazyRow{
            items(tabsItems.size){
                val category = tabsItems[it]
                CategoryTab(category = category.categoryName,
                    onFetchCategory = onFetchCategory,
                    isSelected = newsManager.selectedCategory.value == category
                    )
            }
        }
    }
}

@Composable
fun CategoryTab(category: String,
                isSelected: Boolean = false,
                onFetchCategory: (String) -> Unit){
    val background = if(isSelected) colorResource(id = R.color.purple_200)
                        else colorResource(id = R.color.purple_700)
    Surface(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 16.dp)
            .clickable {
                onFetchCategory(category)
            },
        shape = MaterialTheme.shapes.small,
        color = background
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.body2,
            color = Color.White,
            modifier = Modifier.padding(8.dp)
            )

    }
}