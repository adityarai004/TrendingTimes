package com.example.trendingtimes.auth.presentation.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trendingtimes.auth.data.model.AuthDTO
import com.example.trendingtimes.auth.domain.interactors.LoginUseCase
import com.example.trendingtimes.auth.domain.interactors.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AuthViewModel @Inject constructor(private val loginUseCase: LoginUseCase,private val registerUseCase: RegisterUseCase) : ViewModel() {

    init {
//        savedStateHandle.get<String>(Constants.PARAM_USER_ID)?.let {
//        }
    }

    fun karo(){
        Log.d("Login","Inside karo func")
        viewModelScope.launch {
            val login = loginUseCase.invoke(AuthDTO("adityaadiirai.004@gmail.com","123456"))
            login.collect{
                Log.d("Login","login $it")
            }
        }
    }
}