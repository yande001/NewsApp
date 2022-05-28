package com.example.darren.newsapp

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.darren.newsapp.component.BottomMenu
import com.example.darren.newsapp.models.TopNewsArticle
import com.example.darren.newsapp.network.NewsManager
import com.example.darren.newsapp.screen.Categories
import com.example.darren.newsapp.screen.DetailScreen
import com.example.darren.newsapp.screen.Sources
import com.example.darren.newsapp.screen.TopNews

@Composable
fun NewsApp(){
    val navController = rememberNavController()
    val scrollState = rememberScrollState()
    MainScreen(navController = navController , scrollState = scrollState )
}

@Composable
fun MainScreen(navController: NavHostController, scrollState: ScrollState){
    Scaffold(bottomBar = {
        BottomMenu(navController = navController)
    }) {
        Navigation(navController, scrollState, paddingValues = it)
    }

}


@Composable
fun Navigation(navController: NavHostController,
               scrollState: ScrollState,
               newsManager: NewsManager = NewsManager(),
               paddingValues: PaddingValues
){
    val articles = newsManager.newsResponse.value.articles
    Log.d("news","$articles")

    articles?.let {
        NavHost(navController = navController,
            startDestination = BottomMenuScreen.TopNews.route,
            modifier = Modifier.padding(paddingValues = paddingValues)
        ){

            bottomNavigation(navController, articles, newsManager = newsManager)
            composable("Detail/{index}",
                arguments = listOf(navArgument("index"){type = NavType.IntType})
            ){
                    navBackStackEntry ->
                val index = navBackStackEntry.arguments?.getInt("index")
                index?.let {
                    val article = articles[index]
                    DetailScreen(navController = navController, article, scrollState)
                }

            }
        }
    }


}

fun NavGraphBuilder.bottomNavigation(navController: NavHostController,
                                     articles: List<TopNewsArticle>,
                                     newsManager: NewsManager
){
    composable(BottomMenuScreen.TopNews.route){
        TopNews(navController = navController, articles)
    }
    composable(BottomMenuScreen.Categories.route){
        newsManager.getArticlesByCategory("business")
        newsManager.onSelectedCategoryChanged("business")

        Categories(newsManager = newsManager, onFetchCategory = {
            newsManager.onSelectedCategoryChanged(it)
            newsManager.getArticlesByCategory(it)
        })
    }
    composable(BottomMenuScreen.Sources.route){
        Sources(newsManager)
    }
}

@Composable
fun SourceContent(articles: List<TopNewsArticle>){
    val uriHandler = LocalUriHandler.current

    LazyColumn{
        items(articles){
            article ->
            val annotatedString = buildAnnotatedString {
                pushStringAnnotation(
                    tag = "URL",
                    annotation = article.url ?: "newsapi.org"
                )
                withStyle(style = SpanStyle(
                    color = colorResource(id = R.color.purple_500),
                    textDecoration = TextDecoration.Underline)
                    )
                {
                    append("Read Full Article Here")
                }
            }
            Card(
                backgroundColor = colorResource(id = R.color.purple_700),
                elevation = 6.dp,
                modifier = Modifier.padding(8.dp)
                ) {
                Column(modifier = Modifier
                    .height(200.dp)
                    .padding(end = 8.dp, start = 8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                    ) {
                    Text(
                        text = article.title?: "Not Available",
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                        )
                    Text(
                        text = article.description?: "Not Available",
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis)
                    Card(backgroundColor = Color.White, elevation = 6.dp) {
                        ClickableText(text = annotatedString, onClick = {
                            annotatedString.getStringAnnotations(it,it).firstOrNull()?.let {
                                result ->
                                if(result.tag == "URL"){
                                    uriHandler.openUri(result.item)
                                }
                            }
                        })
                    }
                    
                }
                
            }
            

        }
    }
}