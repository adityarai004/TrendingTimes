package com.example.trendingtimes.data

data class NewsResponse(
    val totalResults: Int,
    val totalPages: Int,
    val currentPage: Int,
    val articles: List<Article>,
)