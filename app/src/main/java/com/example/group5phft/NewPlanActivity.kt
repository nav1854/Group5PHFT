package com.example.group5phft

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NewPlanActivity : AppCompatActivity() {

    private lateinit var etPlanTitle: EditText
    private lateinit var etPlanDuration: EditText
    private lateinit var etPlanSets: EditText
    private lateinit var etPlanDescription: EditText
    private lateinit var btnSavePlan: Button
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newplan)

        etPlanTitle = findViewById(R.id.etPlanTitle)
        etPlanDuration = findViewById(R.id.etPlanDuration)
        etPlanSets = findViewById(R.id.etPlanSets)
        etPlanDescription = findViewById(R.id.etPlanDescription)
        btnSavePlan = findViewById(R.id.btnSavePlan)
        dbHelper = DatabaseHelper(this)

        btnSavePlan.setOnClickListener {
            val title = etPlanTitle.text.toString()
            val duration = etPlanDuration.text.toString()
            val sets = etPlanSets.text.toString().toIntOrNull()
            val description = etPlanDescription.text.toString()

            if (title.isNotBlank()) {
                val success = dbHelper.insertWorkoutPlan(title, duration, sets, description)
                if (success) {
                    Toast.makeText(this, "Workout Plan Saved", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity and return to the previous screen
                } else {
                    Toast.makeText(this, "Failed to Save Plan", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}