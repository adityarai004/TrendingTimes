package com.example.trendingtimes.model.remote

data class User(
    val name: String,
    val email: String,
    val gender: String, // Assuming this represents male or female,
    var imageUrl: String? = null,
    var dob: String
)