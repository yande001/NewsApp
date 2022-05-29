package com.example.darren.newsapp.network

import com.example.darren.newsapp.models.TopNewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    @GET("top-headlines")
    suspend fun getTopArticles(@Query("country") country: String,
    ): TopNewsResponse

    @GET("top-headlines")
    suspend fun getArticlesByCategory(@Query("category") category: String,
    ): TopNewsResponse

    @GET("everything")
    suspend fun getArticlesBySources(@Query("sources") source: String
    ): TopNewsResponse

    @GET("everything")
    suspend fun getArticlesByQuery(@Query("q") query: String
    ): TopNewsResponse

}