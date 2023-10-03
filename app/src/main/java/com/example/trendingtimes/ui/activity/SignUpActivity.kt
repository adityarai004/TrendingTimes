package com.example.trendingtimes.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.util.appendPlaceholders
import com.example.trendingtimes.data.User
import com.example.trendingtimes.databinding.ActivitySignUpBinding
import com.example.trendingtimes.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    lateinit var authViewModel: AuthViewModel
    private var maleOrFemale = ""

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("TAG", "entered Activity successfully")

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
//        authViewModel =
//            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[AuthViewModel::class.java]

        binding.genderRg.setOnCheckedChangeListener(object : OnCheckedChangeListener{
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                maleOrFemale = binding.genderRg.findViewById<RadioButton>(p1).text.toString()
                Log.d("TAG","${binding.genderRg.findViewById<RadioButton>(p1).text}")
            }
        })

        binding.loginTv.setOnClickListener {
            finish()
        }
        binding.signUpBtn.setOnClickListener {
            val name : String = binding.nameEt.text?.toString() ?: ""
            val email : String = binding.emailEt.text?.toString() ?: ""
            val password : String = binding.passwordEt.text?.toString() ?: ""
            val gender : String =  maleOrFemale



            if(name != "" && email != "" && password != "" && gender != "" && email.endsWith("@gmail.com")){
                Log.d("TAG", "entered successfully with gender = $gender")
                binding.progressBar.visibility = View.VISIBLE
                 GlobalScope.launch (Dispatchers.IO){
                     try {
                         if(authViewModel.signUp(email,password)){
                             val signInResult = authViewModel.login(email, password)
                             if(signInResult){
                                 val user = User(name,email,gender)
                                 val db = FirebaseFirestore.getInstance()
                                 val firebaseUser = FirebaseAuth.getInstance().currentUser
                                 firebaseUser?.uid?.let { uid ->
                                     // Save user data to Firestore under the user's UID
                                     db.collection("users")
                                         .document(uid)
                                         .set(user)
                                         .addOnSuccessListener {
                                             // Data saved successfully
                                             // You can perform additional actions here if needed
                                             Toast.makeText(applicationContext,"Logged In",Toast.LENGTH_SHORT).show()
                                             binding.progressBar.visibility = View.GONE
                                             startActivity(Intent(applicationContext,MainActivity::class.java))
                                             finish()
                                         }
                                         .addOnFailureListener { e ->
                                             binding.progressBar.visibility = View.GONE
                                             // Handle any errors that occur
                                             Toast.makeText(applicationContext,"error $e",Toast.LENGTH_SHORT).show()

//                                        Log.d("TAG", "Error adding document", e)
                                         }
                                 }
                             }
                         }
                         else{
                            authViewModel.login(email,password)
                             startActivity(Intent(applicationContext,MainActivity::class.java))
                             finish()
                         }
                     }
                     catch (e:Exception){
                         Toast.makeText(applicationContext,"error $e",Toast.LENGTH_SHORT).show()
                     }
                 }
            }
            else{
                Toast.makeText(applicationContext,"Some inputs may be wrong",Toast.LENGTH_SHORT).show()
            }
        }
    }
}

