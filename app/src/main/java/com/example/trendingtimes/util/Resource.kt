package com.example.trendingtimes.util

sealed class Resource<out T> {
    data class Success<out T>(val result: T) : Resource<T>()
    data class Failure(val error: String?) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}