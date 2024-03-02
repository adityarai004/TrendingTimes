package com.example.trendingtimes

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication: Application(){
    companion object {
        lateinit var instance: MainApplication
            private set
    }

    var currUser: FirebaseUser? = null
    var db:FirebaseFirestore? = null
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        instance = this
        currUser = FirebaseAuth.getInstance().currentUser
        db = FirebaseFirestore.getInstance()
    }
}