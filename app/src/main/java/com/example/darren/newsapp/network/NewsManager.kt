package com.example.darren.newsapp.network

import android.util.Log
import androidx.compose.runtime.*
import com.example.darren.newsapp.models.ArticleCategory
import com.example.darren.newsapp.models.TopNewsResponse
import com.example.darren.newsapp.models.getArticleCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsManager (private val service: NewsService){
    private val _newsResponse = mutableStateOf(TopNewsResponse())
    val newsResponse: State<TopNewsResponse>
        @Composable get() = remember{
            _newsResponse
        }

    private val _getArticleByCategory = mutableStateOf(TopNewsResponse())
    val getArticleByCategory: MutableState<TopNewsResponse>
        @Composable get() = remember {
            _getArticleByCategory
        }

    private val _getArticleBySource = mutableStateOf(TopNewsResponse())
    val getArticleBySource: MutableState<TopNewsResponse>
        @Composable get() = remember {
            _getArticleBySource
        }

    private val _searchNewsResponse = mutableStateOf(TopNewsResponse())
    val searchNewsResponse: MutableState<TopNewsResponse>
        @Composable get() = remember {
            _searchNewsResponse
        }


    val selectedCategory: MutableState<ArticleCategory?> = mutableStateOf(null)

    val sourceName = mutableStateOf("abc-news")

    val query = mutableStateOf("")

    suspend fun getArticles(country: String): TopNewsResponse
    = withContext(Dispatchers.IO)
    {
        service.getTopArticles(country = country)
    }

    suspend fun getArticlesByCategory(category: String): TopNewsResponse
    = withContext(Dispatchers.IO)
    {
        service.getArticlesByCategory(category)
    }

    suspend fun getArticlesBySource(source: String): TopNewsResponse
    = withContext(Dispatchers.IO)
    {
        service.getArticlesBySources(source)
    }

    suspend fun getSearchedArticle(query: String): TopNewsResponse
    = withContext(Dispatchers.IO)
    {
        service.getArticlesByQuery(query)
    }

    fun onSelectedCategoryChanged(category: String){
        val newCategory = getArticleCategory(category = category)
        selectedCategory.value = newCategory
    }
}