package com.example.group5phft

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class RegisterActivity : Activity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize DatabaseHelper
        dbHelper = DatabaseHelper(this)

        // Find views by ID
        val etFirstName = findViewById<EditText>(R.id.etFirstName)
        val etLastName = findViewById<EditText>(R.id.etLastName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etDob = findViewById<EditText>(R.id.etDob)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        // Set up button click listener
        btnSubmit.setOnClickListener {
            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val dob = etDob.text.toString()

            // Check required fields
            if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate email
            if (!email.endsWith(".com")) {
                Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate password
            if (!isPasswordValid(password)) {
                Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save user to database
            val success = dbHelper.insertUser(email, password, firstName, lastName, dob)
            if (success) {
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                finish() // Close the activity
            } else {
                Toast.makeText(this, "Error during registration", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Password validation method
    private fun isPasswordValid(password: String): Boolean {
        val uppercasePattern = Regex("[A-Z]")
        val digitPattern = Regex("\\d")
        val specialCharacterPattern = Regex("[@\$!%*?&]")

        return password.length >= 8 &&
                uppercasePattern.containsMatchIn(password) &&
                digitPattern.containsMatchIn(password) &&
                specialCharacterPattern.containsMatchIn(password)
    }
}