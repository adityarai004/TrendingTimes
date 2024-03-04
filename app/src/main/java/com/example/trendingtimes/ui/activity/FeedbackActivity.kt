package com.example.trendingtimes.ui.activity

//noinspection SuspiciousImport
import android.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trendingtimes.MainApplication
import com.example.trendingtimes.data.Feedback
import com.example.trendingtimes.databinding.ActivityFeedbackBinding

class FeedbackActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedbackBinding
    val currUser = MainApplication.instance.currUser
    val db = MainApplication.instance.db
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val feedbackType: MutableList<String> = ArrayList()
        feedbackType.add("Bug Report")
        feedbackType.add("Improvement Suggestion")
        feedbackType.add("Content Issue")
        feedbackType.add("Other(Please specify)")

        val categoryType: MutableList<String> = ArrayList()
        categoryType.add("General App Issue")
        categoryType.add("Login/Signup")
        categoryType.add("News Content")
        categoryType.add("Other(Please specify)")

        val feedbackTypeAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, feedbackType)

        feedbackTypeAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        binding.spinner.adapter = feedbackTypeAdapter

        val categoryAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, categoryType)

        categoryAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        binding.categorySpinner.adapter = categoryAdapter


        binding.submitBtn.setOnClickListener {
            binding.progressLayout.visibility = View.VISIBLE
        }

        binding.backButtonIv.setOnClickListener {
            finish()
        }
        binding.submitBtn.setOnClickListener {
            binding.progressLayout.visibility = View.VISIBLE
            val explanation = binding.textInputEt.text.toString()
            if (explanation.isEmpty()) {
                binding.progressLayout.visibility = View.GONE
                Toast.makeText(
                    this@FeedbackActivity,
                    "Please explain you query a little bit for faster response",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val selectedFeedbackType = feedbackType[binding.spinner.selectedItemPosition]
                val selectedCategoryType =
                    categoryType[binding.categorySpinner.selectedItemPosition]
                val selectedSeverityId = binding.severityRg.checkedRadioButtonId
                val selectedSeverity = findViewById<RadioButton>(selectedSeverityId).text.toString()

                val feedback = Feedback(
                    selectedFeedbackType,
                    selectedCategoryType,
                    selectedSeverity,
                    explanation
                )
                currUser?.let {
                    db?.collection("feedbacks")?.document(currUser.uid)?.collection("feedback")
                        ?.add(feedback)?.addOnSuccessListener {
                        Toast.makeText(
                            this@FeedbackActivity,
                            "You feedback has been submitted successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        binding.textInputEt.text?.clear()
                        binding.progressLayout.visibility = View.GONE

                    }
                        ?.addOnFailureListener {
                            Log.d("FeedbackActivity", "Exception $it occurred")
                            binding.progressLayout.visibility = View.GONE
                        }
                }
            }
        }
    }
}