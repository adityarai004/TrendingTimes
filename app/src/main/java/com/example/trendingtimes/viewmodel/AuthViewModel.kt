package com.example.trendingtimes.viewmodel

import androidx.lifecycle.ViewModel
import com.example.trendingtimes.util.Resource
import com.example.trendingtimes.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel(){

    suspend fun login(email:String, password:String):Boolean{
        val result = authRepository.login(email, password)
        return result is Resource.Success
    }

    suspend fun signUp(email:String, password:String): Boolean {
        val result = authRepository.signup(email, password)
        return result is Resource.Success
    }

    fun getCurrentUser(): FirebaseUser? {
        return authRepository.getCurrentUser()
    }

    fun logOut(){
        authRepository.logout()
    }
}