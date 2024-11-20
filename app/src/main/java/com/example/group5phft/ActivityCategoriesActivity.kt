package com.example.group5phft

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ActivityCategoriesActivity : AppCompatActivity() {
    private val activityList = mutableListOf("Running", "Walking", "Cycling", "Yoga", "HIIT", "Weightlifting")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        val categoriesListView = findViewById<ListView>(R.id.categoriesListView)
        val addActivityButton = findViewById<Button>(R.id.addActivityButton)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, activityList)
        categoriesListView.adapter = adapter

        addActivityButton.setOnClickListener {
            // Example addition of new activity
            val newActivity = "New Activity ${activityList.size + 1}"
            activityList.add(newActivity)
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "$newActivity added!", Toast.LENGTH_SHORT).show()
        }
    }
}