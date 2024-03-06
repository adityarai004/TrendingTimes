package com.example.trendingtimes.ui.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.trendingtimes.databinding.ActivityUpdateProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateProfileBinding

    private var pickedImg: Uri? = null
    private var imageUrl: String? = null
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            pickedImg = uri
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backNavigationBtn.setOnClickListener {
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
                        ).into(binding.profilePic)
                        val name = docSnapshot.get("name")
                        val email = docSnapshot.get("email")
                        val dob = docSnapshot.get("dob")
                        val gender = docSnapshot.get("gender")
                        binding.tv1.text = name.toString()
                        binding.nameCustomView.setUserDetails(
                            name.toString(),
                            "Name",
                            object : SomethingUpdated {
                                override fun doUpdate(didUpdate: Boolean, newData: String) {
                                    if (didUpdate) {
                                        updateUserInfo("name",newData)
                                    }
                                }
                            })
                        binding.emailCustomView.setUserDetails(
                            email.toString(),
                            "Email",
                            object : SomethingUpdated {
                                override fun doUpdate(didUpdate: Boolean, newData: String) {
                                    if (didUpdate) {
                                        updateUserInfo("email",newData)
                                    }
                                }
                            })
                        binding.dobCustomView.setUserDetails(
                            dob.toString(),
                            "DOB",
                            object : SomethingUpdated {
                                override fun doUpdate(didUpdate: Boolean, newData: String) {
                                    if (didUpdate) {
                                        updateUserInfo("dob",newData)
                                    }
                                }
                            })
                        binding.genderCustomView.setUserDetails(
                            gender.toString(),
                            "Gender",
                            object : SomethingUpdated {
                                override fun doUpdate(didUpdate: Boolean, newData: String) {
                                    if (didUpdate) {
                                        updateUserInfo("gender",newData)
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

    fun updateUserInfo(fieldToUpdate: String,updatedData: String) {
        val currUser = FirebaseAuth.getInstance().currentUser
        currUser?.let {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(currUser.uid)
                .update(fieldToUpdate,updatedData)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        currUser.let {
                            FirebaseFirestore.getInstance().collection("users")
                                .document(currUser.uid)
                                .get()
                                .addOnSuccessListener { docSnapshot ->
                                    if (docSnapshot != null) {
                                        val imageUrl = docSnapshot.get("imageUrl")
                                        Glide.with(this).load(
                                            imageUrl
                                        ).into(binding.profilePic)
                                        val name = docSnapshot.get("name")
                                        val email = docSnapshot.get("email")
                                        val dob = docSnapshot.get("dob")
                                        val gender = docSnapshot.get("gender")
                                        binding.tv1.text = name.toString()
                                        binding.nameCustomView.updateInformation(name.toString(),"Name")
                                        binding.emailCustomView.updateInformation(email.toString(),"Email")
                                        binding.dobCustomView.updateInformation(dob.toString(),"DOB")
                                        binding.genderCustomView.updateInformation(gender.toString(),"Gender")
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.d("TAG", "Exception is $exception")
                                }
                        }
                        Toast.makeText(this,"$fieldToUpdate updated successfully.", Toast.LENGTH_LONG).show()
                    } else{
                        Toast.makeText(this,"Something went wrong updating DOB.", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this,"Exception : $it", Toast.LENGTH_LONG).show()
                }
        }
    }
}

interface SomethingUpdated{
    fun doUpdate(didUpdate: Boolean, newData: String)
}