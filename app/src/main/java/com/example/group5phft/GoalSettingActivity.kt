package com.example.group5phft

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GoalSettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_setting)

        // Existing fields
        val milesRanInput = findViewById<EditText>(R.id.milesRanInput)
        val stepsTakenInput = findViewById<EditText>(R.id.stepsTakenInput)
        val caloriesBurnedInput = findViewById<EditText>(R.id.caloriesBurnedInput)

        // New fields
        val exerciseGoalInput = findViewById<EditText>(R.id.exerciseGoalInput)
        val weightLiftingGoalInput = findViewById<EditText>(R.id.weightLiftingGoalInput)
        val weightGoalInput = findViewById<EditText>(R.id.weightGoalInput)

        val saveGoalButton = findViewById<Button>(R.id.saveGoalButton)
        val viewProgressButton = findViewById<Button>(R.id.viewProgressButton) // New button for viewing progress

        // SharedPreferences to save goal data
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserGoals", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        saveGoalButton.setOnClickListener {
            // Get input values
            val milesRan = milesRanInput.text.toString().toDoubleOrNull()
            val stepsTaken = stepsTakenInput.text.toString().toIntOrNull()
            val caloriesBurned = caloriesBurnedInput.text.toString().toIntOrNull()
            val exerciseGoal = exerciseGoalInput.text.toString()
            val weightLiftingGoal = weightLiftingGoalInput.text.toString()
            val weightGoal = weightGoalInput.text.toString().toDoubleOrNull()

            // Validate inputs
            when {
                milesRan == null || milesRan < 0 -> {
                    Toast.makeText(this, "Please enter a valid number for Miles Ran.", Toast.LENGTH_SHORT).show()
                }
                stepsTaken == null || stepsTaken < 0 -> {
                    Toast.makeText(this, "Please enter a valid number for Steps Taken.", Toast.LENGTH_SHORT).show()
                }
                caloriesBurned == null || caloriesBurned < 0 -> {
                    Toast.makeText(this, "Please enter a valid number for Calories Burned.", Toast.LENGTH_SHORT).show()
                }
                exerciseGoal.isEmpty() -> {
                    Toast.makeText(this, "Please enter an Exercise Goal.", Toast.LENGTH_SHORT).show()
                }
                weightLiftingGoal.isEmpty() -> {
                    Toast.makeText(this, "Please enter a Weight Lifting Goal.", Toast.LENGTH_SHORT).show()
                }
                weightGoal == null || weightGoal < 0 -> {
                    Toast.makeText(this, "Please enter a valid number for Weight Goal.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Save data in SharedPreferences
                    editor.putFloat("MilesRan", milesRan.toFloat())
                    editor.putInt("StepsTaken", stepsTaken)
                    editor.putInt("CaloriesBurned", caloriesBurned)
                    editor.putString("ExerciseGoal", exerciseGoal)
                    editor.putString("WeightLiftingGoal", weightLiftingGoal)
                    editor.putFloat("WeightGoal", weightGoal.toFloat())
                    editor.apply()

                    // Display confirmation
                    val goalText = """
                        Miles Ran: $milesRan
                        Steps Taken: $stepsTaken
                        Calories Burned: $caloriesBurned
                        Exercise Goal: $exerciseGoal
                        Weight Lifting Goal: $weightLiftingGoal
                        Weight Goal: $weightGoal
                    """.trimIndent()
                    Toast.makeText(this, "Goal saved:\n$goalText", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Button to view progress
        viewProgressButton.setOnClickListener {
            // Ensure all values are entered before showing progress
            if (milesRanInput.text.isEmpty() || stepsTakenInput.text.isEmpty() || caloriesBurnedInput.text.isEmpty() ||
                exerciseGoalInput.text.isEmpty() || weightLiftingGoalInput.text.isEmpty() || weightGoalInput.text.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields to view progress.", Toast.LENGTH_SHORT).show()
            } else {
                // Navigate to ProgressMonitoringActivity
                val intent = Intent(this, ProgressMonitoringActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
