package com.example.trendingtimes.model.remote

data class NewsResponse(
    val totalResults: Int,
    val totalPages: Int,
    val currentPage: Int,
    val articles: List<Article>,
)