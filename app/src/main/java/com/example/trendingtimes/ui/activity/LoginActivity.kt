package com.example.trendingtimes.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trendingtimes.databinding.ActivityLoginBinding
import com.example.trendingtimes.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var authViewModel: AuthViewModel

    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        if (FirebaseAuth.getInstance().currentUser != null) {
            val nextAct = Intent(this, MainActivity::class.java)
            if (intent != null && intent.hasExtra("news")) {
                for (key: String in intent.extras?.keySet()!!) {
                    nextAct.putExtra(key, intent.extras?.getString(key))
                    Log.d("TAG", "OnCreate $key Data is ${intent.extras?.getString(key)}")
                }
            }
            startActivity(nextAct)
            finish()
        }

        binding.continueAsAGuest.setOnClickListener {
            mAuth.signInAnonymously()
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInAnonymously:success")
                        val user = mAuth.currentUser
                        val nextAct = Intent(this, MainActivity::class.java)
                        if (intent != null && intent.hasExtra("news")) {
                            for (key: String in intent.extras?.keySet()!!) {
                                nextAct.putExtra(key, intent.extras?.getString(key))
                                Log.d("TAG", "OnCreate $key Data is ${intent.extras?.getString(key)}")
                            }
                        }
                        startActivity(nextAct)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInAnonymously:failure", task.exception)
                        Toast.makeText(
                            this, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        binding.loginBtn.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val email = binding.emailEt.text.toString()
            if (email != "" && binding.passwordEt.text.toString() != "" && email.endsWith("@gmail.com")) {
                authViewModel.viewModelScope.launch {
                    if (authViewModel.login(binding.emailEt.text.toString(), binding.passwordEt.text.toString())) {
                        binding.progressBar.visibility = View.GONE
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "UserId or Password is incorrect", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                if (!email.endsWith("@gmail.com")) {
                    Toast.makeText(applicationContext, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "Please enter a valid entries", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.forgotPassTv.setOnClickListener {
            startActivity(Intent(applicationContext, ForgotPasswordActivity::class.java))
        }
        binding.signUpTv.setOnClickListener {
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
        }
    }
}