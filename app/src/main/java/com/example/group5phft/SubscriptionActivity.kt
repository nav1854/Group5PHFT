package com.example.group5phft

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class SubscriptionActivity : Activity() {

    private var userEmail: String? = null // User email to link the subscription to their account
    private lateinit var currentPlanTextView: TextView // TextView to show the currently selected plan
    private var selectedPlan: String = "None" // Default plan is "None"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_subscriptions)

        // Retrieve the user email passed from the previous activity
        userEmail = intent.getStringExtra("EMAIL")

        // Initialize UI components
        currentPlanTextView = findViewById(R.id.text_current_plan)
        val monthlyButton: Button = findViewById(R.id.btn_monthly)
        val threeMonthButton: Button = findViewById(R.id.btn_three_months)
        val yearlyButton: Button = findViewById(R.id.btn_yearly)
        val proceedButton: Button = findViewById(R.id.btn_continue_payment)

        // Display the initial current plan
        updateCurrentPlanText()

        // Set click listeners for each subscription plan
        monthlyButton.setOnClickListener {
            selectedPlan = "Monthly - $10"
            updateCurrentPlanText() // Update UI dynamically
        }

        threeMonthButton.setOnClickListener {
            selectedPlan = "3 Months - $25"
            updateCurrentPlanText() // Update UI dynamically
        }

        yearlyButton.setOnClickListener {
            selectedPlan = "Yearly - $60"
            updateCurrentPlanText() // Update UI dynamically
        }

        // Navigate to the PaymentActivity
        proceedButton.setOnClickListener {
            navigateToPayment()
        }
    }

    /**
     * Updates the current plan TextView dynamically.
     */
    private fun updateCurrentPlanText() {
        currentPlanTextView.text = "Current Plan: $selectedPlan"
    }

    /**
     * Navigates to the PaymentActivity with the selected subscription plan and user email.
     */
    private fun navigateToPayment() {
        if (userEmail.isNullOrEmpty()) {
            Toast.makeText(this, "User email is not available. Please log in again.", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, PaymentActivity::class.java).apply {
            putExtra("PLAN", selectedPlan) // Pass the selected subscription plan
            putExtra("EMAIL", userEmail) // Pass the user email to link the subscription
        }
        startActivity(intent)
    }
}