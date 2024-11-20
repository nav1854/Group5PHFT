package com.example.group5phft

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class ProgressMonitoringActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_monitoring)

        // Get saved goals from SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserGoals", MODE_PRIVATE)
        val milesRan = sharedPreferences.getFloat("MilesRan", 0f)
        val stepsTaken = sharedPreferences.getInt("StepsTaken", 0)
        val caloriesBurned = sharedPreferences.getInt("CaloriesBurned", 0)
        val weightGoal = sharedPreferences.getFloat("WeightGoal", 0f)

        // Create a list of progress data (X, Y) pairs for graphing
        val progressData = listOf(
            Pair(1f, milesRan),  // Example: day 1, miles ran
            Pair(2f, milesRan + 2f),  // Example: day 2, 2 more miles ran
            Pair(3f, milesRan + 3f)  // Example: day 3, 3 more miles ran
        )

        // Find the ProgressGraphView from the layout and set the data
        val graphView = findViewById<ProgressGraphView>(R.id.progressGraphView)
        graphView.setProgressData(progressData)

        // Show a message with goal details
        val goalText = """
            Miles Ran: $milesRan
            Steps Taken: $stepsTaken
            Calories Burned: $caloriesBurned
            Weight Goal: $weightGoal
        """.trimIndent()
        Toast.makeText(this, "Progress:\n$goalText", Toast.LENGTH_LONG).show()
    }
}
