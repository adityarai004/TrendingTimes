package com.example.trendingtimes.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trendingtimes.databinding.ActivityChangePasswordBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email

        binding.backButtonIv.setOnClickListener {
            finish()
        }
        binding.changePasswordButton.setOnClickListener {
            if (binding.newPasswordEditText.text.toString() == binding.confirmPasswordEditText.text.toString()){
                binding.progressBar.visibility = View.VISIBLE
                val credential = email?.let { EmailAuthProvider.getCredential(it,binding.oldPasswordEditText.text.toString()) }
                if (credential != null) {
                    user.reauthenticate(credential).addOnCompleteListener {
                        if (it.isSuccessful){
                            user.updatePassword(binding.confirmPasswordEditText.text.toString()).addOnCompleteListener {
                                if (it.isSuccessful){
                                    binding.progressBar.visibility = View.GONE
                                    Toast.makeText(this,"Successfully changed password.",Toast.LENGTH_LONG).show()
                                }
                                else{
                                    binding.progressBar.visibility = View.GONE
                                    Toast.makeText(this,"Something went wrong.",Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        else{
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this,"Something went wrong.",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            else{
                Toast.makeText(this,"confirm and new password are different.",Toast.LENGTH_LONG).show()
            }
        }
    }
}