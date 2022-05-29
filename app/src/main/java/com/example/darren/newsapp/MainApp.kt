package com.example.darren.newsapp

import android.app.Application
import com.example.darren.newsapp.data.Repository
import com.example.darren.newsapp.network.Api
import com.example.darren.newsapp.network.NewsManager

class MainApp: Application() {
    private val manager by lazy{
        NewsManager(Api.retrofitService)
    }
    val repository by lazy{
        Repository(manager = manager)
    }
}