package com.example.darren.newsapp

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.darren.newsapp.models.ArticleCategory
import com.example.darren.newsapp.models.TopNewsResponse
import com.example.darren.newsapp.models.getArticleCategory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val repository = getApplication<MainApp>().repository

    private val _newsResponse = MutableStateFlow(TopNewsResponse())
    val newsResponse: StateFlow<TopNewsResponse>
    get() = _newsResponse

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean>
    get() = _isError

    val errorHandler = CoroutineExceptionHandler{
        _, error ->
        if(error is Exception){
            _isError.value = true
        }
    }

    fun getArticles(){
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            _newsResponse.value = repository.getArticles()
            _isLoading.value = false
        }

    }

    private val _getArticleByCategory = MutableStateFlow(TopNewsResponse())
    val getArticleByCategory: StateFlow<TopNewsResponse>
    get() = _getArticleByCategory

    private val _selectedCategory :MutableStateFlow<ArticleCategory?> = MutableStateFlow(null)
    val selectedCategory: StateFlow<ArticleCategory?>
        get() = _selectedCategory

    fun getArticlesByCategory(category: String){
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            _getArticleByCategory.value = repository.getArticlesByCategory(category)
            _isLoading.value = false
        }

    }

    fun onSelectedCategoryChanged(category: String){
        val newCategory = getArticleCategory(category)
        _selectedCategory.value = newCategory
    }

    val sourceName = MutableStateFlow("techcrunch")

    private val _getArticleBySource = MutableStateFlow(TopNewsResponse())
    val getArticleBySource: StateFlow<TopNewsResponse>
    get() = _getArticleBySource

    fun getArticlesBySource(){
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            _getArticleBySource.value = repository.getArticlesBySource(sourceName.value)
            _isLoading.value = false
        }

    }

    val query = MutableStateFlow("")

    private val _getArticleByQuery = MutableStateFlow(TopNewsResponse())
    val getArticleByQuery: StateFlow<TopNewsResponse>
        get() = _getArticleByQuery

    fun getArticlesByQuery(query: String){
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            _newsResponse.value = repository.getSearchedArticles(query)
            _isLoading.value = false
        }

    }
}