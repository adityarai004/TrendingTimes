package com.example.trendingtimes.model.remote

data class Feedback(
    val feedbackType: String,
    val categoryType: String,
    val severity: String,
    val explanation: String
)
