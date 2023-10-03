package com.example.trendingtimes.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trendingtimes.R
import com.example.trendingtimes.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth


class ForgotPasswordActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private lateinit var binding : ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()

        binding.submitBtn.setOnClickListener {
            if(binding.emailForForgotEt.text.toString() != "" && binding.emailForForgotEt.text.toString().endsWith("@gmail.com")){
                val email = binding.emailForForgotEt.text.toString()
                binding.progressBar.visibility = View.VISIBLE

// Check if the email address exists in Firebase Authentication
                FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val signInMethods = task.result?.signInMethods
                            Log.d("SignInMethods", "Methods for $email: $signInMethods")
                                 //The email address is associated with an account; send the password reset email
                                mAuth!!.sendPasswordResetEmail(email)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            binding.progressBar.visibility = View.GONE
                                            Toast.makeText(applicationContext, "A mail has been sent to your email account.", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(applicationContext, "Something went wrong.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                 }
                            }

                        }
                    }
                }
}