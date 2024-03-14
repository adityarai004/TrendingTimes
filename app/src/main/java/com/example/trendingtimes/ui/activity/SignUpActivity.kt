package com.example.trendingtimes.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.trendingtimes.R
import com.example.trendingtimes.model.remote.User
import com.example.trendingtimes.databinding.ActivitySignUpBinding
import com.example.trendingtimes.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    lateinit var authViewModel: AuthViewModel
    private var maleOrFemale = ""

    private var pickedImg: Uri? = null
    private var imageUrl: String? = null
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            binding.pickedImage.setImageURI(uri)
            pickedImg = uri
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        binding.genderRg.setOnCheckedChangeListener { p0, p1 ->
            maleOrFemale = binding.genderRg.findViewById<RadioButton>(p1).text.toString()
            Log.d("TAG", "${binding.genderRg.findViewById<RadioButton>(p1).text}")
        }

        binding.loginTv.setOnClickListener {
            finish()
        }

        binding.pickImgBtn.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.dob.findViewById<ImageView>(R.id.editIcon).setImageResource(R.drawable.baseline_calendar_month_24)

        binding.dob.setUserDetails(
            "01/01/2000",
            "DOB",
            object : SomethingUpdated {
                override fun doUpdate(didUpdate: Boolean, newData: String) {
                    if (didUpdate) {
                        updateUserInfo(newData)
                    }
                }
            })
        binding.signUpBtn.setOnClickListener {
            val name: String = binding.nameEt.text?.toString() ?: ""
            val email: String = binding.emailEt.text?.toString() ?: ""
            val password: String = binding.passwordEt.text?.toString() ?: ""
            val gender: String = maleOrFemale
            val dob: String = binding.dob.findViewById<TextView>(R.id.userDetailsTextView).text.toString()
            val containsLetter = password.any { it.isLetter() }
            val containsDigit = password.any { it.isDigit() }


            if (name != "" && !name.contains(Regex("\\d")) && email != "" && password != "" && containsLetter && containsDigit && gender != "" && email.endsWith("@gmail.com") && pickedImg != null) {
                Log.d("TAG", "entered successfully with gender = $gender")
                binding.progressBar.visibility = View.VISIBLE
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        if (authViewModel.signUp(email, password)) {
                            val signInResult = authViewModel.login(email, password)
                            if (signInResult) {
                                val db = FirebaseFirestore.getInstance()
                                val firebaseUser = FirebaseAuth.getInstance().currentUser

                                val imageFileName = "${firebaseUser?.uid}_profile_image.jpg"
                                val storageRef = FirebaseStorage.getInstance().reference.child("profile_images/$imageFileName")

                                pickedImg?.let { uri ->
                                    storageRef.putFile(uri)
                                        .addOnSuccessListener { takeSnapshot ->
                                            storageRef.downloadUrl.addOnSuccessListener { uri ->
                                                imageUrl = uri.toString()
                                                Log.d("TAG", "IMAGE URL : $imageUrl")

                                                val user = User(name, email, gender, imageUrl,dob)
                                                firebaseUser?.uid?.let { uid ->
                                                    // Save user data to Firestore under the user's UID
                                                    db.collection("users")
                                                        .document(uid)
                                                        .set(user)
                                                        .addOnSuccessListener {
                                                            // Data saved successfully
                                                            // You can perform additional actions here if needed
                                                            Toast.makeText(applicationContext, "Logged In", Toast.LENGTH_SHORT).show()
                                                            binding.progressBar.visibility = View.GONE
                                                            startActivity(Intent(applicationContext, MainActivity::class.java))
                                                            finish()
                                                        }
                                                        .addOnFailureListener { e ->
                                                            binding.progressBar.visibility =
                                                                View.GONE
                                                            // Handle any errors that occur
                                                            Toast.makeText(applicationContext, "error $e", Toast.LENGTH_SHORT).show()

                                                        }
                                                }
                                            }
                                        }
                                        .addOnFailureListener {
                                            Log.d("TAG", "Failed Uploading image")
                                            val user = User(name, email, gender, imageUrl,dob)
                                            firebaseUser?.uid?.let { uid ->
                                                // Save user data to Firestore under the user's UID
                                                db.collection("users")
                                                    .document(uid)
                                                    .set(user)
                                                    .addOnSuccessListener {
                                                        // Data saved successfully
                                                        // You can perform additional actions here if needed
                                                        Toast.makeText(applicationContext, "Logged In", Toast.LENGTH_SHORT).show()
                                                        binding.progressBar.visibility = View.GONE
                                                        startActivity(Intent(applicationContext, MainActivity::class.java))
                                                        finish()
                                                    }
                                                    .addOnFailureListener { e ->
                                                        binding.progressBar.visibility = View.GONE
                                                        // Handle any errors that occur
                                                        Toast.makeText(applicationContext, "error $e", Toast.LENGTH_SHORT).show()

                                                    }
                                            }
                                        }
                                }
                            }
                        } else {
                            authViewModel.login(email, password)
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                            finish()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(applicationContext, "error $e", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                if (pickedImg == null) {
                    Toast.makeText(
                        applicationContext,
                        "Please choose a profile photo",
                        Toast.LENGTH_LONG
                    ).show()
                }
                Toast.makeText(applicationContext, "Some inputs may be wrong ", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun updateUserInfo(newData: String) {
        binding.dob.updateInformation(newData,"DOB")
    }
}

