package com.example.darren.newsapp.models

import com.example.darren.newsapp.models.ArticleCategory.*

enum class ArticleCategory(val categoryName:String) {
    BUSINESS("business"),
    ENTERTAINMENT("entertainment"),
    GENERAL("general"),
    HEALTH("health"),
    SCIENCE("science"),
    SPORTS("sports"),
    TECHNOLOGY("technology")
}
fun getAllArticleCategory():List<ArticleCategory>{
    return listOf(
        ArticleCategory.BUSINESS,
        ArticleCategory.ENTERTAINMENT,
        ArticleCategory.GENERAL,
        ArticleCategory.HEALTH,
        ArticleCategory.SCIENCE,
        ArticleCategory.SPORTS,
        ArticleCategory.TECHNOLOGY
    )
}

fun getArticleCategory(category: String):ArticleCategory?{
    val map= values().associateBy(ArticleCategory::categoryName)
    return map[category]
}