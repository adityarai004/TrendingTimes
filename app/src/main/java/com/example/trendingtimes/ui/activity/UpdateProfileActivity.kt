package com.example.trendingtimes.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.trendingtimes.R
import com.example.trendingtimes.databinding.ActivityUpdateProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backNavigationBtn?.setOnClickListener {
            finish()
        }

        val currUser = FirebaseAuth.getInstance().currentUser
        currUser?.let {
            FirebaseFirestore.getInstance().collection("users")
                .document(currUser.uid)
                .get()
                .addOnSuccessListener { docSnapshot ->
                    if (docSnapshot != null) {
                        val imageUrl = docSnapshot.get("imageUrl")
                        Glide.with(this).load(
                            imageUrl
                        ).into(binding.profilePic!!)
                        val name = docSnapshot.get("name")
                        val email = docSnapshot.get("email")
                        val dob = docSnapshot.get("dob")
                        val gender = docSnapshot.get("gender")
                        binding.tv1!!.text = name.toString()
                        binding.nameCustomView?.setUserDetails(
                            name.toString(),
                            "Name",
                            object : SomethingUpdated {
                                override fun isUpdated(didUpdate: Boolean) {
                                    if (didUpdate) {
                                        updateUserInfo()
                                    }
                                }
                            })
                        binding.emailCustomView?.setUserDetails(
                            email.toString(),
                            "Email",
                            object : SomethingUpdated {
                                override fun isUpdated(didUpdate: Boolean) {
                                    if (didUpdate) {
                                        updateUserInfo()
                                    }
                                }
                            })
                        binding.dobCustomView?.setUserDetails(
                            dob.toString(),
                            "DOB",
                            object : SomethingUpdated {
                                override fun isUpdated(didUpdate: Boolean) {
                                    if (didUpdate) {
                                        updateUserInfo()
                                    }
                                }
                            })
                        binding.genderCustomView?.setUserDetails(
                            gender.toString(),
                            "Gender",
                            object : SomethingUpdated {
                                override fun isUpdated(didUpdate: Boolean) {
                                    if (didUpdate) {
                                        updateUserInfo()
                                    }
                                }
                            })
                    }
                }
                .addOnFailureListener {
                    Log.d("TAG", "Exception is $it")
                }
        }

    }

    fun updateUserInfo() {
        val currUser = FirebaseAuth.getInstance().currentUser
        currUser?.let {
            FirebaseFirestore.getInstance().collection("users")
                .document(currUser.uid)
                .get()
                .addOnSuccessListener { docSnapshot ->
                    if (docSnapshot != null) {
                        val imageUrl = docSnapshot.get("imageUrl")
                        Glide.with(this).load(
                            imageUrl
                        ).into(binding.profilePic!!)
                        val name = docSnapshot.get("name")
                        val email = docSnapshot.get("email")
                        val dob = docSnapshot.get("dob")
                        val gender = docSnapshot.get("gender")
                        binding.tv1!!.text = name.toString()
                        binding.nameCustomView?.updateInformation(name.toString(),"Name",)
                        binding.emailCustomView?.updateInformation(email.toString(),"Email")
                        binding.dobCustomView?.updateInformation(dob.toString(),"DOB")
                        binding.genderCustomView?.updateInformation(gender.toString(),"Gender")
                    }
                }
                .addOnFailureListener {
                    Log.d("TAG", "Exception is $it")
                }
        }
    }
}

interface SomethingUpdated{
    fun isUpdated(didUpdate: Boolean)
}