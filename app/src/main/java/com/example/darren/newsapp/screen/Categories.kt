package com.example.darren.newsapp.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.darren.newsapp.MockData
import com.example.darren.newsapp.MockData.getTimeAgo
import com.example.darren.newsapp.NewsData
import com.example.darren.newsapp.R
import com.example.darren.newsapp.models.TopNewsArticle
import com.example.darren.newsapp.models.getAllArticleCategory
import com.example.darren.newsapp.network.NewsManager
import com.skydoves.landscapist.coil.CoilImage

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
        CategoryContent(articles = newsManager.getArticleByCategory.value.articles?: listOf())

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

@Composable
fun CategoryContent(articles: List<TopNewsArticle>, modifier: Modifier = Modifier){
    LazyColumn() {
        items(articles){
            article->
            Card(modifier.padding(8.dp),
                border = BorderStroke(2.dp, color = colorResource(id = R.color.purple_500)))
            {
                Row(modifier.padding(8.dp)) {
                    CoilImage(
                        imageModel = article.urlToImage,
                        modifier = Modifier.size(100.dp),
                        placeHolder = painterResource(id = R.drawable.breaking_news),
                        error = painterResource(id = R.drawable.breaking_news)
                    )
                    Column(modifier.padding(8.dp)) {
                        Text(text = article.title ?: "Not Available",
                            fontWeight = FontWeight.Bold,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row (
                            modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(text = article.author?: "Not Available")
                            Text(text = MockData.stringToDate(
                                article.publishedAt?:"2022-05-25T12:00:00Z").getTimeAgo())
                        }
                    }

                }
                
            }
        }
    }
}

@Preview
@Composable
fun CategoryContentPreview(){
    CategoryContent(
        listOf(TopNewsArticle(
        author = "Mike Florio",
        title = "Aaron Rodgers violated COVID protocol by doing maskless indoor press conferences - NBC Sports",
        description = "Packers quarterback Aaron Rodgers has been conducting in-person press conferences in the Green Bay facility without wearing a mask. Because he was secretly unvaccinated, Rodgers violated the rules.",
        publishedAt = "2021-11-04T03:21:00Z"
    ))
    )
}