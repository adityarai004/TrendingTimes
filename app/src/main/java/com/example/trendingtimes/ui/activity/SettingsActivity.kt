package com.example.trendingtimes.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.example.trendingtimes.R
import com.example.trendingtimes.databinding.ActivitySettingsBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingsBinding
    private lateinit var mAuth: FirebaseAuth

    var nightMode by Delegates.notNull<Boolean>()
    lateinit var editor: SharedPreferences.Editor
    lateinit var sharePreferences:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val currentNightMode = isDarkModeOn()

        sharePreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE)
        nightMode = sharePreferences.getBoolean("nightMode", currentNightMode)

        if(FirebaseAuth.getInstance().currentUser?.isAnonymous == true){
            binding.changePassBtn.visibility = View.GONE
        }
        binding.backButtonIv.setOnClickListener {
            finish()
        }

        binding.changePassBtn.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }

        if(currentNightMode){
            binding.lightDarkSwitch.isChecked = true
        }
        if(nightMode){
            binding.lightDarkSwitch.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        binding.lightDarkSwitch.setOnClickListener {
            if(nightMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor = sharePreferences.edit()
                editor.putBoolean("nightMode",false)
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor = sharePreferences.edit()
                editor.putBoolean("nightMode",true)
            }
            editor.apply()
        }
 
        mAuth = FirebaseAuth.getInstance()
        binding.logOutBtn.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        var spannableString = SpannableString(getString(R.string.have_feedback_for_us_submit_here))

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                startActivity(Intent(this@SettingsActivity, FeedbackActivity::class.java))
            }

        }

        spannableString.setSpan(clickableSpan,22,33,0)
        binding.feedbackTv.text = spannableString

        binding.feedbackTv.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun isDarkModeOn(): Boolean {
        return when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES -> true
            else -> false
        }
    }
}