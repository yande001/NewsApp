package com.example.darren.newsapp.data

import com.example.darren.newsapp.network.NewsManager

class Repository(val manager: NewsManager) {
    suspend fun getArticles() = manager.getArticles("us")
    suspend fun getArticlesByCategory(category: String) = manager.getArticlesByCategory(category)
    suspend fun getArticlesBySource(source: String) = manager.getArticlesBySource(source)
    suspend fun getSearchedArticles(query: String) = manager.getSearchedArticle(query)
}