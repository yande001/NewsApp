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
import androidx.compose.runtime.*
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
import com.example.darren.newsapp.network.Api
import com.example.darren.newsapp.network.NewsManager
import com.example.darren.newsapp.screen.Categories
import com.example.darren.newsapp.screen.DetailScreen
import com.example.darren.newsapp.screen.Sources
import com.example.darren.newsapp.screen.TopNews

@Composable
fun NewsApp(mainViewModel: MainViewModel){
    val navController = rememberNavController()
    val scrollState = rememberScrollState()
    MainScreen(navController = navController , scrollState = scrollState, mainViewModel = mainViewModel)
}

@Composable
fun MainScreen(navController: NavHostController,
               scrollState: ScrollState,
               mainViewModel: MainViewModel
){
    Scaffold(bottomBar = {
        BottomMenu(navController = navController)
    }) {
        Navigation(navController = navController,
            scrollState = scrollState,
            paddingValues = it,
            viewModel = mainViewModel)
    }

}


@Composable
fun Navigation(navController: NavHostController,
               scrollState: ScrollState,
               newsManager: NewsManager = NewsManager(Api.retrofitService),
               paddingValues: PaddingValues,
               viewModel: MainViewModel
){
    val loading by viewModel.isLoading.collectAsState()
    val error by viewModel.isError.collectAsState()
    val articles = mutableListOf<TopNewsArticle>()
    val topArticles = viewModel.newsResponse.collectAsState().value.articles
    articles.addAll(topArticles?: listOf())

    Log.d("news","$articles")

    articles?.let {
        NavHost(navController = navController,
            startDestination = BottomMenuScreen.TopNews.route,
            modifier = Modifier.padding(paddingValues = paddingValues)
        ){
            val isLoading = mutableStateOf(loading)
            val isError = mutableStateOf(error)
            val queryState = mutableStateOf(viewModel.query.value)
            bottomNavigation(navController, articles, queryState, viewModel, isError = isError, isLoading = isLoading)
            composable("Detail/{index}",
                arguments = listOf(navArgument("index"){type = NavType.IntType})
            ){
                    navBackStackEntry ->
                val index = navBackStackEntry.arguments?.getInt("index")
                index?.let {
                    if(queryState.value != ""){
                        articles.clear()
                        articles.addAll(viewModel.getArticleByQuery.collectAsState().value.articles?: listOf())
                    } else{
                        articles.clear()
                        articles.addAll(viewModel.newsResponse.value.articles?: listOf())
                    }

                    val article = articles[index]
                    DetailScreen(navController = navController, article, scrollState)
                }

            }
        }
    }


}

fun NavGraphBuilder.bottomNavigation(navController: NavHostController,
                                     articles: List<TopNewsArticle>,
                                     query: MutableState<String>,
                                     viewModel: MainViewModel,
                                     isLoading: MutableState<Boolean>,
                                     isError: MutableState<Boolean>
){
    composable(BottomMenuScreen.TopNews.route){
        TopNews(navController = navController,
            articles,
            query = query,
            viewModel = viewModel,
            isLoading = isLoading,
            isError = isError
            )
    }
    composable(BottomMenuScreen.Categories.route){
        viewModel.getArticlesByCategory("business")
        viewModel.onSelectedCategoryChanged("business")

        Categories(viewModel = viewModel,
            onFetchCategory = {
            viewModel.onSelectedCategoryChanged(it)
            viewModel.getArticlesByCategory(it) },
            isLoading = isLoading,
            isError = isError
            )
    }
    composable(BottomMenuScreen.Sources.route){
        Sources(viewModel = viewModel,
            isLoading = isLoading,
            isError = isError)
    }
}