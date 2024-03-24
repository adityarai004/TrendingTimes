package com.example.trendingtimes.core.util

import android.content.Context
import android.content.DialogInterface
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.example.trendingtimes.R
import com.example.trendingtimes.ui.activity.SomethingUpdated
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class CustomUserDetailsView(context: Context, attrs: AttributeSet? = null) : RelativeLayout(context, attrs){

    init {
        inflate(context, R.layout.user_detail_custom_view, this)
    }
    private var editIcon: ImageView? = null
    private var somethingUpdatedListener: SomethingUpdated? = null
    private var currUser: FirebaseUser? = null

    fun setUserDetails(userDetails: String,detailName: String,somethingUpdated: SomethingUpdated) {
        val userDetailsTextView: TextView = findViewById(R.id.userDetailsTextView)
        editIcon = findViewById(R.id.editIcon)
        userDetailsTextView.text = userDetails
        somethingUpdatedListener = somethingUpdated
        initClickListener(detailName, userDetails, somethingUpdatedListener)
        currUser = FirebaseAuth.getInstance().currentUser
    }

    private fun initClickListener(detailName: String, userDetails: String, somethingUpdated: SomethingUpdated?){
        editIcon?.setOnClickListener {
            when (detailName) {
                "Name" -> {
                    val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null)
                    val et = dialogView.findViewById<EditText>(R.id.name_et)
                    val tf = dialogView.findViewById<TextInputLayout>(R.id.name_tf)
                    tf.hint = detailName
                    et.text = SpannableStringBuilder(userDetails)
                    val alertDialog = MaterialAlertDialogBuilder(context)
                        .setTitle("Update your $detailName")
                        .setView(dialogView)
                        .setPositiveButton("Done", object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                if(et.text.toString() == userDetails){
                                    Toast.makeText(context,"Same information as previous.",Toast.LENGTH_LONG).show()
                                } else{
                                    somethingUpdated?.doUpdate(true,et.text.toString().trim())
                                }
                            }
                        })
                        .setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                p0?.dismiss()
                            }

                        }).create()
                    alertDialog.show()
                }

                "DOB" -> {// 06/07/2004
                    val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_layout_calender, null)
                    val calendarView = dialogView.findViewById<DatePicker>(R.id.my_calendar)
                    val year = userDetails.substring(6).toInt()
                    val month = userDetails.substring(3,5).toInt()
                    val day = userDetails.substring(0,2).toInt()

                    calendarView.updateDate(year,month-1,day)
                    val alertDialog = MaterialAlertDialogBuilder(context)
                        .setTitle("Update your $detailName")
                        .setView(dialogView)
                        .setPositiveButton("Done", object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                val newMonth = calendarView.month
                                val monthString: String = if (newMonth > 8){
                                    "${newMonth+1}"
                                } else{
                                    "0${newMonth+1}"
                                }
                                val newDay = calendarView.dayOfMonth
                                val dayString = if (newDay <= 9){
                                    "0$newDay"
                                }
                                else{
                                    "$newDay"
                                }
                                val newDob = "$dayString/$monthString/${calendarView.year}"

                                somethingUpdated?.doUpdate(true,newDob)
                                p0?.dismiss()
                            }
                        })
                        .setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                p0?.dismiss()
                            }

                        }).create()
                    alertDialog.show()
                }

                "Gender" -> {
                    val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_layout_gender, null)
                    val radioGroup = dialogView.findViewById<RadioGroup>(R.id.gender_rg)

                    when (userDetails) {
                        "Male" -> {
                            radioGroup.check(dialogView.findViewById<RadioButton>(R.id.male_radio_button).id)
                        }
                        "Female" -> {
                            radioGroup.check(dialogView.findViewById<RadioButton>(R.id.female_radio_button).id)
                        }
                        else -> {
                            radioGroup.check(dialogView.findViewById<RadioButton>(R.id.other_radio_button).id)
                        }
                    }
                    val alertDialog = MaterialAlertDialogBuilder(context)
                        .setTitle("Update your $detailName")
                        .setView(dialogView)
                        .setPositiveButton("Done", object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                val selected = radioGroup.checkedRadioButtonId
                                val newGender = when (selected) {
                                    R.id.male_radio_button -> "Male"
                                    R.id.female_radio_button -> "Female"
                                    R.id.other_radio_button -> "Other"
                                    else -> "Unknown" // Handle other cases as needed
                                }
                                somethingUpdated?.doUpdate(true,newGender)
                                p0?.dismiss()
                            }
                        })
                        .setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                p0?.dismiss()
                            }

                        }).create()
                    alertDialog.show()
                }
            }
        }
    }
    fun updateInformation(userDetails: String,detailName: String){
        val userDetailsTextView: TextView = findViewById(R.id.userDetailsTextView)
        userDetailsTextView.text = userDetails
        initClickListener(detailName,userDetails,somethingUpdatedListener)
    }
}
